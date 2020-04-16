package cn.com.cup.european.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.util.base.BaseActivity;

public class AboutMeActivity extends BaseActivity {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.version)
    TextView version;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        hideStatueBar(1);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void loadView() {

    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.about_ys,R.id.about_yh})
    public void onClick(View view){
        Intent intent = new Intent(this,WebDetailsActivity.class);
        switch (view.getId()){
            case R.id.about_ys:
                intent.putExtra("url",
                        "https://nbayd.github.io/ouqiuys/ys.html");//华为
                intent.putExtra("title","隐私协议");
                break;
            case R.id.about_yh:
                intent.putExtra("url",
                        "https://nbayd.github.io/ouqiuyf/yf.html");//华为
                intent.putExtra("title","用户协议");
                break;
        }
        this.startActivity(intent);
    }
    private void init() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("关于我们");
        version.setText("版本信息: V"+getVersion());
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }
}
