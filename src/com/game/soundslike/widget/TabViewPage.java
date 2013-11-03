package com.game.soundslike.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TabViewPage extends ViewPager {
	public boolean mCanDrag = true;

	public TabViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		if (mCanDrag) {
			return super.onInterceptTouchEvent(e);
		} else {
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (mCanDrag) {
			return super.onTouchEvent(e);
		} else {
			return false;
		}
	}

	public void setCanDragState(boolean canDrag) {
		mCanDrag = canDrag;
	}

}
