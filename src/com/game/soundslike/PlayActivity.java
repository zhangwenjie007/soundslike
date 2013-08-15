package com.game.soundslike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PlayActivity extends Activity implements OnClickListener{
	Activity act;
	Context ctx; // ������ָ��
	ImageView btn_play; // ���Ű�ť
	public static MediaPlayer mediaPlayer; // music ������
	private final static String MUSIC_INFO = "my_music";
	private static final String TAG = "MainActivity";
	private LinearLayout ll_answer = null;
	private GridView gv_mix_result = null;
	MyTextAdapter textAdapter = null;
	String arr_gate_name; // ��ǰ�Ĵ�
	String arr_postfix_mix_str; // ���ڻ�ϴ𰸵��ַ���
	List<String> list_mix_result = null;
	int[] music_id ={
			R.raw.zqxj,
			R.raw.kdxf
	};
	int current_music = 0;
	private TextView[] tv_results = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		act = (Activity)this;
		ctx = this;
		btn_play = (ImageView)findViewById(R.id.iv_play);
		btn_play.setOnClickListener(this);
		
		// ��ȡ�ϴγ����˳�ʱ���ֲ��ŵ�id
		current_music = getCurrentMusicId(); 
		reloadAnswerAndMixStr(current_music);
		
		// ��Ŵ𰸵�LinearLayout
		ll_answer = (LinearLayout)findViewById(R.id.ll_answer);
		// ������Ӵ𰸰�ť
		addAnswerViews(ll_answer, arr_gate_name.length());
		
		// ����ġ��𰸻�ϡ������û�ѡ�𰸵�һ��GridView
		gv_mix_result = (GridView)findViewById(R.id.gv_mix_result_layout);
		
		// ��ȡ���GridViewҪ��ʾ������
		reloadMixResult();
		// �γ�һ������TextView��ӵ�GridView
		addMixAnswerInGridView(gv_mix_result, list_mix_result);
		// ����������ֻ��ʾ8��
		gv_mix_result.setNumColumns(8);
	}
	
	
	// ���»�ȡ����ϴ𰸡�
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
	
	// ���»�ȡ�𰸺ͻ���ַ���
	private void reloadAnswerAndMixStr(int current_music){
		arr_gate_name = this.getResources().getStringArray(R.array.play_list)[current_music];
		arr_postfix_mix_str = this.getResources().getStringArray(R.array.mix_strlist)[current_music];
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
	
	private void reloadData(){
		textAdapter.setData(reloadMixResult());
		textAdapter.notifyDataSetChanged(); // ����Ĵ���������ˢ��
		addAnswerViews(ll_answer, arr_gate_name.length());
		reloadMusic(); // ���¼�������
		btn_play.setImageResource(R.drawable.play_now);
	}
	
	// ����ָ���ַ����ĳ��ȸ�GridView��Ӱ�ť
	private void addMixAnswerInGridView(GridView gridview, List<String> fillList){
		textAdapter = new MyTextAdapter(this, fillList);
		gridview.setAdapter(textAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
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
								result.setText(text); // �ı��˰�ť������
								tv_resultsAllString += text;
								tv.setText(" ");
								break;
							}else{
								tv_resultsAllString += btnText;
							}
						}
						Log.v(TAG, "i=" + i + ",tv_resultsAllString:");
						// ��ť���ֱ������ˣ����ж��ǲ�����ȷ��
						if(i == (arr_gate_name.length() - 1)){
							if(tv_resultsAllString.equals(arr_gate_name)){
								current_music ++;
								AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
								builder.setTitle("�ش���ȷ!");
								if(current_music == music_id.length){
									builder.setMessage("�������!");	
								}else{
									builder.setMessage("��һ��!");
								}
								current_music = current_music % music_id.length;
								builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
	
	
	// ���ݴ𰸵ĳ��ȸ���������textView
	private void addAnswerViews(LinearLayout ll, int counts){
		ll.removeAllViews();
		tv_results = new TextView[counts];
		for(int i = 0; i < counts; i++){
			tv_results[i] = new TextView(this);
			tv_results[i].setBackgroundResource(R.drawable.btn_keyboard_key);
			tv_results[i].setId(i); // ��������Ϊ��ťid
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
	private int getCurrentMusicId(){ // ��õ�ǰ�������ֵ�id
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
				if(mediaPlayer == null){
						mediaPlayer = MediaPlayer.create(ctx, music_id[current_music]);
						if(mediaPlayer == null){
							return;
						}else{ // ����һ�¼������������ֲ������֮��
							mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer mp) {
									// ���ֲ��Ű�ťͼ���صȴ����ŵķ�ʽ
									btn_play.setBackgroundResource(R.drawable.play_now);
								}
							});
						}
				}
				if(mediaPlayer.isPlaying()){
					btn_play.setBackgroundResource(R.drawable.play_now);
					changeIconHandler.sendEmptyMessage(0);
					
				}else{
					btn_play.setBackgroundResource(R.drawable.play_pause);
					changeIconHandler.sendEmptyMessage(1);
				}
				break;
			}
			case 0: // ����ť��id
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				TextView tv_result = (TextView)v; // ��ǰ����Ĵ�textview
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
		// ����һ�µ�ǰ���ֲ��ŵ�id
		setCurrentMusicId(current_music);
	}
}
