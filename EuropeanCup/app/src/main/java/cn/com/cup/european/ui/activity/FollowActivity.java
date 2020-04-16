package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.FollowBean;
import cn.com.cup.european.bean.HomePeopleBean;
import cn.com.cup.european.ui.adapter.FollowAdapter;
import cn.com.cup.european.ui.adapter.HomePeopleAdapter;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class FollowActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.follow_list)
    ListView listView;
    FollowAdapter adapter;
    List<FollowBean> beans;
    private String type;
    HttpModel httpModel = new HttpModel(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        hideStatueBar(0);
        ButterKnife.bind(this);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        type = getIntent().getStringExtra("type");
        title.setText(type.equals("1") ? "关注列表" : "粉丝列表");
    }

    @Override
    protected void loadData() {
        httpModel.getFansList(type,10001);
    }

    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    beans = JSON.parseArray(object.getString("data"),FollowBean.class);
                    adapter = new FollowAdapter(beans, this);
                    listView.setAdapter(adapter);
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
