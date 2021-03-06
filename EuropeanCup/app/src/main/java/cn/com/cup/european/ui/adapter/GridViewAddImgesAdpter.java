package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.com.cup.european.R;
import cn.com.imageselect.picture.entity.LocalMedia;

/**
 * com.bm.falvzixun.adapter.GridViewAddImgAdpter
 *
 * @author yuandl on 2015/12/24.
 * 添加上传图片适配器
 */
public class GridViewAddImgesAdpter extends BaseAdapter {
    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    /**
     * 可以动态设置最多上传几张，之后就不显示+号了，用户也无法上传了
     * 默认9张
     */
    private int maxImages = 6;
    List<LocalMedia> localMedias;

    public GridViewAddImgesAdpter(List<String> datas, List<LocalMedia> localMedias, Context context) {
        this.datas = datas;
        this.context = context;
        this.localMedias = localMedias;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 获取最大上传张数
     *
     * @return
     */
    public int getMaxImages() {
        return maxImages;
    }

    /**
     * 设置最大上传张数
     *
     * @param maxImages
     */
    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }

    /**
     * 让GridView中的数据数目加1最后一个显示+号
     * 当到达最大张数时不再显示+号
     *
     * @return 返回GridView中的数量
     */
    @Override
    public int getCount() {
        int count = datas == null ? 1 : datas.size() + 1;
        if (count >= maxImages) {
            return maxImages;
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<String> datas, List<LocalMedia> localMedias) {
        this.datas = datas;
        this.localMedias = localMedias;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**代表+号之前的需要正常显示图片**/
        if (datas != null && position < datas.size()) {
            Picasso.get()
                    .load("file://" + datas.get(position))
                    .fit()
                    .centerCrop()
                    .into(viewHolder.ivimage);
            viewHolder.btdel.setVisibility(View.VISIBLE);
            viewHolder.btdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(position);
                    localMedias.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            /**代表+号的需要+号图片显示图片**/
//            Picasso.with(context)
//                    .load(R.drawable.image_add)
//                    .fit()
//                    .centerCrop()
//                    .into(viewHolder.ivimage);
            viewHolder.ivimage.setImageResource(R.drawable.image_add);
            viewHolder.ivimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.btdel.setVisibility(View.GONE);
        }
        return convertView;

    }

    public class ViewHolder {
        public final ImageView ivimage;
        public final Button btdel;
        public final View root;

        public ViewHolder(View root) {
            ivimage = (ImageView) root.findViewById(R.id.iv_image);
            btdel = (Button) root.findViewById(R.id.bt_del);
            this.root = root;
        }
    }

    public void showImg(ImageView imageView, String url) {
        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
                .into(imageView);
    }
}

