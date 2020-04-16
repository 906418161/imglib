package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.bean.HomePeopleBean;
import cn.com.cup.european.bean.HomeTipBean;
import cn.com.cup.european.ui.activity.DarenActivity;
import cn.com.cup.european.ui.activity.PeopleDetailsActivity;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class HomePeopleAdapter extends RecyclerView.Adapter implements RequestCallbackListener {
    List<HomePeopleBean> beans;
    Context context;
    HttpModel httpModel = new HttpModel(this);

    public HomePeopleAdapter(List<HomePeopleBean> beans, Context context) {
        this.beans = beans;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_homepeople, null);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        HomePeopleBean bean = beans.get(position);
        viewHolder.name.setText(bean.getName());
        viewHolder.num.setText(bean.getNum() + "人关注");
        ImageSelectUtil.showImg(viewHolder.head, bean.getHeadimg());
        if (bean.getIsLike().equals("0")) {
            viewHolder.follow.setImageResource(R.drawable.people_follow);
        } else {
            viewHolder.follow.setImageResource(R.drawable.people_follow_yes);
        }
        viewHolder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.isLoging()) {
                    Constants.loading(context);
                    return;
                }
                httpModel.follow(bean.getUserId(), 10001);
                beans.get(position).setIsLike(bean.getIsLike().equals("0") ? "1" : "0");
                beans.get(position).setNum(bean.getIsLike().equals("0") ?
                        String.valueOf(Integer.parseInt(beans.get(position).getNum()) - 1) :
                        String.valueOf(Integer.parseInt(beans.get(position).getNum()) + 1));
                notifyDataSetChanged();
            }
        });
        if (bean.getType().equals("2")) {
            viewHolder.v.setVisibility(View.GONE);
        } else {
            viewHolder.v.setVisibility(View.VISIBLE);
        }
        viewHolder.allLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getType().equals("2")) {
                    Constants.goIntent(context, PeopleDetailsActivity.class, "id", bean.getUserId());
                } else {
                    Constants.goIntent(context, DarenActivity.class, "id", bean.getUserId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.homepeople_head)
        ImageView head;
        @BindView(R.id.homepeople_name)
        TextView name;
        @BindView(R.id.homepeople_num)
        TextView num;
        @BindView(R.id.homepeople_follow)
        ImageView follow;
        @BindView(R.id.homepeople_allLinear)
        LinearLayout allLinear;
        @BindView(R.id.homepeople_v)
        ImageView v;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public void doResult(String result, int action) {
        try {
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {

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
            showToast("网络不给力哦");
        } catch (Exception e) {

        }
    }

    protected void showToast(String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }

    }
}
