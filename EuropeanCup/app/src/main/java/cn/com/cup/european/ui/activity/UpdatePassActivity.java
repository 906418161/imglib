package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;
import cn.com.imageselect.widget.CountDownTextView;

public class UpdatePassActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.loading_oldpass)
    EditText oldpass;
    @BindView(R.id.loading_pass)
    EditText pass;
    @BindView(R.id.loading_newpass)
    EditText newpass;
    @BindView(R.id.loading_btn)
    CountDownTextView codebtn;
    @BindView(R.id.loading_code)
    EditText code;
    @BindView(R.id.loading_oldpass_delete)
    ImageView oldpassdelete;
    @BindView(R.id.loading_pass_delete)
    ImageView passdelete;
    @BindView(R.id.loading_newpass_delete)
    ImageView newpassdelete;
    @BindView(R.id.loading_code_delete)
    ImageView codedelete;
    HttpModel httpModel = new HttpModel(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass);
        ButterKnife.bind(this);
        hideStatueBar(0);
        loadView();
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        codebtn.setNormalText("获取验证码")
                .setCountDownText("重新获取(", ")")
                .setCloseKeepCountDown(true)//关闭页面保持倒计时开关
                .setCountDownClickable(false)//倒计时期间点击事件是否生效开关
                .setShowFormatTime(true)//是否格式化时间
                .setOnCountDownFinishListener(new CountDownTextView.OnCountDownFinishListener() {
                    @Override
                    public void onFinish() {
//                        countView.setBackground(getResources().getDrawable(R.drawable.rectangle_bagred));
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgressDialog("请稍后");
                        httpModel.getSMS(Constants.user.getUserMobile(), 10001);
                    }
                });
//        setOnclick(oldpass, oldpassdelete);
//        setOnclick(pass, passdelete);
//        setOnclick(newpass, newpassdelete);
//        setOnclick(code, codedelete);
    }


    @Override
    protected void loadData() {

        if (oldpass.getText().toString().equals("")) {
            showToast("请输入旧密码");
            return;
        }
        if (pass.getText().toString().equals("")) {
            showToast("请输入新密码");
            return;
        }
        if (newpass.getText().toString().equals("")) {
            showToast("请再次输入新密码");
            return;
        }
        if (!pass.getText().toString().equals(newpass.getText().toString())) {
            showToast("两次输入的新密码不一致");
            return;
        }
        if (code.getText().toString().equals("")) {
            showToast("请输入验证码");
            return;
        }
        showProgressDialog("密码修改中");
        httpModel.updatePass(Constants.user.getId(), oldpass.getText().toString(),
                pass.getText().toString(), code.getText().toString(), 10002);
    }

    @OnClick({R.id.loading_load})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loading_load:
                loadData();
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
                    codebtn.startCountDown(59);
                } else if (action == 10002) {
                    showToast("密码修改成功");
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

    private void setOnclick(EditText editText, ImageView imageView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if (s.toString().equals("")) {
                    imageView.setVisibility(View.INVISIBLE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }
}
