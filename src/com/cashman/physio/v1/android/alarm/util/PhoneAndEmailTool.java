package com.cashman.physio.v1.android.alarm.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import java.util.regex.Pattern;

import com.cashman.physio.v1.android.alarm.R;

public class PhoneAndEmailTool
{
  public static void callPhone(Context paramContext, String paramString)
  {
    paramContext.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + paramString)));
  }

  private static boolean check(String paramString1, String paramString2)
  {
    if (Pattern.compile(paramString2).matcher(paramString1).matches()){
    	return true;
    }
    return false;
  }

  public static boolean checkEmail(String paramString)
  {
    return check(paramString, "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
  }

  public static boolean checkPhone(String paramString)
  {
    return check(paramString, "^\\d{2}[- ]?\\d{3}[- ]?\\d{4}$");
  }

  public static void email(final Context paramContext, final String paramString)
  {
    Resources localResources = paramContext.getResources();
    if (checkEmail(paramString))
    {
      String str = localResources.getString(R.string.email_app_title);
      AlertDialogTool.showPositiveAndNegative(paramContext, str, localResources.getString(R.string.email_send_message,paramString), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
          PhoneAndEmailTool.sendEmail(paramContext,paramString);
        }
      }
      , null);
    }else{
    	 AlertDialogTool.showPositive(paramContext, localResources.getString(R.string.email_alert_title), localResources.getString(R.string.email_alert_message), null);
    }
  }

  public static void message(final Context paramContext,final String paramString1,final String paramString2)
  {
    Resources localResources = paramContext.getResources();
//    if (checkPhone(paramString1))
//    {
      String str = localResources.getString(R.string.send_message_title);
     
//      AlertDialogTool.showPositiveAndNegative(paramContext, str, localResources.getString(R.string.send_message_message, paramString1), new DialogInterface.OnClickListener()
//      {
//        public void onClick(DialogInterface paramDialogInterface, int paramInt)
//        {
//          paramDialogInterface.dismiss();
          PhoneAndEmailTool.sendMessage(paramContext, paramString1, paramString2);
//        }
//      }
//      , null);
//    }else{
//    	AlertDialogTool.showPositive(paramContext, localResources.getString(R.string.phone_number_error_title), localResources.getString(R.string.phone_number_error_message), null);
//    }
  }

  public static void phone(final Context paramContext,final String paramString)
  {
    Resources localResources = paramContext.getResources();
    if (checkPhone(paramString))
    {
      String str = localResources.getString(R.string.phone_call_title);
      
      AlertDialogTool.showPositiveAndNegative(paramContext, str, localResources.getString(R.string.phone_call_message, paramString), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
          PhoneAndEmailTool.callPhone(paramContext,paramString);
        }
      }
      , null);
    }else{
    	AlertDialogTool.showPositive(paramContext, localResources.getString(R.string.phone_number_error_title), localResources.getString(R.string.phone_number_error_message), null);
    }
  }

  public static void sendEmail(Context paramContext, String paramString)
  {
    Uri localUri = Uri.parse("mailto:" + paramString + "?subject=Rehab Email");
    Intent localIntent = new Intent("android.intent.action.SENDTO");
    localIntent.setData(localUri);
    paramContext.startActivity(Intent.createChooser(localIntent, "Send email"));
  }

  public static void sendMessage(Context paramContext, String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + paramString1));
    localIntent.putExtra("sms_body", paramString2);
    paramContext.startActivity(localIntent);
  }
}