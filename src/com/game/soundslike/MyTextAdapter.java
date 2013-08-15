package com.game.soundslike;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyTextAdapter extends BaseAdapter {

	private Context context = null;
	private List<String> list;
	
	public MyTextAdapter(Context ctx, List<String> list) {
		 this.context = ctx;
		 this.list = list;
	}
	
	public void setData(List<String> list){
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
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
		Log.v("TEST", "we got text" + list.get(idx));
		tv.setText((String)list.get(idx));
		tv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_keyboard_key));
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(5, 5, 5, 5);
//		tv.setLayoutParams(new AbsoluteLayout.LyoutParams(LinearLayout.LayoutParams.FILL_PARENT,
//                  LinearLayout.LayoutParams.FILL_PARENT,(float) 1.0));
		tv.setTextSize(20);
		tv.setId(idx);
		tv.setTextColor(Color.BLACK);
		return tv;
	}
}
