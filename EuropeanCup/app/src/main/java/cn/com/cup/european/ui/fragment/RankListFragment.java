package cn.com.cup.european.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.IntegralLeftBean;
import cn.com.cup.european.bean.RankListBean;
import cn.com.cup.european.ui.adapter.IntegralLeftAdapter;
import cn.com.cup.european.ui.adapter.IntegralRightAdapter;
import cn.com.cup.european.ui.adapter.RankListAdapter;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;
import cn.com.imageselect.widget.MyViewPager;

@SuppressLint("ValidFragment")
public class RankListFragment extends BaseFragment implements RequestCallbackListener {
    View contentView;
    Unbinder unbinder;
    MyViewPager viewPager;
    String type;
    @BindView(R.id.ranklist_type)
    TextView tType;
    @BindView(R.id.ranklist_list)
    ListView listView;
    @BindView(R.id.ranklist_refreshLayout)
    SmartRefreshLayout refreshLayout;
    RankListAdapter adapter;
    List<RankListBean> beans;
    HttpModel httpModel = new HttpModel(this);
    private int pageNum = 1;
//    public static RankListFragment newInstance(MyViewPager viewPager, String type) {
//        Bundle bundle = new Bundle();
//        bundle.putString("type", type);
//        RankListFragment fragment = new RankListFragment(viewPager);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    public RankListFragment(MyViewPager viewPager) {
//        this.viewPager = viewPager;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_ranklist, null);
        unbinder = ButterKnife.bind(this, contentView);
        loadView();
        loadData();
        if (type.equals("1")) {
            tType.setText("进球");
//            viewPager.setObjectForPosition(contentView, 0);//0代表tab的位置 0,1,2,3
        } else {
            tType.setText("助攻数");
//            viewPager.setObjectForPosition(contentView, 2);//0代表tab的位置 0,1,2,3
        }
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void loadView() {
        type = getArguments().getString("type");
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(20000/*,false*/);//传入false表示刷新失败
                beans=null;
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
            adapter = new RankListAdapter(beans, getContext());
            listView.setAdapter(adapter);
        }
        httpModel.getRankingShoot(type, String.valueOf(pageNum), 10001);
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
                    List<RankListBean> rankListBeans = JSON.parseArray(object.getString("rows"), RankListBean.class);
                    beans.addAll(rankListBeans);
                    adapter.notifyDataSetChanged();
                    if (rankListBeans.size() >= 10) {
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
