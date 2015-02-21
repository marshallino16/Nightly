package com.anthony.fernandez.nightly;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class RegisterActivity extends SherlockFragmentActivity implements CalendarDatePickerDialog.OnDateSetListener {

	private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_register);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.blue_crepuscule));
	}

	public void pickBirthDay(View v){
		FragmentManager fm = getSupportFragmentManager();
		Calendar now = Calendar.getInstance();
		CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
				.newInstance(RegisterActivity.this, now.get(Calendar.YEAR)-10, now.get(Calendar.MONTH),
						now.get(Calendar.DAY_OF_MONTH));
		calendarDatePickerDialog.setYearRange(now.get(Calendar.YEAR)-90, now.get(Calendar.YEAR));
		calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
	}

	public void male(View v){

	}

	public void female(View v){

	}
	
	public void next(View v){
		Intent intent = new Intent(this, PhoneActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
		String birthday = dayOfMonth + " " + getMonth(monthOfYear+1) + " " + year;
		Log.w("Nightly", birthday);
		((TextView)findViewById(R.id.birthday)).setText(birthday);
	}

	@Override
	protected void onResume() {
		super.onResume();
		CalendarDatePickerDialog calendarDatePickerDialog = (CalendarDatePickerDialog) getSupportFragmentManager()
				.findFragmentByTag(FRAG_TAG_DATE_PICKER);
		if (calendarDatePickerDialog != null) {
			calendarDatePickerDialog.setOnDateSetListener(this);
		}
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

	public String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}
}
