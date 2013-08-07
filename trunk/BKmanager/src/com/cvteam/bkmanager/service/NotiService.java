package com.cvteam.bkmanager.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class NotiService extends Service {

	final static String ACTION = "NotifyServiceAction";
	final static String STOP_SERVICE = "";
	final static int RQS_STOP_SERVICE = 1;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String mssv = "";
		int dongbo = 0;
		Boolean notifications_lich_thi = true;
		Boolean notifications_tkb = true;
		Boolean notifications_diem = true;
		Boolean notifications_hoc_phi = true;
		int intervalTime = 1;

		if (intent != null) {
			mssv = intent.getStringExtra("mssv");
			dongbo = intent.getIntExtra("dongbo", 0);
			notifications_lich_thi = intent.getBooleanExtra("noti_lichthi", true);
			notifications_tkb = intent.getBooleanExtra("noti_diem", true);
			notifications_diem = intent.getBooleanExtra("noti_tkb", true);
			notifications_hoc_phi = intent.getBooleanExtra("noti_hocphi", true);
			intervalTime = intent.getIntExtra("intervalTime", 1);
		}

		Intent intentUpdateService = new Intent(this, UpdateService.class);
		intentUpdateService.putExtra("mssv", mssv);
		intentUpdateService.putExtra("dongbo", dongbo);
		intentUpdateService.putExtra("noti_lichthi", notifications_lich_thi);
		intentUpdateService.putExtra("noti_diem", notifications_diem);
		intentUpdateService.putExtra("noti_tkb", notifications_tkb);

		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intentUpdateService, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		long time = intervalTime * 24 * 3600 * 1000;
		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3600 * 1000, time, pIntent);

		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
