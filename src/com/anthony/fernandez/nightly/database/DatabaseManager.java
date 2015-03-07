package com.anthony.fernandez.nightly.database;

import com.anthony.fernandez.nightly.enums.DaysOfWeek;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

	private Context context;

	private static DatabaseManager mInstance = null;
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase sqliteDatabase = null;

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
			sqliteDatabase = databaseHelper.getWritableDatabase();
			// db.getall...
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				databaseHelper.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				sqliteDatabase.close();
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
}
