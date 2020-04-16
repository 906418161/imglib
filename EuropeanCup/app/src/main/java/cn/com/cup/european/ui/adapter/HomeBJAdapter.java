package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.BbsListBean;
import cn.com.cup.european.ui.activity.UnscrambleActivity;
import cn.com.cup.european.util.Constants;

public class HomeBJAdapter extends BaseAdapter {
    List<BbsListBean> beans;
    Context context;
    public HomeBJAdapter(List<BbsListBean> beans,Context context){
        this.beans=beans;
        this.context=context;
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
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.item_homebj,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BbsListBean bean = beans.get(position);
        viewHolder.num.setText((position+1)+"");
        viewHolder.title.setText(bean.getTitle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.goIntent(context,UnscrambleActivity.class,"id",bean.getBid());
            }
        });
        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.homebj_num)
        TextView num;
        @BindView(R.id.homebj_title)
        TextView title;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
