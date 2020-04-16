package cn.com.cup.european.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.ui.activity.MatchDetailsActivity;
import cn.com.cup.european.util.Constants;
import cn.com.imageselect.ImageSelectUtil;

public class MatchAdapter extends BaseAdapter {
    List<HomeHotBean> beans;
    Context context;
    private boolean isGet = false;

    public MatchAdapter(List<HomeHotBean> beans, Context context) {
        this.beans = beans;
        this.context = context;
    }

    public void setGet(boolean get) {
        isGet = get;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_match, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HomeHotBean bean = beans.get(position);
//        if (bean.getTip().equals("0")) {
//            viewHolder.tip.setVisibility(View.INVISIBLE);
//        } else {
//            viewHolder.tip.setVisibility(View.VISIBLE);
//        }
        viewHolder.title.setText(bean.getName());
        viewHolder.time.setText(bean.getTime());
        viewHolder.score.setText(bean.getScore());
        if (bean.getType().equals("0")) {
            viewHolder.type.setText("未开始");
            viewHolder.type.setTextColor(context.getResources().getColor(R.color.matchStart));
        } else {
            viewHolder.type.setText("已结束");
            viewHolder.type.setTextColor(context.getResources().getColor(R.color.gblack));
        }
        ImageSelectUtil.showImg(viewHolder.t1Img, bean.getT1img());
        ImageSelectUtil.showImg(viewHolder.t2Img, bean.getT2img());
        viewHolder.t1Name.setText(bean.getT1name());
        viewHolder.t2Name.setText(bean.getT2name());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGet) {
                    Intent intent = new Intent();
                    intent.putExtra("bean", (Serializable) bean);
                    //通过intent对象返回结果，必须要调用一个setResult方法，
                    //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                    ((Activity) context).setResult(((Activity) context).RESULT_OK, intent);
                    ((Activity) context).finish(); //结束当前的activity的生命周期
                } else {
                    if (bean.getType().equals("0")) {
                        Toast.makeText(context, "比赛还未开始", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(context, MatchDetailsActivity.class);
                    intent.putExtra("bean", bean);
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.match_hot)
        ImageView tip;
        @BindView(R.id.match_title)
        TextView title;
        @BindView(R.id.match_time)
        TextView time;
        @BindView(R.id.match_t1Img)
        ImageView t1Img;
        @BindView(R.id.match_t1Name)
        TextView t1Name;
        @BindView(R.id.match_t2Img)
        ImageView t2Img;
        @BindView(R.id.match_t2Name)
        TextView t2Name;
        @BindView(R.id.match_score)
        TextView score;
        @BindView(R.id.match_type)
        TextView type;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
