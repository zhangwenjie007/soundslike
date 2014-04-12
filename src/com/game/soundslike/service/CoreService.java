/**
 * 2014.03.22
 * author: tom
 * description:这个服务主要用来后台播放声音，各种声音都可以的。.mp3/wav/来自网络的。。
 */
package com.game.soundslike.service;

import java.io.IOException;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;

import com.game.soundslike.constants.ConstantsParamers;
import com.game.soundslike.info.MusicInfo;

/**
 * @author Administrator
 *
 */
public class CoreService extends IntentService{

    
    // 给外部提供播放结束的回调接口
    interface PlayListener{
        public void onStop();
        public void onCompleted();
    }
    
    OnCompletionListener mCompletionListener = new OnCompletionListener() {
        
        @Override
        public void onCompletion(MediaPlayer mp) {
        }
    };
    
    public static MediaPlayer mediaPlayer; // music 播放器
    
    private final static Handler changeIconHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int play_or_stop = msg.what;
            if(play_or_stop == ConstantsParamers.CORE_SERVICE_MUSIC_PAUSE){
                mediaPlayer.pause();
            }else if(play_or_stop == ConstantsParamers.CORE_SERVICE_MUSIC_START_PLAY){
                mediaPlayer.start();
            }
        }
    };
    
    
    /**
     * @param name
     */
    public CoreService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 开始播放音乐
        if(ConstantsParamers.PLAY_NEW_SOUNDS.equals(intent.getAction())){
            if(mediaPlayer == null){
                MusicInfo musicInfo = (MusicInfo)intent.getExtras().get(ConstantsParamers.MUSIC_INFO);
                mediaPlayer = MediaPlayer.create(this, musicInfo.getRawId());
                if(mediaPlayer == null){
                    return;
                }else{ // 设置一下监听器监听音乐播放完毕之后
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                    try {
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(mediaPlayer.isPlaying()){
                changeIconHandler.sendEmptyMessage(ConstantsParamers.CORE_SERVICE_MUSIC_PAUSE);
            }else{
                changeIconHandler.sendEmptyMessage(ConstantsParamers.CORE_SERVICE_MUSIC_STOP);
            }
         } 
        // 停止播放音乐 
        //else if(Constants.STOP_PLAY_SOUNDS.equals(intent.getAction())){
}
       
    
}
