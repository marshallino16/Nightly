package com.anthony.fernandez.nightly.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.anthony.fernandez.nightly.R;
import com.anthony.fernandez.nightly.api.ParametersApi;
import com.anthony.fernandez.nightly.api.UrlApi;
import com.anthony.fernandez.nightly.database.DatabaseManager;
import com.anthony.fernandez.nightly.enums.DaysOfWeek;
import com.anthony.fernandez.nightly.globalvar.GlobalVars;
import com.anthony.fernandez.nightly.globalvar.GlobalVars.CurrentUserConnected;
import com.anthony.fernandez.nightly.model.Category;
import com.anthony.fernandez.nightly.model.RequestReturn;
import com.anthony.fernandez.nightly.task.listener.OnConnectListener;
import com.anthony.fernandez.nightly.task.listener.OnGCMRegistered;
import com.anthony.fernandez.nightly.task.listener.OnGettingUserInfo;

public class TaskManager {

	private RequestSender requestSender;
	private Context context;

	public TaskManager(Context ctx){
		this.requestSender = new RequestSender();
		this.context = ctx;
	}
	
	private synchronized DatabaseManager getDBAccess() {
		return DatabaseManager.getInstance(context);
	}

	public void sendMessage(String message, int conversationID){
	}

	public void pickUpSomebody(long datetimeUTC){
	}

	public void getListConversations(){
	}

	public void sendGoodNight(String message, int categorieID, int remoteUserID){
	}

	public void connectFacebook(String tokenFacebook){
	}

	public void connectNightly(String username, String password, OnConnectListener listener){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.GRANT_TYPE, "password"));
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.USERNAME, username));
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.PASSWORD, password));
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.CLIENT_ID, "android"));
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.CLIENT_SECRET, "android"));

		if(null != requestSender){
			RequestReturn retour = requestSender.sendRequestPost(UrlApi.URL_API_BASE, UrlApi.GET_OAUTH_TOKEN, nameValuePairs, null);
			if(200 != retour.code){
				listener.onConnectionRefused(context.getResources().getString(R.string.error_occured));
				return;
			}
			if(null == retour.json){
				listener.onConnectionRefused(context.getResources().getString(R.string.error_occured));
				return;
			}
			Log.d("Nightly", "result = " +retour.json);
			try {
				JSONObject jsonData = new JSONObject(retour.json);
				if(jsonData.has("error_description")){
					listener.onConnectionRefused(jsonData.getString("error_description"));
				} else {
					GlobalVars.currentUser = new CurrentUserConnected();
					GlobalVars.currentUser.token = jsonData.getString(ParametersApi.ACCESS_TOKEN);
					Log.w("Nightly", "GlobalVars.currentUser.token = " +GlobalVars.currentUser.token);
					listener.onConnectionAccepted();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void getUserInfos(OnGettingUserInfo listener){
		if(null != GlobalVars.currentUser){
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(ParametersApi.ACCESS_TOKEN, GlobalVars.currentUser.token));

			if(null != requestSender){
				RequestReturn retour = requestSender.sendRequestGet(UrlApi.URL_API_BASE, UrlApi.GET_USER_INFOS, nameValuePairs);
				if(200 != retour.code){
					listener.OnUserInfoFailed(context.getResources().getString(R.string.error_occured));
				}
				if(null == retour.json){
					listener.OnUserInfoFailed(context.getResources().getString(R.string.error_occured));
					return;
				}
				Log.d("Nightly", "result = " +retour.json);
				try {
					JSONObject jsonData = new JSONObject(retour.json);
					if(jsonData.has("error")){
						listener.OnUserInfoFailed(jsonData.getString("error"));
					} else {
						GlobalVars.currentUser._idServer = jsonData.getString(ParametersApi.ID);
						GlobalVars.currentUser.email = jsonData.getString(ParametersApi.EMAIL);
						GlobalVars.currentUser.language = jsonData.getString(ParametersApi.LANGUAGE);
						GlobalVars.currentUser.gmc = jsonData.getString(ParametersApi.GCM_DEVICE_ID);
						
						/**
						 * TODO
						 * uncomment when every field will be available
						 * 
						 */
						if(!getDBAccess().isUserAlreadyStored(GlobalVars.currentUser._idServer)){
							getDBAccess().createUser();
						}
						listener.OnUserInfo();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void register(String firstname, String lastname, String email, String password, byte[] profilPhoto, long birthDate, boolean sexe, String country, String phoneNumber){
	}

	public void updateProfil(String firstname, String lastname, String email, String password, byte[] profilPhoto, long birthDate, boolean sexe, String country, String phoneNumber){
	}

	public void updateClockAlarm(int hours, int minutes, DaysOfWeek dayOfWeek, Category category, boolean isActive){
		
	}

	public void sendGCMRegistrationID(String regID, OnGCMRegistered listener){
		Log.w("Nightly", "sending reg id");
		if(null != GlobalVars.currentUser){
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(ParametersApi.GCM_DEVICE_ID, GlobalVars.currentUser.gmc));

			if(null != requestSender){
				RequestReturn retour = requestSender.sendRequestPost(UrlApi.URL_API_BASE, UrlApi.SET_GCM_ID, nameValuePairs, GlobalVars.currentUser.token);
				if(200 != retour.code){
					listener.OnGCMRegisterFailed(context.getResources().getString(R.string.error_occured));
				}else{
					listener.OnGCMRegister();
				}
			}
		}
	}
}
