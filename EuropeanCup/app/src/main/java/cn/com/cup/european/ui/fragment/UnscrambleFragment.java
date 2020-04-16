package cn.com.cup.european.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.cup.european.R;
import cn.com.cup.european.ui.activity.PostUnsActivity;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseFragment;

public class UnscrambleFragment extends BaseFragment {
    View contentView;
    Unbinder unbinder;
    @BindView(R.id.unscramble_slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.unscramble_viewPager)
    ViewPager viewPager;
    private ArrayList<Fragment> mFragmentsList;
    private String types[] = new String[]{"全部", " 精选", "关注", "收藏"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_unscramble, null);
        unbinder = ButterKnife.bind(this, contentView);
        loadView();
        loadData();
        loadView();
        loadData();
        return contentView;
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(contentView.findViewById(R.id.toplinear));
    }

    @Override
    protected void loadData() {
        mFragmentsList = new ArrayList<Fragment>();
        for (int i = 0; i < types.length; i++) {
            BaseFragment baseFragment = new UnscrambleListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", i + "");
            baseFragment.setArguments(bundle);
            mFragmentsList.add(baseFragment);
        }
        slidingTabLayout.setViewPager(viewPager, types, getActivity(), mFragmentsList);
        viewPager.setOffscreenPageLimit(4);
    }
    @OnClick({R.id.post_unscramble})
    public void onClcik(View view){
        switch (view.getId()){
            case R.id.post_unscramble:
                Constants.goIntent(getContext(),PostUnsActivity.class);
                break;
        }
    }
}
