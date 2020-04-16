package cn.com.cup.european.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.dialog.LogOutdialog;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class PostUnsActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.postuns_name)
    TextView name;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.postuns_title)
    EditText pTitle;
    @BindView(R.id.postuns_content)
    EditText content;
    HomeHotBean bean;
    LogOutdialog dialog;
    HttpModel httpModel = new HttpModel(this);
    private String predict = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postuns);
        ButterKnife.bind(this);
        hideStatueBar(0);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("发表解读");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_zd:
                        predict = "1";
                        break;
                    case R.id.radio_ps:
                        predict = "2";
                        break;
                    case R.id.radio_kd:
                        predict = "3";
                        break;
                }
            }
        });
        dialog = new LogOutdialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog("请稍后");
                httpModel.postReleasenote(bean.getMid(), pTitle.getText().toString(),
                        content.getText().toString(), predict, 10001);
                dialog.dismiss();
            }
        }, this);
        dialog.setTitle("确定发表?");
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.postuns_sub, R.id.postuns_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.postuns_sub:
                if (!Constants.isLoging()) {
                    Constants.loading(this);
                    return;
                }
                if (bean == null) {
                    showToast("请选择比赛");
                    return;
                }
                if (pTitle.getText().toString().equals("")) {
                    showToast("请输入标题");
                    return;
                }
                if (content.getText().toString().equals("")) {
                    showToast("请输入分析思路");
                    return;
                }
                dialog.show();
                break;
            case R.id.postuns_name:
                Intent intent = new Intent(this, MatchListActivity.class);
                startActivityForResult(intent, 3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                bean = (HomeHotBean) data.getSerializableExtra("bean");
                name.setText(bean.getT1name() + "VS" + bean.getT2name());
            }
        }
    }

    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    EventBus.getDefault().post("updatePostuns");
                    showToast("发布成功");
                    finish();
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
}
