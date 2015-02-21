package com.anthony.fernandez.nightly;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class LoginActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_login);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.blue_crepuscule));

		((TextView)findViewById(R.id.textCluf)).setText(Html.fromHtml("<u>"+getResources().getString(R.string.cluf)+"</u>"));
		findViewById(R.id.lostPassword).setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){

				} else if (event.getAction() == MotionEvent.ACTION_UP){

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL){

				}
				return false;
			}
		});
		findViewById(R.id.lostPassword).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	protected void onResume() {
		Animation animationFadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
		Animation animationFromBottom = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
		findViewById(R.id.textLogo).startAnimation(animationFadein);
		findViewById(R.id.email).startAnimation(animationFromBottom);
		findViewById(R.id.password).startAnimation(animationFromBottom);
		findViewById(R.id.lostPassword).startAnimation(animationFromBottom);
		findViewById(R.id.connection).startAnimation(animationFromBottom);
		super.onResume();
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

	public void loginNightly(View v){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	public void passwordLost(View v){

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
