package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.donkingliang.labels.LabelsView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.NewsBean;
import cn.com.cup.european.ui.adapter.NewsAdapter;
import cn.com.cup.european.ui.adapter.QiuxinAdapter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class HomeNewsActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.homenews_closse_img)
    ImageView close;
    @BindView(R.id.homenews_labels)
    LabelsView labels;
    @BindView(R.id.homenews_list)
    ListView listView;
    @BindView(R.id.homenews_refreshLayout)
    SmartRefreshLayout refreshLayout;
    QiuxinAdapter adapter;
    List<NewsBean> beans;
    List<String> tips, tipId;
    private String tid;
    HttpModel httpModel = new HttpModel(this);
    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homenews);
        ButterKnife.bind(this);
        hideStatueBar(0);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        title.setText("球星动态");
        getStatusBarHeight(findViewById(R.id.toplinear));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                beans = null;
                getList();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getList();
            }
        });
    }

    @Override
    protected void loadData() {
        httpModel.getLabel(10001);
    }

    @OnClick({R.id.homenews_closse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homenews_closse:
                labels.setVisibility(labels.getVisibility() == View.VISIBLE ?
                        View.GONE : View.VISIBLE);
                close.setImageResource(labels.getVisibility() == View.VISIBLE ?
                        R.drawable.bbs_button : R.drawable.bbs_top);
                break;
        }
    }

    @Override
    public void doResult(String result, int action) {
        try {
            refreshLayout.finishLoadMore();
            refreshLayout.finishRefresh();
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    tips = new ArrayList<>();
                    tipId = new ArrayList<>();
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        tips.add(object.getString("name"));
                        tipId.add(object.getString("id"));
                    }
                    labels.setLabels(tips); //直接设置一个字符串数组就可以了。
                    tid = tipId.get(0);
                    labels.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
                        @Override
                        public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                            //label是被选中的标签，data是标签所对应的数据，isSelect是是否选中，position是标签的位置。
                            if (isSelect) {
                                tid = tipId.get(position);
                                beans = null;
                                getList();
                            }
                        }
                    });
                    getList();
                } else if (action == 10002) {
                    List<NewsBean> newsBeans = JSON.parseArray(object.getJSONObject("data").getString("rows"), NewsBean.class);
                    beans.addAll(newsBeans);
                    adapter.notifyDataSetChanged();
                    if (newsBeans.size() >= 10) {
                        pageNum++;
                    } else {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
            } else {
                showToast(object.getString("msg"));
            }
        } catch (Exception e) {
            showToast("网络不给力哦");
        }
    }

    @Override
    public void onErr(String errMessage) {
        try {
            refreshLayout.finishLoadMore();
            refreshLayout.finishRefresh();
            dismissProgressDialog();
            showToast("网络不给力哦");
        } catch (Exception e) {

        }
    }

    private void getList() {
        if (beans == null) {
            refreshLayout.resetNoMoreData();
            pageNum = 1;
            beans = new ArrayList<>();
            adapter = new QiuxinAdapter(beans, this);
            listView.setAdapter(adapter);
        }
        httpModel.getStarList(tid, String.valueOf(pageNum), 10002);
    }
}
