package com.anthony.fernandez.nightly.database;

import android.content.Context;

import com.anthony.fernandez.nightly.enums.DaysOfWeek;
import com.anthony.fernandez.nightly.globalvar.GlobalVars;

public class DatabaseManager {

	private Context context;

	private static DatabaseManager mInstance = null;
	private DatabaseHelper databaseHelper;

	public static DatabaseManager getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new DatabaseManager(ctx.getApplicationContext());
		}
		return mInstance;
	}

	public DatabaseManager(Context context) {
		this.context = context;
	}

	private synchronized DatabaseHelper getDBAccess() {
		return DatabaseHelper.getInstance(context);
	}

	public void getAllAlarmClockByUser(int idUserServer) {
		databaseHelper = getDBAccess();
		try {
			// db.getall...
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				databaseHelper.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
	}
	
	public void getAlarmClock(DaysOfWeek day){
		switch (day.numOfDay) {
		case 2: //lundi
			break;
		case 3: //mardi
			break;
		case 4: //mercredi
			break;
		case 5: //jeudi
			break;
		case 6: //vendredi
			break;
		case 0: //samedi
			break;
		case 1: //dimanche
			break;
		}
	}
	
	public void createUser(){
		databaseHelper = getDBAccess();
		try {
			databaseHelper.addUser();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				databaseHelper.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
	}
	
	public void updateUser(String idUser, long connectionTime, String token){
		
	}
	
	public void updateUser(String idUser, long connectionTime){
		
	}
	
	public boolean isUserAlreadyStored(String _idUserServer){
		databaseHelper = getDBAccess();
		try {
			return databaseHelper.isUserAlreadyStored(_idUserServer);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				databaseHelper.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		return false;
	}
	
	public void getAllCurrentUserInfos(String email){
		databaseHelper = getDBAccess();
		try {
			databaseHelper.getAllCurrentUserInfos(email);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				databaseHelper.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
	}
	
	public GlobalVars.CurrentUserConnected getLastConnectedUser(){
		databaseHelper = getDBAccess();
		try {
			return databaseHelper.getLastConnectedUser();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				databaseHelper.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		return null;
	}
}
