package cn.com.imageselect.util.http;

public interface RequestCallbackListener {

    public void doResult(String result, int action);

    public void onErr(String errMessage);

}
