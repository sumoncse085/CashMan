package com.cashman.physio.v1.android.alarm.data;


public class CommonSetting extends ColumnSizeFormat{

	private int id = -1;
	private int alarmId = -1;
	private String createTime = "";
	private String weekday = "";
	private String ringtonePath="";
	private boolean ringtoneEnable=true;
	private boolean vibrateEnable = false;
	private String videoPath = "";
	private boolean activate = true;
	private String name ="";
	private String instruction ="";
	private String videoThumbPath = "";
	private int every = 0;
	
	public int getEvery() {
		return every;
	}

	public void setEvery(int every) {
		this.every = every;
	}

	public CommonSetting(){}
	
	public CommonSetting(int alarmId,String name,String instruction,
			String createTime,String weekday,
			String ringtonePath,boolean ringtoneEnable,
			boolean vibrateEnable,
			String videoPath,String videoThumbPath,
			boolean activate,int every){
		this(-1,alarmId,name,instruction,createTime,weekday,ringtonePath,ringtoneEnable,vibrateEnable,videoPath,videoThumbPath,activate,every);
	}
	
	public CommonSetting(int id,int alarmId,
			String name,String instruction,
			String createTime,String weekday,
			String ringtonePath,boolean ringtoneEnable,
			boolean vibrateEnable,
			String videoPath,String videoThumbPath,
			boolean activate,int every){
		this.id = id;
		this.alarmId = alarmId;
		this.createTime = getColumn(createTime, ColumnSize.CREATE_TIME);
		this.ringtonePath = getColumn(ringtonePath, ColumnSize.RINGTONE_PATH);
		this.ringtoneEnable = ringtoneEnable;
		this.vibrateEnable = vibrateEnable;
		this.videoPath = getColumn(videoPath, ColumnSize.VIDEO_PATH);
		this.activate = activate;
		this.weekday = getColumn(weekday, ColumnSize.WEEKDAY);
		this.name = getColumn(name, ColumnSize.NAME);
		this.instruction = getColumn(instruction, ColumnSize.INSTRUCTION);
		this.videoThumbPath = getColumn(videoThumbPath, ColumnSize.VIDEO_THUMB_PATH);
		this.every = every;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}

	public String getRingtonePath() {
		return ringtonePath;
	}

	public void setRingtonePath(String ringtonePath) {
		this.ringtonePath = getColumn(ringtonePath, ColumnSize.RINGTONE_PATH);
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

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = getColumn(videoPath, ColumnSize.VIDEO_PATH);
	}

	public boolean isActivate() {
		return activate;
	}

	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = getColumn(createTime, ColumnSize.CREATE_TIME);
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = getColumn(weekday,ColumnSize.WEEKDAY);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = getColumn(name, ColumnSize.NAME);
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = getColumn(instruction,ColumnSize.INSTRUCTION);
	}

	public String getVideoThumbPath() {
		return videoThumbPath;
	}

	public void setVideoThumbPath(String videoThumbPath) {
		this.videoThumbPath = getColumn(videoThumbPath, ColumnSize.VIDEO_THUMB_PATH);
	}
	
	public class ColumnSize extends ColumnSizeFormat.ColumnSize{
		public static final int RINGTONE_PATH = 100;
		public static final int VIDEO_PATH = 100;
		public static final int CREATE_TIME = 16;
		public static final int WEEKDAY     = 7;
		public static final int NAME = 100;
		public static final int INSTRUCTION = 1000;
		public static final int VIDEO_THUMB_PATH = 100;
	}
}
