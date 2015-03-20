package com.anthony.fernandez.nightly;

import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.anthony.fernandez.nightly.database.DatabaseManager;
import com.anthony.fernandez.nightly.globalvar.GlobalVars;
import com.anthony.fernandez.nightly.task.TaskManager;
import com.anthony.fernandez.nightly.task.listener.OnConnectListener;
import com.anthony.fernandez.nightly.util.Utils;

public class ReconnectionActivity extends SherlockFragmentActivity implements OnConnectListener{
	
	private TaskManager taskManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.zoom_enter, R.anim.hold);
		setContentView(R.layout.activity_reconnection);
		
		taskManager = new TaskManager(ReconnectionActivity.this);
		reconnect();
	}
	
	private void reconnect(){
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				try {
					Thread.sleep(2000);
					taskManager.connectNightly(GlobalVars.currentUser.email, GlobalVars.currentUser.password, ReconnectionActivity.this);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}
	
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold, R.anim.push_down_out);
		super.onPause();
	}

	@Override
	protected void onStop() {
		overridePendingTransition(R.anim.hold, R.anim.push_down_out);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		overridePendingTransition(R.anim.hold, R.anim.push_down_out);
		super.onDestroy();
	}
	
	@Override
	public void onConnectionAccepted() {
		DatabaseManager.getInstance(getApplicationContext()).updateUserByEmail(GlobalVars.currentUser.email, GlobalVars.currentUser.token);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ReconnectionActivity.this.finish();
			}
		});
	}

	@Override
	public void onConnectionRefused(final String reason) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Utils.createToast(getApplicationContext(), "reco failed");
				reconnect();
			}
		});
	}
}
