package com.anthony.fernandez.nightly;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class RegisterPartTwoActivity extends SherlockFragmentActivity{

	private AutoCompleteTextView email;
	private EditText password;
	private EditText repassword;
	private ImageView seenClear;
	private ImageView reSeenClear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_register_part_two);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.blue_crepuscule));

		email = (AutoCompleteTextView)findViewById(R.id.email);
		password = (EditText)findViewById(R.id.password);
		seenClear = (ImageView)findViewById(R.id.seenClear);
		seenClear.setOnTouchListener(new OnTouchListener() {

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
		repassword = (EditText)findViewById(R.id.repassword);
		reSeenClear = (ImageView)findViewById(R.id.reSeenClear);
		reSeenClear.setOnTouchListener(new OnTouchListener() {

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
	}

	public void next(View v){
		if(email.getText().toString().isEmpty()){
			return;
		}
		if(password.toString().isEmpty()){
			return;
		}
		if(repassword.toString().isEmpty()){
			return;
		}
		if(!password.toString().equals(repassword.toString())){
			Log.w("Nightly", "different : pass = "+password.toString()+"& repass = "+repassword.toString()+"");
			return;
		}

		Intent intent = new Intent(this, PhoneActivity.class);
		startActivity(intent);
	}

	public void previous(View v){
		this.finish();
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
