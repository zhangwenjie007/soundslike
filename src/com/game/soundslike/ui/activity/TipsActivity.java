package com.game.soundslike.ui.activity;


import android.os.Bundle;
import android.widget.TextView;

import com.game.soundslike.R;

public class TipsActivity extends BaseActivity {
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.current_tip_view);
		
		TextView tv_tips = (TextView)findViewById(R.id.tv_tip);
		
	}
}
