package com.anthony.fernandez.nightly;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class RegisterActivity extends SherlockFragmentActivity implements CalendarDatePickerDialog.OnDateSetListener {

	private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

	private EditText lastname;
	private EditText firstname;
	private TextView birthday;
	private ImageButton male;
	private ImageButton female;
	private int day = 0;
	private int month = 0;
	private int year = 0;

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

		male = (ImageButton)findViewById(R.id.male);
		female = (ImageButton)findViewById(R.id.female);
		lastname = (EditText)findViewById(R.id.lastname);
		firstname = (EditText)findViewById(R.id.firstname);
		birthday = (TextView)findViewById(R.id.birthday);
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
		if(female.isSelected()){
			female.setSelected(false);
			male.setSelected(true);
		}
	}

	public void female(View v){
		if(male.isSelected()){
			male.setSelected(false);
			female.setSelected(true);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void next(View v){
		if(!male.isSelected() && !female.isSelected()){
			return;
		}
		if(firstname.getText().toString().isEmpty()){
			return;
		}
		if(lastname.getText().toString().isEmpty()){
			return;
		}
		if(birthday.toString().isEmpty()){
			return;
		}
		if(day == 0 || month == 0 || year == 0){
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		String dateInString = day+"-"+month+"-"+year;
		try {
			Date date = sdf.parse(dateInString);
			if(getAge(date) < 18){
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(this, RegisterPartTwoActivity.class);
		startActivity(intent);
	}

	public static int getAge(Date dateOfBirth) {
		Calendar today = Calendar.getInstance();
		Calendar birthDate = Calendar.getInstance();

		int age = 0;

		birthDate.setTime(dateOfBirth);
		if (birthDate.after(today)) {
			throw new IllegalArgumentException("Can't be born in the future");
		}

		age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

		// If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year   
		if ( (birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
				(birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH ))){
			age--;

			// If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
		}else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH )) &&
				(birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH ))){
			age--;
		}

		return age;
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
