package com.anthony.fernandez.nightly.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.anthony.fernandez.nightly.R;

public class RightPanel extends SherlockFragment{
	
	private View v;
	
	private Typeface candyFont;
	
	public TextView lundi;
	public TextView mardi;
	public TextView mercredi;
	public TextView jeudi;
	public TextView vendredi;
	public TextView samedi;
	public TextView dimanche;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = (ViewGroup) inflater.inflate(R.layout.fragment_right, container, false);
		
		candyFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CandyColouredClown.otf");
		
		lundi = (TextView)v.findViewById(R.id.pillowLundi);
		mardi = (TextView)v.findViewById(R.id.pillowMardi);
		mercredi = (TextView)v.findViewById(R.id.pillowMercredi);
		jeudi = (TextView)v.findViewById(R.id.pillowJeudi);
		vendredi = (TextView)v.findViewById(R.id.pillowVendredi);
		samedi = (TextView)v.findViewById(R.id.pillowSamedi);
		dimanche = (TextView)v.findViewById(R.id.pillowDimanche);
		
		return v;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(null != getActivity()){
			getActivity().findViewById(R.id.selectorCurrent).setVisibility(View.GONE);
			getActivity().findViewById(R.id.selectorCurrentAlarm).setVisibility(View.VISIBLE);
			((TextView)getActivity().findViewById(R.id.moonText)).setTextColor(getResources().getColor(R.color.unpress_grey));
			((TextView)getActivity().findViewById(R.id.alarmText)).setTextColor(getResources().getColor(R.color.facebook));
			((ImageView)getActivity().findViewById(R.id.alarmIcon)).setImageResource(R.drawable.alarm_set);
			((ImageView)getActivity().findViewById(R.id.moonIcon)).setImageResource(R.drawable.moon_unset);
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

}
