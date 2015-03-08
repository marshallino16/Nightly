package com.anthony.fernandez.nightly.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.anthony.fernandez.nightly.R;

public class LeftPanel extends SherlockFragment {

	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = (ViewGroup) inflater.inflate(R.layout.fragment_left, container, false);
		
		return v;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(null != getActivity()){
			getActivity().findViewById(R.id.selectorCurrent).setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.selectorCurrentAlarm).setVisibility(View.GONE);
			((TextView)getActivity().findViewById(R.id.moonText)).setTextColor(getResources().getColor(R.color.facebook));
			((TextView)getActivity().findViewById(R.id.alarmText)).setTextColor(getResources().getColor(R.color.unpress_grey));
			((ImageView)getActivity().findViewById(R.id.alarmIcon)).setImageResource(R.drawable.alarm);
			((ImageView)getActivity().findViewById(R.id.moonIcon)).setImageResource(R.drawable.moon);
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
}
