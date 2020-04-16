package cn.com.cup.european.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.User;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class PassloadingActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.loading_phone)
    EditText phone;
    @BindView(R.id.loading_pass)
    EditText pass;
    @BindView(R.id.loading_eyes)
    CheckBox eyes;
    @BindView(R.id.loading_rember)
    CheckBox rember;
    @BindView(R.id.loading_wangji)
    TextView wangji;
    @BindView(R.id.loading_load)
    Button load;
    @BindView(R.id.loading_register)
    TextView register;
    @BindView(R.id.loading_phone_delete)
    ImageView phonedelete;
    @BindView(R.id.loading_pass_delete)
    ImageView passdelete;
    HttpModel httpModel = new HttpModel(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passloading);
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
                if (isChecked) {
                    //选择状态 显示明文--设置为可见的密码
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        if (Constants.getString("rember", this).equals("isRember")) {
            rember.setChecked(true);
            phone.setText(Constants.getString("accountPhone", this));
            pass.setText(Constants.getString("passWord", this));
        }
//        setOnclick(phone,phonedelete);
//        setOnclick(pass,passdelete);
    }

    @Override
    protected void loadData() {
        if (phone.getText().toString().equals("")) {
            showToast("请输入账号");
            return;
        }
        if (pass.getText().toString().equals("")) {
            showToast("请输入密码");
            return;
        }
        showProgressDialog("登录中");
        httpModel.passLoading(phone.getText().toString(), pass.getText().toString(), 10001);
    }

    @OnClick({R.id.loading_wangji, R.id.loading_load, R.id.loading_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loading_wangji:
                Intent intent = new Intent(this, ForgetPassActivity.class);
                startActivity(intent);
                break;
            case R.id.loading_load:
                loadData();
                break;
            case R.id.loading_register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
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
//                    {"code":1,"msg":"ok","data":{"id":4,"userName":"15208429554","userPhone":"15208429554","headerUrl":"","isVerified":null,"createDate":1560478418000,"accountName":"","idCard":"","signature":null}}
                    Constants.user = JSON.parseObject(object.getString("data"), User.class);
                    if (Constants.user.getHeaderUrl() == null) {
                        Constants.user.setHeaderUrl("");
                    }
                    showToast("登录成功");
                    Constants.setUser(getApplicationContext(), Constants.user);
                    EventBus.getDefault().post("loading");
                    if (rember.isChecked()) {
                        Constants.setString("rember", "isRember", this);
                        Constants.setString("accountPhone", phone.getText().toString(), this);
                        Constants.setString("passWord", pass.getText().toString(), this);
                    } else {
                        Constants.setString("rember", "", this);
                        Constants.setString("accountPhone", "", this);
                        Constants.setString("passWord", "", this);
                    }
//                    Constants.goIntent(PassloadingActivity.this,MainActivity.class,"type","1");
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
