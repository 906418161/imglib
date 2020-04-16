package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.ui.activity.MatchDetailsActivity;
import cn.com.imageselect.ImageSelectUtil;

public class HomeHotAdapter extends RecyclerView.Adapter {
    List<HomeHotBean> beans;
    Context context;

    public HomeHotAdapter(List<HomeHotBean> beans, Context context) {
        this.beans = beans;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_homehot, null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        HomeHotBean bean = beans.get(position);
        ImageSelectUtil.showImg(viewHolder.t1Img, bean.getT1img());
        ImageSelectUtil.showImg(viewHolder.t2Img, bean.getT2img());
        viewHolder.title.setText(bean.getName());
        viewHolder.time.setText(bean.getTime());
        viewHolder.t1Name.setText(bean.getT1name());
        viewHolder.t2Name.setText(bean.getT2name());

        if (bean.getType().equals("0")) {
            viewHolder.score.setVisibility(View.GONE);
            viewHolder.type.setText("未开始");
        } else {
            viewHolder.score.setText(bean.getScore());
            viewHolder.type.setText("已结束");
        }

        if (position == 0) {
            viewHolder.left.setVisibility(View.VISIBLE);
            viewHolder.right.setVisibility(View.GONE);
        } else if (position == beans.size() - 1) {
            viewHolder.left.setVisibility(View.GONE);
            viewHolder.right.setVisibility(View.VISIBLE);
        } else {
            viewHolder.left.setVisibility(View.GONE);
            viewHolder.right.setVisibility(View.GONE);
        }
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getType().equals("0")) {
                    Toast.makeText(context, "比赛还未开始", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, MatchDetailsActivity.class);
                intent.putExtra("bean", bean);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.homehot_t1Img)
        ImageView t1Img;
        @BindView(R.id.homehot_t2Img)
        ImageView t2Img;
        @BindView(R.id.homehot_title)
        TextView title;
        @BindView(R.id.homehot_time)
        TextView time;
        @BindView(R.id.homehot_t1Name)
        TextView t1Name;
        @BindView(R.id.homehot_t2Name)
        TextView t2Name;
        @BindView(R.id.homehot_score)
        TextView score;
        @BindView(R.id.homehot_type)
        TextView type;
        @BindView(R.id.homehot_left)
        View left;
        @BindView(R.id.homehot_right)
        View right;
        @BindView(R.id.homehot_allLinear)
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
