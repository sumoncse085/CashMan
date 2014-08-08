package com.cashman.physio.v1.android.alarm.activity.share;

public final class CONST
{
  public static final class Fackbook
  {
	public static final int ACTIVITY_INDEPENDENT = -1;
    public static final String APP_ID = "184429761691843";
    public static final String[] PERMISSIONS = new String[]{"offline_access","publish_stream","user_photos","publish_checkins","photo_upload"};
  }

  public static final class Twitter
  {
    public static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    public static final String CALLBACK_URL = "http://www.cashmanphysio.co.nz";
    public static final String CONSUMER_KEY = "wMBG3XMAPop6FiSxiVzuRA";
    public static final String CONSUMER_SECRET = "00AE9NwK4Y2e61kPjv5VwuyktbtuCkTyjjf4bT5Q";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_ACCESS_TOKEN_SECRET = "access_token_secret";
    public static final String KEY_USER_NAME = "user_name";
    public static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    public static final String TWITTER_LOGIN_PREFERENCES = "twitter_login_preferences";
  }
  
  public static final int FLAG_SUCCESS = 1;
  public static final int FLAG_FAIL = -1;
  public static final int FLAG_DO_SHARE = 0;
  
  public static final String SHARE_TEXT_PREFERENCES_NAME = "share_text_preferences";
  public static final String KEY_FACEBOOK_SHARE_INFO = "facebook_share_info";
  public static final String KEY_TWITTER_SHARE_INFO = "twitter_share_info";
  public static final String KEY_EMAIL_SHARE_INFO = "email_share_info";
  public static final String KEY_TEXT_SHARE_INFO = "text_share_info";
  
  public static final String SHARE_IMAGE_NAME = "app.png";
}