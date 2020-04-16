package cn.com.imageselect.util.http;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Name: HttpUtil
 * Author: Created by fuliangbo
 * Email: fuliangbo@tieserv.com
 * Date: 2018-05-04 10:19
 * 封装的功能有：
 * 一般的get请求
 * 一般的post请求
 * 上传单个文件(包含进度)
 * 上传list集合文件
 * 上传map集合文件
 * 文件下载(包含进度)
 * 图片下载(实现了图片的压缩)
 */

public class HttpUtil { //具体内容放后面写


    private static HttpUtil mInstance;
    private OkHttpClient mOkHttpClient;
    private Call mCall;
    public static String ERR_MSG = "err";
    public static int TIME_OUT = 10;

    private static final String TAG = "OkHttpClientManager";

    private HttpUtil() {
        mOkHttpClient = new OkHttpClient();
    }

    public static HttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil();
                }
            }
        }
        return mInstance;

    }


    public String getHttpString(String url) {
        try {
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            mCall = mOkHttpClient.newCall(request);
            Response execute = mCall.execute();
            return execute.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERR_MSG;
    }


    public String getHttpString(String url, String token) {
        try {
            if (token.equals("")) {
                return "目标token为空";
            }
            final Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            mCall = mOkHttpClient.newCall(request);
            Response execute = mCall.execute();
            return execute.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERR_MSG;
    }


    public String postHttpString(String url, Map<String, String> prams) {
//        try {
//            FormBody.Builder bulider = new FormBody.Builder();
//            for (String key:prams.keySet()){
//                bulider.add(key,prams.get(key));
//            }
//            FormBody formBody = bulider.build();
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(formBody)
//                    .build();
//            mCall = mOkHttpClient.newCall(request);
//            Response execute = mCall.execute();
//            return execute.body().string();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        try {
            String content = prams != null && prams.entrySet().size() > 0 ? JSON.toJSONString(prams) : "";
            RequestBody bulider = FormBody.create(MediaType.parse("application/json"), content);
            Request request = new Request.Builder().url(url).post(bulider).build();
            mCall = mOkHttpClient.newCall(request);
            Response execute = mCall.execute();
            return execute.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERR_MSG;
    }


    public String postHttpString(String url, Map<String, String> prams, String token) {
        try {
            if (token.equals("")) {
                return "目标token为空";
            }
            FormBody.Builder bulider = new FormBody.Builder();
            for (String key : prams.keySet()) {
                bulider.add(key, prams.get(key));
            }
            FormBody formBody = bulider.build();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .post(formBody)
                    .build();
            mCall = mOkHttpClient.newCall(request);
            Response execute = mCall.execute();
            return execute.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERR_MSG;
    }


    public String postHttpString(String url, Map<String, String> prams, Map<String, File> files) {
        try {

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (String key : prams.keySet()) {
                builder.addFormDataPart(key, prams.get(key));
            }
            //原本的上传方式但是后台接收不到
//            for (String file : files.keySet()) {
//                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), files.get(file));
//                builder.addFormDataPart("file", file, fileBody);
//            }
            //原本的上传方式但是后台接收不到修改为新的参数
            for (String file : files.keySet()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), files.get(file));
                builder.addFormDataPart("files", files.get(file).getName(), fileBody);
            }
            MultipartBody body = builder.build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            mCall = mOkHttpClient.newCall(request);
            //            Response execute = mCall.execute();
            return mCall.execute().body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERR_MSG;
    }


    public String postHttpString(String url, Map<String, String> prams, Map<String, File> files, String token) {
        try {
            if (token.equals("")) {
                return "目标token为空";
            }
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (String key : prams.keySet()) {
                builder.addFormDataPart(key, prams.get(key));
            }


            for (String file : files.keySet()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), files.get(file));
                builder.addFormDataPart("file", file, fileBody);
            }
            MultipartBody body = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .post(body)
                    .build();
            mCall = mOkHttpClient.newCall(request);
            //            Response execute = mCall.execute();

            return mCall.execute().body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERR_MSG;
    }


    public String postHttpImgs(String url, Map<String, String> prams, Map<String, List<File>> files) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (String key : prams.keySet()) {
                builder.addFormDataPart(key, prams.get(key));
            }


            for (String file : files.keySet()) {
                List<File> fileList = files.get(file);
                for (File file1 : fileList) {
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file1);
                    builder.addFormDataPart("files", file1.getName(), fileBody);
                }
            }
            MultipartBody body = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            mCall = mOkHttpClient.newCall(request);
            //            Response execute = mCall.execute();

            return mCall.execute().body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERR_MSG;
    }


    public void setTimeout(int times) {
        TIME_OUT = times;
        mOkHttpClient.newBuilder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)//10秒连接超时
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)//10m秒写入超时
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)//10秒读取超时
                .build();
    }


    public String postjsonBody(String url, String body) {
        try {


            MediaType mediaType = MediaType.parse("application/json");

            //创建RequestBody对象，将参数按照指定的MediaType封装
            RequestBody requestBody = RequestBody.create(mediaType, body);
            Request request = new Request
                    .Builder()
                    .post(requestBody)//Post请求的参数传递
                    .url(url)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            String result = response.body().string();
            response.body().close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ERR_MSG;

    }


    public String postjsonBody(String url, String body, String token) {
        try {
            if (token.equals("")) {
                return "目标token为空";
            }
            MediaType mediaType = MediaType.parse("application/json");

            //创建RequestBody对象，将参数按照指定的MediaType封装
            RequestBody requestBody = RequestBody.create(mediaType, body);
            Request request = new Request
                    .Builder()
                    .addHeader("Authorization", "Bearer " + token)
                    .post(requestBody)//Post请求的参数传递
                    .url(url)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            String result = response.body().string();
            response.body().close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return ERR_MSG;

    }

}