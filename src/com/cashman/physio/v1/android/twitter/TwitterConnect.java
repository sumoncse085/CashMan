package com.cashman.physio.v1.android.twitter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.cashman.physio.v1.android.alarm.activity.share.CONST;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
public class TwitterConnect {
    Context context;
    public static Twitter twitter;
    public static RequestToken requestToken;
    public static AccessToken accessToken;
    
    private static final String TAG = "TwitterConnect";
    
    private static final String CONSUMER_KEY = CONST.Twitter.CONSUMER_KEY;
    private static final String CONSUMER_SECRET = CONST.Twitter.CONSUMER_SECRET;
    
    public TwitterConnect(Context context) {
        this.context = context;
        twitter = new TwitterFactory().getInstance();
        
    }
    public String twitterLoginOAuth() {
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
     
        try {
            requestToken = twitter.getOAuthRequestToken();
            return requestToken.getAuthenticationURL() + "&force_login=true";
        } catch (TwitterException e) {
            e.printStackTrace();
            return CONST.Twitter.CALLBACK_URL;
        }
        // 已经得到临时访问令牌
        // Request token=7HMpOfNr5ev1kjcW036mDI1hpvycbb1sRkKK3r6Ax30
        // Request token secret=FAhiBFX04lbQhme392htH1LgL8hQxm2p0IJ5Kzlofk
        // url=http://api.twitter.com/oauth/authenticate?oauth_token=svrKsiu1ArJ7h24RvHPHeNnzfXqLIebRY7uefydmB9k
    }
    // 得到用户名
    public String getUserName() {
        try {
            return twitter.showUser(accessToken.getUserId()).getName();
        } catch (Exception e) {
            return "";
        }
    }
    // 得到用户头像
    public Bitmap getUserImage(String userImageUrl) {
        Bitmap image = null;
        try {
            URL url = new URL(userImageUrl);
            image = BitmapFactory.decodeStream(url.openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public static boolean isSessionValid(Context context){
    	String accessToken = PreferencesTool.get(context, CONST.Twitter.TWITTER_LOGIN_PREFERENCES, CONST.Twitter.KEY_ACCESS_TOKEN);
    	String accessSecret = PreferencesTool.get(context, CONST.Twitter.TWITTER_LOGIN_PREFERENCES, CONST.Twitter.KEY_ACCESS_TOKEN_SECRET);
    	if(accessToken != null && accessToken.length() > 0 && accessSecret != null && accessSecret.length() > 0){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 
     * @param text
     *            评论内容
     * @param input
     *            图片的一个InputStream
     * @param description
     *            图片描述
     * @return
     */
    public boolean TwitterContribute(String text, InputStream input,
            String description) {
        boolean isOk = false;
        Configuration config = null;
        ImageUpload uploads = null;
        
        Status status = null;
        try {
            
            if (input != null) {
                if (config == null) {
                    ConfigurationBuilder confBuild = new ConfigurationBuilder();
                    confBuild.setOAuthConsumerKey(CONSUMER_KEY);
                    confBuild.setOAuthConsumerSecret(CONSUMER_SECRET);
                    confBuild.setOAuthAccessToken(accessToken.getToken());
                    
                    confBuild.setOAuthAccessTokenSecret(accessToken.getTokenSecret());
//                    confBuild.setMediaProviderAPIKey(TWITPIC_API_KEY);
                    // 发送图片和对图片的描述
                    config = confBuild.build();
                    uploads = new ImageUploadFactory(config).getInstance(MediaProvider.TWITTER);
                    
                }
                String uploadre = "";
                BufferedInputStream buffer = new BufferedInputStream(input,8192);
                if(description.length() > 110){
                	int left = description.length() - 110;
                	description = description.substring(left);
                }
                uploadre = uploads.upload("", buffer, description);
                
                System.out.println("uploadre" + uploadre);
                
                if(buffer != null) {
                	try {
                        buffer.close();
                    } catch (IOException e) {
                    }
                }
                if (input != null)
                    try {
                        input.close();
                    } catch (IOException e) {
                    }
                
                if (!uploadre.equals("")) {
                	isOk = true;
                    System.out.println("uploadre=" + uploadre);
                }
            } else {
                status = twitter.updateStatus(getTweet(text));
            }
            
            if (status != null) {
                System.out.println("发表内容:" + status.getText());
                isOk = true;
            }
            
        } catch (Exception e) {
        	Log.e(TAG,"exception in TwitterContribute",e);
        	try {
				status = twitter.updateStatus(getTweet(text));
				if(status != null){
					 isOk = true;
				}
			} catch (TwitterException e1) {
				Log.e(TAG,"exception in TwitterContribute",e);
				isOk = false;
			}
            
        }
        return isOk;
    }
    
    private String getTweet(String str1){
    	String tweet = null;
    	if(str1.length() > 140){
    		int left = str1.length() - 140;
    		tweet = str1.substring(left);
    	}else{
    		tweet = str1;
    	}
    	LocalLog.i(TAG, "###### tweet : "+tweet);
    	return tweet;
    }
}