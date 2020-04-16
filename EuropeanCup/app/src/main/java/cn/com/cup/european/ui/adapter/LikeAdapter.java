package cn.com.cup.european.ui.adapter;

import android.content.Context;
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
import cn.com.cup.european.bean.LikeBean;
import cn.com.imageselect.ImageSelectUtil;

public class LikeAdapter extends BaseAdapter {
    List<LikeBean> beans;
    Context context;

    public LikeAdapter(List<LikeBean> beans, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_like, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LikeBean bean = beans.get(position);
        ImageSelectUtil.showImg(viewHolder.head, bean.getHedaimg());
        if (bean.getImg().equals("")) {
            viewHolder.img.setVisibility(View.GONE);
        } else {
            viewHolder.img.setVisibility(View.VISIBLE);
            ImageSelectUtil.showImg(viewHolder.img, bean.getImg());
        }
        viewHolder.name.setText(bean.getName());
        viewHolder.time.setText(bean.getTime());
        viewHolder.title.setText(bean.getTitle());
        viewHolder.content.setText(bean.getContent());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.like_head)
        ImageView head;
        @BindView(R.id.like_img)
        ImageView img;
        @BindView(R.id.like_name)
        TextView name;
        @BindView(R.id.like_time)
        TextView time;
        @BindView(R.id.like_title)
        TextView title;
        @BindView(R.id.like_content)
        TextView content;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
