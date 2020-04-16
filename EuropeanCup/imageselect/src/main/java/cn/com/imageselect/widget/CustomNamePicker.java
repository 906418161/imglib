package cn.com.imageselect.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import java.util.List;

import cn.com.imageselect.R;

/**
 * Created by Bob on 2018/6/13.
 */

public class CustomNamePicker {
    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(String name);
        void handleindex(int index);
    }
    List<String> list;
    TextView cancle, select;
    Context context;
    DatePickerView namepickview;
    private Dialog datePickerDialog;
    private TextView titleText;
    private ResultHandler handler;
    private String data;
    private int index=0;
    public CustomNamePicker(Context context, List<String> list, ResultHandler resultHandler){
        this.list=list;
        this.context = context;
        this.handler=resultHandler;
        initDialog();
        initView();
    }
    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.time_dialog);
            datePickerDialog.setCancelable(true);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.custom_name_picker);
            Window window = datePickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            window.setAttributes(lp);
        }
    }
    public void setTitle(String title){
        titleText.setText(title);
    }
    public void setData(List<String> list){
        this.list=list;
        namepickview.setData(list);
    }
    public void setSelected(String data){
        namepickview.setSelected(data);
        this.data=data;
        index=list.indexOf(data);
    }
    public void setSelected(int selected) {
        namepickview.setSelected(selected);
        index = selected;
        data = list.get(selected);
    }
    private void initView(){
        cancle = datePickerDialog.findViewById(R.id.name_cancle);
        select = datePickerDialog.findViewById(R.id.name_select);
        namepickview = datePickerDialog.findViewById(R.id.name_pv);
        titleText = datePickerDialog.findViewById(R.id.name_title);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.dismiss();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data==null){
                    data = list.get(0);
                }
                handler.handle(data);
                handler.handleindex(index);
                datePickerDialog.dismiss();
            }
        });
        namepickview.setData(list);
        namepickview.setIsLoop(false);
        namepickview.setCanScroll(list.size()>1);
        namepickview.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if(text==null){
                    data = list.get(0);
                }else {
                    data = text;
                }
            }

            @Override
            public void onIndex(int index) {
                CustomNamePicker.this.index=index;
            }
        });
    }
    public void show(){
        datePickerDialog.show();
    }
}
