package cn.com.cup.european.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import cn.com.cup.european.bean.User;
import cn.com.cup.european.ui.activity.PassloadingActivity;


public class Constants {

    //小米
    public static String APP_ID = "H63097DzJhoVmd9JJSXaQgK6-MdYXbMMI";
    public static String APP_KEY = "2ai3yK5o2H4rYYqjsY5crSzY";
    //华为
//    public static String APP_ID = "BuclvyHBl6UlUFsO6l9MYSnN-MdYXbMMI";
//    public static String APP_KEY = "D51jLrrS9u8sS3iYLmFisUcm";
    public static User user;
    public static final String USER_MODEL = "thirteenrides";
    public static final String PASSWORD_ENC_SECRET = "zsjbase64";
    //    public static String BASE_URL = "http://47.112.30.2/capital";
    public static String IMAGEURL = "http://139.9.186.134/";
    //        public static String  BASE_URL="http://192.168.3.13";//测试地址
//    public static String BASE_URL = "http://192.168.3.127:2022";//本地测试地址
    public static String BASE_URL = "http://139.9.186.134/euro";//

    public static String GENRE = "2";
    public static String SMS = "/user/getSms";//获取验证码
    public static String IMGFILE = "/web/user/upload";//上传图片
    public static String RECORD = "/about/feedback";//反馈

    public static boolean isPhoneCode = false;//是否动态密码登录
    public static boolean isTop = true;//是否计算全面屏
    public static String PASSLOADING = "/user/login";//密码登录
    public static String PASSREGISTER = "/user/register";//注册
    public static String UPDATEPASS = "/user/update_pwd";//修改密码
    public static String FORGETPASS = "/user/forgetPassWord";//忘记密码

    public static String USERINFORMATION = "/user/updateUserInformation";//修改用户资料
    public static String REALNAME = "/user/real_name";//实名认证


    public static String HOMEEXPERTS = "/home/experts";//专家列表
    public static String HOTTHEME = "/home/hot_theme";//热门主题
    public static String HOTMATCH = "/home/hot_match";//热门比赛
    public static String FOLLOW = "/bbs/fans";//关注
    public static String REDLIST = "/home/red_list";//达人详情
    public static String BBSDETAILS = "/bbs/details";//达人解读详情
    public static String NEWSLIST = "/news/list";//新闻列表
    public static String PLLIST = "/news/pl_list";//新闻评论列表
    public static String ADDPL = "/news/add_pl";//添加评论
    public static String NEWCOLLECTION = "/news/collection";//收藏新闻
    public static String NEWSZAN = "/news/zan";//新闻评论点赞
    public static String MATCHCALENDAR = "/match/calendar";//比赛日历
    public static String MATCHLIS = "/match/list";//比赛列表
    public static String MATCHSQUAD = "/match/squad";//比赛阵容
    public static String MATCHANALYZE = "/match/analyze";//赛前分析
    public static String MATCHSTATISTICAL = "/match/technology_statistical";//技术统计
    public static String INTEGRAL = "/ranking/integral";//积分榜
    public static String RANKINGSHOOT = "/ranking/shoot";//1射手榜2助攻榜
    public static String BBSINTEREST = "/bbs/interest";//论坛可能敢兴趣的人
    public static String POSTBBS = "/bbs/release";//发布帖子
    public static String THEMELIST = "/bbs/theme_list";//发布页面主题列表
    public static String HOTBBS = "/bbs/hot_bbs";//热帖
    public static String BBSZAN = "/bbs/zan";//点赞帖子
    public static String FANSBBS = "/bbs/fans_bbs";//关注人发布的帖子
    public static String HOMEBANNER = "/home/lunbo";//首页轮播
    public static String FANSLIST = "/about/fans";//关注粉丝列表
    public static String BBSPL = "/bbs/pl";//评论帖子
    public static String ABOUTUSER = "/about/user";//个人信息
    public static String ABOUTRELEASE = "/about/release";//个人发布
    public static String ABOUTZAN="/about/zan";//获赞
    public static String HOMEBBSLIST="/home/bbs_list";//根据类型查询话题
    public static String LABEL="/label/get_all";//获取标签
    public static String STARLIST="/label/get_star_list";//球星资讯
    public static String EUROCUPHISTORY= "/eurocup/history";//欧杯历史
    public static String EUROCUPDETAILS="/eurocup/details";//欧杯详情
    public static String EUROCUPIMG="/eurocup/img";//欧杯详情
    public static String ISCERTIFICATION="/about/is_certification";//是否认证
    public static String CERTIFICATION="/about/certification";//认证
    public static String HOMEUNSCRAMBLE="/home/unscramble";//首页专家解读
    public static String HOTNOTE="/home/hot_note";//解读
    public static String NOTESLIST="/home/notes";//笔记列表
    public static String FANSNOTES="/home/fans_notes";//关注人发的笔记
    public static String COLLECTIONNOTES="/home/collection";//收藏的笔记
    public static String COLLECTIONNOTE="/home/collection_note";//收藏笔记
    public static String RELEASENOTE="/bbs/release_note";//发布笔记
    public static String MATCHLIST="/match/match_list";//比赛列表
    public static String USERNOTE="/bbs/user_note";//我发布的


