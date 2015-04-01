package com.anthony.fernandez.nightly;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.anthony.fernandez.nightly.adapter.InitPagerAdapter;
import com.anthony.fernandez.nightly.animation.ExpandCollapse;
import com.anthony.fernandez.nightly.api.GCMParams;
import com.anthony.fernandez.nightly.database.DatabaseHelper;
import com.anthony.fernandez.nightly.enums.DaysOfWeek;
import com.anthony.fernandez.nightly.fragment.LeftPanel;
import com.anthony.fernandez.nightly.fragment.RightPanel;
import com.anthony.fernandez.nightly.gcm.GCMUtils;
import com.anthony.fernandez.nightly.globalvar.GlobalVars;
import com.anthony.fernandez.nightly.task.TaskManager;
import com.anthony.fernandez.nightly.task.listener.OnAlarmClockAdded;
import com.anthony.fernandez.nightly.task.listener.OnGCMRegistered;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;
import com.facebook.Session;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends SherlockFragmentActivity implements TimePickerDialogFragment.TimePickerDialogHandler, OnGCMRegistered, OnAlarmClockAdded {

	//GCM
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";

	private String SENDER_ID = "926014171825";
	private GoogleCloudMessaging gcm;
	private SharedPreferences prefs;
	private String regid;

	//task
	private TaskManager taskManager = null;

	//Views
	private RelativeLayout containerClock;
	private RelativeLayout mainContainer;
	private PagerAdapter pagerAdapter;
	public static ViewPager pager;
	private List<Object> fragments;
	private LayoutInflater inflater;
	private View splashScreen;

	//clock
	private TextView currentDay = null;
	private DaysOfWeek dayOfWeek = null;

	//error
	private LinearLayout error;
	private TextView message_error;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		setContentView(R.layout.activity_main);
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
				//logout
				logoutAllServices(getApplicationContext());
			}
		});
		getSupportActionBar().getCustomView().findViewById(R.id.logout).setVisibility(View.VISIBLE);
		getSupportActionBar().getCustomView().findViewById(R.id.logo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ListConversationActivity.class);
				intent.putExtra("unread", true);
				startActivity(intent);
			}
		});


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setNavigationBarTintColor(getResources().getColor(R.color.blue_background_dark));
		tintManager.setStatusBarTintColor(getResources().getColor(R.color.facebook));

		taskManager = new TaskManager(MainActivity.this);
		fragments = new Vector<Object>();

		// Ajout des Fragments dans la liste
		fragments.add(Fragment.instantiate(this,LeftPanel.class.getName()));
		fragments.add(Fragment.instantiate(this,RightPanel.class.getName()));

		this.pagerAdapter = new InitPagerAdapter(super.getSupportFragmentManager(), fragments);
		this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		splashScreen = inflater.inflate(R.layout.splashscreen_wait, null);
		error = (LinearLayout)findViewById(R.id.message);
		message_error = (TextView)findViewById(R.id.message_error);
		mainContainer = (RelativeLayout)findViewById(R.id.containerMain);
		containerClock = (RelativeLayout)findViewById(R.id.containerMenuClock);
		pager = (ViewPager) findViewById(R.id.viewpager);
		// Affectation de l'adapter au ViewPager
		pager.setAdapter(this.pagerAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
		indicator.setFillColor(getResources().getColor(R.color.pink_circle));
		indicator.setStrokeColor(getResources().getColor(android.R.color.white));
		indicator.setViewPager(pager);

		if (GCMUtils.checkPlayServices(this)) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(this);
			Log.w("Nightly", "regid = " +regid);
			if (regid.isEmpty()) {
				registerInBackground();
			}
			if(regid != null && !regid.isEmpty()){
				if(null!= GlobalVars.currentUser && !regid.equals(GlobalVars.currentUser.gmc)){
					GlobalVars.currentUser.gmc = regid;
					new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... arg0) {
							taskManager.sendGCMRegistrationID(regid, MainActivity.this);
							return null;
						}
					}.execute();
				}
			}
		} else {
			Log.i("NIghtly", "No valid Google Play Services APK found.");
		}

		final Bundle bundle = getIntent().getExtras();
		if(bundle != null && bundle.containsKey(GCMParams.CATEGORY) && bundle.containsKey(GCMParams.MESSAGE)){
			new AsyncTask<Void, Void, Void>(){

				@Override
				protected Void doInBackground(Void... params) {
					try {
						Thread.sleep(2000);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Intent intentMessage = new Intent(MainActivity.this, MessageReceivedActivity.class);
								intentMessage.putExtra(GCMParams.CATEGORY, bundle.getString(GCMParams.CATEGORY));
								intentMessage.putExtra(GCMParams.MESSAGE, bundle.getString(GCMParams.MESSAGE));
								startActivity(intentMessage);
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return null;
				}

			}.execute();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.setIntent(intent);
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
	public void onDialogTimeSet(final int reference, final int hourOfDay, final int minute) {
		String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
		currentDay.setText(time);

		new AsyncTask<Void, Void, Void>() {
			//waitBar

			@Override
			protected Void doInBackground(Void... arg0) {
				taskManager.setClock(dayOfWeek, hourOfDay, minute, true, "54dd06a2839e17a314eee65e", MainActivity.this, currentDay);
				return null;
			}
		}.execute();
	}

	private void pickSleepingTime(){
		TimePickerBuilder tpb = new TimePickerBuilder()
		.setFragmentManager(getSupportFragmentManager())
		.setStyleResId(R.style.BetterPickersDialogFragment_Light);
		tpb.show();
	}

	private void logoutAllServices(Context context){
		prefs = getSharedPreferences("com.nightly", MODE_PRIVATE);
		if (!prefs.getBoolean("firstrun", true)) {
			prefs.edit().putBoolean("firstrun", true).commit();
		}
		context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
		GlobalVars.currentUser = null;
		callFacebookLogout(context);
		this.finish();
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
		Intent intent = new Intent(this, ProfilActivity.class);
		startActivity(intent);
	}
	
	public void collapse(View v){
		expandOrCollapseView(false);
	}
	
	private void expandOrCollapseView(boolean condition){
		if(condition){ //true == expand
			if(View.GONE == error.getVisibility()){
				ExpandCollapse.expand(error);
				autoClose();
			} 
		} else {
			if(View.VISIBLE == error.getVisibility()){
				ExpandCollapse.collapse(error);
			} 
		}
	}

	private void settings(){
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	public void openClockMenu(){
		if(containerClock.getVisibility() == View.GONE){
			containerClock.setVisibility(View.GONE);
			Animation animation = new TranslateAnimation(0, 0, -pager.getHeight(), 0);
			animation.setStartOffset(0L);
			animation.setFillAfter(true);
			animation.setFillBefore(true);
            animation.setDuration(600);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animatiofillAftern) {
                	containerClock.setVisibility(View.VISIBLE);
                }
            });

            containerClock.startAnimation(animation);
		}
	}

	
	public void quitClockMenu(View v){
		if(containerClock.getVisibility() == View.VISIBLE){
			containerClock.setVisibility(View.VISIBLE);
			Animation animation=new TranslateAnimation(0, 0, 0, -pager.getHeight());
			animation.setStartOffset(0L);
            animation.setDuration(600);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animatiofillAftern) {
                	containerClock.setVisibility(View.GONE);
                }
            });

            containerClock.startAnimation(animation);
		}
	}
	
	public void changeCategory(View v){
		quitClockMenu(null);
	}
	
	public void changeHour(View v){
		pickSleepingTime();
		quitClockMenu(null);
	}

	public void lundi(View v){
		openClockMenu();
		currentDay = ((RightPanel)((InitPagerAdapter) pagerAdapter).getItem(1)).lundi;
		dayOfWeek = DaysOfWeek.LUNDI;
	}

	public void mardi(View v){
		openClockMenu();
		currentDay = ((RightPanel)((InitPagerAdapter) pagerAdapter).getItem(1)).mardi;
		dayOfWeek = DaysOfWeek.MARDI;
	}

	public void mercredi(View v){
		openClockMenu();
		currentDay = ((RightPanel)((InitPagerAdapter) pagerAdapter).getItem(1)).mercredi;
		dayOfWeek = DaysOfWeek.MERCREDI;
	}

	public void jeudi(View v){
		openClockMenu();
		currentDay = ((RightPanel)((InitPagerAdapter) pagerAdapter).getItem(1)).jeudi;
		dayOfWeek = DaysOfWeek.JEUDI;
	}

	public void vendredi(View v){
		openClockMenu();
		currentDay = ((RightPanel)((InitPagerAdapter) pagerAdapter).getItem(1)).vendredi;
		dayOfWeek = DaysOfWeek.VENDREDI;
	}

	public void samedi(View v){
		openClockMenu();
		currentDay = ((RightPanel)((InitPagerAdapter) pagerAdapter).getItem(1)).samedi;
		dayOfWeek = DaysOfWeek.SAMEDI;
	}

	public void dimanche(View v){
		openClockMenu();
		currentDay = ((RightPanel)((InitPagerAdapter) pagerAdapter).getItem(1)).dimanche;
		dayOfWeek = DaysOfWeek.DIMANCHE;
	}

	@TargetApi(19)  
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
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
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing registration ID is not guaranteed to work with
		// the new app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i("NIghtly", "App version changed.");
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
					if(GlobalVars.currentUser != null){
						GlobalVars.currentUser.gmc = regid;
					}

					// You should send the registration ID to your server over HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your app.
					// The request to your server should be authenticated if your app
					// is using accounts.
					final SharedPreferences prefs = getGCMPreferences(MainActivity.this);
					if(prefs.getString(PROPERTY_REG_ID, regid) != null){
						sendRegistrationIdToBackend(regid);
					}

					// Persist the registration ID - no need to register again.
					storeRegistrationId(MainActivity.this, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.w("Nightly", "msg error = " +msg);
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
	private void sendRegistrationIdToBackend(final String regID) {
		// Your implementation here.
		//TODO if db -> getUser -> KEY_FIRST_CO = 1 so register
		if(null != taskManager){
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... arg0) {
					taskManager.sendGCMRegistrationID(regID, MainActivity.this);
					return null;
				}
			}.execute();
		}
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		//TODO change to pick from database
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i("Nightly", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * Logout From Facebook 
	 */
	private static void callFacebookLogout(Context context) {
		Session session = Session.getActiveSession();
		if (session != null) {
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				//clear your preferences if saved
			}
		} else {
			session = new Session(context);
			Session.setActiveSession(session);
			session.closeAndClearTokenInformation();
			//clear your preferences if saved
		}
	}

	@Override
	public void OnGCMRegister() {

	}

	@Override
	public void OnGCMRegisterFailed(final String reason) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				expandOrCollapseView(true);
				message_error.setText(reason);
//				Utils.createToast(getApplicationContext(), reason);
			}
		});
	}

	@Override
	public void OnAlarmClockAdd() {
		dayOfWeek = null;
		currentDay = null;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				expandOrCollapseView(true);
				message_error.setText("Alarm set successfuly");
//				Utils.createToast(getApplicationContext(), "Alarm set successfuly");
			}
		});
	}

	@Override
	public void OnAlarmClockAddFailed(final String reason) {
		dayOfWeek = null;
		currentDay = null;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				expandOrCollapseView(true);
				message_error.setText(reason);
//				Utils.createToast(getApplicationContext(), reason);
			}
		});
	}
	
	private void autoClose(){
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				try {
					Thread.sleep(2500);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							expandOrCollapseView(false);
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}
}
