package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.LineupBean;
import cn.com.cup.european.bean.StechnologyBean;

public class StechnologyAdapter extends BaseAdapter {
    List<StechnologyBean> beans;
    Context context;
    String[] title = new String[]{
            "控球时间", "射门次数", "射正次数", "任意球次数", "角球次数", "越位次数", "犯规次数"};

    public StechnologyAdapter(List<StechnologyBean> beans, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_stechnology, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StechnologyBean bean = beans.get(position);
        double allNum =  Double.valueOf(bean.getT1ji()) + Double.valueOf(bean.getT2ji());
        int letfB = letfB = (int) ((Double.valueOf(bean.getT1ji()) / allNum) * 100);
        int rightB = (int) ((Double.valueOf(bean.getT2ji()) / allNum) * 100);
        viewHolder.leftBar.setProgress(letfB);
        viewHolder.rightBar.setProgress(rightB);
        viewHolder.leftNum.setText(bean.getT1ji());
        viewHolder.rightNum.setText(bean.getT2ji());
        if (position == 0) {
            viewHolder.leftNum.setText(bean.getT1ji() + "%");
            viewHolder.rightNum.setText(bean.getT2ji() + "%");
        }
        if (position < 7) {
            viewHolder.title.setText(title[position]);
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.leftNum)
        TextView leftNum;
        @BindView(R.id.rightNum)
        TextView rightNum;
        @BindView(R.id.progressbar_left)
        ProgressBar leftBar;
        @BindView(R.id.progressbar_right)
        ProgressBar rightBar;
        @BindView(R.id.progressbar_title)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
