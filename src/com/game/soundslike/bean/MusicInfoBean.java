package com.game.soundslike.bean;

import java.io.Serializable;

public class MusicInfoBean implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	
	private String name;  // 片段名字
	
	private String band;  // 乐队
	
	private String discription; // 一些音乐的介绍，用于第三个Fragment展示
	
	private String alphaSrc; // 音乐专辑图片，直接保存成路径吧。
	
	private String musicSrc; // 音乐本机缓存，也直接保存路径吧。
	
	private String musicUrl; // 音乐网络链接，也直接保存路径吧。
	
	private boolean isPlay = false;
	
	private int raw_id = -1;  // 片段在res目录raw目录下的id
	
	private String mix_answer; // 混合答案（答案就是name）
	
	public  void  setRawId(int raw_id){
		this.raw_id = raw_id;
	}
	
	public int getRawId(){
		return raw_id;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBand() {
		return band;
	}
	
	public void setBand(String band) {
		this.band = band;
	}
	
	public String getDiscription() {
		return discription;
	}
	
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	
	public String getAlphaSrc() {
		return alphaSrc;
	}
	
	public void setAlphaSrc(String alphaSrc) {
		this.alphaSrc = alphaSrc;
	}
	
	public String getMusicSrc() {
		return musicSrc;
	}
	
	public void setMusicSrc(String musicSrc) {
		this.musicSrc = musicSrc;
	}
	
	public boolean isPlay() {
		return isPlay;
	}
	
	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}
 
    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getMix_answer() {
        return mix_answer;
    }

    /**
     * @param mix_answer the mix_answer to set
     */
    public void setMix_answer(String mix_answer) {
        this.mix_answer = mix_answer;
    }
}
