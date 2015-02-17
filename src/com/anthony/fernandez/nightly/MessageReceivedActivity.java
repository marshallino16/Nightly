package com.anthony.fernandez.nightly;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.anthony.fernandez.nightly.api.GCMParams;

public class MessageReceivedActivity extends SherlockActivity {
	
	private TextView category;
	private TextView message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.zoom_enter, R.anim.hold);
		setContentView(R.layout.activity_message_received);
		
		category = (TextView)findViewById(R.id.catName);
		message = (TextView)findViewById(R.id.catMessage);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle.containsKey(GCMParams.CATEGORY) && bundle.containsKey(GCMParams.MESSAGE)){
			category.setText(bundle.getString(GCMParams.CATEGORY));
			message.setText(bundle.getString(GCMParams.MESSAGE));
		}
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
	
	public void quit(View v){
		this.finish();
	}
}
