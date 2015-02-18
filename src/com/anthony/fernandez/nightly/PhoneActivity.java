package com.anthony.fernandez.nightly;

import java.lang.reflect.Field;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.anthony.fernandez.nightly.enums.CountryPhoneCode;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class PhoneActivity extends SherlockActivity {
	
	private final static int COUNTRY_SELECTOR = 1;
	private final static String EXTRA_KEY = "code";
	private String countryCode;
	
	private TextView countryName;
	private TextView indicatif;
	private ImageView countryFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_phone);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.blue_crepuscule));
		((TextView)findViewById(R.id.textCluf)).setText(Html.fromHtml("<u>"+getResources().getString(R.string.cluf)+"</u>"));
		
		countryName = (TextView)findViewById(R.id.row_title);
		countryFlag = (ImageView)findViewById(R.id.row_icon);
		indicatif = (TextView)findViewById(R.id.indicatif);
	}

	@Override
	protected void onResume() {
		Animation animationFadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
		findViewById(R.id.textLogo).startAnimation(animationFadein);
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
	
	@Override
	public void onBackPressed() {
		this.finish();
		super.onBackPressed();
	}
	
	public void openCountrySelection(View v){
		Intent intent = new Intent(this, CountrySelectionActivity.class);
		startActivityForResult(intent, COUNTRY_SELECTOR);
	}
	
	public void registerPhone(View v){
		//TODO webservices
		this.finish();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(null != data && COUNTRY_SELECTOR == requestCode && RESULT_OK == resultCode){
			if(null != data.getExtras() && data.getExtras().containsKey(EXTRA_KEY)){
				countryCode = data.getExtras().getString(EXTRA_KEY);
				Log.w("Nightly", "countryCode = " + countryCode);
				String name = CountryPhoneCode.getCountryName(countryCode);
				countryName.setText(name);
				String phoneCode = CountryPhoneCode.getPhoneCode(countryCode);
				indicatif.setText(phoneCode.substring(1));
				String drawableName = "flag_"+ countryCode.toLowerCase(Locale.ENGLISH);
				countryFlag.setImageResource(getResId(drawableName));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private int getResId(String drawableName) {
		try {
			Class<com.countrypicker.R.drawable> res = com.countrypicker.R.drawable.class;
			Field field = res.getField(drawableName);
			int drawableId = field.getInt(null);
			return drawableId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
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
