/**
 * @date    : 2014.4.9
 * @author  : tom
 * @descrip : 本应用的主界面，包含若干个fragment，请查看  {@link HomeAdapter}里面包含的Fragment
 * 获取相关信息。
 */

package com.game.soundslike;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.game.soundslike.ui.adapter.HomeAdapter;
import com.game.soundslike.widget.MainActivityViewPager;

public class MainActivity extends FragmentActivity 
    implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		
		
	}

	private void initView(){
	    //增加页面缓存数量,以提升滑动性能,可能会影响启动速度，后期优化 - haole
	    MainActivityViewPager mViewPager = (MainActivityViewPager)findViewById(R.id.home_viewpager);
	    mViewPager.setAdapter(new HomeAdapter(getSupportFragmentManager()));
	    mViewPager.setOffscreenPageLimit(3);
	    mViewPager.setOnPageChangeListener(pagerChangeListener);
	}
	
	MyOnPageChangeListener pagerChangeListener = new MyOnPageChangeListener(){
	    
	};
	
	
	public class MyOnPageChangeListener implements OnPageChangeListener {
        private int mCurrentPosition = -1; 
        private int mNextPosition = -1;
        
        public MyOnPageChangeListener() {
        }

        public void setCurrentPosition(int position) {
            mCurrentPosition = position;
        }
        
        @Override
        public void onPageSelected(int position) {
            mNextPosition = position;  
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:{
                    
                    mCurrentPosition = mNextPosition;
                }
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:{
                    
                }
                    break;
                    
                case ViewPager.SCROLL_STATE_SETTLING:{
                }
                    break;
                }
        }
    }
	
	@Override
	public void onClick(View v) {
		
	}

}
