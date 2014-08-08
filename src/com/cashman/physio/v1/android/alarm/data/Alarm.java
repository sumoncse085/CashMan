package com.cashman.physio.v1.android.alarm.data;


public class Alarm extends ColumnSizeFormat{
	
	private int id = -1;
	private String startTime;
	private int foreignId = -1;
	
	public static final int FOREIGN_ID_DEFAULT = -1;
	
	public Alarm(){};
	
	public Alarm(String start,int foreignId){
		this.startTime  = getColumn(start, ColumnSize.START_TIME);
		this.foreignId  = foreignId;
	}
	
	public Alarm(String start){
		this(start,FOREIGN_ID_DEFAULT);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = getColumn(startTime, ColumnSize.START_TIME);
	}

	public int getForeignId() {
		return foreignId;
	}

	public void setForeignId(int foreignId) {
		this.foreignId = foreignId;
	}
	
	public class ColumnSize extends ColumnSizeFormat.ColumnSize{
		
		public static final int START_TIME  = 5;
		
	}
}
