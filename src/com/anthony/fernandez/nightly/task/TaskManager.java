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
import com.anthony.fernandez.nightly.enums.DaysOfWeek;
import com.anthony.fernandez.nightly.globalvar.GlobalVars;
import com.anthony.fernandez.nightly.globalvar.GlobalVars.CurrentUserConnected;
import com.anthony.fernandez.nightly.task.listener.OnConnectListener;
import com.anthony.fernandez.nightly.task.listener.OnGettingUserInfo;

public class TaskManager {

	private RequestSender requestSender;
	private Context context;

	public TaskManager(Context ctx){
		this.requestSender = new RequestSender();
		this.context = ctx;
	}

	public boolean sendMessage(String message, int conversationID){
		return false;
	}

	public boolean pickUpSomebody(long datetimeUTC){
		return false;
	}

	public boolean getListConversations(){
		return false;
	}

	public boolean sendGoodNight(String message, int categorieID, int remoteUserID){
		return false;
	}

	public boolean connectFacebook(String tokenFacebook){
		return false;
	}

	public void connectNightly(String username, String password, OnConnectListener listener){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.GRANT_TYPE, "password"));
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.USERNAME, username));
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.PASSWORD, password));
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.CLIENT_ID, "android"));
		nameValuePairs.add(new BasicNameValuePair(ParametersApi.CLIENT_SECRET, "android"));

		if(null != requestSender){
			String result = requestSender.sendRequestPost(UrlApi.URL_API_BASE, UrlApi.GET_OAUTH_TOKEN, nameValuePairs, null);
			if(null == result){
				listener.onConnectionRefused(context.getResources().getString(R.string.error_occured));
				return;
			}
			Log.d("Nightly", "result = " +result);
			try {
				JSONObject jsonData = new JSONObject(result);
				if(jsonData.has("error_description")){
					listener.onConnectionRefused(jsonData.getString("error_description"));
				} else {
					GlobalVars.currentUser = new CurrentUserConnected();
					GlobalVars.currentUser.token = jsonData.getString(ParametersApi.ACCESS_TOKEN);
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
				String result = requestSender.sendRequestGet(UrlApi.URL_API_BASE, UrlApi.GET_USER_INFOS, nameValuePairs);
				if(null == result){
					listener.OnUserInfoFailed(context.getResources().getString(R.string.error_occured));
					return;
				}
				Log.d("Nightly", "result = " +result);
				try {
					JSONObject jsonData = new JSONObject(result);
					if(jsonData.has("error")){
						listener.OnUserInfoFailed(jsonData.getString("error"));
					} else {
						GlobalVars.currentUser = new CurrentUserConnected();
						GlobalVars.currentUser._idServer = jsonData.getString(ParametersApi.ID);
						GlobalVars.currentUser.email = jsonData.getString(ParametersApi.EMAIL);
						GlobalVars.currentUser.language = jsonData.getString(ParametersApi.LANGUAGE);
						listener.OnUserInfo();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean register(String firstname, String lastname, String email, String password, byte[] profilPhoto, long birthDate, boolean sexe, String country, String phoneNumber){
		return false;
	}

	public boolean updateProfil(String firstname, String lastname, String email, String password, byte[] profilPhoto, long birthDate, boolean sexe, String country, String phoneNumber){
		return false;
	}

	public boolean updateClockAlarm(int hours, int minutes, DaysOfWeek dayOfWeek){
		return false;
	}

	public boolean sendRegistrationID(String regID){
		return false;
	}

	public boolean updateRegistrationID(String regID){
		return false;
	}
}
