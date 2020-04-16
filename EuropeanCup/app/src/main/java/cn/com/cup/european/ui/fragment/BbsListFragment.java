package cn.com.cup.european.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import butterknife.Unbinder;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.BbsListBean;
import cn.com.cup.european.bean.HomePeopleBean;
import cn.com.cup.european.ui.adapter.BbsListAdapter;
import cn.com.cup.european.ui.adapter.HomePeopleAdapter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class BbsListFragment extends BaseFragment implements RequestCallbackListener {
    View contentView;
    Unbinder unbinder;
    @BindView(R.id.bbslist_list)
    ListView listView;
    @BindView(R.id.bbslist_refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.bbslist_top)
    RelativeLayout top;
    String type;
    BbsListAdapter adapter;
    List<BbsListBean> beans;
    HttpModel httpModel = new HttpModel(this);
    int pageNum = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_bbslist, null);
        unbinder = ButterKnife.bind(this, contentView);
        loadView();
        loadData();
        return contentView;
    }

    @Override
    protected void loadView() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        type = getArguments().getString("type");
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                if (type.equals("1") && !Constants.isLoging()) {
                    refreshlayout.finishRefresh();
                    return;
                }
                beans = null;
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
            adapter = new BbsListAdapter(beans, getContext());
            listView.setAdapter(adapter);
        }
        if (type.equals("1")) {
            if (!Constants.isLoging()) {
                top.setVisibility(View.VISIBLE);
                return;
            }
            httpModel.getFansBbs(String.valueOf(pageNum), 10001);
        } else {
            httpModel.getHotBbs(String.valueOf(pageNum), 10001);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(getActivity());
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
                    object = object.getJSONObject("data");
                    List<BbsListBean> bbsListBeans = JSON.parseArray(object.getString("rows"), BbsListBean.class);
                    beans.addAll(bbsListBeans);
                    adapter.notifyDataSetChanged();
                    if(pageNum==1&&beans.size()>0){
                        top.setVisibility(View.GONE);
                    }else {
                        top.setVisibility(View.VISIBLE);
                    }
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
            refreshLayout.finishLoadMore();
            refreshLayout.finishRefresh();
            dismissProgressDialog();
            showToast("网络不给力哦");
        } catch (Exception e) {

        }
    }

    @Subscribe
    public void handleEvent(String event) {
        if (event.equals("loading") || event.equals("signout") ||
                event.equals("darenFollow") || event.equals("unscrambleFollow")
                || event.equals("bbsdetailsFollow") || event.equals("bbsdetailsLike")
                || event.equals("peopleFollow")||event.equals("typebbsFollow")) {
            beans = null;
            loadData();
        } else if (event.equals("bbsFollow")) {
            if (type.equals("1")) {
                beans = null;
                loadData();
            }
        }else if (event.equals("updataBbs")){
            if(type.equals("0")){
                beans = null;
                loadData();
            }
        }
    }
}
