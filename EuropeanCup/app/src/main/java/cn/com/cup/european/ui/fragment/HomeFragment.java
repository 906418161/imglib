package cn.com.cup.european.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

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
import cn.com.cup.european.R;
import cn.com.cup.european.bean.BbsListBean;
import cn.com.cup.european.bean.HomeCupBean;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.bean.HomePeopleBean;
import cn.com.cup.european.bean.HomeTipBean;
import cn.com.cup.european.bean.User;
import cn.com.cup.european.ui.activity.AuthenticationActivity;
import cn.com.cup.european.ui.activity.CupHisActivity;
import cn.com.cup.european.ui.activity.DarenListActivity;
import cn.com.cup.european.ui.activity.HomeNewsActivity;
import cn.com.cup.european.ui.activity.MatchActivity;
import cn.com.cup.european.ui.activity.NewsActivity;
import cn.com.cup.european.ui.activity.UnscrambleActivity;
import cn.com.cup.european.ui.adapter.HomeBJAdapter;
import cn.com.cup.european.ui.adapter.HomeCupAdapter;
import cn.com.cup.european.ui.adapter.HomeGridAdapter;
import cn.com.cup.european.ui.adapter.HomeHotAdapter;
import cn.com.cup.european.ui.adapter.HomePeopleAdapter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class HomeFragment extends BaseFragment implements RequestCallbackListener {
    View contentView;
    Unbinder unbinder;
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.tv_title_back)
    RelativeLayout back;
    @BindView(R.id.home_banner)
    MZBannerView bannerView;
    @BindView(R.id.home_gridview)
    GridView gridView;
    @BindView(R.id.home_hot)
    RecyclerView hot;
    @BindView(R.id.home_people)
    RecyclerView people;
    @BindView(R.id.home_cupgridview)
    GridView cupGridview;
    @BindView(R.id.home_zjbanner)
    MZBannerView zjbanner;
    @BindView(R.id.home_hotbj)
    ListView hotbj;
    @BindView(R.id.home_renzhen)
    TextView rezhen;
    private List<String> bannerUrl, bannerId;
    HomeGridAdapter adapter;
    List<HomeTipBean> tipBeans;
    HomeHotAdapter hotAdapter;
    List<HomeHotBean> hotBeans;
    HomePeopleAdapter peopleAdapter;
    List<HomePeopleBean> peopleBeans;
    HttpModel httpModel = new HttpModel(this);
    HomeCupAdapter cupAdapter;
    List<HomeCupBean> beans;
    List<BbsListBean> bbsListBeans, bjBeans;
    HomeBJAdapter bjAdapter;
    private String isRz;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_home, null);
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
        getStatusBarHeight(contentView.findViewById(R.id.toplinear));
        title.setText("首页");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hot.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        people.setLayoutManager(layoutManager1);
        back.setVisibility(View.GONE);
        bannerView.setIndicatorVisible(true);
