package cn.com.cup.european.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.cup.european.R;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.imageselect.widget.MyViewPager;

public class RankFragment extends BaseFragment {
    View contentView;
    Unbinder unbinder;
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.rank_slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.rank_viewPager)
    ViewPager viewPager;
    private ArrayList<Fragment> mFragmentsList;
    private String types[] = new String[]{"积分榜", " 射手榜", "助攻榜"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_rank, null);
        unbinder = ButterKnife.bind(this, contentView);
        loadView();
        loadData();
        return contentView;
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(contentView.findViewById(R.id.toplinear));
        title.setText("排行榜");
    }

    @Override
    protected void loadData() {
        mFragmentsList = new ArrayList<Fragment>();
//        BaseFragment fragment = RankListFragment.newInstance((MyViewPager) viewPager, "0");
//        mFragmentsList.add(fragment);
//        fragment = IntegralFragment.newInstance((MyViewPager) viewPager);
//        mFragmentsList.add(fragment);
//        fragment = RankListFragment.newInstance((MyViewPager) viewPager, "1");
//        mFragmentsList.add(fragment);
        BaseFragment fragment = new IntegralFragment();
        mFragmentsList.add(fragment);
        fragment = new RankListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "1");
        fragment.setArguments(bundle);
        mFragmentsList.add(fragment);
        fragment = new RankListFragment();
        bundle = new Bundle();
        bundle.putString("type", "2");
        fragment.setArguments(bundle);
        mFragmentsList.add(fragment);
        slidingTabLayout.setViewPager(viewPager, types, getActivity(), mFragmentsList);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                ((MyViewPager) viewPager).resetHeight(i);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
