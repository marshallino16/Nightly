package com.anthony.fernandez.nightly;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class CategoriesActivity extends SherlockActivity implements OnTouchListener{
	
	//views
	private ImageView imageHumour;
	private TextView humour;
	
	private ImageView imagePoesie;
	private TextView poesie;
	
	private ImageView imageAmour;
	private TextView amour;
	
	//animation
	private Animation zoomIn;
	private Animation zoomOut;
	
	private ImageView previousTouch = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_categories);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.action_bar_menu);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().getCustomView().findViewById(R.id.settings).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settings();
			}
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.facebook));
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/FlexDisplay-Thin.otf");
		zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoomin);
		zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoomout);
		
		humour = (TextView)findViewById(R.id.humour);
		humour.setTypeface(tf);
		poesie = (TextView)findViewById(R.id.poesie);
		poesie.setTypeface(tf);
		amour = (TextView)findViewById(R.id.amour);
		amour.setTypeface(tf);
		
		
		imageHumour = (ImageView)findViewById(R.id.imageHumour);
		imageHumour.setOnTouchListener(this);
		
		imagePoesie = (ImageView)findViewById(R.id.imagePoesie);
		imagePoesie.setOnTouchListener(this);
		
		imageAmour = (ImageView)findViewById(R.id.imageAmour);
		imageAmour.setOnTouchListener(this);
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
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else { 
			winParams.flags &= ~bits;
		} 
		win.setAttributes(winParams);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(final View v, MotionEvent event) {
		resetAllImagesAnimation();
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			v.startAnimation(zoomIn);
		}
		previousTouch = (ImageView) v;
		return false;
	}
	
	private void resetAllImagesAnimation(){
		if(null != previousTouch){
			previousTouch.startAnimation(zoomOut);
			previousTouch = null;
		}
	}
}
