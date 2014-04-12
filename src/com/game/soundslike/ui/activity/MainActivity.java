package com.game.soundslike.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.game.soundslike.R;
import com.game.soundslike.constants.ConstantsParamers;
import com.game.soundslike.ui.adapter.HomeAdapter;
import com.game.soundslike.widget.MainActivityViewPager;

public class MainActivity extends FragmentActivity implements OnClickListener{
    
	private MainActivityViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		initView();
	}
	
	private void initView(){
	    
		viewPager = (MainActivityViewPager)findViewById(R.id.home_viewpager); 
		// 设置ViewPager的适配器
		HomeAdapter adapter = new HomeAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		// 设置页面发生变化的监听器
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int idx) {
			    Toast.makeText(getApplicationContext(), "you change page", Toast.LENGTH_SHORT).show();
			    viewPager.setCurrentItem(idx);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				 
			}
		});
		
		viewPager.setCurrentItem(ConstantsParamers.FRAGMENT_PLAY);
		viewPager.setOffscreenPageLimit(3);
	}
	 
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
            case 0:
                break;
            default:
                break;
        }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	

}
