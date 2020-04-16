package cn.com.cup.european.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeCupBean;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.bean.HomePeopleBean;
import cn.com.cup.european.bean.HomeTipBean;
import cn.com.cup.european.ui.adapter.HomeCupAdapter;
import cn.com.cup.european.ui.adapter.HomeGridAdapter;
import cn.com.cup.european.ui.adapter.HomeHotAdapter;
import cn.com.cup.european.ui.adapter.HomePeopleAdapter;
import cn.com.cup.european.ui.fragment.HomeFragment;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class CupHisActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.gridview)
    GridView gridView;
    HomeCupAdapter cupAdapter;
    List<HomeCupBean> beans;
    HttpModel httpModel = new HttpModel(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuphis);
        hideStatueBar(0);
        ButterKnife.bind(this);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        title.setText("历届欧洲杯");
        getStatusBarHeight(findViewById(R.id.toplinear));
    }

    @Override
    protected void loadData() {
        httpModel.getEurocupHistory(String.valueOf(100),10001);
    }


    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    beans = JSON.parseArray(object.getJSONObject("data").getString("rows"), HomeCupBean.class);
                    cupAdapter = new HomeCupAdapter(beans, this);
                    gridView.setAdapter(cupAdapter);
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
