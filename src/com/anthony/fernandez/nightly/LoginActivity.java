package com.anthony.fernandez.nightly;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.actionbarsherlock.app.SherlockActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class LoginActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.pink_circle));
	}
	
	@Override
	protected void onResume() {
		Animation animApparitionFromBottom = AnimationUtils.loadAnimation(this, R.anim.fadein);
		findViewById(R.id.textLogo).startAnimation(animApparitionFromBottom);
		super.onResume();
	}
	
	public void facebookLogin(View v){
		getFacebookInfos();
	}
	
	private void getFacebookInfos(){
		
	}
	
	public void loginNightly(View v){
		
	}
	
	public void passwordLost(View v){
		
	}
	
	public void registerAccount(View v){
		
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
