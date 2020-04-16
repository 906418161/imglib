package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.LineupBean;

public class LineupAdapter extends BaseAdapter {
    List<LineupBean> beans;
    Context context;

    public LineupAdapter(List<LineupBean> beans, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lineup, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LineupBean bean = beans.get(position);
        viewHolder.leftName.setText(bean.getT1zr());
        viewHolder.rightName.setText(bean.getT2zr());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.lineup_leftName)
        TextView leftName;
        @BindView(R.id.lineup_rightName)
        TextView rightName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
