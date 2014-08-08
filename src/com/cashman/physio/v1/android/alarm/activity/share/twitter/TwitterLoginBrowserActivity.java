package com.cashman.physio.v1.android.alarm.activity.share.twitter;

import twitter4j.TwitterException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.share.CONST;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;
import com.cashman.physio.v1.android.twitter.TwitterConnect;

public class TwitterLoginBrowserActivity extends Activity {
    private WebView webview;
    private ProgressDialog progressDialog;// 定义一个进度条
    private TwitterConnect tc;
    private String authenticateUrl = "";
    private ConnectTask task;
    private String oauthToken;
    private String oauthVerifier;
    private Context mContext;
    
    private static final String TAG = "TwitterLoginBrowser";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.twitter_loginbrowser);
        mContext = TwitterLoginBrowserActivity.this;
        progressDialog = new ProgressDialog(TwitterLoginBrowserActivity.this);
        progressDialog.setTitle(R.string.pls_wait);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();// 启动的时候就让它显示
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.requestFocus();
        tc = new TwitterConnect(this);
        task = new ConnectTask(this);
        task.execute("login");// 执行载入页面任务
        webview.setWebViewClient(new WebViewClient() {
           
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                
            	LocalLog.i(TAG, "**** Twitter : load url = "+url);
            	
                if (url != null && url.startsWith(CONST.Twitter.CALLBACK_URL) && !url.equalsIgnoreCase(CONST.Twitter.CALLBACK_URL)) {
                	
                	webview.stopLoading();
                	
                	if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                	
                    String[] param = url.split("\\?")[1].split("&");
                    if (param[0].startsWith("oauth_token")) {
                        oauthToken = param[0].split("=")[1];
                    } else if (param[1].startsWith("oauth_token")) {
                        oauthToken = param[1].split("=")[1];
                    }
                    if (param[0].startsWith("oauth_verifier")) {
                        oauthVerifier = param[0].split("=")[1];
                    } else if (param[1].startsWith("oauth_verifier")) {
                        oauthVerifier = param[1].split("=")[1];
                    }
                    System.out.println("oauthToken=" + oauthToken);
                    System.out.println("oauthVerifier=" + oauthVerifier);
                    Log.i(TAG,"oauthToken=" + oauthToken+";"+"oauthVerifier=" + oauthVerifier);
                    try {
                        TwitterConnect.accessToken = TwitterConnect.twitter
                                .getOAuthAccessToken(
                                        TwitterConnect.requestToken,
                                        oauthVerifier);
                        PreferencesTool.save(mContext, CONST.Twitter.TWITTER_LOGIN_PREFERENCES, CONST.Twitter.KEY_ACCESS_TOKEN, TwitterConnect.accessToken.getToken());
                        PreferencesTool.save(mContext, CONST.Twitter.TWITTER_LOGIN_PREFERENCES, CONST.Twitter.KEY_ACCESS_TOKEN_SECRET, TwitterConnect.accessToken.getTokenSecret());
                        PreferencesTool.save(mContext, CONST.Twitter.TWITTER_LOGIN_PREFERENCES, CONST.Twitter.KEY_USER_NAME,tc.getUserName());
                        
                        setResult(CONST.FLAG_SUCCESS);
                        finish();
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 页面载入完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // url=https://api.twitter.com/oauth/authorize?oauth_token=9kpqNY7VhmyWC5NRXF2eQ73zN4VKXjQcgZj62sIZU&oauth_verifier=3B7FBAs9XOYH8I3QEQrFGuqJlgMUfQH5fzmz7j3Ws
                Toast.makeText(TwitterLoginBrowserActivity.this, "load complete",
                        Toast.LENGTH_LONG).show();
                
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            // 控制进度条的显示
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress != 100) {
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                } else {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

//    class WebTask extends AsyncTask<String, Integer, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            String result = "";
//            if (oauthToken != null && oauthVerifier != null) {
//                System.out.println("开始获取用户名和头像");
//                String userImageUrl = "";// 用户头像url
//                try {
//                    userImageUrl = TwitterConnect.twitter
//                            .showUser(TwitterConnect.accessToken.getUserId())
//                            .getProfileImageURL().toString();
//                } catch (TwitterException e) {
//                    e.printStackTrace();
//                }
//                String userName = tc.getUserName();
//                
////                // 登陆成功 回到来的画面
//                Intent intent = getIntent();
////                Bundle bundle = new Bundle();
////                bundle.putParcelable("userImage", userImage);
////                bundle.putString("userName", userName);
////                intent.putExtra("userInfo", bundle);
//                // 保存登陆状态
//                SharedPreferences spf = getSharedPreferences("bravesoft",
//                        Context.MODE_PRIVATE);
//                Editor editor = spf.edit();
//                editor.putString("oauth_token",
//                        TwitterConnect.accessToken.getToken());
//                editor.putString("oauth_token_secret",
//                        TwitterConnect.accessToken.getTokenSecret());
//                editor.putBoolean("connection_tatus", true);
//                editor.putString("username", userName);
//                editor.putString("userimgurl", userImageUrl);
//                editor.commit();
//                TwitterLoginBrowser.this.setResult(1, intent);
////                bundle.clear();
//                result = "ok";
//                
//                
//            }
//            return result;
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (result.equals("ok")) {
//                Toast.makeText(TwitterLoginBrowser.this, R.string.log_in,
//                        Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(TwitterLoginBrowser.this, "登陆失败",
//                        Toast.LENGTH_LONG).show();
//            }
//             TwitterLoginBrowser.this.finish();
//        }
//    }
    
    class ConnectTask extends AsyncTask<String, Integer, String> {
        public ConnectTask(Context context) {
        }
        @Override
        protected String doInBackground(String... params) {
            authenticateUrl = tc.twitterLoginOAuth();
            return authenticateUrl;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            
            if (!result.equals("")) {
                webview.loadUrl(result);// 载入网页
            } else {
            	Resources res = getResources();
            	String error = res.getString(R.string.twitter_error_colon)+res.getString(R.string.authorize_fail_message);
                Toast.makeText(TwitterLoginBrowserActivity.this, error,
                        Toast.LENGTH_LONG).show();
                TwitterLoginBrowserActivity.this.finish();
            }
        }
    }
}
