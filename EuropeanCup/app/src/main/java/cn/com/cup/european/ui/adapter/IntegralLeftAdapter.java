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
import cn.com.cup.european.bean.IntegralLeftBean;

public class IntegralLeftAdapter extends BaseAdapter {
    List<IntegralLeftBean> beans;
    Context context;

    public IntegralLeftAdapter(List<IntegralLeftBean> beans, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_integralleft, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        IntegralLeftBean bean = beans.get(position);
        viewHolder.title.setText(bean.getName());
        if (bean.isSelect()) {
            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.title.setTextColor(context.getResources().getColor(R.color.matchStart));
        } else {
            viewHolder.img.setVisibility(View.INVISIBLE);
            viewHolder.title.setTextColor(context.getResources().getColor(R.color.tblack));
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.integralleft_img)
        ImageView img;
        @BindView(R.id.integralleft_text)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
