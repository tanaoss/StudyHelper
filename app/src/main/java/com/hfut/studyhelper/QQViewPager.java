package com.hfut.studyhelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class QQViewPager extends ViewPager {
    //必须实现带一个参数的构造方法
    public QQViewPager(Context context) {
        super(context);
    }

    //必须实现此构造方法，否则在界面设计器中不能正常显示
    public QQViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
