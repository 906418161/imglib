package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.calendarview.Calendar;
import cn.com.calendarview.CalendarLayout;
import cn.com.calendarview.CalendarView;
import cn.com.cup.european.R;
import cn.com.cup.european.bean.HomeHotBean;
import cn.com.cup.european.ui.adapter.MatchAdapter;
import cn.com.cup.european.util.base.BaseActivity;
import cn.com.cup.european.util.base.BaseFragment;
import cn.com.cup.european.util.http.HttpModel;
import cn.com.imageselect.util.http.RequestCallbackListener;

public class MatchActivity extends BaseActivity implements CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener, RequestCallbackListener {
    @BindView(R.id.tv_title_name)
    TextView title;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;
    @BindView(R.id.tv_year)
    TextView mTextYear;
    @BindView(R.id.tv_lunar)
    TextView mTextLunar;
    @BindView(R.id.tv_current_day)
    TextView mTextCurrentDay;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;
    @BindView(R.id.history_nomore)
    ImageView nomore;
    private int mYear;
    @BindView(R.id.tv_title_back)
    View back;
    @BindView(R.id.history_listview)
    ListView listView;
    MatchAdapter adapter;
    List<HomeHotBean> beans;
    HttpModel httpModel = new HttpModel(this);
    private int lastYear;
    private String lastDate = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_match);
        ButterKnife.bind(this);
        hideStatueBar(0);
        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
//        back.setVisibility(View.GONE);
        getStatusBarHeight(findViewById(R.id.toplinear));
        title.setText("赛事");
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        lastYear = mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    protected void loadData() {
        httpModel.getMatchCalendar(String.valueOf(mYear), 10001);
    }



    @OnClick({R.id.fl_current, R.id.tv_month_day})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_current:
                mCalendarView.scrollToCurrent();
                break;
            case R.id.tv_month_day:
                if (!calendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
                break;
        }
    }


    @Override
    public void onYearChange(int year) {
        if (mTextYear.getVisibility() == View.GONE) {
            mTextMonthDay.setText(String.valueOf(year));
            if (lastYear == year) {
                return;
            }
            httpModel.getMatchCalendar(String.valueOf(year), 10001);
            lastYear = year;
        }
    }


    /**
     * 封装数据
     *
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

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        if (mCalendarView.isYearSelectLayoutVisible()) {
            return;
        }
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
//        if (isClick) {
        String date = calendar.getYear() + "-";
        if (calendar.getMonth() < 10) {
            date = date + "0";
        }
        date = date + calendar.getMonth() + "-";
        if (calendar.getDay() < 10) {
            date = date + "0";
        }
        date = date + calendar.getDay();
//        }
        if (lastDate.equals(date)) {
            return;
        }
        httpModel.getMatchList(date, 10002);
        lastDate = date;
        if (lastYear == mYear) {
            return;
        }
        httpModel.getMatchCalendar(String.valueOf(mYear), 10001);
        lastYear = mYear;
    }

    @Override
    public void doResult(String result, int action) {
        try {
            dismissProgressDialog();
            JSONObject object = new JSONObject(result);
            if (object.getString("code").equals("1")) {
                if (action == 10001) {
                    JSONArray array = object.getJSONArray("data");
                    List<Calendar> schemes = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject timeObj = array.getJSONObject(i);
                        String[] time = timeObj.getString("time").split("-");
                        schemes.add(getSchemeCalendar(Integer.parseInt(time[0]),
                                Integer.parseInt(time[1]),
                                Integer.parseInt(time[2]),
                                getResources().getColor(R.color.carbar)));
                    }
                    mCalendarView.setSchemeDate(schemes);
                } else if (action == 10002) {
                    beans = JSON.parseArray(object.getString("data"), HomeHotBean.class);
                    if (beans.size() > 0) {
                        listView.setVisibility(View.VISIBLE);
                        nomore.setVisibility(View.GONE);
                        adapter = new MatchAdapter(beans, this);
                        listView.setAdapter(adapter);
                    } else {
                        nomore.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                }
            } else {
                showToast(object.getString("msg"));
            }
        } catch (Exception e) {
            showToast("网络不给力哦");
        }
    }

    @Override
    public void onErr(String errMessage) {
        try {
            dismissProgressDialog();
            showToast("网络不给力哦");
        } catch (Exception e) {

        }
    }
}
