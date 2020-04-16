package cn.com.cup.european.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.sj.mymodule.BaseModuleUtil;

import cn.com.cup.european.R;
import cn.com.cup.european.bean.User;
import cn.com.cup.european.util.Constants;


public class EuropeanCupAppliction extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Constants.user = Constants.getUser(this);
        if (Constants.user == null) {
            Constants.user = new User();
            Constants.user.setId("");
        }
//        FlowManager.init(this);
//        AVOSCloud.initialize(this,Constants.APP_ID,Constants.APP_KEY);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
//        SDKInitializer.initialize(this);
//        自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
//        包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
//        SDKInitializer.setCoordType(CoordType.BD09LL);
        BaseModuleUtil.init(this, Constants.APP_ID, Constants.APP_KEY);

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.transparent, android.R.color.white);//全局设置主题颜色
                layout.setPrimaryColorsId(R.color.transparent, R.color.tblack);
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800)//default=devicescreendimensions内存缓存文件的最大长宽
                .threadPoolSize(3)//default线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)//default设置当前线程的优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO)//default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(2 * 1024 * 1024)//内存缓存的最大值
                .memoryCacheSizePercentage(13)//default
                .diskCacheSize(50 * 1024 * 1024)//50Mbsd卡(本地)缓存的最大值
                .diskCacheFileCount(100)//可以缓存的文件数量
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())//default
                .writeDebugLogs()//打印debuglog
                .build();//开始构建
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 主要是添加下面这句代码
        MultiDex.install(this);
    }
}
