package com.anthony.fernandez.nightly;

import java.util.List;
import java.util.Vector;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.anthony.fernandez.nightly.adapter.InitPagerAdapter;
import com.anthony.fernandez.nightly.fragment.LeftPanel;
import com.anthony.fernandez.nightly.fragment.RightPanel;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment.TimePickerDialogHandler;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends SherlockFragmentActivity {

	private RelativeLayout mainContainer;
	private PagerAdapter pagerAdapter;
	public static ViewPager pager;
	private List<Object> fragments;
	private LayoutInflater inflater;
	private View splashScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_main);
		//Project ID: animated-codex-750 Project Number: 926014171825
		//API KEY = AIzaSyBod3FoppvHG6F8lXihLLwg433pJnkmscQ
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
		tintManager.setTintColor(getResources().getColor(R.color.blue_aciton_bar));

		fragments = new Vector<Object>();

		// Ajout des Fragments dans la liste
		fragments.add(Fragment.instantiate(this,LeftPanel.class.getName()));
		fragments.add(Fragment.instantiate(this,RightPanel.class.getName()));

		this.pagerAdapter = new InitPagerAdapter(super.getSupportFragmentManager(), fragments);
		this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		splashScreen = inflater.inflate(R.layout.splashscreen_wait, null);

		mainContainer = (RelativeLayout)findViewById(R.id.containerMain);
		pager = (ViewPager) findViewById(R.id.viewpager);
		// Affectation de l'adapter au ViewPager
		pager.setAdapter(this.pagerAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
		indicator.setFillColor(getResources().getColor(R.color.pink_circle));
		indicator.setStrokeColor(getResources().getColor(android.R.color.white));
		indicator.setViewPager(pager);
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

	public void pickSleepingTime(View v){
		TimePickerBuilder btp = new TimePickerBuilder()
		.setFragmentManager(getSupportFragmentManager())
		.setStyleResId(R.style.BetterPickersDialogFragment);
		btp.addTimeSetListener(new TimePickerDialogHandler() {

			@Override
			public void onDialogTimeSet(int hourOfDay, int minute) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDialogCancel() {
				// TODO Auto-generated method stub

			}
		});  
		btp.show();
	}

	/**
	 * @param view v
	 * Slide to leftPanel 
	 */
	public void leftPanel(View v){
		if(0 != pager.getCurrentItem()){
			pager.setCurrentItem(0);
		}
	}

	/**
	 * @param view v
	 * Slide to rightPanel 
	 */
	public void rightPanel(View v){
		if(1 != pager.getCurrentItem()){
			pager.setCurrentItem(1);
		}
	}

	/**
	 * @param view representing owner of all child views
	 * @param enabled ?
	 */
	public static void enableDisableView(View view, boolean enabled) {
		view.setEnabled(enabled);
		if ( view instanceof ViewGroup ) {
			ViewGroup group = (ViewGroup)view;

			for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
				enableDisableView(group.getChildAt(idx), enabled);
			}
		}
	}

	public void pickUpSomeone(View v){
		if(mainContainer.findViewById(R.id.splashContainer) == null){
			enableDisableView(mainContainer, false);
			mainContainer.addView(splashScreen, mainContainer.getChildCount()-1);
		}
	}

	public void disableSplashScreen(View v){
		if(mainContainer.findViewById(R.id.splashContainer) != null){
			mainContainer.removeView(splashScreen);
			enableDisableView(mainContainer, true);
		}
	}

	public void categories(View v){
		Intent intent = new Intent(this, CategoriesActivity.class);
		startActivity(intent);
	}

	public void conversationsView(View v){
		Intent intent = new Intent(this, ListConversationActivity.class);
		startActivity(intent);
	}
	
	public void profil(View v){
		Intent intent = new Intent(this, MessageReceivedActivity.class);
		startActivity(intent);
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

	@Override
	public void onBackPressed() {
		disableSplashScreen(null);
	}
}
