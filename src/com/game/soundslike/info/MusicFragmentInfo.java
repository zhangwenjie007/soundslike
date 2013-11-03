package com.game.soundslike.info;

import java.io.Serializable;

public class MusicFragmentInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 音乐片段 
	 * 乐队（演唱者）
	 *  描述  
	 *  来源的动画或者游戏图片
	 */
	private String name;
	private String band;
	private String discription;
	private String alphaSrc; // 图片，直接保存成路径吧。
	private String musicSrc; // 音乐，也直接保存路径吧。
	private boolean isPlay = false;
	private int raw_id = -1;
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
}
