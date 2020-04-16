package cn.com.cup.european.util.base;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.dialog.CustomDialog;


/**
 * Name: BaseFragment
 * Date: 2018-06-12 13:44
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 绑定相关资源
     */
    protected abstract void loadView();

    /**
     * 加载相关数据
     */
    protected abstract void loadData();

    public void setType(String s) {
    };


    public void showImage(ImageView imageView, String url) {
        ImageSelectUtil.showImg(imageView, url, getContext());
    }

    protected void showToast(String msg) {
        try {
            Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }

    }


    public void showProgressDialog(String msg) {
        try {
            CustomDialog.show(this.getContext(), msg, false, null);
        } catch (Exception e) {
        }
    }

    public void dismissProgressDialog() {
        CustomDialog.dismissDialog();
    }


    public void setTextViewContent(TextView textView, String str) {
        String content = str == null ? "" : str;
        textView.setText(content);
    }



    //获取手机状态栏高度
    public int getStatusBarHeight(View view) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getContext().getResources().getDimensionPixelSize(x);
            view.setPadding(0, statusBarHeight, 0, 0);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    //获取手机状态栏高度
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏透明，并且设置状态栏文字颜色
     *
     * @param type 0 : 白色 1：黑色
     */
    public void hideStatueBar(int type, Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= 24) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (type == 0) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else if (type == 1) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
        }
    }
}
