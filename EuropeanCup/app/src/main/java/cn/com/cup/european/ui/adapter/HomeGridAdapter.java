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
import cn.com.cup.european.bean.HomeTipBean;
import cn.com.cup.european.ui.activity.BbsListActivity;
import cn.com.cup.european.util.Constants;

public class HomeGridAdapter extends BaseAdapter {
    List<HomeTipBean> beans;
    Context context;

    public HomeGridAdapter(List<HomeTipBean> beans, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_homegrid, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HomeTipBean bean = beans.get(position);
        viewHolder.tip.setText(bean.getName());
        int drawable = 0;
        switch (position) {
            case 0:
                drawable = R.drawable.home_tip1;
                break;
            case 1:
                drawable = R.drawable.home_tip2;
                break;
            case 2:
                drawable = R.drawable.home_tip3;
                break;
            case 3:
                drawable = R.drawable.home_tip4;
                break;
            case 4:
                drawable = R.drawable.home_tip5;
                break;
            default:
                drawable = R.drawable.home_tip1;
                break;
        }
        viewHolder.img.setImageResource(drawable);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.goIntent(context,BbsListActivity.class,"id",bean.getId());
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.homegrid_img)
        ImageView img;
        @BindView(R.id.homegrid_tip)
        TextView tip;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
