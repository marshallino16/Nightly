package com.anthony.fernandez.nightly;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.anthony.fernandez.nightly.adapter.InitPagerAdapter;
import com.anthony.fernandez.nightly.fragment.LeftPanel;
import com.anthony.fernandez.nightly.fragment.RightPanel;
import com.anthony.fernandez.nightly.gcm.GCMUtils;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment.TimePickerDialogHandler;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends SherlockFragmentActivity {
	
	//GCM
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    
    String SENDER_ID = "926014171825";
    
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;

    String regid;
    
    //Views
	private RelativeLayout mainContainer;
	private PagerAdapter pagerAdapter;
	public static ViewPager pager;
	private List<Object> fragments;
	private LayoutInflater inflater;
	private View splashScreen;
	
	private EditText registrationID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_main);
		//Project ID: animated-codex-750 Project Number: 926014171825
		//API KEY = AIzaSyBod3FoppvHG6F8lXihLLwg433pJnkmscQ
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.action_bar_menu);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().getCustomView().findViewById(R.id.settings).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settings();
			}
		});
		getSupportActionBar().getCustomView().findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		getSupportActionBar().getCustomView().findViewById(R.id.logout).setVisibility(View.VISIBLE);
		

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.blue_aciton_bar));

		fragments = new Vector<Object>();

		// Ajout des Fragments dans la liste
		fragments.add(Fragment.instantiate(this,LeftPanel.class.getName()));
		fragments.add(Fragment.instantiate(this,RightPanel.class.getName()));

		this.pagerAdapter = new InitPagerAdapter(super.getSupportFragmentManager(), fragments);
		this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		splashScreen = inflater.inflate(R.layout.splashscreen_wait, null);

		mainContainer = (RelativeLayout)findViewById(R.id.containerMain);
		pager = (ViewPager) findViewById(R.id.viewpager);
		// Affectation de l'adapter au ViewPager
		pager.setAdapter(this.pagerAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
		indicator.setFillColor(getResources().getColor(R.color.pink_circle));
		indicator.setStrokeColor(getResources().getColor(android.R.color.white));
		indicator.setViewPager(pager);
		
		registrationID = (EditText)findViewById(R.id.dddd);
		
		if (GCMUtils.checkPlayServices(this)) {
			gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(this);
            Log.w("Nightly", "regid = " +regid);
            if (regid.isEmpty()) {
                registerInBackground();
            }
            if(regid != null){
            	 addText(regid);
            }
	    } else {
	    	Log.i("NIghtly", "No valid Google Play Services APK found.");
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

	public void pickSleepingTime(View v){
		TimePickerBuilder btp = new TimePickerBuilder()
		.setFragmentManager(getSupportFragmentManager())
		.setStyleResId(R.style.BetterPickersDialogFragment);
		btp.addTimeSetListener(new TimePickerDialogHandler() {

			@Override
			public void onDialogTimeSet(int hourOfDay, int minute) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDialogCancel() {
				// TODO Auto-generated method stub

			}
		});  
		btp.show();
	}

	/**
	 * @param view v
	 * Slide to leftPanel 
	 */
	public void leftPanel(View v){
		if(0 != pager.getCurrentItem()){
			pager.setCurrentItem(0);
		}
	}

	/**
	 * @param view v
	 * Slide to rightPanel 
	 */
	public void rightPanel(View v){
		if(1 != pager.getCurrentItem()){
			pager.setCurrentItem(1);
		}
	}

	/**
	 * @param view representing owner of all child views
	 * @param enabled ?
	 */
	public static void enableDisableView(View view, boolean enabled) {
		view.setEnabled(enabled);
		if ( view instanceof ViewGroup ) {
			ViewGroup group = (ViewGroup)view;

			for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
				enableDisableView(group.getChildAt(idx), enabled);
			}
		}
	}

	public void pickUpSomeone(View v){
		if(mainContainer.findViewById(R.id.splashContainer) == null){
			enableDisableView(mainContainer, false);
			mainContainer.addView(splashScreen, mainContainer.getChildCount()-1);
		}
	}

	public void disableSplashScreen(View v){
		if(mainContainer.findViewById(R.id.splashContainer) != null){
			mainContainer.removeView(splashScreen);
			enableDisableView(mainContainer, true);
		}
	}

	public void categories(View v){
		Intent intent = new Intent(this, CategoriesActivity.class);
		startActivity(intent);
	}

	public void conversationsView(View v){
		Intent intent = new Intent(this, ListConversationActivity.class);
		startActivity(intent);
	}
	
	public void profil(View v){
		Intent intent = new Intent(this, MessageReceivedActivity.class);
		startActivity(intent);
	}

	private void settings(){
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
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
	public void onBackPressed() {
		disableSplashScreen(null);
	}
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i("NIghtly", "Registration not found.");
	         addText("Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing registration ID is not guaranteed to work with
	    // the new app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i("NIghtly", "App version changed.");
	         addText("App version changed.");
	        return "";
	    }
	    return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the registration ID in your app is up to you.
	    return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
	    new AsyncTask<Void, Void, String>() {
	        @Override
	        protected String doInBackground(Void... params) {
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;
	                Log.w("Nightly", "msg = " +msg);
	                if(regid != null)
	                 addText(regid);

	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
	                final SharedPreferences prefs = getGCMPreferences(MainActivity.this);
	                if(prefs.getString(PROPERTY_REG_ID, regid) != null){
	                	sendRegistrationIdToBackend();
	                }

	                // Persist the registration ID - no need to register again.
	                storeRegistrationId(MainActivity.this, regid);
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	                Log.w("Nightly", "msg error = " +msg);
	                 addText(msg);
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
	        }
	    }.execute(null, null, null);
	}
	
	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
	    // Your implementation here.
	}
	
	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i("Nightly", "Saving regId on app version " + appVersion);
	     addText("Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	public void addText(String msg){
		if(msg != null && registrationID != null){
			registrationID.setText(registrationID.getText() + "\n" + msg);
		}
	}
}
