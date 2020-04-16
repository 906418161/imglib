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

public class CustomAreaPicker {

    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void city(int provinceindex,int cityindex);
        void onSelect(int provinceindex);
        void all(String province,String city,String area);
    }
    List<String> list,citys,area;
    TextView cancle, select;
    Context context;
    DatePickerView namepickview,citypickview,areapickview;
    private Dialog datePickerDialog;
    private TextView titleText;
    private ResultHandler handler;
    private String data,citydata,areadata;
    private int index=0,cityindex=0,areaindex=0 ;
    public CustomAreaPicker(Context context, List<String> list,List<String>citys,List<String>area, ResultHandler resultHandler){
        this.list=list;
        this.citys = citys;
        this.area=area;
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
            datePickerDialog.setContentView(R.layout.custom_area_picker);
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

    public void setArea(List<String> list){
        this.area=list;
        areapickview.setData(area);
        areapickview.setCanScroll(area.size()>1);
        setAreaSelected(0);
    }
    public void setSelected(String data){
        namepickview.setSelected(data);
        this.data=data;
        index=list.indexOf(data);
    }

    /**
     * 选择省
     * @param selected
     */
    public void setSelected(int selected) {
        namepickview.setSelected(selected);
        index = selected;
        data = list.get(selected);
    }
    //选择市
    public void setCitySelected(int selected) {
        citypickview.setSelected(selected);
        cityindex = selected;
        citydata = citys.get(selected);
    }
    //选择区
    public void setAreaSelected(int selected) {
        areapickview.setSelected(selected);
        areaindex = selected;
        areadata = area.get(selected);
    }
    private void initView(){
        cancle = datePickerDialog.findViewById(R.id.name_cancle);
        select = datePickerDialog.findViewById(R.id.name_select);
        namepickview = datePickerDialog.findViewById(R.id.name_pv);
        citypickview = datePickerDialog.findViewById(R.id.city_cust);
        areapickview = datePickerDialog.findViewById(R.id.area_pv);
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
                if(citydata==null){
                    citydata=citys.get(0);
                }
                if(areadata==null){
                    areadata=area.get(0);
                }
                handler.all(data,citydata,areadata);
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
                CustomAreaPicker.this.index=index;
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
                CustomAreaPicker.this.cityindex=index;
                handler.city(CustomAreaPicker.this.index,index);
            }
        });
        areapickview.setData(area);
        areapickview.setIsLoop(false);
        areapickview.setCanScroll(area.size()>1);
        areapickview.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if(text==null){
                    areadata = area.get(0);
                }else {
                    areadata = text;
                }
            }

            @Override
            public void onIndex(int index) {
                CustomAreaPicker.this.areaindex=index;
            }
        });
    }
    public void show(){
        datePickerDialog.show();
    }
}
