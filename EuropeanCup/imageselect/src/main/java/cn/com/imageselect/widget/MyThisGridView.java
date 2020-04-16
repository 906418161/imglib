package cn.com.imageselect.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyThisGridView extends ThisGridView {
	public MyThisGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyThisGridView(Context context) {
		super(context);
	}

	public MyThisGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}