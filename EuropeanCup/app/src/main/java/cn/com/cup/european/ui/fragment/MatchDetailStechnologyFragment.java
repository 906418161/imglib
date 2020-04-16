package cn.com.cup.european.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.LineupBean;
import cn.com.cup.european.bean.StechnologyBean;
import cn.com.cup.european.ui.adapter.LineupAdapter;
import cn.com.cup.european.ui.adapter.StechnologyAdapter;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;
import cn.com.imageselect.widget.MyViewPager;

@SuppressLint("ValidFragment")
public class MatchDetailStechnologyFragment extends BaseFragment implements RequestCallbackListener {
    View contentView;
    Unbinder unbinder;
    MyViewPager viewPager;
    @BindView(R.id.detailstechnology_t1Img)
    ImageView t1Img;
    @BindView(R.id.detailstechnology_t2Img)
    ImageView t2Img;
    @BindView(R.id.detailstechnology_t1Name)
    TextView t1Name;
    @BindView(R.id.detailstechnology_t1S)
    TextView t1S;
    @BindView(R.id.detailstechnology_t2Name)
    TextView t2Name;
    @BindView(R.id.detailstechnology_t2S)
    TextView t2S;
    @BindView(R.id.detailstechnology_toplist)
    ListView topList;
    StechnologyAdapter adapter;
    List<StechnologyBean> beans;
    HttpModel httpModel = new HttpModel(this);
    private String id;
    public static MatchDetailStechnologyFragment newInstance(MyViewPager viewPager, String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        MatchDetailStechnologyFragment fragment = new MatchDetailStechnologyFragment(viewPager);
        fragment.setArguments(bundle);
        return fragment;
    }

    public MatchDetailStechnologyFragment(MyViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_detailstechnology, null);
        unbinder = ButterKnife.bind(this, contentView);
        viewPager.setObjectForPosition(contentView, 2);//0代表tab的位置 0,1,2,3
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
        httpModel.getMatchStatistical(id,10001);
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
                    object = object.getJSONObject("data");
                    ImageSelectUtil.showImg(t1Img, object.getString("t1img"));
                    ImageSelectUtil.showImg(t2Img, object.getString("t2img"));
                    t1Name.setText(object.getString("t1name"));
                    t2Name.setText(object.getString("t2name"));
                    t1S.setText(object.getString("t1zr"));
                    t2S.setText(object.getString("t2zr"));
                    beans = JSON.parseArray(object.getString("tongji"),StechnologyBean.class);
                    adapter = new StechnologyAdapter(beans,getContext());
                    topList.setAdapter(adapter);
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
