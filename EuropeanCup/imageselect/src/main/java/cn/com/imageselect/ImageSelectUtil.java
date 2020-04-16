package cn.com.imageselect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.com.imageselect.picture.PictureSelector;
import cn.com.imageselect.picture.config.PictureConfig;
import cn.com.imageselect.picture.config.PictureMimeType;
import cn.com.imageselect.picture.entity.LocalMedia;
import cn.com.imageselect.picture.tools.PictureFileUtils;

/**
 * 图片选择控制器
 * Created by Bob on 2018/6/5.
 */

public class ImageSelectUtil {
    private Context context;
    private static Target target;

    public ImageSelectUtil(Context context) {
        this.context = context;
    }

    /**
     * 选择图片
     */
    public void selectImage() {
// 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create((Activity) context)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(3)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
//                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .compressSavePath(getPath())//压缩图片保存地址
//                .freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer()// 是否圆形裁剪 true or false
//                .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                .rotateEnabled() // 裁剪是否可旋转图片 true or false
//                .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
//                .videoQuality()// 视频录制质量 0 or 1 int
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 选择图片
     */
    public void selectImage(int num, List<LocalMedia> selectionMedia) {
// 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create((Activity) context)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(num)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
//                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .compressSavePath(getPath())//压缩图片保存地址
//                .freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer()// 是否圆形裁剪 true or false
//                .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                .rotateEnabled() // 裁剪是否可旋转图片 true or false
//                .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
//                .videoQuality()// 视频录制质量 0 or 1 int
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)

                .selectionMedia(selectionMedia)//已选择的图片
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
     */
    public static void deleteCacheDirFile(Context context) {
        PictureFileUtils.deleteCacheDirFile(context);
    }

    /**
     * 启动视频
     */
    public void video() {
        PictureSelector.create((Activity) context)
                .openGallery(PictureMimeType.ofVideo())
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 查看图片
     *
     * @param path
     */
    public void lookImage(String path) {
        LocalMedia localMedia = new LocalMedia();
        localMedia.setPath(path);
        List<LocalMedia> list = new ArrayList<>();
        list.add(localMedia);
        lookImage(0, list);
    }

    /**
     * 查看图片
     *
     * @param
     */
    public void lookImages(int position, List<String> listpath) {
        LocalMedia localMedia = null;
        List<LocalMedia> list = new ArrayList<>();
        for (int i = 0; i < listpath.size(); i++) {
            localMedia = new LocalMedia();
            localMedia.setPath(listpath.get(i));
            list.add(localMedia);
        }
        lookImage(position, list);
    }

    public void lookImage(int position, String[] listpath) {
        LocalMedia localMedia = null;
        List<LocalMedia> list = new ArrayList<>();
        for (int i = 0; i < listpath.length; i++) {
            localMedia = new LocalMedia();
            localMedia.setPath(listpath[i]);
            list.add(localMedia);
        }
        lookImage(position, list);
    }

    /**
     * 查看图片
     *
     * @param position
     * @param selectList
     */
    public void lookImage(int position, List<LocalMedia> selectList) {
        PictureSelector.create((Activity) context).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
    }

    public static String getPath() {
        String filename = Environment.getExternalStorageDirectory() + "/ImageSave";
        File dir = new File(filename);
        // 如果目录不存在，创建这个目录
        if (!dir.exists()) {
            dir.mkdir();
        }
        return filename;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public long getFolderSize(File file) {
        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public String getFormatSize(long size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void showImg(ImageView imageView, String url) {
        if (url == null || url.equals("")) {
            imageView.setImageResource(R.drawable.allbg);
            return;
        }
//        Picasso.with(context)
        Picasso.get()
                .load(url)
//                .placeholder(R.drawable.allbg)
                .error(R.drawable.allbg)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    public static void showImg(ImageView imageView, String url, Context context) {
//        Picasso.with(context)
        if (url == null || url.equals("")) {
            imageView.setImageResource(R.drawable.allbg);
            return;
        }
        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    public static void showImg(ImageView imageView, Uri url, Context context) {
//        Picasso.with(context)
        Picasso.get()
                .load(url)
//                .placeholder(R.drawable.allbg)
//                .error(R.drawable.allbg)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    public static void getBitmap(final ImageView imageView, String url, final Context context) {
        target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {


                ViewTreeObserver vto = imageView.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        double width = dip2px(context, bitmap.getWidth());
                        double height = dip2px(context, bitmap.getHeight());
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        double scale = height / width;
                        params.height = (int) (imageView.getWidth() * scale);
                        imageView.setLayoutParams(params);
                        imageView.setImageBitmap(bitmap);
                        target = null;
                    }
                });


            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        imageView.setTag(target);
        Picasso.get()
                .load(url)
                .into(target);
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
