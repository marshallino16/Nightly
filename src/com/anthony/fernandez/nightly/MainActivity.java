package com.anthony.fernandez.nightly;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.anthony.fernandez.nightly.adapter.InitPagerAdapter;
import com.anthony.fernandez.nightly.fragment.LeftPanel;
import com.anthony.fernandez.nightly.fragment.RightPanel;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends SherlockFragmentActivity {

	private PagerAdapter pagerAdapter;
	public static ViewPager pager;
	private List<Object> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.action_bar_menu);
		getSupportActionBar().setHomeButtonEnabled(true);

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
}
