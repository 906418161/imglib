package cn.com.cup.european.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.donkingliang.labels.LabelsView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.ui.adapter.GridViewAddImgesAdpter;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.picture.PictureSelector;
import cn.com.imageselect.picture.config.PictureConfig;
import cn.com.imageselect.picture.entity.LocalMedia;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class PostActivity extends BaseActivity implements RequestCallbackListener {

    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.post_title)
    EditText tTitle;
    @BindView(R.id.post_content)
    EditText content;
    @BindView(R.id.post_gridview)
    GridView gridView;
    @BindView(R.id.post_labels)
    LabelsView labels;
    List<String> tips, tipId;
    private GridViewAddImgesAdpter adpter;
    private List<String> imgpaths;
    ImageSelectUtil imageSelectUtil = new ImageSelectUtil(this);
    private List<LocalMedia> localMedias = new ArrayList<>();
    HttpModel httpModel = new HttpModel(this);
    private String tid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        hideStatueBar(0);
        ButterKnife.bind(this);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("发布");
        adpter = new GridViewAddImgesAdpter(imgpaths, localMedias, this);
        gridView.setAdapter(adpter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageSelectUtil.selectImage(6, localMedias);
            }
        });
    }

    @Override
    protected void loadData() {
        httpModel.getThemeList(10003);
    }


    @OnClick({R.id.post_sub})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_sub:
                if (!Constants.isLoging()) {
                    Constants.loading(this);
                    return;
                }
                if (tTitle.getText().toString().equals("")) {
                    showToast("请输入标题");
                    return;
                } else if (content.getText().toString().equals("")) {
                    showToast("请输入标题");
                    return;
                } else if (tid.equals("")) {
                    showToast("请选择主题");
                    return;
                }
                if (imgpaths != null && imgpaths.size() > 0) {
                    List<File> fileList = new ArrayList<>();
                    for (int i = 0; i < imgpaths.size(); i++) {
                        fileList.add(new File(imgpaths.get(i)));
                    }
                    showProgressDialog("图片上传中");
                    httpModel.imgFile(fileList, 10001);
                } else {
                    showProgressDialog("正在发布");
                    httpModel.postBbs(tid, tTitle.getText().toString(), content.getText().toString(),
                            "", 10002);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST://选择图片集合的回调
                    // 图片、视频、音频选择结果回调
                    localMedias = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (localMedias == null)
                        return;
                    imgpaths = new ArrayList<>();
                    for (LocalMedia localMedia : localMedias) {
                        if (localMedia.isCompressed()) {
                            imgpaths.add(localMedia.getCompressPath());
                        } else {
                            imgpaths.add(localMedia.getPath());
                        }
                    }
                    adpter = new GridViewAddImgesAdpter(imgpaths, localMedias, this);
                    gridView.setAdapter(adpter);
                    break;
            }
    }

    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (action == 10001) {
                if (object.getString("status").equals("0")) {
                    String[] imgs = object.getString("uploadPath").split(",");
                    String httpImage = "";
                    for (int i = 0; i < imgs.length; i++) {
                        httpImage = httpImage + Constants.IMAGEURL + imgs[i] + ",";
                    }
                    showProgressDialog("正在发布");
                    httpModel.postBbs(tid, tTitle.getText().toString(), content.getText().toString(),
                            httpImage, 10002);
                } else {
                    if (object.isNull("msg")) {
                        showToast(object.getString("result"));
                    } else {
                        showToast(object.getString("msg"));
                    }

                }
            } else {
                if (object.getString("code").equals("1")) {
                    if (action == 10002) {
                        showToast("发布成功");
                        finish();
                        EventBus.getDefault().post("updataBbs");
                    } else if (action == 10003) {
                        tips = new ArrayList<>();
                        tipId = new ArrayList<>();
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            tips.add(array.getJSONObject(i).getString("name"));
                            tipId.add(array.getJSONObject(i).getString("id"));
                        }
                        tid = tipId.get(0);
                        labels.setLabels(tips); //直接设置一个字符串数组就可以了。
                        labels.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
                            @Override
                            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                                //label是被选中的标签，data是标签所对应的数据，isSelect是是否选中，position是标签的位置。
                                tid = tipId.get(position);
                            }
                        });
                    }
                } else {
                    showToast(object.getString("msg"));
                }
            }
        } catch (Exception e) {
            showToast("网络不给力哦");
        }
    }

    @Override
    public void onErr(String errMessage) {
        try {
            dismissProgressDialog();
            showToast("网络不给力哦");
        } catch (Exception e) {

        }
    }
}
