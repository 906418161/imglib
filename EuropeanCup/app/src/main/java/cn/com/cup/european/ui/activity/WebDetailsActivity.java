package cn.com.cup.european.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.util.base.BaseActivity;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebDetailsActivity extends BaseActivity {
    @BindView(R.id.newsweb)
    WebView webView;
    @BindView(R.id.tv_title_name)
    TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webdetails);
        ButterKnife.bind(this);
        hideStatueBar(0);
        init();
        //方式2：加载apk包中的html页面
//        webView.loadUrl(getIntent().getStringExtra("url"));
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
//        tvTitle.setText(title);
        webView.loadUrl(url);
//        webView.loadData(url,
//                "text/html; charset=UTF-8", null);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                tvTitle.setText(title);
            }
        });
    }

    @Override
    protected void loadView() {

    }

    @Override
    protected void loadData() {

    }

    private void init() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        //声明WebSettings子类
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
        webView.setWebViewClient(webViewClient);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });

    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView wv, String url) {
            if (url == null)
                return false;

            try {
                if (url.startsWith("weixin://") //微信
                        || url.startsWith("alipays://") //支付宝
                        || url.startsWith("mailto://") //邮件
                        || url.startsWith("tel://")//电话
                        || url.startsWith("dianping://")//大众点评
                        || url.startsWith("baiduboxlite://")//百度
                        || url.startsWith("baiduboxapp://")//百度
                    //其他自定义的scheme
                        ) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            }
            //处理http和https开头的url
            wv.loadUrl(url);
            return true;
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
