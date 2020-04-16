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
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

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
import cn.com.cup.european.bean.RankListBean;
import cn.com.cup.european.ui.adapter.HomeBJAdapter;
import cn.com.cup.european.ui.adapter.UnsListAdapter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class UnscrambleListFragment extends BaseFragment implements RequestCallbackListener {
    View contentView;
    Unbinder unbinder;
    @BindView(R.id.unscramblelist_top)
    RelativeLayout top;
    @BindView(R.id.unscramblelist_list)
    ListView listView;
    @BindView(R.id.unscramblelist_refreshLayout)
    SmartRefreshLayout refreshLayout;
    List<BbsListBean> beans;
    UnsListAdapter adapter;
    HttpModel httpModel = new HttpModel(this);
    private int pageNum = 1;
    private String type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_unscramblelist, null);
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
        top.setVisibility(View.GONE);
        type = getArguments().getString("type");
    }

    @Override
    protected void loadData() {
        if (beans == null) {
            pageNum = 1;
            beans = new ArrayList<>();
            adapter = new UnsListAdapter(beans, getContext());
            adapter.setType(type);
            listView.setAdapter(adapter);
        }
        switch (type) {
            case "0":
                httpModel.getNotesList("1", String.valueOf(pageNum), 10001);
                break;
            case "1":
                httpModel.getNotesList("2", String.valueOf(pageNum), 10001);
                break;
            case "2":
                if (Constants.isLoging()) {
                    httpModel.getFansNotes(String.valueOf(pageNum), 10001);
                }
                break;
            case "3":
                if (Constants.isLoging()) {
                    httpModel.getCollectionNotes(String.valueOf(pageNum), 10001);
                }
                break;
        }
    }


    @Subscribe
    public void handleEvent(String event) {
        if (event.equals("loading") || event.equals("peopleFollow") ||
                event.equals("darenFollow") || event.equals("unscrambleFollow")
                || event.equals("bbsFollow")) {
            beans = null;
            loadData();
        } else if (event.equals("signout")) {
            top.setVisibility(View.VISIBLE);
        } else if (event.equals("unsFollow")) {
            if (type.equals("2")) {
                beans = null;
                loadData();
            }
        } else if (event.equals("Collection")) {
            if (type.equals("3")) {
                beans = null;
                loadData();
            }
        }else if(event.equals("updatePostuns")){
            if (type.equals("0")) {
                beans = null;
                loadData();
            }
        }
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
                    List<BbsListBean> bbsListBeans = JSON.parseArray(object.getString("rows"), BbsListBean.class);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(getActivity());
    }
}
