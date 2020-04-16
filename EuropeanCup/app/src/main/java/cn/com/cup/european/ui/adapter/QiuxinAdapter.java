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
import cn.com.cup.european.bean.NewsBean;
import cn.com.cup.european.ui.activity.NewsDetailsActivity;
import cn.com.cup.european.util.Constants;
import cn.com.imageselect.ImageSelectUtil;

public class QiuxinAdapter extends BaseAdapter {
    List<NewsBean> beans;
    Context context;

    public QiuxinAdapter(List<NewsBean> beans, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_qiuxin, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsBean bean = beans.get(position);
        ImageSelectUtil.showImg(viewHolder.img, bean.getImg());
        viewHolder.title.setText(bean.getTitle());
//        viewHolder.hot.setText(bean.getSee());
        viewHolder.time.setText(bean.getTime());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.goIntent(context,NewsDetailsActivity.class,"id",bean.getId());
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.news_title)
        TextView title;
        @BindView(R.id.news_hot)
        TextView hot;
        @BindView(R.id.news_time)
        TextView time;
        @BindView(R.id.news_img)
        ImageView img;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
