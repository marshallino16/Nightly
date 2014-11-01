package com.anthony.fernandez.nightly;

import java.util.List;
import java.util.Vector;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.anthony.fernandez.nightly.adapter.InitPagerAdapter;
import com.anthony.fernandez.nightly.fragment.LeftPanel;
import com.anthony.fernandez.nightly.fragment.RightPanel;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends SherlockFragmentActivity {

	private PagerAdapter pagerAdapter;
	public static ViewPager pager;
	private List<Object> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Project ID: animated-codex-750 Project Number: 926014171825
		//API KEY = AIzaSyBod3FoppvHG6F8lXihLLwg433pJnkmscQ
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.action_bar_menu);
		getSupportActionBar().setHomeButtonEnabled(true);
		
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

		pager = (ViewPager) findViewById(R.id.viewpager);
		// Affectation de l'adapter au ViewPager
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
