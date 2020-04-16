package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.dialog.LogOutdialog;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class AuthenticationActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.authentication_name)
    EditText name;
    @BindView(R.id.authentication_idcard)
    EditText idcard;
    @BindView(R.id.authentication_content)
    EditText content;
    LogOutdialog dialog;
    HttpModel httpModel = new HttpModel(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        hideStatueBar(0);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        title.setText("专家认证");
        getStatusBarHeight(findViewById(R.id.toplinear));
        dialog = new LogOutdialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog("请稍后");
                httpModel.setCertification(10001);
                dialog.dismiss();
            }
        }, this);
        dialog.setTitle("确定提交?");
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.authentication_sub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.authentication_sub:
                if (!Constants.isLoging()) {
                    Constants.loading(this);
                    return;
                }
                if (name.getText().toString().equals("")) {
                    showToast("请输入姓名");
                    return;
                }
                if (idcard.getText().toString().equals("")) {
                    showToast("请输入身份证号码");
                    return;
                }
                if (content.getText().toString().equals("")) {
                    showToast("请输入认证描述");
                    return;
                }
                dialog.show();
                break;
        }
    }

    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                EventBus.getDefault().post("Authentication");
                showToast("已提交认证");
                finish();
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
