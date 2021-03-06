package cn.com.imageselect.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.com.imageselect.R;


public class LogUpdatedialog extends Dialog {
    private TextView titles,cancel,confirm;
    private EditText editText;
    private View.OnClickListener listener;

    public LogUpdatedialog(@NonNull Context context) {
        super(context);
    }

    public LogUpdatedialog(View.OnClickListener listener , Context context){
        super(context,R.style.no_border_dialog);
        this.listener = listener;
        setCancelable(true);
        setContentView(R.layout.logout_update);
        Window window = getWindow();
//        window.setGravity(Gravity.CENTER);
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = dm.widthPixels;
        window.setAttributes(lp);
        initView();
    }

    private void initView() {
        titles = findViewById(R.id.logoutdialog_title);
        cancel = findViewById(R.id.logoutdialog_cancel);
        confirm = findViewById(R.id.logoutdialog_confirm);
        editText = findViewById(R.id.logoutdialog_edit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirm.setOnClickListener(listener);
    }
    public void setTitle(String title){
        titles.setText(title);
    }
    public void setCancelListener(View.OnClickListener l){
        cancel.setOnClickListener(l);
    }
    public void setEditText(String s){
        editText.setText(s);
    }

    public String getEditText(){
        return editText.getText().toString().trim();
    }
}
