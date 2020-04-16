package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import cn.com.cup.european.bean.NewsCommentBean;
import cn.com.cup.european.ui.activity.NewsDetailsActivity;
import cn.com.cup.european.ui.activity.PeopleDetailsActivity;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class NewsCommentAdapter extends BaseAdapter implements RequestCallbackListener {
    List<NewsCommentBean> beans;
    Context context;
    HttpModel httpModel = new HttpModel(this);

    public NewsCommentAdapter(List<NewsCommentBean> beans, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_newscomment, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsCommentBean bean = beans.get(position);
        ImageSelectUtil.showImg(viewHolder.img, bean.getHeadimg());
        viewHolder.name.setText(bean.getName());
        if (bean.getIsLike() != null) {
            if (bean.getIsLike().equals("0")) {
                viewHolder.like.setImageResource(R.drawable.newscommet_like);
            } else {
                viewHolder.like.setImageResource(R.drawable.newscommet_like_yes);
            }
            viewHolder.likeNum.setText(bean.getZan());
            viewHolder.likeLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Constants.isLoging()) {
                        Constants.loading(context);
                        return;
                    }
                    httpModel.newsZan(bean.getPid(), 10001);
                    beans.get(position).setIsLike(beans.get(position).getIsLike().equals("0") ? "1" : "0");
                    beans.get(position).setZan(beans.get(position).getIsLike().equals("0") ?
                            String.valueOf(Integer.parseInt(beans.get(position).getZan()) - 1) :
                            String.valueOf(Integer.parseInt(beans.get(position).getZan()) + 1));
                    notifyDataSetChanged();
                }
            });
        } else {
            viewHolder.likeLinear.setVisibility(View.GONE);
        }
        viewHolder.content.setText(bean.getContent());
        viewHolder.time.setText(bean.getTime());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.newscomment_img)
        ImageView img;
        @BindView(R.id.newscomment_like)
        ImageView like;
        @BindView(R.id.newscomment_name)
        TextView name;
        @BindView(R.id.newscomment_like_num)
        TextView likeNum;
        @BindView(R.id.newscomment_comment)
        TextView content;
        @BindView(R.id.newscomment_time)
        TextView time;
        @BindView(R.id.newscomment_like_linear)
        LinearLayout likeLinear;

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
