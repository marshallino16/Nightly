package com.anthony.fernandez.nightly;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.anthony.fernandez.nightly.database.DatabaseManager;
import com.anthony.fernandez.nightly.globalvar.GlobalVars;
import com.anthony.fernandez.nightly.globalvar.GlobalVars.CurrentUserConnected;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ContinueAsActivity extends SherlockActivity {
	
	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_continue_as);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.blue_crepuscule));
		
		final Button continueAs = (Button)findViewById(R.id.continueAs);

		if (this.getIntent().getExtras() != null
				&& this.getIntent().getExtras().containsKey("email")
				&& this.getIntent().getExtras().containsKey("firstname")
				&& this.getIntent().getExtras().containsKey("lastname")) {
			this.email = this.getIntent().getExtras().getString("email");
			continueAs.setText(getResources().getString(R.string.continue_as, this.getIntent().getExtras().getString("firstname"),this.getIntent().getExtras().getString("lastname")));
		} else {
			this.finish();
		}

		final TextView notYou = (TextView)findViewById(R.id.notYou);
		notYou.setText(Html.fromHtml("<u>"+getResources().getString(R.string.not_you)+"</u>"));
		notYou.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					notYou.setText(Html.fromHtml(getResources().getString(R.string.not_you)));
				} else if (event.getAction() == MotionEvent.ACTION_UP){
					notYou.setText(Html.fromHtml("<u>"+getResources().getString(R.string.not_you)+"</u>"));
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL){
					notYou.setText(Html.fromHtml("<u>"+getResources().getString(R.string.not_you)+"</u>"));
				}
				return false;
			}
		});
		notYou.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ContinueAsActivity.this, HomeActivity.class);
				startActivity(intent);
				ContinueAsActivity.this.finish();
			}
		});
	}
	
	public void continueTo(View v){
		GlobalVars.currentUser = new CurrentUserConnected();
		DatabaseManager.getInstance(getApplicationContext()).getAllCurrentUserInfos(email);
		Intent intent = new Intent(ContinueAsActivity.this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		super.onPause();
	}

	@Override
	protected void onStop() {
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
		super.onDestroy();
	}

	@TargetApi(19)  
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else { 
			winParams.flags &= ~bits;
		} 
		win.setAttributes(winParams);
	}
}
