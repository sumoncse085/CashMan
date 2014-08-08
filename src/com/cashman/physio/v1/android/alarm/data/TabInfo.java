package com.cashman.physio.v1.android.alarm.data;

import android.content.Intent;

public class TabInfo {
	
	private String tag ;
	private int labelId;
	private int drawableId;
	private Intent content;
	
	public TabInfo(){
		
	}
	
	public TabInfo(String tag,int labelId,int drawableId,Intent content){
		this.tag = tag;
		this.labelId = labelId;
		this.drawableId = drawableId;
		this.content = content;
	}
	
	public TabInfo(String tag,int labelId,Intent content){
		this(tag,labelId,0,content);
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public int getDrawableId() {
		return drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	public Intent getContent() {
		return content;
	}

	public void setContent(Intent content) {
		this.content = content;
	}
	
}
