package com.anthony.fernandez.nightly;

import java.util.List;
import java.util.Vector;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.anthony.fernandez.nightly.adapter.InitPagerAdapter;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.viewpagerindicator.CirclePageIndicator;

public class TutorialActivity extends SherlockFragmentActivity {
	
	private PagerAdapter pagerAdapter;
	public static ViewPager pager;
	private List<Object> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.blue_background_dark));

		fragments = new Vector<Object>();

		// Ajout des Fragments dans la liste
		
		//TODO add fragment classes
//		fragments.add(Fragment.instantiate(this,??.class.getName()));
//		fragments.add(Fragment.instantiate(this,??.class.getName()));

		this.pagerAdapter = new InitPagerAdapter(super.getSupportFragmentManager(), fragments);
		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setAdapter(this.pagerAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
		indicator.setFillColor(getResources().getColor(R.color.pink_circle));
		indicator.setStrokeColor(getResources().getColor(android.R.color.white));
		indicator.setViewPager(pager);
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
