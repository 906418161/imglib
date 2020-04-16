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

public class CustomCityPicker {
    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(String city, int cityindex);
        void onSelect(int provinceindex);

    }
    List<String> list,citys;
    TextView cancle, select;
    Context context;
    DatePickerView namepickview,citypickview;
    private Dialog datePickerDialog;
    private TextView titleText;
    private ResultHandler handler;
    private String data,citydata;
    private int index=0,cityindex=0 ;
    public CustomCityPicker(Context context, List<String> list,List<String>citys, ResultHandler resultHandler){
        this.list=list;
        this.citys = citys;
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
            datePickerDialog.setContentView(R.layout.custom_city_picker);
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
    public void setCity(List<String> list){
        this.citys=list;
        citypickview.setData(citys);
        citypickview.setCanScroll(citys.size()>1);
        setCitySelected(0);
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
    public void setCitySelected(int selected) {
        citypickview.setSelected(selected);
        cityindex = selected;
        citydata = citys.get(selected);
    }
    private void initView(){
        cancle = datePickerDialog.findViewById(R.id.name_cancle);
        select = datePickerDialog.findViewById(R.id.name_select);
        namepickview = datePickerDialog.findViewById(R.id.name_pv);
        citypickview = datePickerDialog.findViewById(R.id.city_pv);
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
                handler.handle(citydata,cityindex);

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
                CustomCityPicker.this.index=index;
                handler.onSelect(index);
            }
        });
        citypickview.setData(citys);
        citypickview.setIsLoop(false);
        citypickview.setCanScroll(citys.size()>1);
        citypickview.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if(text==null){
                    citydata = citys.get(0);
                }else {
                    citydata = text;
                }
            }

            @Override
            public void onIndex(int index) {
                CustomCityPicker.this.cityindex=index;
            }
        });
    }
    public void show(){
        datePickerDialog.show();
    }
}
