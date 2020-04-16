package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class FeedBackActvity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.question_content)
    TextView content;
    @BindView(R.id.question_qq)
    TextView qq;
    HttpModel httpModel = new HttpModel(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_feedback);
        ButterKnife.bind(this);
        hideStatueBar(1);
        init();
    }

    @Override
    protected void loadView() {

    }

    @Override
    protected void loadData() {

    }

    private void init() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("投诉建议");
    }
    @OnClick({R.id.question_submit})
    public void onClikc(View view){
        switch (view.getId()){
            case R.id.question_submit:
                if(content.getText().toString().equals("")){
                    showToast("请输入反馈内容");
                }else if(qq.getText().toString().equals("")){
                    showToast("请输入联系方式");
                }else {
                    showProgressDialog("请稍后");
                    httpModel.record(content.getText().toString(),qq.getText().toString(),10001);
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
                showToast("反馈成功");
                content.setText("");
                qq.setText("");
            } else {
                if (object.isNull("msg")) {
                    showToast(object.getString("result"));
                } else {
                    showToast(object.getString("msg"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("连接服务器失败了");
        }
    }

    @Override
    public void onErr(String errMessage) {
        dismissProgressDialog();
        showToast("网络不给力啊");
    }
}
