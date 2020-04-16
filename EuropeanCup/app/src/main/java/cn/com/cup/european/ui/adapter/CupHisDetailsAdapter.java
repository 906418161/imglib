package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.imageselect.ImageSelectUtil;

public class CupHisDetailsAdapter extends BaseAdapter {
    List<String> benas;
    Context context;
    ImageSelectUtil imageSelectUtil;
    public CupHisDetailsAdapter(List<String> benas,Context context){
        this.benas=benas;
        this.context=context;
        imageSelectUtil = new ImageSelectUtil(context);
    }
    @Override
    public int getCount() {
        return benas.size();
    }

    @Override
    public Object getItem(int position) {
        return benas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.item_cuphisdetails,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ImageSelectUtil.showImg(viewHolder.img,benas.get(position));
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelectUtil.lookImages(position,benas);
            }
        });
        return convertView;
    }
    class ViewHolder{
        @BindView(R.id.img)
        ImageView img;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
