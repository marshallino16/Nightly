package com.anthony.fernandez.nightly.task;

import com.anthony.fernandez.nightly.enums.DaysOfWeek;

import android.content.Context;

@SuppressWarnings("unused")
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
	
	public boolean connectNightly(String username, String password){
		return false;
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
