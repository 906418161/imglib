package cn.com.cup.european.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.com.calendarview.Calendar;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.bean.IntegralLeftBean;
import cn.com.cup.european.bean.IntegralRightBean;
import cn.com.cup.european.ui.adapter.IntegralLeftAdapter;
import cn.com.cup.european.ui.adapter.IntegralRightAdapter;
import cn.com.cup.european.ui.adapter.MatchAdapter;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;
import cn.com.imageselect.widget.MyViewPager;

@SuppressLint("ValidFragment")
public class IntegralFragment extends BaseFragment implements RequestCallbackListener {
    View contentView;
    Unbinder unbinder;
    String id;

//    public static IntegralFragment newInstance(MyViewPager viewPager ) {
//        IntegralFragment fragment = new IntegralFragment(viewPager);
//        return fragment;
//    }
//
//    public IntegralFragment(MyViewPager viewPager) {
//        this.viewPager = viewPager;
//    }

    @BindView(R.id.integral_leftlist)
    ListView leftList;
    @BindView(R.id.integral_rightlist)
    ListView rightlist;
    IntegralLeftAdapter leftAdapter;
    List<IntegralLeftBean> leftBeans;
    private int lastIndex = 0;
    IntegralRightAdapter rightAdapter;
    List<IntegralRightBean> rightBeans;
    HttpModel httpModel = new HttpModel(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_integral, null);
        unbinder = ButterKnife.bind(this, contentView);
        loadView();
        loadData();
//        viewPager.setObjectForPosition(contentView, 1);//0代表tab的位置 0,1,2,3
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void loadView() {

    }

    @Override
    protected void loadData() {
        httpModel.getIntegral(10001);
    }

    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    leftBeans = JSON.parseArray(object.getString("data"), IntegralLeftBean.class);
                    if (leftBeans.size() > 0) {
                        leftBeans.get(0).setSelect(true);
                        rightAdapter = new IntegralRightAdapter(leftBeans.get(0).getList(), getContext());
                        rightlist.setAdapter(rightAdapter);
                    }
                    leftAdapter = new IntegralLeftAdapter(leftBeans, getContext());
                    leftList.setAdapter(leftAdapter);
                    leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == lastIndex) {
                                return;
                            }
                            leftBeans.get(lastIndex).setSelect(false);
                            leftBeans.get(position).setSelect(true);
                            leftAdapter.notifyDataSetChanged();
                            rightAdapter = new IntegralRightAdapter(leftBeans.get(position).getList(), getContext());
                            rightlist.setAdapter(rightAdapter);
                            lastIndex = position;
                        }
                    });
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
