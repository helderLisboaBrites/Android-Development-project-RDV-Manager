package com.example.android_development_project_rdv_manager;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class NotificationHelper {

	static String CHANNEL_ID = "channel_01";

	public static int REQUEST_CODE = 200;

	public static void initialize(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "Rdv reminder";
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription("");
			NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);

		}
	}

	public static void notify(Context context, View view, Rdv rdv) {
		Intent notifyIntent = new Intent(context, MyBroadcastReceiver.class);
		notifyIntent.putExtra("id", rdv.getId());

		LocalDateTime notificationTime = rdv.getNotification();
		long milli = notificationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		if(milli < System.currentTimeMillis()) return;

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
			context, REQUEST_CODE, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT
		);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setExact(AlarmManager.RTC_WAKEUP, milli, pendingIntent);
	}

	 public static class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int id = intent.getExtras().getInt("id");

			Intent intent1 = new Intent(context, MyService.class);
			intent1.putExtra("id", id);

			context.startService(intent1);
		}
	}

	public static class MyService extends IntentService {
		public MyService() {
			super("NotificationService");
		}

		@Override
		protected void onHandleIntent(Intent intent) {
			DatabaseHelper database = new DatabaseHelper(this);
			database.open();

			if(!intent.hasExtra("id")) return;

			Rdv rdv;

			try {
				rdv = database.getRdv((intent.getExtras().getInt("id")));
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}

			Intent notificationIntent= new Intent(this, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
			NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setContentTitle(rdv.getTitle())
				.setContentText(rdv.getDescription())
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setContentIntent(pendingIntent);
			// .addAction(new NotificationCompat.Action(R.drawable.ic_launcher_background,"share",pendingIntent));
			notificationManager.notify(200 + rdv.getId(), notificationBuilder.build());
		}
	}
}
