package com.game.soundslike.constants;


public final class ConstantsParamers {

	// 播放文件的类型
	public static final String MUSIC_INFO = "music_info";
    public static String PLAY_NEW_SOUNDS = "play_new_sounds"; // 播放新音乐
    public static String STOP_SOUNDS = "stop_sounds"; // 停止播放
    public static String PAUSE_SOUNDS = "pause_sounds"; // 暂停播放
    public static String CONTINUE_SOUNDS = "continue_sounds"; // 继续播放
    
    public static String ACTION_SOUNDS_COMPLETION = "action_sounds_completion"; // 停止播放
    
    // 播放状态  0 -启动播放, 1 - 暂停, 2 - 停止 
    public static final int CORE_SERVICE_MUSIC_START_PLAY = 0;
    public static final int CORE_SERVICE_MUSIC_PAUSE = 1;
    public static final int CORE_SERVICE_MUSIC_STOP = 2;
    
    // 主界面Fragment 索引
    public static final int FRAGMENT_PLAYLIST = 0;
    public static final int FRAGMENT_PLAY = 1;
    public static final int FRAGMENT_DESCRIPTION = 2;
    public static final int FRAGMENT_IDX_MAX = 3; 
}
