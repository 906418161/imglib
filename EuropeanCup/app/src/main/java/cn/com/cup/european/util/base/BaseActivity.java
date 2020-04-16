package cn.com.cup.european.util.base;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.imageselect.ImageSelectUtil;
import cn.com.imageselect.util.dialog.CustomDialog;

/**
 * Name: BaseActivity
 * Date: 2018-06-06 15:17
 */

public abstract class BaseActivity extends FragmentActivity {

    protected static String TAG;
    private CustomDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatueBar(0);
    }

    /**
     * 绑定相关资源
     */
    protected abstract void loadView();

    /**
     * 加载相关数据
     */
    protected abstract void loadData();


    public void goBack(View view) {
        finish();
    }

    public void showImage(ImageView imageView, String url) {
        ImageSelectUtil.showImg(imageView, url, this);
    }

    public void showImage(ImageView imageView, Uri url) {
        ImageSelectUtil.showImg(imageView, url, this);
    }
    public void showProgressDialog(String msg) {
        try {
            mProgressDialog = CustomDialog.show(this, msg, false, null);
        } catch (Exception e) {

        }
    }

    public void setProgressDialogText(String msg) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(msg);
        }
    }

    public void dismissProgressDialog() {
        try{
            CustomDialog.dismissDialog();
        }catch (Exception x){

        }
    }


    public void setTextViewContent(TextView textView, String str) {
        String content = str == null ? "" : str.equals("") ? " " : str;
        textView.setText(content);
    }


    /**
     * 判断身份证号
     *
     * @param IDNumber
     * @return
     */
    public static boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾


        boolean matches = IDNumber.matches(regularExpression);

        //判断第18位校验值
        if (matches) {
            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常:" + IDNumber);
                    return false;
                }
            }

        }
        return matches;
    }

    /**
     * 判断手机号
     *
     * @param telNum
     * @return
     */
    public static boolean isMobileNum(String telNum) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(telNum);
        return m.matches();
    }


    /**
     * 提示消息
     */
    public void showToast(String message, int duration) {
        getFragmentManager();
        Toast toast = Toast
                .makeText(getApplicationContext(), message, duration);
        toast.show();
    }


    /**
     * 获取Editext的内容
     *
     * @param edit
     * @return
     */
    public String getEditext(EditText edit) {
        return edit.getText().toString().trim();
    }

    /**
     * 获取TextView的内容
     *
     * @param textView
     * @return
     */
    public String getTextViewText(TextView textView) {
        return textView.getText().toString().trim();
    }

    public void showToast(String text) {
        try {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }


    /**
     * 设置状态栏透明，并且设置状态栏文字颜色
     *
     * @param type 0 : 白色 1：黑色
     */
    public void hideStatueBar(int type) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
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
            statusBarHeight = getApplicationContext().getResources().getDimensionPixelSize(x);
            view.setPadding(0, statusBarHeight, 0, 0);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

}
