package cn.com.cup.european.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.User;
import cn.com.cup.european.ui.activity.AboutMeActivity;
import cn.com.cup.european.ui.activity.FeedBackActvity;
import cn.com.cup.european.ui.activity.FollowActivity;
import cn.com.cup.european.ui.activity.LikeActivity;
import cn.com.cup.european.ui.activity.MyBbsListActivity;
import cn.com.cup.european.ui.activity.MyDetailsActivity;
import cn.com.cup.european.ui.activity.MyUnsActivity;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class OtherFragment extends BaseFragment implements RequestCallbackListener {
    View contentView;
    Unbinder unbinder;
    @BindView(R.id.other_head)
    ImageView head;
    @BindView(R.id.other_name)
    TextView name;
    @BindView(R.id.other_sig)
    TextView sig;
    @BindView(R.id.other_followNum)
    TextView followNum;
    @BindView(R.id.other_fansNum)
    TextView fansNum;
    @BindView(R.id.other_likeNum)
    TextView likeNum;
    HttpModel httpModel = new HttpModel(this);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_other, null);
        unbinder = ButterKnife.bind(this, contentView);
        loadView();
        loadData();
        return contentView;
    }

    @Override
    protected void loadView() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        getStatusBarHeight(contentView.findViewById(R.id.toplinear));
        setUser();
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.other_head, R.id.other_follow_linear, R.id.other_fans_linear, R.id.other_like_linear,
            R.id.other_setting, R.id.other_dynamic, R.id.other_feck, R.id.other_about, R.id.other_uns})
    public void onClick(View view) {
        if (!Constants.isLoging() && view.getId() != R.id.other_feck
                && view.getId() != R.id.other_about) {
            Constants.loading(getContext());
            return;
        }
        switch (view.getId()) {
            case R.id.other_head:
                Constants.goIntent(getContext(), MyDetailsActivity.class);
                break;
            case R.id.other_follow_linear:
                Constants.goIntent(getContext(), FollowActivity.class, "type", "1");
                break;
            case R.id.other_fans_linear:
                Constants.goIntent(getContext(), FollowActivity.class, "type", "2");
                break;
            case R.id.other_like_linear:
                Constants.goIntent(getContext(), LikeActivity.class);
                break;
            case R.id.other_setting:
                Constants.goIntent(getContext(), MyDetailsActivity.class);
                break;
            case R.id.other_dynamic:
                Constants.goIntent(getContext(), MyBbsListActivity.class);
                break;
            case R.id.other_feck:
                Constants.goIntent(getContext(), FeedBackActvity.class);
                break;
            case R.id.other_about:
                Constants.goIntent(getContext(), AboutMeActivity.class);
                break;
            case R.id.other_uns:
                Constants.goIntent(getContext(), MyUnsActivity.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(getActivity());
    }

    private void setUser() {
        if (Constants.user == null || Constants.user.getId().equals("")) {
            return;
        }
        if (!Constants.user.getHeaderUrl().equals("")) {
            Picasso.get().load(Constants.user.getHeaderUrl())
                    .fit()
                    .error(R.drawable.allbg)
                    .centerCrop()
                    .into(head);
        }
        name.setText(Constants.user.getUserName());
        sig.setText(Constants.user.getSignature());
        httpModel.getAboutUser(Constants.user.getId(), 10001);
    }

    @Subscribe
    public void handleEvent(String event) {
        if (event.equals("loading")) {
            setUser();
        } else if (event.equals("signout")) {
            Constants.user = new User();
            Constants.user.setHeaderUrl("");
            Constants.user.setUserMobile("");
            Constants.user.setUserName("");
            Constants.user.setId("");
            Constants.setUser(getContext(), Constants.user);
            name.setText("未登录");
            sig.setText("未登录");
            head.setImageResource(R.drawable.allbg);
            followNum.setText("0");
            fansNum.setText("0");
            likeNum.setText("0");
        } else if (event.equals("updaOther")) {
            if (Constants.user == null || Constants.user.getId().equals("")) {
                return;
            }
            httpModel.getAboutUser(Constants.user.getId(), 10001);
        }
    }

    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    object = object.getJSONObject("data");
                    followNum.setText(object.getString("guanzhu"));
                    fansNum.setText(object.getString("fensi"));
                    likeNum.setText(object.getString("zan"));
                } else if (action == 10002) {

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
