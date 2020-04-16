package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;
import cn.com.imageselect.widget.CountDownTextView;

public class ForgetPassActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.loading_phone)
    EditText phone;
    @BindView(R.id.loading_code)
    EditText code;
    @BindView(R.id.loading_pass)
    EditText pass;
    @BindView(R.id.loading_eyes)
    CheckBox eyes;
    @BindView(R.id.loading_btn)
    CountDownTextView codebtn;
    @BindView(R.id.loading_pass_delete)
    ImageView passdelet;
    @BindView(R.id.loading_phone_delete)
    ImageView phonedelet;
    @BindView(R.id.loading_code_delete)
    ImageView codedelet;
    HttpModel httpModel = new HttpModel(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        ButterKnife.bind(this);
        hideStatueBar(0);
        loadView();
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        eyes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    //默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
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
                        if(phone.getText().toString().equals("")){
                            showToast("请输入手机号");
                            return;
                        }else if(phone.getText().toString().length()<11){
                            showToast("请输入正确的手机号");
                            return;
                        }
                        showProgressDialog("请稍后");
                        httpModel.getSMS(phone.getText().toString(),10001);
                    }
                });
//        setOnclick(phone,phonedelet);
//        setOnclick(code,codedelet);
//        setOnclick(pass,passdelet);
    }

    @Override
    protected void loadData() {
        if(phone.getText().toString().equals("")){
            showToast("请输入账号");
            return;
        }
        if(pass.getText().toString().equals("")){
            showToast("请输入密码");
            return;
        }
        if(code.getText().toString().equals("")){
            showToast("请输入密码");
            return;
        }
        showProgressDialog("密码修改中");
        httpModel.forgetPass(phone.getText().toString(),pass.getText().toString(),code.getText().toString(),10002);
    }
    @OnClick({R.id.loading_load})
    public void onClick(View view){
        switch (view.getId()){
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
            if(object.getString("code").equals("1")){
                if(action==10001){
                    codebtn.startCountDown(59);
                }else if(action==10002){
                    showToast("密码已修改,请登录");
                    finish();
                }
            }else {
                showToast(object.getString("msg"));
            }
        }catch (Exception e){
            showToast("网络不给力哦");
        }
    }

    @Override
    public void onErr(String errMessage) {
        try {
            dismissProgressDialog();
            showToast("网络不给力哦");
        }catch (Exception e){

        }
    }

    private void setOnclick(EditText editText,ImageView imageView){
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
                if(s.toString().equals("")){
                    imageView.setVisibility(View.INVISIBLE);
                }else {
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
