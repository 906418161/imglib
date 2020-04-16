package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
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
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.BbsListBean;
import cn.com.cup.european.ui.adapter.BbsListAdapter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class PeopleDetailsActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.people_head)
    ImageView head;
    @BindView(R.id.people_follow)
    ImageView follow;
    @BindView(R.id.people_name)
    TextView name;
    @BindView(R.id.people_sig)
    TextView sig;
    @BindView(R.id.people_followNum)
    TextView followNum;
    @BindView(R.id.people_fansNum)
    TextView fansNum;
    @BindView(R.id.people_likeNum)
    TextView likeNum;
    @BindView(R.id.people_refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.people_list)
    ListView listView;
    @BindView(R.id.alllinear)
    View alllinear;
    private boolean isFollow = false;
    BbsListAdapter adapter;
    List<BbsListBean> beans;
    private String id;
    HttpModel httpModel = new HttpModel(this);
    private int pageNum = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        hideStatueBar(0);
        ButterKnife.bind(this);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        id = getIntent().getStringExtra("id");
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("个人中心");
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
            httpModel.getAboutUser(id, 10001);
            pageNum = 1;
            beans = new ArrayList<>();
            adapter = new BbsListAdapter(beans, this);
            adapter.setPeople(true);
            listView.setAdapter(adapter);
        }
        httpModel.getAboutRelease(id, String.valueOf(pageNum), 10002);
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
                    object = object.getJSONObject("data");
                    ImageSelectUtil.showImg(head, object.getString("headimg"));
                    name.setText(object.getString("name"));
                    sig.setText(object.getString("signature"));
                    isFollow = object.getString("isLike").equals("1");
                    follow.setImageResource(isFollow ? R.drawable.people_follow_yes : R.drawable.people_follow);
                    followNum.setText(object.getString("guanzhu"));
                    fansNum.setText(object.getString("fensi"));
                    likeNum.setText(object.getString("zan"));
                    alllinear.setVisibility(View.VISIBLE);
                } else if (action == 10002) {
                    List<BbsListBean> bbsListBeans = JSON.parseArray(object.getJSONObject("data").getString("rows"), BbsListBean.class);
                    beans.addAll(bbsListBeans);
                    adapter.notifyDataSetChanged();
                    if (bbsListBeans.size() >= 10) {
                        pageNum++;
                    } else {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else if (action == 10003) {
                    EventBus.getDefault().post("peopleFollow");
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

    @OnClick({R.id.people_follow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.people_follow:
                if(!Constants.isLoging()){
                    Constants.loading(PeopleDetailsActivity.this);
                    return;
                }
                httpModel.follow(id, 10003);
                isFollow = !isFollow;
                if (isFollow) {
                    follow.setImageResource(R.drawable.people_follow_yes);
                } else {
                    follow.setImageResource(R.drawable.people_follow);
                }
                break;
        }
    }
}
