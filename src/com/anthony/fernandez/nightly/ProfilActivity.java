package com.anthony.fernandez.nightly;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ProfilActivity extends SherlockFragmentActivity {

//	private LinearLayout map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_profil);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.action_bar_menu);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().getCustomView().findViewById(R.id.settings).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settings();
			}
		});
		getSupportActionBar().getCustomView().findViewById(R.id.logout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProfilActivity.this.finish();
			}
		});
		getSupportActionBar().getCustomView().findViewById(R.id.logout).setVisibility(View.VISIBLE);
		((ImageView)getSupportActionBar().getCustomView().findViewById(R.id.logout)).setImageResource(R.drawable.ic_action_back);

//		map = (LinearLayout)findViewById(R.id.message);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setNavigationBarTintColor(getResources().getColor(R.color.blue_dark));
		tintManager.setStatusBarTintColor(getResources().getColor(R.color.facebook));


		//		SystemBarConfig config = tintManager.getConfig();

		//		int marginTop = config.getActionBarHeight()+config.getStatusBarHeight();
		//		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)map.getLayoutParams();
		//		lp.setMargins(0, marginTop, 0, 0);
		//		map.setLayoutParams(lp);
		//		findViewById(R.id.imageCloud).setLayoutParams(lp);

		//hide some view 
		findViewById(R.id.next).setVisibility(View.GONE);
		findViewById(R.id.separator).setVisibility(View.GONE);
		findViewById(R.id.previous).setVisibility(View.GONE);
		((Button)findViewById(R.id.nextTwo)).setText(getResources().getString(R.string.save_changes));
		((Button)findViewById(R.id.nextTwo)).setBackgroundResource(R.drawable.btn_violet);
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

	private void settings(){
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@TargetApi(19)  
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
		if (on) {
			winParams.flags |= bits;
		} else { 
			winParams.flags &= ~bits;
		} 
		win.setAttributes(winParams);
	} 
}
