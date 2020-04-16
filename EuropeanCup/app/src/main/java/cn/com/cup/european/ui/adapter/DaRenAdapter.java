package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.FollowBean;
import cn.com.cup.european.bean.HomePeopleBean;
import cn.com.cup.european.ui.activity.DarenActivity;
import cn.com.cup.european.ui.activity.PeopleDetailsActivity;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class DaRenAdapter extends BaseAdapter implements RequestCallbackListener {
    List<HomePeopleBean> beans;
    Context context;
    HttpModel httpModel = new HttpModel(this);

    public DaRenAdapter(List<HomePeopleBean> beans, Context context) {
        this.beans = beans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_follow, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HomePeopleBean bean = beans.get(position);
        ImageSelectUtil.showImg(viewHolder.head, bean.getHeadimg());
        viewHolder.fans.setText("粉丝:" + bean.getNum());
        viewHolder.name.setText(bean.getName());
        if (bean.getType().equals("0")) {
            viewHolder.v.setVisibility(View.GONE);
        } else {
            viewHolder.v.setVisibility(View.VISIBLE);
        }
        viewHolder.follow.setImageResource(bean.getIsLike().equals("0")
                ? R.drawable.daren_follow_yes : R.drawable.people_follow_yes);
        viewHolder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpModel.follow(bean.getUserId(), 10001);
                beans.get(position).setIsLike(bean.getIsLike().equals("0") ? "1" : "0");
                beans.get(position).setNum(bean.getIsLike().equals("0") ?
                        String.valueOf(Integer.parseInt(bean.getNum()) - 1) :
                        String.valueOf(Integer.parseInt(bean.getNum()) + 1));
                notifyDataSetChanged();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getType().equals("2")) {
                    Constants.goIntent(context, PeopleDetailsActivity.class,"id",bean.getUserId());
                } else {
                    Constants.goIntent(context, DarenActivity.class,"id",bean.getUserId());
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.follow_head)
        ImageView head;
        @BindView(R.id.follow_name)
        TextView name;
        @BindView(R.id.follow_fans)
        TextView fans;
        @BindView(R.id.follow_sig)
        TextView sig;
        @BindView(R.id.follow_follow)
        ImageView follow;
        @BindView(R.id.follow_v)
        ImageView v;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
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

    public void showToast(String text) {
        try {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }
}
