package cn.com.imageselect.update;

public interface IUpdateManager  {


    /**
     * 设置下载地址 必须
     * @param url
     */
    public void setDownloadUrl(String url);

    /**
     * 设置下载路径 设置路径名称即可 如 /CG/file
     * @param path
     */
    public void setFilePath(String path);

    /**
     * 点击返回取消
     * @param isChannel
     */
    public void setCanChannel(boolean isChannel);

    /**
     * 点击空白区域取消
     * @param isChannel
     */
    public void setCanceledOnTouchOutside(boolean isChannel);

    /**
     * 显示
     */
    public void show();

    /**
     * 设置显示信息
     * @param message
     */
    public void setMessage(String message);

    /**
     * 取消
     */
    public void dismiss();

    /**
     * 设置版本名称
     * @param name
     */
    public void addVersionName(String name);


    /**
     * 设置更新状态回调
     * @param mListener
     */
    public void setStatueListener(UpdateStatueListener mListener);

    /**
     * 设置取消按钮回调
     * @param mListener
     */
    public void setOnCancelButtonClickListener(OnCancelButtonClickListener mListener);

}
