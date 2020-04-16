package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.DarenListBean;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.bean.HomePeopleBean;
import cn.com.cup.european.bean.HomeTipBean;
import cn.com.cup.european.ui.adapter.DarenListAdapter;
import cn.com.cup.european.ui.adapter.HomeGridAdapter;
import cn.com.cup.european.ui.adapter.HomeHotAdapter;
import cn.com.cup.european.ui.adapter.HomePeopleAdapter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class DarenActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.daren_head)
    ImageView head;
    @BindView(R.id.daren_name)
    TextView name;
    @BindView(R.id.daren_follow)
    ImageView follow;
    @BindView(R.id.daren_followNum)
    TextView followNum;
    @BindView(R.id.daren_listview)
    ListView listView;
    @BindView(R.id.daren_refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.alllinear)
    LinearLayout allLinear;
    DarenListAdapter adapter;
    List<DarenListBean> beans;
    HttpModel httpModel = new HttpModel(this);
    int pageNum = 1, follownum = 0;
    boolean isLike = false;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daren);
        hideStatueBar(0);
        ButterKnife.bind(this);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        id = getIntent().getStringExtra("id");
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("达人解读");
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
            adapter = new DarenListAdapter(beans, this);
            listView.setAdapter(adapter);
        }
        httpModel.redList(id, String.valueOf(pageNum), 10001);
    }

    @OnClick({R.id.daren_follow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.daren_follow:
                if (!Constants.isLoging()) {
                    Constants.loading(this);
                    return;
                }
                httpModel.follow(id, 10002);
                isLike = !isLike;
                follownum = isLike ? follownum + 1 : follownum - 1;
                followNum.setText(follownum + "人关注");
                follow.setImageResource(isLike ? R.drawable.people_follow_yes : R.drawable.daren_follow_yes);
                break;
        }
    }


    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    object = object.getJSONObject("data");
                    if (pageNum == 1) {
                        ImageSelectUtil.showImg(head, object.getString("headimg"));
                        name.setText(object.getString("name"));
                        follownum = object.getInt("num");
                        followNum.setText(follownum + "人关注");
                        isLike = object.getString("isLike").equals("1");
                        follow.setImageResource(isLike ? R.drawable.people_follow_yes : R.drawable.daren_follow_yes);
                        allLinear.setVisibility(View.VISIBLE);
                    }
                    List<DarenListBean> array = JSON.parseArray(object.getString("list"), DarenListBean.class);
                    beans.addAll(array);
                    adapter.notifyDataSetChanged();
                    if (array.size() >= 10) {
                        pageNum++;
                    } else {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else if (action == 10002) {
                    EventBus.getDefault().post("darenFollow");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void handleEvent(String event) {
        if (event.equals("loading")||event.equals("unscrambleFollow")) {
            beans = null;
            loadData();
        }
    }
}
