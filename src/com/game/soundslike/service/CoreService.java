/**
 * 2014.03.22
 * author: tom
 * description:这个服务主要用来后台播放声音，各种声音都可以的。.mp3/wav/来自网络的。。
 * 当播放完成、停止之后会发送广播给通知观察者
 */
package com.game.soundslike.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.game.soundslike.bean.MusicInfoBean;
import com.game.soundslike.constants.ConstantsParamers;

/**
 * @author Administrator
 *
 */
public class CoreService extends Service 
    implements OnErrorListener{
    
    private static final String MUSIC_PATH = new String("/sdcard/"); //音乐的路径
    
    Context mContext = null; // 上下文

    NoisyAudioStreamReceiver myNoisyAudioStreamReceiver; // 广播接收者，用于接收音频设备发生变化
    
    public static MediaPlayer mediaPlayer; // music 播放器
 
    private String playType = "";
    
    private int currentRawId;

    private String TAG = "CoreService";
 
    public CoreService() { 
        super();
    } 
     
    /**
     * @author tom
     * @description 本方法执行本服务的一些初始化的工作
     */
    private void initMyData(){
        mContext = this;
        
        registerReceiver(); // 注册输出设备发送变化广播
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        initMyData(); // 初始化工作
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
                // 提示一下
                Toast.makeText(mContext, "you change audio device", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void registerReceiver() {
        myNoisyAudioStreamReceiver = new NoisyAudioStreamReceiver();
//        mContext.registerReceiver(myNoisyAudioStreamReceiver, 
//                new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
    }

    private void unregisterReceiver() {
        unregisterReceiver(myNoisyAudioStreamReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        // 开始播放音乐
        if (ConstantsParamers.PLAY_NEW_SOUNDS.equals(intent.getAction())) {
            playType = ConstantsParamers.PLAY_NEW_SOUNDS;
            MusicInfoBean musicInfo = (MusicInfoBean)intent.getExtras().get(
                    ConstantsParamers.MUSIC_INFO);
            playMusic(musicInfo);
            Toast.makeText(mContext, "you are play music", Toast.LENGTH_SHORT).show();
        } else if (ConstantsParamers.PAUSE_SOUNDS.equals(intent.getAction())) {
            playType = ConstantsParamers.PLAY_NEW_SOUNDS;
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        } else if (ConstantsParamers.PAUSE_SOUNDS.equals(intent.getAction())) {
            playType = ConstantsParamers.PAUSE_SOUNDS;
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
        
      //播放音乐 
        private void playMusic(MusicInfoBean musicInfoBean){
            try {  
            if (musicInfoBean.getRawId() != 0) {

                // 这里如果播放音乐已经创建句柄，我们就不创建了。只要重头开始播放音乐
                // 否则使用raw文件夹的资源创建一个新的MediaPlayer对象
                if (playType == ConstantsParamers.PLAY_NEW_SOUNDS
                        && currentRawId != musicInfoBean.getRawId()) {
                    Log.i(TAG, "it's new music");
                    mediaPlayer = MediaPlayer.create(mContext, musicInfoBean.getRawId());
                    currentRawId = musicInfoBean.getRawId();
                } else {
                    mediaPlayer.seekTo(0);
                    Log.i(TAG, "it's old music");
                }
                mediaPlayer.setOnErrorListener(this);
                // 如果播放本地音乐，我们就可以直接设置本地路径就行了
            } else if (TextUtils.isEmpty(musicInfoBean.getMusicSrc())) {
                mediaPlayer.setDataSource(MUSIC_PATH + musicInfoBean.getMusicSrc());
                mediaPlayer.prepare();
                // 播放网络音乐，使用URI解析音乐
            } else if (TextUtils.isEmpty(musicInfoBean.getMusicSrc())) {
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


        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
        
}
