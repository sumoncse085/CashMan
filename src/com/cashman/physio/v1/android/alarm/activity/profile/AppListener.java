/***
  Copyright (c) 2009-11 CommonsWare, LLC
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.cashman.physio.v1.android.alarm.activity.profile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;

import com.cashman.physio.v1.android.alarm.receiver.AlarmReceiver;
import com.commonsware.cwac.wakeful.WakefulIntentService;

public class AppListener implements WakefulIntentService.AlarmListener {
	long time;
	public  AppListener(long time){
		this.time=time;
	}
  public void scheduleAlarms(AlarmManager mgr, PendingIntent pi,
                             Context ctxt) {
    mgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
    		time,
                            AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
  }

  public void sendWakefulWork(Context ctxt) {
    WakefulIntentService.sendWakefulWork(ctxt, AlarmReceiver.class);
  }

  public long getMaxAge() {
    return(AlarmManager.INTERVAL_FIFTEEN_MINUTES*2);
  }
}