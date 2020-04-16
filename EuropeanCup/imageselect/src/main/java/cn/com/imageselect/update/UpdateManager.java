package cn.com.imageselect.update;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.com.imageselect.R;


public class UpdateManager implements IUpdateManager {

    private String mDownLoadUrl;
    private String mDownLoadPath;
    private boolean isCancel;
    private boolean isCancelOnTouch;
    private String mMessage;
    private Context mContext;
    private UpdateStatueListener mListener;
    private OnCancelButtonClickListener mChannelClick;
    private UpdateDialog mDialog;
    private String mVersionName;
    private DownLoadManager mDownLoadManager;


    public UpdateManager(Context mContext) {
        this.mContext = mContext;
        mDownLoadManager = new DownLoadManager();
        init();
    }

    private void init() {
        //设置Dialog准备显示
        initDialog();
    }


    @Override
    public void setDownloadUrl(String url) {
        this.mDownLoadUrl = url;
    }

    @Override
    public void setFilePath(String path) {
        this.mDownLoadPath = path;
    }

    @Override
    public void setCanChannel(boolean isChannel) {
        this.isCancel = isChannel;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean isChannel) {
        this.isCancelOnTouch = isChannel;
    }

    @Override
    public void show() {
        message.setText(mMessage);
        version.setText(mVersionName);
        mDialog.show();
    }

    @Override
    public void setMessage(String message) {
        this.mMessage = message;
    }

    @Override
    public void dismiss() {
        mDialog.dismiss();
        if (mChannelClick != null) {
            mChannelClick.onCancel();
        }

    }

    @Override
    public void addVersionName(String name) {
        this.mVersionName = name;
    }

    @Override
    public void setStatueListener(UpdateStatueListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void setOnCancelButtonClickListener(OnCancelButtonClickListener mListener) {
        this.mChannelClick = mListener;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//下载成功
                if (mListener != null) {
                    mListener.onSuccess();
                }
                message.setText("下载成功");
                oK.setEnabled(true);
            } else if (msg.what == 2) {//下载失败
                if (mListener != null) {
                    mListener.onFaild();
                }
                message.setText("下载失败");
            }
        }
    };

    private ProgressBar progressBar;
    private TextView message;
    private TextView version;
    private Button oK;

    private void initDialog() {
        mDialog = new UpdateDialog(mContext, R.style.CustomDialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.update_dialog, null);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(isCancelOnTouch);
        mDialog.setCancelable(isCancel);
        progressBar = view.findViewById(R.id.progress);
        message = view.findViewById(R.id.message);
        version = view.findViewById(R.id.version);
        message.setText(mMessage);
        version.setText(mVersionName);
        oK = view.findViewById(R.id.ok);
        final Button no = view.findViewById(R.id.no);
//        oK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                oK.setEnabled(false);
//                try {
//                    mDownLoadManager.download(mDownLoadUrl, mDownLoadPath, new DownLoadManager.OnDownloadListener() {
//                        @Override
//                        public void onDownloadSuccess() {
//
//                            Message msg = new Message();
//                            msg.what = 1;
//                            handler.sendMessage(msg);
//
//
//                        }
//
//                        @Override
//                        public void onDownloading(int progress) {
//                            progressBar.setProgress(progress);
//                        }
//
//                        @Override
//                        public void onDownloadFailed() {
//                            Message msg = new Message();
//                            msg.what = 2;
//                            handler.sendMessage(msg);
//
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChannelClick != null) {
                    mChannelClick.onCancel();
                }
                mDialog.dismiss();
            }
        });
    }

    public void update(){
        try {
            mDownLoadManager.download(mDownLoadUrl, mDownLoadPath, new DownLoadManager.OnDownloadListener() {
                @Override
                public void onDownloadSuccess() {

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);


                }

                @Override
                public void onDownloading(int progress) {
                    progressBar.setProgress(progress);
                }

                @Override
                public void onDownloadFailed() {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