    public static String[] internet = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.READ_EXTERNAL_STORAGE,};

    public static void setUser(Context context, User user) {
        SharedPreferences fer = context.getSharedPreferences(USER_MODEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = fer.edit();
        editor.putString("userName", encryptPassword(user.getUserName()));
        editor.putString("userMobile", encryptPassword(user.getUserMobile()));
        editor.putString("headerUrl", encryptPassword(user.getHeaderUrl()));
        editor.putString("id", encryptPassword(user.getId()));
        editor.putString("isVerified", encryptPassword(user.getIsVerified()));
        editor.putString("isPush", encryptPassword(user.getIsPush()));
        editor.putString("accountName", encryptPassword(user.getAccountName()));
        editor.putString("idCard", encryptPassword(user.getIdCard()));
        editor.putString("signature", encryptPassword(user.getSignature()));
        editor.putString("token", encryptPassword(user.getToken()));

        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences fer = context.getSharedPreferences(USER_MODEL, Context.MODE_PRIVATE);
        String userName = fer.getString("userName", "");
        String userMobile = fer.getString("userMobile", "");
        String headerUrl = fer.getString("headerUrl", "");
        String id = fer.getString("id", "");
        String isVerified = fer.getString("isVerified", "");
        String isPush = fer.getString("isPush", "");
        String accountName = fer.getString("accountName", "");
        String idCard = fer.getString("idCard", "");
        String signature = fer.getString("signature", "");
        String token = fer.getString("token", "");
        if (userName.equals("") && userName.equals("")) {
            return null;
        }
        User user = new User();
        user.setUserName(decryptPassword(userName));
        user.setUserMobile(decryptPassword(userMobile));
        user.setHeaderUrl(decryptPassword(headerUrl));
        user.setId(decryptPassword(id));
        user.setIsVerified(decryptPassword(isVerified));
        user.setIsPush(decryptPassword(isPush));
        user.setAccountName(decryptPassword(accountName));
        user.setSignature(decryptPassword(signature));
        user.setIdCard(decryptPassword(idCard));
        user.setToken(decryptPassword(token));
        return user;
    }

    /**
     * 加密
     **/
    private static String encryptPassword(String clearText) {
        try {
            DESKeySpec keySpec = new DESKeySpec(
                    PASSWORD_ENC_SECRET.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String encrypedPwd = Base64.encodeToString(cipher.doFinal(clearText
                    .getBytes("UTF-8")), Base64.DEFAULT);
            return encrypedPwd;
        } catch (Exception e) {
        }
        return clearText;
    }


    /**
     * 解密
     **/
    private static String decryptPassword(String encryptedPwd) {
        try {
            DESKeySpec keySpec = new DESKeySpec(PASSWORD_ENC_SECRET.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encryptedWithoutB64 = Base64.decode(encryptedPwd, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = cipher.doFinal(encryptedWithoutB64);
            return new String(plainTextPwdBytes);
        } catch (Exception e) {
        }
        return encryptedPwd;
    }

    public static void loading(Context context) {
        goIntent(context, PassloadingActivity.class);
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    public static void getPermission(String permissions[], Context context) {
        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(context, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, toApplyList.toArray(tmpList), 123);
        }
    }

    public static String getString(String key, Context context) {
        SharedPreferences fer = context.getSharedPreferences(USER_MODEL, Context.MODE_PRIVATE);
        String string = decryptPassword(fer.getString(key, ""));
        return string;
    }

    public static void setString(String key, String v, Context context) {
        SharedPreferences fer = context.getSharedPreferences(USER_MODEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = fer.edit();
        editor.putString(key, encryptPassword(v));
        editor.apply();
    }

    public static boolean isLoging() {
        if (Constants.user == null || Constants.user.getId().equals("")) {
            return false;
        }
        return true;
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    public static boolean getExternal(Context context) {
        String[] external = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        ArrayList<String> toApplyList = new ArrayList<String>();
        boolean isRedSdCard = true;
        for (String perm : external) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(context, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.
                isRedSdCard = false;
            }
        }
        return isRedSdCard;
    }

    //这是一个方法，其中filename是放在assets中的本地JSON文件名
    public static String getJson(String fileName, Context context) {
        //这个用来保存JSON格式字符串
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void goIntent(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void goIntent(Context context, Class c, String key, String v) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, v);
        context.startActivity(intent);
    }

    /**
     * 获取时间
     *
     * @param type
     * @return
     */
    public static String getVoiceFileName(String type) {
        long getNowTimeLong = System.currentTimeMillis();
        SimpleDateFormat time = new SimpleDateFormat(type);
        String result = time.format(getNowTimeLong);
        return result;
    }

    /**
     * 拨打电话
     *
     * @param phoneNum
     * @param context
     */
    public static void callPhone(String phoneNum, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

}
