package com.game.soundslike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	Activity act;
	Context ctx; // 上下文指针
	ImageView btn_play; // 播放按钮
	public static MediaPlayer mediaPlayer; // music 播放器
	private final static String MUSIC_INFO = "my_music";
	private static final String TAG = "MainActivity";
	private LinearLayout ll_answer = null;
	private GridView gv_mix_result = null;
	MyTextAdapter textAdapter = null;
	String[] arr_gate_name; // 所有的答案，arr_game_name[current_music]即答案
	String[] arr_postfix_mix_str; // 用于混合答案的字符串数组
	String str_mix_result = "";
	int[] music_id ={
			R.raw.zqxj,
			R.raw.kdxf
	};
	int current_music = 0;
	private Button[] btns = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		act = (Activity)this;
		ctx = this;
		btn_play = (ImageView)findViewById(R.id.iv_play);
		btn_play.setOnClickListener(this);
		arr_gate_name = this.getResources().getStringArray(R.array.play_list);
		arr_postfix_mix_str = this.getResources().getStringArray(R.array.mix_strlist);
		current_music = getCurrentMusicId(); // 获取上次程序退出时音乐播放的id
		ll_answer = (LinearLayout)findViewById(R.id.ll_answer);
		addButtons(ll_answer, arr_gate_name[current_music].length());
		gv_mix_result = (GridView)findViewById(R.id.gv_mix_result_layout);
		reloadMixResult();
		addMixAnswerInGridView(gv_mix_result, str_mix_result);
		gv_mix_result.setNumColumns(8);
	}
	
	private void reloadMixResult(){
		str_mix_result = getMixString(current_music);
	}
	
	
	private final static Handler changeIconHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int play_or_stop = msg.what;
			if(play_or_stop == 0){
				mediaPlayer.pause();
			}else if(play_or_stop == 1){
				mediaPlayer.start();
			}
		}
		
	};
	
	// 获取混合之后的字符串
	private String getMixString(int current_music){
		List<String> list = new ArrayList<String>();
		String mix_result = arr_gate_name[current_music] + arr_postfix_mix_str[current_music];
		String final_mix_result = "";
		for( int i = 0; i < mix_result.length(); i++){
			list.add(mix_result.substring(i, i+1));
		}
		Collections.shuffle(list);
		for( int i = 0; i < list.size(); i++ ){
			final_mix_result += list.get(i);
		}
		Log.v(TAG, "after shuffle:" + list.toString());
		return final_mix_result;
	}
	
	private void reloadData(){
		reloadMixResult();
		textAdapter.setData(getMixString(current_music));
		textAdapter.notifyDataSetChanged(); // 下面的答案文字重新刷新
		addButtons(ll_answer, arr_gate_name[current_music].length());
		reloadMusic(); // 重新加载音乐
		btn_play.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_now));
	}
	
	// 按照指定字符串的长度给GridView添加按钮
	@SuppressLint("NewApi")
	private void addMixAnswerInGridView(GridView gridview, String fillStr){
		textAdapter = new MyTextAdapter(this, fillStr);
		gridview.setAdapter(textAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					TextView tv = (TextView)v;
					String text = tv.getText().toString();
					if(!text.equals(" ")){
						int i = 0;
						String btnResult = "";
						for(; i < arr_gate_name[current_music].length(); i++){
							Button btn = (Button)ll_answer.findViewById(i);
							String btnText = btn.getText().toString();
							if(btnText.equals(" ")){
								btn.setText(text); // 改变了按钮的文字
								btnResult += text;
								tv.setText(" ");
								break;
							}else{
								btnResult += btnText;
							}
						}
						Log.v(TAG, "i=" + i + ",btnResult:");
						// 按钮文字被填满了，就判断是不是正确答案
						if(i == (arr_gate_name[current_music].length() - 1)){
							if(btnResult.equals(arr_gate_name[current_music])){
								current_music ++;
								AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
								builder.setTitle("回答正确!");
								if(current_music == music_id.length){
									builder.setMessage("答题完毕!");	
								}else{
									builder.setMessage("下一题!");
								}
								current_music = current_music % music_id.length;
								builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int witch) {
										Log.v(TAG, "current_music:" + current_music);
										reloadData();
										dialog.dismiss();
									}
								});
								AlertDialog dialog = builder.create();
								dialog.setCancelable(false);
								dialog.setOnKeyListener(new OnKeyListener() {
									
									@Override
									public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
										return true;
									}
									
								});
								dialog.show();
							}else{
								for(i = 0; i < arr_gate_name[current_music].length(); i++){
									Button btn = (Button)findViewById(i);
									btn.setTextColor(Color.RED);
								}
							}
						}
					}
				}
			});
	}
	
	
	// 根据答案的长度给答题框添加响应长度的按钮
	private void addButtons(LinearLayout ll, int counts){
		ll.removeAllViews();
		btns = new Button[counts];
		for(int i = 0; i < counts; i++){
			btns[i] = new Button(this);
			btns[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_key_hl_bg));
			btns[i].setId(i); // 用索引作为按钮id
			btns[i].setOnClickListener(this);
			btns[i].setText(" ");
			ll.addView(btns[i]);
		}
	}

	@SuppressLint({ "WorldWriteableFiles", "WorldReadableFiles" })
	private int getCurrentMusicId(){ // 获得当前播放音乐的id
		SharedPreferences sp = getSharedPreferences(MUSIC_INFO, MODE_WORLD_READABLE);
		return sp.getInt("CURRENT_MUSIC", 0);
	}
	
	@SuppressLint("WorldWriteableFiles")
	private void setCurrentMusicId(int id){
		SharedPreferences sp = getSharedPreferences(MUSIC_INFO, MODE_WORLD_WRITEABLE);
		Editor editor = sp.edit();
		editor.putInt("CURRENT_MUSIC", id);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	private void reloadMusic(){
		if(mediaPlayer != null){
			mediaPlayer.stop();
			mediaPlayer = MediaPlayer.create(ctx, music_id[current_music]);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iv_play:
			{
				if(mediaPlayer == null){
						mediaPlayer = MediaPlayer.create(ctx, music_id[current_music]);
						if(mediaPlayer == null){
							return;
						}else{ // 设置一下监听器监听音乐播放完毕之后
							mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer mp) {
									// 音乐播放按钮图标变回等待播放的方式
									btn_play.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_now));
								}
							});
						}
				}
				if(mediaPlayer.isPlaying()){
					btn_play.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_now));
					changeIconHandler.sendEmptyMessage(0);
					
				}else{
					btn_play.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_pause));
					changeIconHandler.sendEmptyMessage(1);
				}
				break;
			}
			case 0: // 给按钮的id
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				Button btn = (Button)v;
				String btnStr = btn.getText().toString();
				if(!btnStr.equals(" ")){
					Log.v(TAG, "btnStr:" + btnStr);
					Log.v(TAG, "str_mix_result:" + str_mix_result);
					Log.v(TAG, "current back id:" + str_mix_result.indexOf(btnStr));
					TextView tv = (TextView)gv_mix_result.findViewById(
							str_mix_result.indexOf(btnStr));
					tv.setText(btnStr);
					btn.setTextColor(Color.WHITE);
					btn.setText(" ");
				}
				break;
			default:
				break;
			}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null != mediaPlayer){
			mediaPlayer.release();
		}
		// 保存一下当前音乐播放的id
		setCurrentMusicId(current_music);
	}
	
	

}
