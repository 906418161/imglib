package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.DarenListBean;
import cn.com.cup.european.ui.activity.UnscrambleActivity;
import cn.com.cup.european.util.Constants;

public class DarenListAdapter extends BaseAdapter {
    List<DarenListBean> beans;
    Context context;

    public DarenListAdapter(List<DarenListBean> beans, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_darenlist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DarenListBean bean = beans.get(position);
        viewHolder.title.setText(bean.getTitle());
        viewHolder.gTitle.setText(bean.getZuo());
        viewHolder.team.setText(bean.getYou());
        viewHolder.time.setText(bean.getTime());
//        if (bean.getType().equals("0")) {
//            viewHolder.type.setImageResource(R.drawable.daren_black);
//        } else {
//            viewHolder.type.setImageResource(R.drawable.daren_red);
//        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UnscrambleActivity.class);
                intent.putExtra("id", bean.getBid());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.darenlist_title)
        TextView title;
        @BindView(R.id.darenlist_gTitle)
        TextView gTitle;
        @BindView(R.id.darenlist_img)
        ImageView type;
        @BindView(R.id.darenlist_team)
        TextView team;
        @BindView(R.id.darenlist_time)
        TextView time;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
