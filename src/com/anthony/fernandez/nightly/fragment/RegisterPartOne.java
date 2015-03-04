package com.anthony.fernandez.nightly.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.anthony.fernandez.nightly.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterPartOne extends SherlockFragment {

	private View v;
	
	public CircleImageView profilPicture;
	public RelativeLayout actions;
	public RelativeLayout container;
	public EditText lastname;
	public EditText firstname;
	public TextView birthday;
	public TextView connectionState;
	public ImageButton male;
	public ImageButton female;
	public ImageView stateProfil;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = (ViewGroup) inflater.inflate(R.layout.activity_register_part_one, container, false);
		
		this.profilPicture = (CircleImageView)v.findViewById(R.id.profile_image);
		this.actions = (RelativeLayout)v.findViewById(R.id.actionsPhoto);
		this.container = (RelativeLayout) v.findViewById(R.id.container);
		this.male = (ImageButton) v.findViewById(R.id.male);
		this.female = (ImageButton) v.findViewById(R.id.female);
		this.lastname = (EditText) v.findViewById(R.id.lastname);
		this.firstname = (EditText) v.findViewById(R.id.firstname);
		this.birthday = (TextView) v.findViewById(R.id.birthday);
		this.connectionState = (TextView) v.findViewById(R.id.connectionState);
		this.stateProfil = (ImageView) v.findViewById(R.id.statesProfilImage);

		
		return v;
	}

}
