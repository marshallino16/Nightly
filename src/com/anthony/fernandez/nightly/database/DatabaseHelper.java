package com.anthony.fernandez.nightly.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper mInstance = null;

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "NIGHTLY_DB";
	
	private static final String TABLE_CUSTOMER = "USERS";
	private static final String TABLE_CONVERSATION = "CONVERSATIONS";
	private static final String TABLE_MESSAGE = "MESSAGES";
	
	//USER
	private static final String KEY_ID_USER_LOCAL = "_ID_USER_LOCAL";
	private static final String KEY_TOKEN = "TOKEN";
	private static final String KEY_LAST_CONNECTION = "LAST_CONNECTION"; //long - timestamp
	
	//CONVERSATION
	private static final String KEY_ID_CONV_LOCAL = "_ID_CONV_LOCAL";
	private static final String KEY_ID_CONV_SERVER = "_ID_CONV_SERVER";
	private static final String KEY_ID_OWNER_SERVER = "_ID_OWNER_SERVER";
	private static final String KEY_ID_PARTICIPANT_SERVER = "_ID_PARTICIPANT_SERVER";
	private static final String KEY_READ = "READ"; //boolean
	private static final String KEY_TIME_CREATION = "TIME_CREATION";
	
	//MESSAGE
	private static final String KEY_REFERENCE_CONV = "_REF_CONV";
	private static final String KEY_ID_MESSAGE_LOCAL = "_ID_MESSAGE_LOCAL";
	private static final String KEY_ID_MESSAGE_SERVER = "_ID_MESSAGE_SERVER";
	private static final String KEY_DIRECTION = "DIRECTION"; //int
	private static final String KEY_MESSAGE = "MESSAGE";
	private static final String KEY_TIME = "TIME"; //long - timestamp
	private static final String KEY_NUMBER_LOVE = "LOVE_COUNTER"; //int
	private static final String KEY_PENDING_SENDING = "PENDING_SENDING";

	public static DatabaseHelper getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(ctx.getApplicationContext());
		}
		return mInstance;
	}

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
		// Create tables again
		onCreate(db);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATION);
		onCreate(db);
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
		onCreate(db);
	}
}
