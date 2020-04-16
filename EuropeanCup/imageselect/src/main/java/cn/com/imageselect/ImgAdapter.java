package cn.com.imageselect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class ImgAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    ImageSelectUtil imageSelectUtil;

    public ImgAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        imageSelectUtil = new ImageSelectUtil(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHoler;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_img, null);
            viewHoler = new ViewHolder();
            viewHoler.imageView = convertView.findViewById(R.id.image);
            convertView.setTag(viewHoler);
        } else {
            viewHoler = (ViewHolder) convertView.getTag();
        }
        ImageSelectUtil.showImg(viewHoler.imageView, list.get(position), context);
        viewHoler.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelectUtil.lookImages(position, list);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
