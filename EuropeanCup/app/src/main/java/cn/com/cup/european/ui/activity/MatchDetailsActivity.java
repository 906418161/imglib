package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.ui.fragment.MatchDetailStechnologyFragment;
import cn.com.cup.european.ui.fragment.MatchDetailsLineupFragment;
import cn.com.cup.european.ui.fragment.MatchDetailsTeamFragment;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.widget.MyViewPager;

public class MatchDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.matchdetails_t1Img)
    ImageView t1Img;
    @BindView(R.id.matchdetails_t2Img)
    ImageView t2Img;
    @BindView(R.id.matchdetails_t1Name)
    TextView t1Name;
    @BindView(R.id.matchdetails_t2Name)
    TextView t2Name;
    @BindView(R.id.matchdetails_score)
    TextView score;
    @BindView(R.id.matchdetails_slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.matchdetails_viewPager)
    ViewPager viewPager;
    private ArrayList<Fragment> mFragmentsList;
    private String types[] = new String[]{"赛前分析", " 比赛阵容", "技术统计"};
    private HomeHotBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchdetails);
        ButterKnife.bind(this);
        hideStatueBar(0);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("比赛详情");
        bean = (HomeHotBean) getIntent().getSerializableExtra("bean");
        ImageSelectUtil.showImg(t1Img, bean.getT1img());
        ImageSelectUtil.showImg(t2Img, bean.getT2img());
        t1Name.setText(bean.getT1name());
        t2Name.setText(bean.getT2name());
        score.setText(bean.getScore());
    }

    @Override
    protected void loadData() {
        mFragmentsList = new ArrayList<Fragment>();
        BaseFragment fragment = MatchDetailsTeamFragment.newInstance((MyViewPager) viewPager, bean.getMid());
        mFragmentsList.add(fragment);
        fragment = MatchDetailsLineupFragment.newInstance((MyViewPager) viewPager, bean.getMid());
        mFragmentsList.add(fragment);
        fragment = MatchDetailStechnologyFragment.newInstance((MyViewPager) viewPager, bean.getMid());
        mFragmentsList.add(fragment);
        slidingTabLayout.setViewPager(viewPager, types, this, mFragmentsList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ((MyViewPager) viewPager).resetHeight(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
