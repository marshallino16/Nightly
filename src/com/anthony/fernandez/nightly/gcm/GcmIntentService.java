package com.anthony.fernandez.nightly.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.anthony.fernandez.nightly.HomeActivity;
import com.anthony.fernandez.nightly.R;
import com.anthony.fernandez.nightly.api.GCMParams;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */
			if (GoogleCloudMessaging.
					MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				//TODO
//				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_DELETED.equals(messageType)) {
				//TODO
//				sendNotification("Deleted messages on server: " +
//						extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				// This loop represents the service doing some work.
				sendNotification(extras);//"Received: " + extras.toString()
				Log.i("Nightly", "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Bundle extras) {
		int NOTIFICATION_ID = (int) System.currentTimeMillis();
		mNotificationManager = (NotificationManager)
				this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(GCMParams.CATEGORY, extras.getString(GCMParams.CATEGORY));
		intent.putExtra(GCMParams.MESSAGE, extras.getString(GCMParams.MESSAGE));
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setAutoCancel(true)
		.setSmallIcon(R.drawable.ic_launcher)
		.addAction(R.drawable.ic_action_accept, "marquer comme lu", contentIntent)
		.addAction(R.drawable.ic_action_new_blank, "bonsoir...", contentIntent)
		.setContentTitle(this.getResources().getString(R.string.title_good_night))
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText(extras.getString(GCMParams.CATEGORY) + " \n" + extras.getString(GCMParams.MESSAGE)))
		.setContentText(extras.getString(GCMParams.CATEGORY) + " \n" + extras.getString(GCMParams.MESSAGE));

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}