package com.cashman.physio.v1.android.alarm.data;

public class Staff
{
  private int iconId;
  private String name;

  public Staff(String paramString, int paramInt)
  {
    this.iconId = paramInt;
    this.name = paramString;
  }

  public int getIconId()
  {
    return this.iconId;
  }

  public String getName()
  {
    return this.name;
  }

  public void setIconId(int paramInt)
  {
    this.iconId = paramInt;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }
}