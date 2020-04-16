package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.BbsListBean;
import cn.com.cup.european.bean.LikeBean;
import cn.com.cup.european.ui.adapter.LikeAdapter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class LikeActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.like_refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.like_list)
    ListView listView;
    LikeAdapter adapter;
    List<LikeBean> beans;
    HttpModel httpModel = new HttpModel(this);
    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        hideStatueBar(0);
        ButterKnife.bind(this);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("获赞");
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                loadData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(20000/*,false*/);//传入false表示加载失败
                loadData();
            }
        });
    }

    @Override
    protected void loadData() {
        if (beans == null) {
            pageNum = 1;
            beans = new ArrayList<>();
            adapter = new LikeAdapter(beans, this);
            listView.setAdapter(adapter);
        }
        httpModel.getAboutAzn(String.valueOf(pageNum), 10001);
    }

    @Override
    public void doResult(String result, int action) {
        try {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    List<LikeBean> likeBeans = JSON.parseArray(object.getJSONObject("data").getString("rows"), LikeBean.class);
                    beans.addAll(likeBeans);
                    adapter.notifyDataSetChanged();
                    if (likeBeans.size() >= 10) {
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
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
            dismissProgressDialog();
            showToast("网络不给力哦");
        } catch (Exception e) {

        }
    }
}
