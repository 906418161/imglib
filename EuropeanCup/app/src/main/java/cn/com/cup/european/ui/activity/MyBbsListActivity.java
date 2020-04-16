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
import cn.com.cup.european.ui.adapter.BbsListAdapter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class MyBbsListActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.bbslista_list)
    ListView listView;
    @BindView(R.id.bbslista_refreshLayout)
    SmartRefreshLayout refreshLayout;
    String type;
    BbsListAdapter adapter;
    List<BbsListBean> beans;
    HttpModel httpModel = new HttpModel(this);
    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbslista);
        hideStatueBar(0);
        ButterKnife.bind(this);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        title.setText("我的动态");
        getStatusBarHeight(findViewById(R.id.toplinear));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                if (type.equals("1") && !Constants.isLoging()) {
                    refreshlayout.finishRefresh();
                    return;
                }
                loadData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(20000/*,false*/);//传入false表示加载失败
                if (type.equals("1") && !Constants.isLoging()) {
                    refreshlayout.finishLoadMore();
                    return;
                }
                loadData();
            }
        });
    }

    @Override
    protected void loadData() {
        if (beans == null) {
            pageNum = 1;
            beans = new ArrayList<>();
            adapter = new BbsListAdapter(beans, this);
            adapter.setPeople(true);
            listView.setAdapter(adapter);
        }
        httpModel.getAboutRelease(Constants.user.getId(), String.valueOf(pageNum), 10001);
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
                    List<BbsListBean> bbsListBeans = JSON.parseArray(object.getJSONObject("data").getString("rows"), BbsListBean.class);
                    beans.addAll(bbsListBeans);
                    adapter.notifyDataSetChanged();
                    if (bbsListBeans.size() >= 10) {
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
