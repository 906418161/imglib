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
import cn.com.cup.european.bean.RankListBean;

public class RankListAdapter extends BaseAdapter {
    List<RankListBean> beans;
    Context context;

    public RankListAdapter(List<RankListBean> beans, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ranklist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RankListBean bean = beans.get(position);
        viewHolder.name.setText(bean.getPlayers());
        viewHolder.num.setText(bean.getNum());
        viewHolder.team.setText(bean.getTeam());
        switch (position) {
            case 0:
                viewHolder.img.setVisibility(View.VISIBLE);
                viewHolder.img.setImageResource(R.drawable.rank_1);
                viewHolder.rank1.setVisibility(View.GONE);
                viewHolder.rank2.setVisibility(View.GONE);
                viewHolder.rank3.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.img.setVisibility(View.VISIBLE);
                viewHolder.img.setImageResource(R.drawable.rank_2);
                viewHolder.rank1.setVisibility(View.GONE);
                viewHolder.rank2.setVisibility(View.GONE);
                viewHolder.rank3.setVisibility(View.GONE);
                break;
            case 2:
                viewHolder.img.setVisibility(View.VISIBLE);
                viewHolder.img.setImageResource(R.drawable.rank_3);
                viewHolder.rank1.setVisibility(View.GONE);
                viewHolder.rank2.setVisibility(View.GONE);
                viewHolder.rank3.setVisibility(View.GONE);
                break;
            default:
                viewHolder.img.setVisibility(View.GONE);
                if (position + 1 < 10) {
                    viewHolder.rank1.setVisibility(View.VISIBLE);
                    viewHolder.rank1.setText((position + 1) + "");
                    viewHolder.rank2.setVisibility(View.GONE);
                    viewHolder.rank3.setVisibility(View.GONE);
                } else if (position + 1 < 100) {
                    viewHolder.rank2.setVisibility(View.VISIBLE);
                    viewHolder.rank1.setVisibility(View.GONE);
                    viewHolder.rank2.setText((position + 1) + "");
                    viewHolder.rank3.setVisibility(View.GONE);
                } else {
                    viewHolder.rank3.setVisibility(View.VISIBLE);
                    viewHolder.rank2.setVisibility(View.GONE);
                    viewHolder.rank3.setText((position + 1) + "");
                    viewHolder.rank1.setVisibility(View.GONE);
                }
                break;
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.ranklist_img)
        ImageView img;
        @BindView(R.id.ranklist_one)
        TextView rank1;
        @BindView(R.id.ranklist_two)
        TextView rank2;
        @BindView(R.id.ranklist_three)
        TextView rank3;
        @BindView(R.id.ranklist_name)
        TextView name;
        @BindView(R.id.ranklist_team)
        TextView team;
        @BindView(R.id.ranklist_num)
        TextView num;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
