/**
 * 2014.03.22
 * author: tom
 * description:这个服务主要用来后台播放声音，各种声音都可以的。.mp3/wav/来自网络的。。
 * 当播放完成、停止之后会发送广播给通知观察者
 */
package com.game.soundslike.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.game.soundslike.bean.MusicInfoBean;
import com.game.soundslike.constants.ConstantsParamers;

/**
 * @author Administrator
 *
 */
public class CoreService extends IntentService 
    implements OnErrorListener{
    
    private static final String MUSIC_PATH = new String("/sdcard/"); //音乐的路径
    
    Context mContext = null; // 上下文

    NoisyAudioStreamReceiver myNoisyAudioStreamReceiver; // 广播接收者，用于接收音频设备发生变化
    
    public static MediaPlayer mediaPlayer; // music 播放器
    
    private Handler mMainHandler;
    
    public CoreService() {
        super("CoreService");
        mContext = this;
        mMainHandler = new Handler(Looper.getMainLooper());
        myNoisyAudioStreamReceiver = new NoisyAudioStreamReceiver();
        registerReceiver(); // 注册输出设备发送变化广播
    } 
    
    
    @Override
    public void onCreate() {
        super.onCreate();
    }




    /**
     * 2014.4.16
     * 这个广播用于监听音频输出设备是否发生变化，例如耳机拔掉会切换到扬声器
     * 这里会监听到这个广播
     */
    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                // Pause the playback
            }
        }
    }

    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    private void registerReceiver() {
        registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(myNoisyAudioStreamReceiver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        mediaPlayer.setOnErrorListener(this);
        // 开始播放音乐
        if(ConstantsParamers.PLAY_NEW_SOUNDS.equals(intent.getAction())){
//            if(mediaPlayer != null){
                MusicInfoBean musicInfo = (MusicInfoBean)intent.getExtras()
                        .get(ConstantsParamers.MUSIC_INFO);
                playMusic(musicInfo);
                Toast.makeText(mContext, "you are play music", Toast.LENGTH_SHORT).show();
//            } 
        } else if(ConstantsParamers.PAUSE_SOUNDS.equals(intent.getAction())){
            if(mediaPlayer != null){
                mediaPlayer.pause();
            }
        } else if(ConstantsParamers.PAUSE_SOUNDS.equals(intent.getAction())){
            if(mediaPlayer != null){
                mediaPlayer.pause();
            }
        }
    }
        
      //播放音乐 
        private void playMusic(MusicInfoBean musicInfoBean){
            try {  
                 if(musicInfoBean.getRawId() != 0){ 
                     // release MediaPlayer resource
                     if(mediaPlayer != null){
                         mediaPlayer.reset();
                         mediaPlayer.release();
                         mediaPlayer = null;
                     }
                     mediaPlayer = MediaPlayer.create(mContext, musicInfoBean.getRawId());
                     mediaPlayer.setOnErrorListener(this);
                 }else if(TextUtils.isEmpty(musicInfoBean.getMusicSrc())){
                     mediaPlayer.setDataSource( MUSIC_PATH + musicInfoBean.getMusicSrc());
                     mediaPlayer.prepare();
                 }else if(TextUtils.isEmpty(musicInfoBean.getMusicSrc())){
                     mediaPlayer.setDataSource(mContext, Uri.parse(musicInfoBean.getMusicUrl()));
                     mediaPlayer.prepare();
                 }
     
                 mediaPlayer.start();
                 mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                     @Override
                     public void onCompletion(MediaPlayer mp) {
                          sendBroadcast(new Intent(ConstantsParamers.ACTION_SOUNDS_COMPLETION));
                     }
                 });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            unregisterReceiver();
        }
        
}
