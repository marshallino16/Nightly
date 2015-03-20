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
	
	public void initAlarmClock(){
		databaseHelper = getDBAccess();
		try {
			databaseHelper.initAlarmClock();
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

	public String getAlarmClock(DaysOfWeek day){
		databaseHelper = getDBAccess();
		try {
			switch (day.numOfDay) {
			case 2: //lundi
				return databaseHelper.getTimeLundi();
			case 3: //mardi
				return databaseHelper.getTimeMardi();
			case 4: //mercredi
				return databaseHelper.getTimeMercredi();
			case 5: //jeudi
				return databaseHelper.getTimeJeudi();
			case 6: //vendredi
				return databaseHelper.getTimeVendredi();
			case 0: //samedi
				return databaseHelper.getTimeSamedi();
			case 1: //dimanche
				return databaseHelper.getTimeDimanche();
			}
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
	
	public void setAlarmClock(DaysOfWeek day, int hour, int minute, String category, boolean active){
		databaseHelper = getDBAccess();
		String time = String.format("%02d", hour) + ":" + String.format("%02d", minute);
		try {
			switch (day.numOfDay) {
			case 2: //lundi
				databaseHelper.setTimeLundi(time, active, category);
				break;
			case 3: //mardi
				databaseHelper.setTimeMardi(time, active, category);
				break;
			case 4: //mercredi
				databaseHelper.setTimeMercredi(time, active, category);
				break;
			case 5: //jeudi
				databaseHelper.setTimeJeudi(time, active, category);
				break;
			case 6: //vendredi
				databaseHelper.setTimeVendredi(time, active, category);
				break;
			case 0: //samedi
				databaseHelper.setTimeSamedi(time, active, category);
				break;
			case 1: //dimanche
				databaseHelper.setTimeDimanche(time, active, category);
				break;
			}
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

	public void updateUserByEmail(String email, String token){
		databaseHelper = getDBAccess();
		try {
			databaseHelper.updateUserByEmail(email, token);
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

	@Deprecated
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
