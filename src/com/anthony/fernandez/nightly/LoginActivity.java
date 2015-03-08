package com.anthony.fernandez.nightly;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.anthony.fernandez.nightly.task.TaskManager;
import com.anthony.fernandez.nightly.task.listener.OnConnectListener;
import com.anthony.fernandez.nightly.task.listener.OnGettingUserInfo;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class LoginActivity extends SherlockActivity implements OnConnectListener, OnGettingUserInfo {

	private AutoCompleteTextView email;
	private EditText password;

	private TextView connectionState;
	private ProgressBar progressBar;
	private LinearLayout containerConnection;

	private TaskManager taskManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_login);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.blue_crepuscule));

		taskManager = new TaskManager(this);

		email = (AutoCompleteTextView)findViewById(R.id.email);
		password = (EditText)findViewById(R.id.password);
		connectionState = (TextView)findViewById(R.id.connectionState);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		containerConnection = (LinearLayout)findViewById(R.id.connectionContainer);

		final TextView cluf = (TextView)findViewById(R.id.textCluf);
		cluf.setText(Html.fromHtml("<u>"+getResources().getString(R.string.cluf)+"</u>"));
		cluf.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					cluf.setText(Html.fromHtml(getResources().getString(R.string.cluf)));
				} else if (event.getAction() == MotionEvent.ACTION_UP){
					cluf.setText(Html.fromHtml("<u>"+getResources().getString(R.string.cluf)+"</u>"));
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL){
					cluf.setText(Html.fromHtml("<u>"+getResources().getString(R.string.cluf)+"</u>"));
				}
				return false;
			}
		});
		cluf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(LoginActivity.this, CGUActivity.class);
				startActivity(intent);
			}
		});
		
		
		findViewById(R.id.lostPassword).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){

				} else if (event.getAction() == MotionEvent.ACTION_UP){

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL){

				}
				return false;
			}
		});
		findViewById(R.id.lostPassword).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	protected void onResume() {
		Animation animationFadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
		Animation animationFromBottom = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
		findViewById(R.id.textLogo).startAnimation(animationFadein);
		findViewById(R.id.email).startAnimation(animationFromBottom);
		findViewById(R.id.password).startAnimation(animationFromBottom);
		findViewById(R.id.lostPassword).startAnimation(animationFromBottom);
		findViewById(R.id.connection).startAnimation(animationFromBottom);
		super.onResume();
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

	public void loginNightly(View v){
		if(email.getText().toString().isEmpty()){
			displayError(R.string.fill_all_fields);
			return;
		}
		if(!com.anthony.fernandez.nightly.util.Utils.isValidEmail(email.getText().toString())){
			displayError(R.string.email_invalid);
			return;
		}
		if(password.getText().toString().isEmpty() || password.getText().toString().length() <6){
			displayError(R.string.password_to_short);
			return;
		}

		connecting();

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				taskManager.connectNightly(email.getText().toString(), password.getText().toString(), LoginActivity.this);
				return null;
			}
		}.execute();

		//		Intent intent = new Intent(this, MainActivity.class);
		//		startActivity(intent);
		//		this.finish();
	}

	public void passwordLost(View v){

	}

	public void displayError(int errorID){
		if(View.GONE == containerConnection.getVisibility()){
			containerConnection.setVisibility(View.VISIBLE);
		}
		if(View.VISIBLE == progressBar.getVisibility()){
			progressBar.setVisibility(View.GONE);
		}
		connectionState.setBackgroundColor(getResources().getColor(R.color.not_connected));
		connectionState.setText(getResources().getString(errorID));
	}

	public void displayError(String reason){
		if(View.GONE == containerConnection.getVisibility()){
			containerConnection.setVisibility(View.VISIBLE);
		}
		if(View.VISIBLE == progressBar.getVisibility()){
			progressBar.setVisibility(View.GONE);
		}
		connectionState.setBackgroundColor(getResources().getColor(R.color.not_connected));
		connectionState.setText(reason);
	}

	public void connecting(){
		if(View.GONE == containerConnection.getVisibility()){
			containerConnection.setVisibility(View.VISIBLE);
		}
		if(View.GONE == progressBar.getVisibility()){
			progressBar.setVisibility(View.VISIBLE);
		}
		connectionState.setBackgroundColor(getResources().getColor(R.color.progress));
		connectionState.setText(getResources().getString(R.string.connecting));
	}

	public void connected(){
		if(View.GONE == containerConnection.getVisibility()){
			containerConnection.setVisibility(View.VISIBLE);
		}
		if(View.VISIBLE == progressBar.getVisibility()){
			progressBar.setVisibility(View.GONE);
		}
		connectionState.setBackgroundColor(getResources().getColor(R.color.connected));
		connectionState.setText(getResources().getString(R.string.connected));
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
	public void onConnectionAccepted() {
		taskManager.getUserInfos(LoginActivity.this);
	}

	@Override
	public void onConnectionRefused(final String reason) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				displayError(reason);
			}
		});
	}

	@Override
	public void OnUserInfo() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				connected();
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}
		});
	}

	@Override
	public void OnUserInfoFailed(final String reason) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				displayError(reason);
			}
		});
	} 
}
