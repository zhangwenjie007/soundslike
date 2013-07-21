package com.game.soundslike;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyTextAdapter extends BaseAdapter {

	private Context context = null;
	
	private String showStr = "";
	public MyTextAdapter(Context ctx, String fillStr) {
		 context = ctx;
		 showStr = fillStr;
	}
	
	public void setData(String showStr){
		this.showStr = showStr;
	}
	
	@Override
	public int getCount() {
		return showStr.length();
	}
	@Override
	public Object getItem(int arg0) {
		return null;
	}
	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int idx, View v, ViewGroup parentViewGroup) {
		TextView tv = null;
		tv = new TextView(context);
		tv.setText(showStr.substring(idx, idx+1));
		tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_keyboard_key));
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(20);
		tv.setId(idx);
		tv.setTextColor(Color.WHITE);
		return tv;
	}
}
