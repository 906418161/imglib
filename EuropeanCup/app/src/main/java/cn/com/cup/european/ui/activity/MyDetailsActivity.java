package cn.com.cup.european.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.cup.european.R;
import cn.com.cup.european.util.Constants;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.picture.PictureSelector;
import cn.com.imageselect.picture.config.PictureConfig;
import cn.com.imageselect.picture.entity.LocalMedia;
import cn.com.imageselect.util.dialog.LogOutdialog;
import cn.com.imageselect.util.dialog.LogUpdatedialog;
import cn.com.imageselect.util.http.RequestCallbackListener;
import cn.com.imageselect.widget.RoundImageView;

public class MyDetailsActivity extends BaseActivity implements RequestCallbackListener {
    @BindView(R.id.my_head)
    RoundImageView head;
    @BindView(R.id.my_name)
    EditText name;
    @BindView(R.id.my_signature)
    EditText signature;
    @BindView(R.id.my_signout)
    LinearLayout loging;
    @BindView(R.id.tv_title_edit)
    TextView titleEdit;
    @BindView(R.id.my_account)
    TextView account;
    @BindView(R.id.my_address_updatepass)
    LinearLayout updatepass;
    @BindView(R.id.my_abboutme)
    LinearLayout abboutme;
    @BindView(R.id.my_salesaccount)
    LinearLayout salesaccount;
    private boolean isEdit = false;
    private String imagepath, httpimagepath;
    ImageSelectUtil selectUtil = new ImageSelectUtil(this);
    HttpModel httpModel = new HttpModel(this);
    LogOutdialog logOutdialog, cancellationDialog;
    LogUpdatedialog nameDialog, signatureDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydetails);
        hideStatueBar(0);
        ButterKnife.bind(this);
        loadView();
    }


    @Override
    protected void loadData() {

    }

    @Override
    protected void loadView() {
        getStatusBarHeight(findViewById(R.id.toplinear));
        if (Constants.user != null) {
            name.setText(Constants.user.getUserName());
//            signature.setText(Constants.user.getSignature());
            account.setText(Constants.user.getUserMobile());
            if (!Constants.user.getHeaderUrl().equals("")) {
                Picasso.get().load(Constants.user.getHeaderUrl())
                        .fit()
                        .error(R.drawable.allbg)
                        .centerCrop()
                        .into(head);
            }
        }
        logOutdialog = new LogOutdialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.user.setId("");
                Constants.setUser(MyDetailsActivity.this, Constants.user);
                EventBus.getDefault().post("signout");
                logOutdialog.dismiss();
                finish();
            }
        }, this);
        nameDialog = new LogUpdatedialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameDialog.dismiss();
                if (nameDialog.getEditText().equals("")) {
                    showToast("请输入昵称");
                    return;
                }
                name.setText(nameDialog.getEditText());
                httpModel.userInformation(name.getText().toString(), signature.getText().toString(), Constants.user.getHeaderUrl(), 10002);
                showProgressDialog("资料修改中");
            }
        }, this);
        nameDialog.setTitle("修改昵称");
        signatureDialog = new LogUpdatedialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureDialog.dismiss();
                if (signatureDialog.getEditText().equals("")) {
                    showToast("请输入个性签名");
                    return;
                }
                signature.setText(signatureDialog.getEditText());
                httpModel.userInformation(name.getText().toString(), signature.getText().toString(), Constants.user.getHeaderUrl(), 10002);
                showProgressDialog("资料修改中");
            }
        }, this);
        signatureDialog.setTitle("修改签名");
    }

    @OnClick({R.id.my_edit, R.id.tv_title_edit, R.id.my_signout,
            R.id.my_head, R.id.my_address_edit, R.id.my_address_updatepass
            , R.id.my_signature_edit, R.id.my_abboutme,R.id.my_salesaccount})//R.id.my_name_edit
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_edit:
//                if (!isEdit) {
//                    return;
//                }
                selectUtil.selectImage(1, null);
                break;
            case R.id.tv_title_edit:
                if (titleEdit.getText().toString().equals("编辑")) {
                    titleEdit.setText("保存");
                    loging.setVisibility(View.GONE);
                    updatepass.setVisibility(View.GONE);
//                    abboutme.setVisibility(View.GONE);
                    salesaccount.setVisibility(View.GONE);
                    isEdit = true;
                    name.setEnabled(true);
                    signature.setEnabled(true);
                } else {
                    if(name.getText().toString().equals("")){
                        showToast("请输入昵称");
                        return;
                    }
                    if (httpimagepath == null && imagepath != null) {
                        httpModel.imgFile(imagepath, 10001);
                        showProgressDialog("图片上传中...");
                        return;
                    }
                    if (name.getText().toString().equals(Constants.user.getUserName())
                            && signature.getText().toString().equals(Constants.user.getSignature())) {
                        titleEdit.setText("编辑");
                        updatepass.setVisibility(View.VISIBLE);
                        loging.setVisibility(View.VISIBLE);
//                        abboutme.setVisibility(View.VISIBLE);
                        salesaccount.setVisibility(View.VISIBLE);
                        isEdit = false;
                        name.setEnabled(false);
                        signature.setEnabled(false);
                        return;
                    }
                    httpModel.userInformation(name.getText().toString(), signature.getText().toString(), Constants.user.getHeaderUrl(), 10002);
                    showProgressDialog("资料修改中");
                }
                break;
            case R.id.my_head:
                if (isEdit) {
                    selectUtil.selectImage(1, null);
                    return;
                }
                if (Constants.user.getHeaderUrl() != null)
                    selectUtil.lookImage(Constants.user.getHeaderUrl());
                break;
            case R.id.my_signout:
                logOutdialog.show();
                break;
