package cn.com.imageselect.update;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

public class UpdateDialog extends Dialog {

    private Context mContext;

    public UpdateDialog(@NonNull Context context) {
        super(context);
    }

    public UpdateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    private void initView() {
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    }


}
