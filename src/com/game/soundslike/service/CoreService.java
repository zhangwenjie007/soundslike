/**
 * 2014.03.22
 * author: tom
 * description:这个服务主要用来后台播放声音，各种声音都可以的。.mp3/wav/来自网络的。。
 * 当播放完成、停止之后会发送广播给通知观察者
 */
package com.game.soundslike.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
    
    
    //音乐的路径
    private static final String MUSIC_PATH = new String("/sdcard/");
    
    // 上下文
    Context mContext = null;
    
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
    
    
    private Handler mMainHandler;
    
    public CoreService() {
        super("CoreService");
        mContext = this;
        mMainHandler = new Handler(Looper.getMainLooper());
//        mediaPlayer = new MediaPlayer();
    }
    
    /**
     * @param name
     */
    public CoreService(String name) {
        super(name); 
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        mediaPlayer.setOnErrorListener(this);
        // 开始播放音乐
        if(ConstantsParamers.PLAY_NEW_SOUNDS.equals(intent.getAction())){
//            if(mediaPlayer != null){
                MusicInfoBean musicInfo = (MusicInfoBean)intent.getExtras().get(ConstantsParamers.MUSIC_INFO);
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
//                     if(mediaPlayer != null){
//                         mediaPlayer.reset();
//                         mediaPlayer.release();
//                         mediaPlayer = null;
//                     }
                     mediaPlayer = MediaPlayer.create(mContext, musicInfoBean.getRawId());
//                     mediaPlayer.setDataSource(mContext, Uri.parse());
                     mediaPlayer.setOnErrorListener(this);
                 }else if(TextUtils.isEmpty(musicInfoBean.getMusicSrc())){
                     mediaPlayer.setDataSource( MUSIC_PATH + musicInfoBean.getMusicSrc() );
                 }else if(TextUtils.isEmpty(musicInfoBean.getMusicSrc())){
                     mediaPlayer.setDataSource(mContext, Uri.parse(musicInfoBean.getMusicUrl()));
                 }
     
                 mediaPlayer.prepare();
                 mediaPlayer.start();
                 mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                     
                     @Override
                     public void onCompletion(MediaPlayer mp) { 
                          
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
}