//            case R.id.my_address_edit:
//                Intent intent = new Intent(this,AddressActivity.class);
//                startActivity(intent);
//                break;
            case R.id.my_address_updatepass:
                Intent intent = new Intent(this, UpdatePassActivity.class);
                startActivity(intent);
                break;
            case R.id.my_abboutme:
//                intent = new Intent(this, AboutMeActivity.class);
//                startActivity(intent);
                break;
//            case R.id.my_signature_edit:
//                signatureDialog.show();
//                break;
//            case R.id.my_name_edit:
//                nameDialog.show();
//                break;
            case R.id.my_salesaccount:
                cancellationDialog.show();
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
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (localMedias == null)
                        return;
                    LocalMedia localMedia = localMedias.get(0);
                    if (localMedia.isCompressed()) {
                        imagepath = localMedia.getCompressPath();
                    } else {
                        imagepath = localMedia.getPath();
                    }
                    httpimagepath = null;
                    Picasso.get().load("file://" + imagepath)
                            .fit()
                            .centerCrop()
                            .into(head);
//                    if (httpimagepath == null && imagepath != null) {
//                        httpModel.imgFile(imagepath, 10001);
//                        showProgressDialog("图片上传中...");
//                        return;
//                    }
                    break;
            }
    }

    @Override
    public void doResult(String result, int action) {
        dismissProgressDialog();
        try {
            JSONObject object = new JSONObject(result);
            if (action == 10001) {
//                    {"status":0,"msg":null,"uploadPath":"/upload/image/files/4120d647fd9c4ea298589370a1628b7c.jpg","result":null}
                if (object.getString("status").equals("0")) {
                    httpimagepath = Constants.IMAGEURL + object.getString("uploadPath");
                    httpModel.userInformation(name.getText().toString(), signature.getText().toString(), httpimagepath, 10002);
                    showProgressDialog("资料修改中");
                }
            } else if (action == 10002) {
                titleEdit.setText("编辑");
                loging.setVisibility(View.VISIBLE);
                updatepass.setVisibility(View.VISIBLE);
//                abboutme.setVisibility(View.VISIBLE);
                salesaccount.setVisibility(View.VISIBLE);
                isEdit = false;
                name.setEnabled(false);
                signature.setEnabled(false);
                Constants.user.setUserName(name.getText().toString());
                Constants.user.setSignature(signature.getText().toString());
                if (httpimagepath == null) {
                    httpimagepath = Constants.user.getHeaderUrl();
                }
                Constants.user.setHeaderUrl(httpimagepath);
                Constants.setUser(getApplicationContext(), Constants.user);
                EventBus.getDefault().post("loading");
                showToast("修改成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showToast("连接服务器失败了");
        }
    }

    @Override
    public void onErr(String errMessage) {
//        dismiss();
        dismissProgressDialog();
        showToast("网络不给力啊");
    }
}
