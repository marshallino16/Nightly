package com.anthony.fernandez.nightly.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.anthony.fernandez.nightly.R;

public class RegisterPartTwo extends SherlockFragment{
	
	private View v;

	public AutoCompleteTextView email;
	public TextView connectionState;
	public EditText password;
	public EditText repassword;
	public ImageView seenClear;
	public ImageView reSeenClear;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = (ViewGroup) inflater.inflate(R.layout.activity_register_part_two, container, false);
		
		this.email = (AutoCompleteTextView)v.findViewById(R.id.email);
		this.connectionState = (TextView)v.findViewById(R.id.connectionState);
		this.password = (EditText)v.findViewById(R.id.password);
		this.seenClear = (ImageView)v.findViewById(R.id.seenClear);
		this.seenClear.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					password.setInputType(InputType.TYPE_CLASS_TEXT);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				return false;
			}
		});
		this.repassword = (EditText)v.findViewById(R.id.repassword);
		this.reSeenClear = (ImageView)v.findViewById(R.id.reSeenClear);
		this.reSeenClear.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					repassword.setInputType(InputType.TYPE_CLASS_TEXT);
				} else if (event.getAction() == MotionEvent.ACTION_UP)  {
					repassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
				return false;
			}
		});

		
		return v;
	}
}
