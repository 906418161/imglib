package cn.com.calendarview.wight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.calendarview.Calendar;
import cn.com.calendarview.CalendarView;
import cn.com.imageselect.R;

/**
 * 封装日历控件
 * Created by Bob on 2018/6/8.
 */

public class ColorfulView extends LinearLayout implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener{
    TextView mTextMonthDay;
    TextView mTextYear;
    TextView mTextLunar;
    TextView mTextCurrentDay;
    CalendarView mCalendarView;
    RelativeLayout mRelativeTool;
    private int mYear;
//    CalendarLayout mCalendarLayout;
    private View colorfulview;
    OnDateSelectedListener onDateSelectedListener;
    public ColorfulView(Context context) {
        super(context);
    }

    public ColorfulView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        colorfulview = inflater.inflate(R.layout.view_colorful, this);
        initView();
//        initData();
    }

    public ColorfulView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void initView() {
        mTextMonthDay = (TextView) colorfulview.findViewById(R.id.tv_month_day);
        mTextYear = (TextView) colorfulview.findViewById(R.id.tv_year);
        mTextLunar = (TextView) colorfulview.findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) colorfulview.findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) colorfulview.findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) colorfulview.findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                    if (!mCalendarLayout.isExpand()) {
//                        mCalendarView.showYearSelectLayout(mYear);
//                        return;
//                    }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
//        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }


    protected void initData() {
        List<Calendar> schemes = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        schemes.add(getSchemeCalendar(year, month, 3, 0xFF40db25));
        schemes.add(getSchemeCalendar(year, month, 6, 0xFFe69138));
        schemes.add(getSchemeCalendar(year, month, 9, 0xFFdf1356));
        schemes.add(getSchemeCalendar(year, month, 13, 0xFFedc56d));
        schemes.add(getSchemeCalendar(year, month, 14, 0xFFedc56d));
        schemes.add(getSchemeCalendar(year, month, 15, 0xFFaacc44));
        schemes.add(getSchemeCalendar(year, month, 18, 0xFFbc13f0));
        schemes.add(getSchemeCalendar(year, month, 25, 0xFF13acf0));
        mCalendarView.setSchemeDate(schemes);
    }

    /**
     * 获取当天
     *
     * @return 返回今天
     */
    public int getCurDay() {
        return  mCalendarView.getCurDay();
    }

    /**
     * 获取本月
     *
     * @return 返回本月
     */
    public int getCurMonth() {
        return  mCalendarView.getCurMonth();
    }

    /**
     * 获取本年
     *
     * @return 返回本年
     */
    public int getCurYear() {
        return mCalendarView.getCurYear();
    }


    /**
     * 添加标记
     * @param mSchemeDate
     */
    public void setSchemeDate(List<Calendar> mSchemeDate){
        mCalendarView.setSchemeDate(mSchemeDate);
    }

    /**
     * 封装数据
     * @param year
     * @param month
     * @param day
     * @param color
     * @return
     */
    public Calendar getSchemeCalendar(int year, int month, int day, int color) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme("");
        return calendar;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        if(isClick){
            onDateSelectedListener.onDateSelected(calendar,isClick);
        }
    }



    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.onDateSelectedListener = listener;
    }
    /**
     * 外部日期选择事件
     */
    public interface OnDateSelectedListener {
        void onDateSelected(Calendar calendar, boolean isClick);
    }
}
