package com.game.soundslike.activity;

import android.app.Activity;

public class BaseActivity extends Activity {

	@Override
	protected void onDestroy() {
		this.finish();
	}

}
