package cn.com.cup.european.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.LineupBean;
import cn.com.cup.european.bean.MatchDetailsTeamBean;
import cn.com.cup.european.ui.adapter.LineupAdapter;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;
import cn.com.imageselect.widget.MyViewPager;

@SuppressLint("ValidFragment")
public class MatchDetailsTeamFragment extends BaseFragment implements RequestCallbackListener {
    View contentView;
    Unbinder unbinder;
    MyViewPager viewPager;
    @BindView(R.id.detailsteam_radio)
    RadioGroup radioGroup;
    @BindView(R.id.detailsteam_button1)
    RadioButton button1;
    @BindView(R.id.detailsteam_button2)
    RadioButton button2;
    @BindView(R.id.detailsteam_zhanli)
    TextView zhanli;
    @BindView(R.id.detailsteam_zhengrong)
    TextView zhengrong;
    @BindView(R.id.alllinear)
    LinearLayout alllinear;
    HttpModel httpModel = new HttpModel(this);
    private String id;
    private List<MatchDetailsTeamBean> beans;

    public static MatchDetailsTeamFragment newInstance(MyViewPager viewPager, String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        MatchDetailsTeamFragment fragment = new MatchDetailsTeamFragment(viewPager);
        fragment.setArguments(bundle);
        return fragment;
    }

    public MatchDetailsTeamFragment(MyViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_detailsteam, null);
        unbinder = ButterKnife.bind(this, contentView);
        viewPager.setObjectForPosition(contentView, 0);//0代表tab的位置 0,1,2,3
        loadView();
        loadData();
        return contentView;
    }

    @Override
    protected void loadView() {
        id = getArguments().getString("id");
    }

    @Override
    protected void loadData() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.detailsteam_button1:
                        select(0);
                        break;
                    case R.id.detailsteam_button2:
                        select(1);
                        break;
                }
            }
        });
        httpModel.getMatchAnalyze(id, 10001);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    beans = JSON.parseArray(object.getString("data"), MatchDetailsTeamBean.class);
                    if (beans.size() >= 2) {
                        button1.setText(beans.get(0).getName());
                        button2.setText(beans.get(1).getName());
                        select(0);
                    }
                    alllinear.setVisibility(View.VISIBLE);
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

    private void select(int index) {
        if (beans == null) {
            return;
        }
        zhanli.setText(beans.get(index).getPower());
        zhengrong.setText(beans.get(index).getSurgery());
    }
}
