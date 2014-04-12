package com.game.soundslike.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MainActivityViewPager extends ViewPager {

    public boolean mCanDrag = true;

    public MainActivityViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (mCanDrag) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (mCanDrag) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }

    public void setCanDragState(boolean canDrag) {
        mCanDrag = canDrag;
    }

}
