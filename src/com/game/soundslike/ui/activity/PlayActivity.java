package com.game.soundslike.ui.activity;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.soundslike.R;
import com.game.soundslike.constants.ConstantsParamers;
import com.game.soundslike.info.MusicInfo;
import com.game.soundslike.ui.adapter.MyTextAdapter;

public class PlayActivity extends Activity implements OnClickListener{
	Activity act;
	Context ctx; // 上下文指针
	ImageView btn_play; // 播放按钮
	public static MediaPlayer mediaPlayer; // music 播放器
	private final static String MUSIC_INFO = "my_music";
	private static final String TAG = "MainActivity";
	private LinearLayout ll_answer = null;
	private GridView gv_mix_result = null;
	MyTextAdapter textAdapter = null;
	String arr_gate_name; // 当前的答案
	String arr_postfix_mix_str; // 用于混合答案的字符串
	List<String> list_mix_result = null;
	int[] music_id ={
			R.raw.zqxj,
			R.raw.kdxf
	};
	int current_music = -1;
	private TextView[] tv_results = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		
		Intent intent = getIntent();
		if(null != intent && null != intent.getExtras()){
			MusicInfo info = (MusicInfo)intent.getExtras().getSerializable(
					ConstantsParamers.MUSIC_INFO);
			System.out.println("music_info" + info.toString());
			if(info.getRawId() == R.raw.kdxf){
				current_music = 0;
			}
		}
		act = this;
		ctx = this;
		btn_play = (ImageView)findViewById(R.id.iv_play);
		btn_play.setOnClickListener(this);
		
		// 获取上次程序退出时音乐播放的id
		if(-1 == current_music){
			current_music = getCurrentMusicId(); 
		}
		reloadAnswerAndMixStr(current_music);
		
		// 存放答案的LinearLayout
		ll_answer = (LinearLayout)findViewById(R.id.ll_answer);
		// 给它添加答案按钮
		addAnswerViews(ll_answer, arr_gate_name.length());
		
		// 下面的“答案混合”，让用户选答案的一个GridView
		gv_mix_result = (GridView)findViewById(R.id.gv_mix_result_layout);
		
		// 获取这个GridView要显示的文字
		reloadMixResult();
		// 形成一个个的TextView添加到GridView
		addMixAnswerInGridView(gv_mix_result, list_mix_result);
		// 设置列数，只显示8列
		gv_mix_result.setNumColumns(8);
	}
	
	
	// 重新获取“混合答案”
	private List<String> reloadMixResult(){
		if(list_mix_result != null){
			list_mix_result.clear();
		}else{
			list_mix_result = new ArrayList<String>();
		}
		
		reloadAnswerAndMixStr(current_music);
		String mix_result = arr_gate_name + arr_postfix_mix_str;
		for( int i = 0; i < mix_result.length(); i++){
			list_mix_result.add(mix_result.substring(i, i+1));
			Log.v(TAG, "mix[i]:" + mix_result.substring(i, i+1));
		}
		Collections.shuffle(list_mix_result);
		return list_mix_result;
	}
	
	// 重新获取答案和混合字符串
	private void reloadAnswerAndMixStr(int current_music){
		arr_gate_name = this.getResources().getStringArray(R.array.play_list)[current_music];
		arr_postfix_mix_str = this.getResources().getStringArray(R.array.mix_strlist)[current_music];
	}
	
	
	private void reloadData(){
		textAdapter.setData(reloadMixResult());
		textAdapter.notifyDataSetChanged(); // 下面的答案文字重新刷新
		addAnswerViews(ll_answer, arr_gate_name.length());
		reloadMusic(); // 重新加载音乐
		btn_play.setImageResource(R.drawable.play_now);
	}
	
	// 按照指定字符串的长度给GridView添加按钮
	private void addMixAnswerInGridView(GridView gridview, List<String> fillList){
		textAdapter = new MyTextAdapter(this, fillList);
		gridview.setAdapter(textAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					TextView tv = (TextView)v;
					String text = tv.getText().toString();
					if(!text.equals(" ")){
						int i = 0;
						String tv_resultsAllString = "";
						for(; i < arr_gate_name.length(); i++){
							TextView result = (TextView)ll_answer.findViewById(i);
							String btnText = result.getText().toString();
							if(btnText.equals(" ")){
								result.setText(text); // 改变了按钮的文字
								tv_resultsAllString += text;
								tv.setText(" ");
								break;
							}else{
								tv_resultsAllString += btnText;
							}
						}
						Log.v(TAG, "i=" + i + ",tv_resultsAllString:");
						// 按钮文字被填满了，就判断是不是正确答案
						if(i == (arr_gate_name.length() - 1)){
							if(tv_resultsAllString.equals(arr_gate_name)){
								current_music ++;
								AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
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
								for(i = 0; i < arr_gate_name.length(); i++){
									TextView result = (TextView)findViewById(i);
									result.setTextColor(Color.RED);
								}
							}
						}
					}
				}
			});
	}
	
	
	// 根据答案的长度给答题框添加textView
	private void addAnswerViews(LinearLayout ll, int counts){
		ll.removeAllViews();
		tv_results = new TextView[counts];
		for(int i = 0; i < counts; i++){
			tv_results[i] = new TextView(this);
			tv_results[i].setBackgroundResource(R.drawable.btn_keyboard_key);
			tv_results[i].setId(i); // 用索引作为按钮id
			tv_results[i].setOnClickListener(this);
			tv_results[i].setText(" ");
			tv_results[i].setPadding(15, 10, 15, 10);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
			params.leftMargin = 2;
			params.rightMargin = 2;
			tv_results[i].setTextColor(Color.BLACK);
			ll.addView(tv_results[i],params);
		}
	}

	@SuppressLint({ "WorldWriteableFiles", "WorldReadableFiles" })
	private int getCurrentMusicId(){ // 获得当前播放音乐的id
		@SuppressWarnings("deprecation")
		SharedPreferences sp = getSharedPreferences(MUSIC_INFO, MODE_WORLD_READABLE);
		return sp.getInt("CURRENT_MUSIC", 0);
	}
	
	@SuppressLint("WorldWriteableFiles")
	private void setCurrentMusicId(int id){
		@SuppressWarnings("deprecation")
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
			    // 
                OnCompletionListener completionListener = new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // 音乐播放按钮图标变回等待播放的方式
                        btn_play.setBackgroundResource(R.drawable.play_now);
                    }
                };
			    Intent playIntent = new Intent(ConstantsParamers.PLAY_NEW_SOUNDS);
			    playIntent.putExtra(ConstantsParamers.MUSIC_INFO, new MusicInfo());
			    startService(playIntent);
				break;
			}
			case 0: // 给按钮的id
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				TextView tv_result = (TextView)v; // 当前点击的答案textview
				String currentBack = tv_result.getText().toString();
				if(!currentBack.equals(" ")){
					TextView tv = (TextView)gv_mix_result.findViewById(
							list_mix_result.indexOf(currentBack));
					tv.setText(currentBack);
					tv_result.setTextColor(Color.BLACK);
					tv_result.setText(" ");
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
