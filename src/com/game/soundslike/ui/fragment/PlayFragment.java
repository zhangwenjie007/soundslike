/**
 * @date 2014.4.9
 * @author tom
 * @description 这个Fragment在主界面，用于显示当前的选中的音乐，并且
 * 
 */
package com.game.soundslike.ui.fragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.soundslike.R;
import com.game.soundslike.bean.MusicInfoBean;
import com.game.soundslike.constants.ConstantsParamers;
import com.game.soundslike.service.CoreService;
import com.game.soundslike.ui.adapter.MyTextAdapter;

public class PlayFragment extends Fragment
    implements OnClickListener{
    
    ImageView btn_play; // 播放按钮
    
    public static MediaPlayer mediaPlayer; // music 播放器
    
    private final static String MUSIC_INFO = "my_music";
    
    private static final String TAG = "MainActivity";
    
    private LinearLayout ll_answer = null;
    
    private GridView gv_mix_result = null;
    
    MyTextAdapter textAdapter = null;
    
    private View currentView = null;
    
    String arr_gate_name; // 当前的答案
    
    String arr_postfix_mix_str; // 用于混合答案的字符串
    
    List<String> list_mix_result = null;
    
    int[] music_id ={
            R.raw.zqxj,
            R.raw.kdxf
    };
    
    int current_music = -1;
    
    private TextView[] tv_results = null;
    
    Activity activity = null;
    Fragment fragment;

    private boolean is_playing = false;
    
    /**
     * Fragment的生命周期的相关方法
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        activity = getActivity();
        fragment = this;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.activity_play, null);
        
        btn_play = (ImageView)contentView.findViewById(R.id.iv_play); 
        
        btn_play.setOnClickListener(this);
        
        current_music = 0;
        
        reloadAnswerAndMixStr(current_music);
        
        // 存放答案的LinearLayout
        ll_answer = (LinearLayout)contentView.findViewById(R.id.ll_answer);
        
        // 给它添加答案按钮
        addAnswerViews(ll_answer, arr_gate_name.length());
        
        // 下面的“答案混合”，让用户选答案的一个GridView
        gv_mix_result = (GridView)contentView.findViewById(R.id.gv_mix_result_layout);
        
        // 获取这个GridView要显示的文字
        reloadMixResult();
        
        // 形成一个个的TextView添加到GridView
        addMixAnswerInGridView(gv_mix_result, list_mix_result);
        
        // 设置列数，只显示8列
        gv_mix_result.setNumColumns(8);
        
        return contentView;
    }
    
    
 // 按照指定字符串的长度给GridView添加按钮
    private void addMixAnswerInGridView(GridView gridview, List<String> fillList){
        textAdapter = new MyTextAdapter(activity, fillList);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                                    
                                    TextView result = (TextView)fragment.getView().findViewById(i);
                                    result.setTextColor(Color.RED);
                                }
                            }
                        }
                    }
                }
            });
    }
    
    
    private void reloadData(){
        textAdapter.setData(reloadMixResult());
        textAdapter.notifyDataSetChanged(); // 下面的答案文字重新刷新
        addAnswerViews(ll_answer, arr_gate_name.length());
        reloadMusic(); // 重新加载音乐
        btn_play.setImageResource(R.drawable.play_now);
    }
    
    private void reloadMusic(){
        
        // MusicInfo
        MusicInfoBean playBean = new MusicInfoBean();
        playBean.setRawId(music_id[current_music]);
        // 启动服务播放数据
        Intent serviceIntent = new Intent(activity, CoreService.class);
        serviceIntent.setAction(ConstantsParamers.PLAY_NEW_SOUNDS);
        serviceIntent.putExtra(ConstantsParamers.MUSIC_INFO, playBean);
        activity.startService(serviceIntent);
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
    
    // 根据答案的长度给答题框添加textView
    private void addAnswerViews(LinearLayout ll, int counts){
        ll.removeAllViews();
        tv_results = new TextView[counts];
        for(int i = 0; i < counts; i++){
            tv_results[i] = new TextView(ll.getContext());
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
    
 // 重新获取答案和混合字符串
    private void reloadAnswerAndMixStr(int current_music){
        arr_gate_name = this.getResources().getStringArray(R.array.play_list)[current_music];
        arr_postfix_mix_str = this.getResources().getStringArray(R.array.mix_strlist)[current_music];
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                if(!is_playing){
                    reloadMusic();
                }else{
                    stopMusic();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 停止当前播放的音乐
     */
    private void stopMusic() {
        // 启动服务播放数据
        Intent serviceIntent = new Intent(activity, CoreService.class);
        serviceIntent.setAction(ConstantsParamers.PAUSE_SOUNDS); 
        activity.startService(serviceIntent);
    }
}
