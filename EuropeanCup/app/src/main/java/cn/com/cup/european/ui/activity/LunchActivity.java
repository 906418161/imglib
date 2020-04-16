package cn.com.cup.european.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.cup.european.R;
import cn.com.cup.european.util.base.BaseActivity;

public class LunchActivity extends BaseActivity {
    private long currentSecond = 4;//当前毫秒数
    private Handler mhandle = new Handler();
    @BindView(R.id.textview)
    TextView textview;
    @BindView(R.id.image)
    ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);
        ButterKnife.bind(this);
        hideStatueBar(0);
        getStatusBarHeight(findViewById(R.id.toplinear));
        mhandle.post(timeRunable);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Picasso.get()
                .load(R.drawable.lunch)
                .fit()
                .centerCrop()
                .into(image);
    }

    @Override
    protected void loadView() {

    }

    @Override
    protected void loadData() {

    }


    /*****************计时器*******************/
    private Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            currentSecond = currentSecond - 1;
            textview.setText(currentSecond + "s");
            if (currentSecond == 0) {
                finish();
                return;
            }
            //递归调用本runable对象，实现每隔一秒一次执行任务
            mhandle.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandle.removeCallbacks(timeRunable);
    }
}
