package cn.com.cup.european.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.NewsBean;
import cn.com.cup.european.bean.NewsCommentBean;
import cn.com.cup.european.ui.adapter.NewsAdapter;
import cn.com.cup.european.ui.adapter.NewsCommentAdapter;
import cn.com.cup.european.util.AndroidBug5497Workaround;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class NewsDetailsActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.newsdetails_webview)
    WebView webView;
    @BindView(R.id.newsdetails_num)
    TextView num;
    @BindView(R.id.newsdetails_listview)
    ListView listView;
    @BindView(R.id.newsdetails_edittext)
    EditText editText;
    @BindView(R.id.newsdetails_like)
    ImageView like;
    private String keyWord;
    NewsCommentAdapter adapter;
    List<NewsCommentBean> beans;
    private String id;
    HttpModel httpModel = new HttpModel(this);
    boolean isCollection = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetails);
        AndroidBug5497Workaround.assistActivity(this);
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
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("新闻详情");
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“发送”键*/
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    keyWord = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(keyWord)) {
                        showToast("请输入要内容");
                        return true;
                    }
                    if (!Constants.isLoging()) {
                        Constants.loading(NewsDetailsActivity.this);
                        hideSoftKeyboard();
                        return true;
                    }
                    httpModel.addPl(id, editText.getText().toString(), 10002);
                    hideSoftKeyboard();
                    return true;
                }
                return false;
            }
        });
        init();
        id = getIntent().getStringExtra("id");
        webView.loadUrl("http://139.9.186.134/news/?id=" + id);
    }

    @Override
    protected void loadData() {
        httpModel.getPlList(id, 10001);
    }


    @OnClick({R.id.newsdetails_like})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newsdetails_like:
                if (!Constants.isLoging()) {
                    Constants.loading(this);
                    return;
                }
                isCollection = !isCollection;
                like.setImageResource(isCollection ? R.drawable.news_like_yes : R.drawable.news_like);
                httpModel.newCollection(id, 10003);
                break;
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    private void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    object = object.getJSONObject("data");
                    num.setText("评论(" + object.getString("num") + ")");
                    isCollection = object.getString("isCollection").equals("1");
                    like.setImageResource(isCollection ? R.drawable.news_like_yes : R.drawable.news_like);
                    beans = JSON.parseArray(object.getString("list"), NewsCommentBean.class);
                    adapter = new NewsCommentAdapter(beans, this);
                    listView.setAdapter(adapter);
                } else if (action == 10002) {
                    loadData();
                } else if (action == 10003) {

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
        if (event.equals("loading")) {
            loadData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }
}
