package cn.com.cup.european.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.BbsListBean;
import cn.com.cup.european.bean.LikeBean;
import cn.com.cup.european.ui.activity.BbsDetailsActivity;
import cn.com.cup.european.ui.activity.DarenActivity;
import cn.com.cup.european.ui.activity.PeopleDetailsActivity;
import cn.com.cup.european.ui.activity.UnscrambleActivity;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.ImgAdapter;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class UnsListAdapter extends BaseAdapter implements RequestCallbackListener {
    List<BbsListBean> beans;
    Context context;
    ImageSelectUtil imageSelectUtil;
    private boolean isPeople = false, isPost = false;
    HttpModel httpModel = new HttpModel(this);
    private String type;

    public UnsListAdapter(List<BbsListBean> beans, Context context) {
        this.beans = beans;
        this.context = context;
        imageSelectUtil = new ImageSelectUtil(context);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPeople(boolean people) {
        isPeople = people;
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    public void setPost(boolean post) {
        isPost = post;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_unslist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = new ViewHolder(convertView);
        }
        BbsListBean bean = beans.get(position);
        ImageSelectUtil.showImg(viewHolder.head, bean.getHeadimg());
        viewHolder.name.setText(bean.getName());
        if (isPeople) {
            viewHolder.follow.setVisibility(View.GONE);
        } else {
            viewHolder.follow.setVisibility(View.VISIBLE);
            if (bean.getType().equals("1")) {
                if (bean.getIsLike().equals("0")) {
                    viewHolder.follow.setImageResource(R.drawable.daren_follow_yes);
                } else {
                    viewHolder.follow.setImageResource(R.drawable.people_follow_yes);
                }
            } else {
                if (bean.getIsLike().equals("0")) {
                    viewHolder.follow.setImageResource(R.drawable.people_follow);
                } else {
                    viewHolder.follow.setImageResource(R.drawable.people_follow_yes);
                }
            }

        }
        if (Constants.isLoging()) {
            if (bean.getUserId().equals(Constants.user.getId())) {
                viewHolder.follow.setVisibility(View.GONE);
            }
        }
        viewHolder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.isLoging()) {
                    Constants.loading(context);
                    return;
                }
                httpModel.follow(bean.getUserId(), 10001);
                beans.get(position).setIsLike(beans.get(position).getIsLike().equals("0") ? "1" : "0");
                for (BbsListBean bean1 : beans) {
                    if (bean1.getUserId().equals(beans.get(position).getUserId())) {
                        bean1.setIsLike(beans.get(position).getIsLike());
                    }
                }
                notifyDataSetChanged();
            }
        });
        viewHolder.title.setText(bean.getTitle());
        viewHolder.time.setText(bean.getTime());
        viewHolder.collectionImg.setImageResource(bean.getIsCollection().equals("0") ?
                R.drawable.collection_no : R.drawable.collection_yes);
        viewHolder.collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.isLoging()) {
                    Constants.loading(context);
                    return;
                }
                httpModel.collectionNote(bean.getBid(), 10002);
                beans.get(position).setIsCollection(bean.getIsCollection().equals("0") ? "1" : "0");
                notifyDataSetChanged();
            }
        });
//        if (bean.getType().equals("1")) {
        if (bean.getType().equals("1")) {
            viewHolder.v.setVisibility(View.VISIBLE);
        } else {
            viewHolder.v.setVisibility(View.GONE);
        }

        viewHolder.game.setVisibility(View.VISIBLE);
        viewHolder.gemeName.setText(bean.getZuo());
        viewHolder.teamName.setText(bean.getYou());
//        } else {
//            viewHolder.v.setVisibility(View.GONE);
//            viewHolder.game.setVisibility(View.GONE);
//            if (bean.getImgs().size() > 0) {
//                if (bean.getImgs().size() == 1) {
//                    viewHolder.gridView.setVisibility(View.GONE);
//                    viewHolder.cardView.setVisibility(View.VISIBLE);
//                    ImageSelectUtil.showImg(viewHolder.img, bean.getImgs().get(0));
//                    viewHolder.img.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            imageSelectUtil.lookImage(bean.getImgs().get(0));
//                        }
//                    });
//                } else {
//                    viewHolder.cardView.setVisibility(View.GONE);
//                    viewHolder.gridView.setVisibility(View.VISIBLE);
//                    viewHolder.gridView.setAdapter(new ImgAdapter(bean.getImgs(), context));
//                }
//            } else {
//                viewHolder.cardView.setVisibility(View.GONE);
//                viewHolder.gridView.setVisibility(View.GONE);
//            }
//        }
        viewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.isLoging()) {
                    if (Constants.user.getId().equals(bean.getUserId())) {
                        return;
                    }
                }
                if (bean.getType().equals("1")) {
                    Constants.goIntent(context, DarenActivity.class, "id", bean.getUserId());
                } else {
                    Constants.goIntent(context, PeopleDetailsActivity.class, "id", bean.getUserId());
                }
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (bean.getType().equals("1")) {
                Constants.goIntent(context, UnscrambleActivity.class, "id", bean.getBid());
//                } else {
//                    Intent intent = new Intent(context, BbsDetailsActivity.class);
//                    intent.putExtra("bean", bean);
//                    context.startActivity(intent);
//                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.uns_head)
        ImageView head;
        @BindView(R.id.uns_v)
        ImageView v;
        @BindView(R.id.uns_follow)
        ImageView follow;
        @BindView(R.id.uns_collection_img)
        ImageView collectionImg;
        @BindView(R.id.uns_img)
        ImageView img;
        @BindView(R.id.uns_name)
        TextView name;
        @BindView(R.id.uns_title)
        TextView title;
        @BindView(R.id.uns_gameName)
        TextView gemeName;
        @BindView(R.id.uns_teamName)
        TextView teamName;
        @BindView(R.id.uns_game)
        LinearLayout game;
        @BindView(R.id.uns_img_card)
        CardView cardView;
        @BindView(R.id.uns_grid)
        GridView gridView;
        @BindView(R.id.uns_collection)
        LinearLayout collection;
        @BindView(R.id.uns_time)
        TextView time;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void doResult(String result, int action) {
        try {
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    if (!type.equals("2")) {
                        EventBus.getDefault().post("unsFollow");
                    }
                } else if (action == 10002) {
                    if (!type.equals("3")) {
                        EventBus.getDefault().post("Collection");
                    }
                }
            } else {
                showToast(object.getString("msg"));
            }
        } catch (Exception e) {
            showToast("网络不给力哦");
        }
    }

    @Override
    public void onErr(String errMessage) {
        try {
            showToast("网络不给力哦");
        } catch (Exception e) {

        }
    }

    protected void showToast(String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }

    }
}
