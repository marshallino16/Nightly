package com.anthony.fernandez.nightly;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.anthony.fernandez.nightly.gcm.GCMUtils;
import com.anthony.fernandez.nightly.util.Utils;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class HomeActivity extends SherlockFragmentActivity implements android.view.View.OnClickListener{

	//views 
	private RelativeLayout mainContainer;
	private LayoutInflater inflater;
	private View splashScreen;

	//facebook components
	private Button loginBtn;

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
		loginBtn = (Button) findViewById(R.id.connectionFb);
		loginBtn.setOnClickListener(this);

		if (GCMUtils.checkPlayServices(this)) {
			// If this check succeeds, proceed with normal processing.
			// Otherwise, prompt user to get valid Play Services APK.
		}
	}

	@Override
	protected void onResume() {
		Animation animationFadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
		Animation animationFromBottom = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
		findViewById(R.id.textLogo).startAnimation(animationFadein);
		findViewById(R.id.connection).startAnimation(animationFromBottom);
		findViewById(R.id.connectionFb).startAnimation(animationFromBottom);
		findViewById(R.id.registerUser).startAnimation(animationFromBottom);
		GCMUtils.checkPlayServices(this);
		super.onResume();
	}

	public void registerAccount(View v){
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	public void loginNightly(View v){
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
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
	public void onClick(View v) {
		Session currentSession = Session.getActiveSession();
		if (currentSession == null || currentSession.getState().isClosed()) {
			Log.w("Nightly", "Fb session closed");
			Session session = new Session.Builder(HomeActivity.this).build();
			Session.setActiveSession(session);
			currentSession = session;
		}

		if (currentSession.isOpened()) {
			Log.w("Nightly", "Fb session opened");
			// Do whatever u want. User has logged in

		} else if (!currentSession.isOpened()) {
			Log.w("Nightly", "Fb session not opened obviusly");
			// Ask for username and password
			OpenRequest op = new Session.OpenRequest((Activity) HomeActivity.this);

			op.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);//changement 0.1
			op.setCallback(null);

			List<String> permissions = new ArrayList<String>();
			permissions.add("publish_actions");
			permissions.add("user_friends");
			permissions.add("email");
			permissions.add("user_birthday");
			op.setPermissions(permissions);

			Session session = new Builder(HomeActivity.this).build();
			Session.setActiveSession(session);
			session.openForPublish(op);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (Session.getActiveSession() != null)
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);

		Session currentSession = Session.getActiveSession();
		if (currentSession == null || currentSession.getState().isClosed()) {
			Session session = new Session.Builder(HomeActivity.this).build();
			Session.setActiveSession(session);
			currentSession = session;
		}

		if (currentSession.isOpened()) {
			Session.openActiveSession(this, true, new Session.StatusCallback() {

				@SuppressWarnings("deprecation")
				@Override
				public void call(final Session session, SessionState state,
						Exception exception) {

					if (session.isOpened()) {

						Request.executeMeRequestAsync(session,
								new Request.GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								if (user != null) {

									String access_token = session.getAccessToken();
									String firstName = user.getFirstName();
									String fb_user_id = user.getId();

									System.out.println("Facebook Access token: "+ access_token);
									System.out.println("First Name:"+ firstName);
									System.out.println("FB USER ID: "+ fb_user_id);
									
									Utils.createToast(getApplicationContext(), "Firstname is " + firstName);
								}
							}
						});
					}
				}
			});
		}
	}


}
