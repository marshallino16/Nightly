package com.anthony.fernandez.nightly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.anthony.fernandez.nightly.globalvar.GlobalVars;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper mInstance = null;

	private static final int DATABASE_VERSION = 1;

	public static final String DATABASE_NAME = "NIGHTLY_DB";

	private static final String TABLE_CUSTOMER = "USERS";
	private static final String TABLE_CONVERSATION = "CONVERSATIONS";
	private static final String TABLE_MESSAGE = "MESSAGES";
	private static final String TABLE_ALARM = "ALARMS";

	//USER
	private static final String KEY_ID_USER_LOCAL = "_ID_USER_LOCAL";
	private static final String KEY_ID_USER_SERVER = "_ID_USER_SERVER";
	private static final String KEY_EMAIL = "EMAIL";
	private static final String KEY_PASSWORD = "PASSWORD";
	private static final String KEY_FIRSTNAME = "FIRSTNAME";
	private static final String KEY_LASTNAME = "LASTNAME";
	private static final String KEY_LANGUAGE = "LANGUAGE";
	private static final String KEY_IMAGE_URL = "IMAGE";
	private static final String KEY_TOKEN = "TOKEN";
	private static final String KEY_GCM = "GCM";
	private static final String KEY_LAST_TOKEN_UPDATE = "LAST_CONNECTION"; //long - timestamp

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

	//ALARM
	private static final String KEY_REFERENCE_USER = "_REF_USER";
	private static final String KEY_ID_ALARM_LOCAL = "_ID_ALARM_LOCAL";
	private static final String KEY_ID_ALARM_SERVER = "_ID_ALAMR_SERVER";
	private static final String KEY_TIME_TIME = "TIME_TIME";
	private static final String KEY_CAT_TIME= "CAT_TIME";
	private static final String KEY_ACTIVE_TIME = "ACTIVE_TIME";

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
		String CREATE_TABLE_USER = "CREATE TABLE "
				+ TABLE_CUSTOMER + " ("
				+ KEY_ID_USER_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_ID_USER_SERVER + " TEXT,"
				+ KEY_TOKEN + " TEXT,"
				+ KEY_GCM + " TEXT,"
				+ KEY_EMAIL + " TEXT,"
				+ KEY_FIRSTNAME + " TEXT,"
				+ KEY_LASTNAME + " TEXT,"
				+ KEY_LANGUAGE + " TEXT,"
				+ KEY_IMAGE_URL + " TEXT,"
				+ KEY_LAST_TOKEN_UPDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
				+ KEY_PASSWORD + " TEXT)";
		db.execSQL(CREATE_TABLE_USER);

		String CREATE_TABLE_ALARM_CLOCK = "CREATE TABLE " 
				+ TABLE_ALARM + " (" 
				+ KEY_ID_ALARM_LOCAL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_ID_ALARM_SERVER + " TEXT,"
				+ KEY_REFERENCE_USER + " INTEGER," 
				+ KEY_TIME_TIME + " TEXT DEFAULT \'22:30\',"
				+ KEY_CAT_TIME + " TEXT,"
				+ KEY_ACTIVE_TIME + " INTEGER,"
				+"FOREIGN KEY("+KEY_REFERENCE_USER+") REFERENCES "+TABLE_CUSTOMER+"("+KEY_ID_USER_SERVER+") "+")";	
		db.execSQL(CREATE_TABLE_ALARM_CLOCK);
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

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
		onCreate(db);
	}

	public long addUser(){
		Long connectionTime = System.currentTimeMillis()/1000;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID_USER_SERVER, GlobalVars.currentUser._idServer);
		values.put(KEY_TOKEN, GlobalVars.currentUser.token);
		values.put(KEY_GCM, GlobalVars.currentUser.gmc);
		values.put(KEY_EMAIL, GlobalVars.currentUser.email);
		values.put(KEY_FIRSTNAME, GlobalVars.currentUser.firstname);
		values.put(KEY_LASTNAME, GlobalVars.currentUser.lastname);
		values.put(KEY_IMAGE_URL, GlobalVars.currentUser.imgURL);
		values.put(KEY_LAST_TOKEN_UPDATE, connectionTime);
		values.put(KEY_PASSWORD, GlobalVars.currentUser.password);

		long idUser = db.insert(TABLE_CUSTOMER, null, values);

		db.close();
		return idUser;
	}

	public boolean deleteUser(String _idUserServer){
		SQLiteDatabase db = this.getWritableDatabase();
		boolean retour = db.delete(TABLE_CUSTOMER, KEY_ID_USER_SERVER + "='" + _idUserServer+"'", null) > 0;
		db.close();
		return retour;
	}

	public void updateUser(String _idUserServer, String token){
		Long connectionTime = System.currentTimeMillis()/1000;
		SQLiteDatabase db = this.getWritableDatabase();
		String strFilter = KEY_ID_USER_SERVER+"='" + _idUserServer+"'";
		ContentValues values = new ContentValues();
		values.put(KEY_LAST_TOKEN_UPDATE, connectionTime);
		values.put(KEY_TOKEN, token);
		db.update(TABLE_CUSTOMER, values, strFilter, null);
		db.close();
	}
	
	public void updateUserByEmail(String email, String token){
		Long connectionTime = System.currentTimeMillis()/1000;
		SQLiteDatabase db = this.getWritableDatabase();
		String strFilter = KEY_EMAIL+"='" + email+"'";
		ContentValues values = new ContentValues();
		values.put(KEY_LAST_TOKEN_UPDATE, connectionTime);
		values.put(KEY_TOKEN, token);
		db.update(TABLE_CUSTOMER, values, strFilter, null);
		db.close();
	}

	public boolean isUserAlreadyStored(String _idUserServer){
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQueryItems = "SELECT COUNT("+KEY_ID_USER_LOCAL+") FROM " + TABLE_CUSTOMER+" WHERE " + KEY_ID_USER_SERVER + "='" + _idUserServer+"'";
		Cursor cursorItems = db.rawQuery(selectQueryItems, null);
		if(cursorItems.moveToFirst()){
			if(cursorItems.getInt(0) == 0){
				Log.d("Nightly", "isUserAlreadyStored return false");
				cursorItems.close();
				db.close();
				return false;
			} else {
				Log.d("Nightly", "isUserAlreadyStored return true");
				cursorItems.close();
				db.close();
				return true;
			}
		} else {
			Log.d("Nightly", "isUserAlreadyStored error");
			return false;
		}
	}
	
	public boolean isUserAlreadyStoredByEmail(String email){
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQueryItems = "SELECT COUNT("+KEY_ID_USER_LOCAL+") FROM " + TABLE_CUSTOMER+" WHERE " + KEY_EMAIL + "='" + email+"'";
		Cursor cursorItems = db.rawQuery(selectQueryItems, null);
		if(cursorItems.moveToFirst()){
			if(cursorItems.getInt(0) == 0){
				Log.d("Nightly", "isUserAlreadyStored return false");
				cursorItems.close();
				db.close();
				return false;
			} else {
				Log.d("Nightly", "isUserAlreadyStored return true");
				cursorItems.close();
				db.close();
				return true;
			}
		} else {
			Log.d("Nightly", "isUserAlreadyStored error");
			return false;
		}
	}
	
	public void getAllCurrentUserInfos(String email){
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQueryItems = "SELECT * FROM " + TABLE_CUSTOMER+" WHERE " + KEY_EMAIL + "='" + email+"'";
		Cursor cursorItems = db.rawQuery(selectQueryItems, null);
		if(cursorItems.moveToFirst()){
			do{
				GlobalVars.currentUser._idLocal = cursorItems.getInt(0);
				GlobalVars.currentUser._idServer = cursorItems.getString(1);
				GlobalVars.currentUser.token = cursorItems.getString(2);
				GlobalVars.currentUser.email = cursorItems.getString(4);
				GlobalVars.currentUser.firstname = cursorItems.getString(5);
				GlobalVars.currentUser.imgURL = cursorItems.getString(8);
				GlobalVars.currentUser.language = cursorItems.getString(7);
				GlobalVars.currentUser.lastname = cursorItems.getString(6);
				GlobalVars.currentUser.password = cursorItems.getString(10);
				GlobalVars.currentUser.gmc = cursorItems.getString(3);
			} while (cursorItems.moveToNext());
		}
		cursorItems.close();
		db.close();
	}
	
	@Deprecated
	public GlobalVars.CurrentUserConnected getLastConnectedUser(){
		GlobalVars.CurrentUserConnected user = null;
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQueryItems = "SELECT USERS."+KEY_EMAIL+", USERS."+KEY_FIRSTNAME+", USERS."+KEY_LASTNAME+", USERS."+KEY_LAST_TOKEN_UPDATE+",  USERS."+KEY_PASSWORD+" FROM " + TABLE_CUSTOMER
				+" JOIN (SELECT "+KEY_EMAIL+", "+KEY_FIRSTNAME+", "+KEY_LASTNAME+", MAX("+KEY_LAST_TOKEN_UPDATE+") AS "+KEY_LAST_TOKEN_UPDATE+", "+KEY_PASSWORD+" FROM " 
				+TABLE_CUSTOMER+" GROUP BY " +KEY_EMAIL
				+ ") T2 ON USERS."+KEY_EMAIL+"= T2."+KEY_EMAIL 
				+" AND USERS."+ KEY_FIRSTNAME  + "= T2."+ KEY_FIRSTNAME 
				+" AND USERS."+KEY_LASTNAME + "= T2."+KEY_LASTNAME
				+" AND USERS."+ KEY_LAST_TOKEN_UPDATE + "= T2."+ KEY_LAST_TOKEN_UPDATE
				+" AND USERS."+ KEY_PASSWORD + "= T2."+KEY_PASSWORD
				+ ""; 
		Cursor cursorItems = db.rawQuery(selectQueryItems, null);
		if(cursorItems.moveToFirst()){
			do{
				user = new GlobalVars.CurrentUserConnected();
				user.email = cursorItems.getString(0);
				user.firstname = cursorItems.getString(1);
				user.lastname = cursorItems.getString(2);
				user.lastTokenUpdate = cursorItems.getLong(3);
				user.password = cursorItems.getString(4);
				Log.d("Nightly", "user.lastTokenUpdate = " + user.lastTokenUpdate);
				Log.d("Nightly", "user.email = " + user.email );
			} while (cursorItems.moveToNext());
		}
		cursorItems.close();
		db.close();
		return user;
	}

	public void addConversation(){

	}

	public void addMessageToSend(){

	}

	public void resendMessage(){

	}

	public void addMessageToConversation(){

	}

	public void deleteConversation(){

	}

	public void initAlamrForUser(int _idUserLocal, int _idUserServer){
		
	}

	public void setTimeLundi(String time){

	}

	public void setTimeMardi(String time){

	}

	public void setTimeMercredi(String time){

	}

	public void setTimeJeudi(String time){

	}

	public void setTimeVendredi(String time){

	}

	public void setTimeSamedi(String time){

	}

	public void setTimeDimanche(String time){

	}

	public String getTimeLundi(){
		return null;
	}

	public String getTimeMardi(){
		return null;
	}

	public String getTimeMercredi(){
		return null;
	}

	public String getTimeJeudi(){
		return null;
	}

	public String getTimeVendredi(){
		return null;
	}

	public String getTimeSamedi(){
		return null;
	}

	public String getTimeDimanche(){
		return null;
	}
}
