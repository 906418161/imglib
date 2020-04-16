package cn.com.cup.european.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sj.mymodule.BaseModuleUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.ui.fragment.BbsFragment;
import cn.com.cup.european.ui.fragment.HomeFragment;
import cn.com.cup.european.ui.fragment.MatchFragment;
import cn.com.cup.european.ui.fragment.OtherFragment;
import cn.com.cup.european.ui.fragment.RankFragment;
import cn.com.cup.european.ui.fragment.UnscrambleFragment;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.imageselect.util.fragment.Tab;
import cn.com.imageselect.util.fragment.TabFragmentHost;

public class MainActivity extends BaseActivity {

    private TabFragmentHost mTabHost;
    private LayoutInflater mInflater;
    private List<Tab> mTabs = new ArrayList<>();
    //    String[] tab = new String[]{"首页", "赛事", "排行榜", "发现", "我的"};
    String[] tab = new String[]{"首页", "排行榜","解读", "发现", "我的"};
    Map<String, ImageView> map = new HashMap<>();
    RelativeLayout framentrela;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LunchActivity.class);
        startActivity(intent);
        hideStatueBar(0);
        ButterKnife.bind(this);
        Constants.getPermission(Constants.internet, this);
        loadView();
        BaseModuleUtil.startActivity(this, new BaseModuleUtil.ImpStartLister() {
            @Override
            public void start() {
                //调用原来自己项目的跳转方法

            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    protected void loadView() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        framentrela = (RelativeLayout) findViewById(R.id.framerela);
        mInflater = LayoutInflater.from(this);
        mTabHost = (TabFragmentHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        Tab homeFragment = new Tab("首页", R.mipmap.ic_launcher, HomeFragment.class);
        Tab matchFragment = new Tab("赛事", R.mipmap.ic_launcher, MatchFragment.class);
        Tab unscrambleFragment = new Tab("解读", R.mipmap.ic_launcher, UnscrambleFragment.class);
        Tab rankFragment = new Tab("排行榜", R.mipmap.ic_launcher, RankFragment.class);
        Tab bbsFragment = new Tab("发现", R.mipmap.ic_launcher, BbsFragment.class);
        Tab otherFragment = new Tab("我的", R.mipmap.ic_launcher, OtherFragment.class);
        mTabs.add(homeFragment);
//        mTabs.add(matchFragment);
        mTabs.add(rankFragment);
        mTabs.add(unscrambleFragment);
        mTabs.add(bbsFragment);
        mTabs.add(otherFragment);
        for (int i = 0; i < mTabs.size(); i++) {
            View view = mInflater.inflate(R.layout.view_tab, null);
            ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
            map.put(tab[i], img);
            TextView tex = (TextView) view.findViewById(R.id.txt_indicator);
            img.setImageResource(mTabs.get(i).getIcon());
            tex.setText(mTabs.get(i).getTitle());

            mTabHost.addTab(mTabHost.newTabSpec(mTabs.get(i).getTitle()).setIndicator(view),
                    mTabs.get(i).getFragment(), null);
        }

        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_home:
                        mTabHost.setCurrentTab(0);
                        break;
//                    case R.id.tab_match:
//                        mTabHost.setCurrentTab(1);
//                        break;
                    case R.id.tab_rank:
                        mTabHost.setCurrentTab(1);
                        break;
                    case R.id.tab_unscramble:
                        mTabHost.setCurrentTab(2);
                        break;
                    case R.id.tab_bbs:
                        mTabHost.setCurrentTab(3);
                        break;
                    case R.id.tab_other:
                        mTabHost.setCurrentTab(4);
                        EventBus.getDefault().post("updaOther");
                        break;
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (Constants.getExternal(MainActivity.this)) {

            } else {
                Toast.makeText(MainActivity.this, "缺少必要权限", Toast.LENGTH_SHORT).show();
                finish();
            }
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
        if (event.equals("home_rank")) {
            radioGroup.check(R.id.tab_rank);
        } else if (event.equals("home_match")) {
            radioGroup.check(R.id.tab_match);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post("updaOther");
    }
}
