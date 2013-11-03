package com.game.soundslike.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.game.soundslike.R;
import com.game.soundslike.adapter.HomeAdapter;
import com.game.soundslike.widget.TabViewPage;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener{
	
	
	LocalActivityManager activityManager;
	TabViewPage viewPager;
	View tab_sounds_list, tab_iguass, tab_tip;
	ImageView iv_cursor;
	private int offset;
	private int current_pos = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activityManager = getLocalActivityManager();
		initView();
	}
	
	private void initView(){
		findViewById(R.id.tab_sounds_list).setOnClickListener(this);
		findViewById(R.id.tab_iguass).setOnClickListener(this);
		findViewById(R.id.tab_tip).setOnClickListener(this);
		
		iv_cursor = (ImageView)findViewById(R.id.iv_cursor);
		viewPager = (TabViewPage)findViewById(R.id.tvp_main);
		Intent intent1 = new Intent(this, MusicListActivity.class);
		Intent intent2 = new Intent(this, PlayActivity.class);
		Intent intent3 = new Intent(this, TipsActivity.class);
		
		View v1 = activityManager.startActivity("MusicListActivity", intent1).getDecorView();
		View v2 = activityManager.startActivity("PlayActivity", intent2).getDecorView();
		View v3 = activityManager.startActivity("TipsActivity", intent3).getDecorView();
		List<View> view_list = new ArrayList<View>(2);
		view_list.add(v1);
		view_list.add(v2);
		view_list.add(v3);
		
		HomeAdapter adapter = new HomeAdapter(view_list);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int idx) {
				moveCursorToCurrentIndex(idx);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				 
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				 
			}
		});
		initCursor();
	}
	
	/**
	 * 把导航的光标移到当前的位置
	 * @param idx -- 当前位置的id
	 */
	private void moveCursorToCurrentIndex(int idx){
		 Animation anim = new TranslateAnimation(offset*current_pos, offset*idx, 0, 0);
		 current_pos = idx;
		 anim.setDuration(200);
		 anim.setFillAfter(true);
		 iv_cursor.startAnimation(anim);
	}

	private void initCursor(){
		ImageView iv_cursor = (ImageView)findViewById(R.id.iv_cursor);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		offset = displayMetrics.widthPixels / 3;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		iv_cursor.getLayoutParams().width = offset;
		current_pos = 0;
		iv_cursor.setImageMatrix(matrix);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tab_sounds_list:
			// moveCursorToCurrentIndex(0);
			viewPager.setCurrentItem(0);
			break;
		case R.id.tab_iguass:
			// moveCursorToCurrentIndex(1);
			viewPager.setCurrentItem(1);
			break;
		case R.id.tab_tip:
			// moveCursorToCurrentIndex(2);
			viewPager.setCurrentItem(2);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	

}