//        bannerView.setIndicatorRes(R.drawable.o_bagg, R.drawable.o_bagr);
        bannerView.setDelayedTime(5000);
        zjbanner.setIndicatorVisible(true);
        zjbanner.setIndicatorRes(R.drawable.o_bagr, R.drawable.o_bagg);
        zjbanner.setDelayedTime(5000);
    }

    @Override
    protected void loadData() {
//        httpModel.getHomeExperts(10001);
        httpModel.getHotTheme(10002);
//        httpModel.getHotMatch(10003);
        httpModel.getHomeBanner(10004);
//        httpModel.getEurocupHistory(String.valueOf(4),10005);
        if (Constants.isLoging()) {
            httpModel.getIsCertification(10006);
        }
        httpModel.getHomeUnscramble(10007);
        httpModel.getHotNote(10008);
    }


    @OnClick({R.id.home_rank, R.id.home_news, R.id.home_match, R.id.home_matchlist
            , R.id.home_cupmore, R.id.home_renzhen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_rank:
//                EventBus.getDefault().post("home_rank");
//                Constants.goIntent(getContext(), DarenListActivity.class);
                Constants.goIntent(getContext(), CupHisActivity.class);
                break;
            case R.id.home_news:
//                Constants.goIntent(getContext(), NewsActivity.class);
                Constants.goIntent(getContext(), DarenListActivity.class);
                break;
            case R.id.home_match:
//                EventBus.getDefault().post("home_match");
                Constants.goIntent(getContext(), HomeNewsActivity.class);
                break;
            case R.id.home_matchlist:
                Constants.goIntent(getContext(), MatchActivity.class);
                break;
            case R.id.home_cupmore:
                Constants.goIntent(getContext(), CupHisActivity.class);
                break;
            case R.id.home_renzhen:
                if(isRz.equals("0")){
                    Constants.goIntent(getContext(), AuthenticationActivity.class);
                }
                break;
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
    public void onPause() {
        super.onPause();
        bannerView.pause();//暂停轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        bannerView.start();//开始轮播
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
                } else if (action == 10002) {
                    tipBeans = JSON.parseArray(object.getString("data"), HomeTipBean.class);
                    adapter = new HomeGridAdapter(tipBeans, getContext());
                    gridView.setAdapter(adapter);
                } else if (action == 10003) {
                    hotBeans = JSON.parseArray(object.getString("data"), HomeHotBean.class);
                    hotAdapter = new HomeHotAdapter(hotBeans, getContext());
                    hot.setAdapter(hotAdapter);
                } else if (action == 10004) {
                    bannerUrl = new ArrayList<>();
                    bannerId = new ArrayList<>();
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        bannerUrl.add(array.getJSONObject(i).getString("img"));
                        bannerId.add(array.getJSONObject(i).getString("id"));
                    }
                    bannerView.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
                        @Override
                        public void onPageClick(View view, int i) {
                            Intent intent = new Intent(getContext(), UnscrambleActivity.class);
                            intent.putExtra("id", bannerId.get(i));
                            startActivity(intent);
                        }
                    });
                    // 设置数据
                    bannerView.setPages(bannerUrl, new MZHolderCreator<TopBeanner>() {
                        @Override
                        public TopBeanner createViewHolder() {
                            return new TopBeanner();
                        }
                    });
                } else if (action == 10005) {
                    beans = JSON.parseArray(object.getJSONObject("data").getString("rows"), HomeCupBean.class);
                    cupAdapter = new HomeCupAdapter(beans, getContext());
                    cupGridview.setAdapter(cupAdapter);
                } else if (action == 10006) {
                    isRz = object.getString("data");
                    rezhen.setText(isRz.equals("0") ? "立即认证" : "审核中");
                    rezhen.setBackgroundResource(isRz.equals("0") ? R.drawable.renzheng_red : R.drawable.renzheng_gray);
                } else if (action == 10007) {
                    bbsListBeans = JSON.parseArray(object.getString("data"), BbsListBean.class);
                    zjbanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
                        @Override
                        public void onPageClick(View view, int i) {
                            Intent intent = new Intent(getContext(), UnscrambleActivity.class);
                            intent.putExtra("id", bbsListBeans.get(i).getBid());
                            startActivity(intent);
                        }
                    });
                    // 设置数据
                    zjbanner.setPages(bbsListBeans, new MZHolderCreator<BbsBeanner>() {
                        @Override
                        public BbsBeanner createViewHolder() {
                            return new BbsBeanner();
                        }
                    });
                } else if (action == 10008) {
                    bjBeans = JSON.parseArray(object.getString("data"), BbsListBean.class);
                    bjAdapter = new HomeBJAdapter(bjBeans, getContext());
                    hotbj.setAdapter(bjAdapter);
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

    public class TopBeanner implements MZViewHolder<String> {
        private ImageView image;
        private View view;

        @Override
        public View createView(Context context) {
            if (view == null) {
                // 返回页面布局
                view = LayoutInflater.from(context).inflate(R.layout.view_hometop, null);
                image = view.findViewById(R.id.hometop_image);
            }
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            // 数据绑定
            showImage(image, data);
        }
    }


    public class BbsBeanner implements MZViewHolder<BbsListBean> {
        private ImageView image;
        private TextView title, zuo, you, name, time;
        private View view;

        @Override
        public View createView(Context context) {
            if (view == null) {
                // 返回页面布局
                view = LayoutInflater.from(context).inflate(R.layout.view_homedaren, null);
                title = view.findViewById(R.id.darenlist_title);
                zuo = view.findViewById(R.id.darenlist_gTitle);
                you = view.findViewById(R.id.darenlist_team);
                name = view.findViewById(R.id.darenlist_name);
                time = view.findViewById(R.id.darenlist_time);
                image = view.findViewById(R.id.darenlist_head);
            }
            return view;
        }

        @Override
        public void onBind(Context context, int position, BbsListBean data) {
            // 数据绑定
            showImage(image, data.getHeadimg());
            title.setText(data.getTitle());
            zuo.setText(data.getZuo());
            you.setText(data.getYou());
            name.setText(data.getName());
            time.setText(data.getTime());
        }
    }

    @Subscribe
    public void handleEvent(String event) {
        if (event.equals("loading")) {
            httpModel.getIsCertification(10006);
        } else if (event.equals("signout")) {
            rezhen.setText("立即认证");
            rezhen.setBackgroundResource(R.drawable.renzheng_red);
        } else if (event.equals("Authentication")) {
            httpModel.getIsCertification(10006);
        }
//        if (event.equals("loading") || event.equals("darenFollow")
//                || event.equals("signout") || event.equals("unscrambleFollow") || event.equals("bbsFollow")) {
////            httpModel.getHomeExperts(10001);
//        }
    }


}
