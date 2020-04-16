package cn.com.cup.european.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.DarenListBean;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class UnscrambleActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.unscramble_head)
    ImageView head;
    @BindView(R.id.unscramble_name)
    TextView name;
    @BindView(R.id.unscramble_followNum)
    TextView followNum;
    @BindView(R.id.unscramble_follow)
    ImageView follow;
    @BindView(R.id.unscramble_gname)
    TextView gname;
    @BindView(R.id.unscramble_img)
    ImageView img;
    @BindView(R.id.unscramble_t1Img)
    ImageView t1Img;
    @BindView(R.id.unscramble_t1Name)
    TextView t1Name;
    @BindView(R.id.unscramble_score)
    TextView score;
    @BindView(R.id.unscramble_time)
    TextView time;
    @BindView(R.id.unscramble_t2Img)
    ImageView t2Img;
    @BindView(R.id.unscramble_t2Name)
    TextView t2Name;
    @BindView(R.id.unscramble_content)
    WebView webView;
    @BindView(R.id.alllinear)
    LinearLayout allLinear;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.unscramble_pname)
    TextView pname;
    @BindView(R.id.unscramble_ptime)
    TextView ptime;
    @BindView(R.id.unscramble_v)
    ImageView v;
    private String id,userId;
    private int follownum = 0;
    boolean isLike = false;
    boolean isDaren = false;
    HttpModel httpModel = new HttpModel(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unscramble);
        hideStatueBar(0);
        ButterKnife.bind(this);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        id = getIntent().getStringExtra("id");
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("解读详情");
        init();
    }

    @Override
    protected void loadData() {
        httpModel.getBbsDetails(id,"1",10001);
    }

    @OnClick({R.id.unscramble_follow})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.unscramble_follow:
                if (!Constants.isLoging()) {
                    Constants.loading(this);
                    return;
                }
                httpModel.follow(userId, 10002);
                isLike = !isLike;
                follownum = isLike ? follownum + 1 : follownum - 1;
                followNum.setText(follownum + "人关注");
                if(isDaren){
                    follow.setImageResource(isLike ? R.drawable.people_follow_yes : R.drawable.daren_follow_yes);
                }else {
                    follow.setImageResource(isLike ? R.drawable.people_follow_yes : R.drawable.people_follow);
                }
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
                    object = object.getJSONObject("data");
                    userId = object.getString("userId");
                    ImageSelectUtil.showImg(head, object.getString("headimg"));
                    name.setText(object.getString("name"));
                    follownum = object.getInt("num");
                    followNum.setText(follownum + "人关注");
                    isLike = object.getString("isLike").equals("1");
                    allLinear.setVisibility(View.VISIBLE);
                    gname.setText(object.getString("mname"));
                    img.setImageResource(object.getString("hit").equals("1")?R.drawable.unscramble_red:R.drawable.unscramble_black);
                    ImageSelectUtil.showImg(t1Img,object.getString("t1img"));
                    t1Name.setText(object.getString("t1name"));
                    score.setText(object.getString("score"));
                    time.setText(object.getString("time"));
                    ImageSelectUtil.showImg(t2Img,object.getString("t2img"));
                    t2Name.setText(object.getString("t2name"));
                    webView.loadData(getHtmlData(object.getString("content")), "text/html; charset=utf-8", null);
                    switch (object.getString("predict")){
                        case "1":
                            radioGroup.check(R.id.radio_zd);
                            break;
                        case "2":
                            radioGroup.check(R.id.radio_ps);
                            break;
                        case "3":
                            radioGroup.check(R.id.radio_kd);
                            break;
                    }
                    pname.setText(object.getString("title"));
                    ptime.setText(object.getString("fatime"));
                    v.setVisibility(object.getString("type").equals("1")?View.VISIBLE:View.GONE);
                    isDaren = object.getString("type").equals("1");
                    if(isDaren){
                        follow.setImageResource(isLike ? R.drawable.people_follow_yes : R.drawable.daren_follow_yes);
                    }else {
                        follow.setImageResource(isLike ? R.drawable.people_follow_yes : R.drawable.people_follow);
                    }
                    if(Constants.isLoging()){
                        if(Constants.user.getId().equals(userId)){
                            follow.setVisibility(View.GONE);
                        }
                    }
                } else if (action == 10002) {
                    EventBus.getDefault().post("unscrambleFollow");
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

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
    private void init() {
//        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
// 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

//支持插件
        webSettings.setPluginState(WebSettings.PluginState.ON);
//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
// 解决对某些标签的不支持出现白屏
        webSettings.setDomStorageEnabled(true);
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webView.setWebViewClient(new MyWebviewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
    }

    /**
     * 重写WebViewClient
     */
    private class MyWebviewClient extends WebViewClient {
        //        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //微信H5支付核心代码
            if (url.startsWith("weixin://")
                    || url.startsWith("alipays://")
                    || url.startsWith("mqqapi://")) {
                //打开本地App进行支付
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        //处理https请求
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
            handler.proceed();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void handleEvent(String event) {
        if (event.equals("loading") ){
            if(Constants.user.getId().equals(userId)){
                follow.setVisibility(View.GONE);
            }
        }
    }
}
