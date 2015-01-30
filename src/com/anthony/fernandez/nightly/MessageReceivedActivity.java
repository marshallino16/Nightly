package com.anthony.fernandez.nightly;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class MessageReceivedActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.zoom_enter, R.anim.hold);
		setContentView(R.layout.activity_message_received);
	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold, R.anim.zoom_exit);
		super.onPause();
	}

	@Override
	protected void onStop() {
		overridePendingTransition(R.anim.hold, R.anim.zoom_exit);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		overridePendingTransition(R.anim.hold, R.anim.zoom_exit);
		super.onDestroy();
	}
}
