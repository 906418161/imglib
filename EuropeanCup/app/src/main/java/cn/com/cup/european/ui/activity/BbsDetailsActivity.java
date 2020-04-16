package cn.com.cup.european.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.BbsListBean;
import cn.com.cup.european.bean.NewsCommentBean;
import cn.com.cup.european.ui.adapter.NewsCommentAdapter;
import cn.com.cup.european.util.AndroidBug5497Workaround;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.ImgAdapter;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class BbsDetailsActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.bbsdetails_head)
    ImageView head;
    @BindView(R.id.bbsdetails_follow)
    ImageView follow;
    @BindView(R.id.bbsdetails_likeImg)
    ImageView likeImg;
    @BindView(R.id.bbsdetails_img)
    ImageView img;
    @BindView(R.id.bbsdetails_name)
    TextView name;
    @BindView(R.id.bbsdetails_title)
    TextView tTitle;
    @BindView(R.id.bbsdetails_content)
    TextView content;
    @BindView(R.id.bbsdetails_theme)
    TextView theme;
    @BindView(R.id.bbsdetails_comment)
    TextView comment;
    @BindView(R.id.bbsdetails_likeNum)
    TextView likeNum;
    @BindView(R.id.bbsdetails_img_card)
    CardView cardView;
    @BindView(R.id.bbsdetails_grid)
    GridView gridView;
    @BindView(R.id.bbsdetails_like)
    LinearLayout like;
    @BindView(R.id.bbsdetails_buttom)
    LinearLayout buttom;
    @BindView(R.id.bbsdetails_time)
    TextView time;
    @BindView(R.id.bbsdetails_edittext)
    EditText editText;
    @BindView(R.id.bbsdetails_num)
    TextView num;
    @BindView(R.id.bbsdetails_listview)
    ListView listView;
    BbsListBean bean;
    ImageSelectUtil imageSelectUtil;
    private String keyWord;
    NewsCommentAdapter adapter;
    List<NewsCommentBean> beans;
    HttpModel httpModel = new HttpModel(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbsdetails);
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
        title.setText("详情");
        bean = (BbsListBean) getIntent().getSerializableExtra("bean");
        imageSelectUtil = new ImageSelectUtil(this);
        setView();
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
                        Constants.loading(BbsDetailsActivity.this);
                        return true;
                    }
                    httpModel.bbsPl(bean.getBid(), editText.getText().toString(), 10002);
                    hideSoftKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void loadData() {
        httpModel.getBbsDetails(bean.getBid(), "2", 10001);
    }

    private void setView() {
        ImageSelectUtil.showImg(head, bean.getHeadimg());
        name.setText(bean.getName());
        if (bean.getIsLike().equals("0")) {
            follow.setImageResource(R.drawable.people_follow);
        } else {
            follow.setImageResource(R.drawable.people_follow_yes);
        }
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Constants.isLoging()){
                    Constants.loading(BbsDetailsActivity.this);
                    return;
                }
                httpModel.follow(bean.getUserId(), 10003);
                bean.setIsLike(bean.getIsLike().equals("0") ? "1" : "0");
                if (bean.getIsLike().equals("0")) {
                    follow.setImageResource(R.drawable.people_follow);
                } else {
                    follow.setImageResource(R.drawable.people_follow_yes);
                }
            }
        });
        if (Constants.isLoging()) {
            if (bean.getUserId().equals(Constants.user.getId())) {
                follow.setVisibility(View.GONE);
            }
        }
        tTitle.setText(bean.getTitle());
        content.setText(bean.getContent());
        time.setText(bean.getTime());
        theme.setText(bean.getTheme());
        comment.setText(bean.getPl());
        likeNum.setText(bean.getZan());
        if (bean.getIsZan().equals("0")) {
            likeImg.setImageResource(R.drawable.newscommet_like);
        } else {
            likeImg.setImageResource(R.drawable.newscommet_like_yes);
        }
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Constants.isLoging()){
                    Constants.loading(BbsDetailsActivity.this);
                    return;
                }
                bean.setIsZan(bean.getIsZan().equals("0") ? "1" : "0");
                bean.setZan(bean.getIsZan().equals("0") ?
                        String.valueOf(Integer.parseInt(bean.getZan()) - 1) :
                        String.valueOf(Integer.parseInt(bean.getZan()) + 1));
                likeNum.setText(bean.getZan());
                if (bean.getIsZan().equals("0")) {
                    likeImg.setImageResource(R.drawable.newscommet_like);
                } else {
                    likeImg.setImageResource(R.drawable.newscommet_like_yes);
                }
                httpModel.like(bean.getBid(), 10004);
            }
        });
        if (bean.getImgs().size() > 0) {
            if (bean.getImgs().size() == 1) {
                gridView.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
                ImageSelectUtil.showImg(img, bean.getImgs().get(0));
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageSelectUtil.lookImage(bean.getImgs().get(0));
                    }
                });
            } else {
                cardView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                gridView.setAdapter(new ImgAdapter(bean.getImgs(), this));
            }
        } else {
            cardView.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
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

    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    object = object.getJSONObject("data");
                    num.setText("评论(" + object.getString("pl") + ")");
                    bean.setIsLike(object.getString("isLike"));
                    if (bean.getIsLike().equals("0")) {
                        follow.setImageResource(R.drawable.people_follow);
                    } else {
                        follow.setImageResource(R.drawable.people_follow_yes);
                    }
                    beans = JSON.parseArray(object.getString("list"), NewsCommentBean.class);
                    adapter = new NewsCommentAdapter(beans, this);
                    listView.setAdapter(adapter);
                } else if (action == 10002) {
                    loadData();
                } else if (action == 10003) {
                    EventBus.getDefault().post("bbsdetailsFollow");
                } else if (action == 10004) {
                    EventBus.getDefault().post("bbsdetailsLike");
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
        if (event.equals("loading") || event.equals("signout") ||
                event.equals("darenFollow") || event.equals("unscrambleFollow")
                || event.equals("bbsdetailsFollow") || event.equals("bbsdetailsLike")
                || event.equals("peopleFollow")) {
            loadData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.bbsdetails_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bbsdetails_head:
                if (Constants.isLoging()) {
                    if (Constants.user.getId().equals(bean.getUserId())) {
                        return;
                    }
                }
                Constants.goIntent(this, PeopleDetailsActivity.class, "id", bean.getUserId());
                break;
        }
    }

}
