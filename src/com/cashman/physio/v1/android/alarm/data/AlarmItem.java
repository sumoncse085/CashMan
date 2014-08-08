package com.cashman.physio.v1.android.alarm.data;

import java.io.Serializable;
import java.util.List;

public class AlarmItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -225608816594124749L;
	private int alarmId;
	private String name;
	private String instruction;
	private String thumbPath;
	private String videoPath;
	private List<String> startTimeList;
	private boolean activate;
	private String ringTime;
	private String weekday;
	private String ringtoneUri;
	private boolean ringtoneEnable;
	private boolean vibrateEnable;
	
	
	
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public String getThumbPath() {
		return thumbPath;
	}
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
	public String getVideoPath() {
		return videoPath;
	}
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	public List<String> getStartTimeList() {
		return startTimeList;
	}
	public void setStartTimeList(List<String> startTimeList) {
		this.startTimeList = startTimeList;
	}
	public boolean isActivate() {
		return activate;
	}
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	public String getRingTime() {
		return ringTime;
	}
	public void setRingTime(String ringTime) {
		this.ringTime = ringTime;
	}
	public String getWeekday() {
		return weekday;
	}
	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	public String getRingtoneUri() {
		return ringtoneUri;
	}
	public void setRingtoneUri(String ringtoneUri) {
		this.ringtoneUri = ringtoneUri;
	}
	public boolean isRingtoneEnable() {
		return ringtoneEnable;
	}
	public void setRingtoneEnable(boolean ringtoneEnable) {
		this.ringtoneEnable = ringtoneEnable;
	}
	public boolean isVibrateEnable() {
		return vibrateEnable;
	}
	public void setVibrateEnable(boolean vibrateEnable) {
		this.vibrateEnable = vibrateEnable;
	}
	
}
