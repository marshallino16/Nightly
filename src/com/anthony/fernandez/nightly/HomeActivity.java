package com.anthony.fernandez.nightly;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.anthony.fernandez.nightly.util.FacebookConfig;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.readystatesoftware.systembartint.SystemBarTintManager;

@SuppressWarnings("deprecation")
public class HomeActivity extends SherlockActivity {

	//views 
	private RelativeLayout mainContainer;
	private LayoutInflater inflater;
	private View splashScreen;

	//facebook components
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true); 
		} 

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(getResources().getColor(R.color.pink_circle));
		((TextView)findViewById(R.id.textCluf)).setText(Html.fromHtml("<u>"+getResources().getString(R.string.cluf)+"</u>"));

		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		splashScreen = inflater.inflate(R.layout.splashscreen_wait, null);

		mainContainer = (RelativeLayout)findViewById(R.id.containerMain);

		facebook = new Facebook(FacebookConfig.FACEBOOK_APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
	}

	@Override
	protected void onResume() {
		Animation animationFadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
		Animation animationFromBottom = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
		findViewById(R.id.textLogo).startAnimation(animationFadein);
		findViewById(R.id.connection).startAnimation(animationFromBottom);
		findViewById(R.id.connectionFb).startAnimation(animationFromBottom);
		findViewById(R.id.registerUser).startAnimation(animationFromBottom);
		super.onResume();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.w("nightly", "activity result code = " + resultCode);
		facebook.authorizeCallback(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			getFacebookInfos();
		}
	}

	public void registerAccount(View v){

	}

	public void facebookLogin(View v){
		enableSplashScreen();
		loginToFacebook();
	}

	public void enableSplashScreen(){
		if(mainContainer.findViewById(R.id.splashContainer) == null){
			enableDisableView(mainContainer, false);
			mainContainer.addView(splashScreen, mainContainer.getChildCount());
		}
	}

	public void disableSplashScreen(View v){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(mainContainer.findViewById(R.id.splashContainer) != null){
					mainContainer.removeView(splashScreen);
					enableDisableView(mainContainer, true);
				}
			}
		});
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

	private void getFacebookInfos(){
		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Profile", response);
				String json = response;
				try {
					JSONObject profile = new JSONObject(json);
					// getting name of the user
					final String name = profile.getString("name");
					Log.w("nightly", "name = " + name);
					// getting email of the user
					final String email = profile.getString("email");
					Log.w("nightly", "email = " + email);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							disableSplashScreen(null);
							Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
						}

					});
				} catch (JSONException e) {
					e.printStackTrace();
					disableSplashScreen(null);
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
				disableSplashScreen(null);
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				disableSplashScreen(null);
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				disableSplashScreen(null);
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				disableSplashScreen(null);
			}
		});
	}

	private void loginToFacebook(){
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

				@Override
				public void onCancel() {
					// Function to handle cancel event
					disableSplashScreen(null);
				}

				@Override
				public void onComplete(Bundle values) {
					// Function to handle complete event
					// Edit Preferences and update facebook acess_token
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token",
							facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();
				}

				@Override
				public void onError(DialogError error) {
					// Function to handle error
					disableSplashScreen(null);
				}

				@Override
				public void onFacebookError(FacebookError fberror) {
					// Function to handle Facebook errors
					disableSplashScreen(null);
				}

			});
		}
	}

	public void logoutFromFacebook() {
		mAsyncRunner.logout(this, new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				Log.d("Logout from Facebook", response);
				if (Boolean.parseBoolean(response) == true) {
					// User successfully Logged out
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}

	public void loginNightly(View v){
		Intent intent = new Intent(this, LoginActivity.class);
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
}
