package com.anthony.fernandez.nightly.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class NetworkListener {

	private static NetworkListener mInstance = null;
	private TelephonyManager tManager;
	private Context context;

	public static NetworkListener getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new NetworkListener(ctx.getApplicationContext());
		}
		return mInstance;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public NetworkListener(Context context) {
		this.context = context;

		tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		tManager.listen(new CustomPhoneStateListener(context),
				PhoneStateListener.LISTEN_CALL_STATE
				| PhoneStateListener.LISTEN_CELL_INFO // Requires API 17
				| PhoneStateListener.LISTEN_CELL_LOCATION
				| PhoneStateListener.LISTEN_DATA_ACTIVITY
				| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
				| PhoneStateListener.LISTEN_SERVICE_STATE
				| PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
				| PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
				| PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR);

	}
}
