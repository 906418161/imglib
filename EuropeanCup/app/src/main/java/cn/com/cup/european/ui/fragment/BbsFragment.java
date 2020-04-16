package cn.com.cup.european.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.calendarview.Calendar;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.bean.HomePeopleBean;
import cn.com.cup.european.bean.HomeTipBean;
import cn.com.cup.european.ui.activity.PostActivity;
import cn.com.cup.european.ui.adapter.HomeGridAdapter;
import cn.com.cup.european.ui.adapter.HomeHotAdapter;
import cn.com.cup.european.ui.adapter.HomePeopleAdapter;
import cn.com.cup.european.ui.adapter.MatchAdapter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class BbsFragment extends BaseFragment implements RequestCallbackListener {
    View contentView;
    Unbinder unbinder;
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.tv_title_back)
    RelativeLayout back;
    @BindView(R.id.bbs_people)
    RecyclerView people;
    @BindView(R.id.bbs_slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.bbs_viewPager)
    ViewPager viewPager;
    @BindView(R.id.bbs_closse_img)
    ImageView closeImg;
    private ArrayList<Fragment> mFragmentsList;
    private String types[] = new String[]{"热帖", " 关注"};
    HomePeopleAdapter peopleAdapter;
    List<HomePeopleBean> peopleBeans;
    HttpModel httpModel = new HttpModel(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_bbs, null);
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
        title.setText("发现");
        back.setVisibility(View.GONE);
        getStatusBarHeight(contentView.findViewById(R.id.toplinear));
        mFragmentsList = new ArrayList<Fragment>();
        for (int i = 0; i < types.length; i++) {
            BaseFragment fragment = new BbsListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", i + "");
            fragment.setArguments(bundle);
            mFragmentsList.add(fragment);
        }
        slidingTabLayout.setViewPager(viewPager, types, getActivity(), mFragmentsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        people.setLayoutManager(layoutManager);
    }

    @Override
    protected void loadData() {
        httpModel.getBbsInterest(10001);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(getActivity());
    }

    @OnClick({R.id.bbs_post,R.id.bbs_closse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bbs_post:
                Constants.goIntent(getContext(), PostActivity.class);
                break;
            case R.id.bbs_closse:
                    people.setVisibility(people.getVisibility()==View.VISIBLE?
                    View.GONE:View.VISIBLE);
                closeImg.setImageResource(people.getVisibility()==View.VISIBLE?
                R.drawable.bbs_button:R.drawable.bbs_top);
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
                    peopleBeans = JSON.parseArray(object.getString("data"), HomePeopleBean.class);
                    peopleAdapter = new HomePeopleAdapter(peopleBeans, getContext());
                    people.setAdapter(peopleAdapter);
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

    @Subscribe
    public void handleEvent(String event) {
        if (event.equals("loading") || event.equals("signout")
                ||event.equals("bbsFollow")||event.equals("peopleFollow")) {
            loadData();
        }
    }
}
