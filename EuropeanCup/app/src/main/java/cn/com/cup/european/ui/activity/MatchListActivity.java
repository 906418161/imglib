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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.calendarview.Calendar;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.ui.adapter.MatchAdapter;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class MatchListActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.matchlist_refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.matchlist_listview)
    ListView listView;
    MatchAdapter adapter;
    List<HomeHotBean> beans;
    HttpModel httpModel = new HttpModel(this);
    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchlist);
        ButterKnife.bind(this);
        hideStatueBar(0);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("比赛列表");
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                beans = null;
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
            adapter = new MatchAdapter(beans, this);
            adapter.setGet(true);
            listView.setAdapter(adapter);
        }
        httpModel.getPMatchList(String.valueOf(pageNum), 10001);
    }

    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    List<HomeHotBean> homeHotBeans = JSON.parseArray(object.getJSONObject("data").getString("rows"), HomeHotBean.class);
                    beans.addAll(homeHotBeans);
                    adapter.notifyDataSetChanged();
                    if (homeHotBeans.size() >= 10) {
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
            dismissProgressDialog();
            showToast("网络不给力哦");
        } catch (Exception e) {

        }
    }
}
