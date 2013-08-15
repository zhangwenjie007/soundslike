package com.game.soundslike;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends ActivityGroup implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LocalActivityManager activityManager = getLocalActivityManager();
		initView();
	}

	
	private void initView(){
		
	}
	@Override
	public void onClick(View v) {
		
	}

}
