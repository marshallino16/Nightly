package com.anthony.fernandez.nightly.api;

public interface ParametersApi {
	
	public final static String GCM_DEVICE_ID = "gcm_device_id";
	public final static String AUTHORIZATION = "authorization";
	public final static String CATEGORIE = "category";
	public final static String HOUR = "hour";
	public final static String MINUTE = "minute";
	public final static String DAY = "day";
	public final static String ACTIVE = "active";
	public final static String GENRE = "genre";
	public final static String ID_CLOCK = "id_clock";
	public final static String CONTENT = "content";
	public final static String ID_GOODNIGHT = "id_goodnight";
	
	/**
	 * @value = boolean
	 */
	public final static String MINE = "mine";
	public final static String ACCESS_TOKEN = "access_token";
	public final static String GRANT_TYPE = "grant_type";
	public final static String USERNAME = "username";
	public final static String PASSWORD = "password";
	public final static String CLIENT_ID = "client_id";
	public final static String CLIENT_SECRET = "client_secret";
	public final static String REFRESH_TOKEN = "refresh_token";
	public final static String EXPIRES_IN = "expires_in";
	public final static String TOKEN_TYPE = "token_type";
	/**
	 * Put user language (fr, en, ru, zh...)
	 */
	public final static String LANGUAGE = "language";

	public final static String SKIP = "skip";
	public final static String LIMIT = "limit";
	public final static String SENDED_GOODNIGHT = "sended_goodnight";
	public final static String RECEIVED_GOODNIGHT = "received_goodnight";
	public final static String SORT_LIKE = "sort_like";
	public final static String SORT_CREATED = "sort_created";
	
	public final static String FIRSTNAME = "firstname";
	public final static String LASTNAME = "lastname";
	public final static String PHONE = "phone";
	public final static String BORN = "born";
	
	/**
	 * result
	 */
	public final static String ID = "_id";
	public final static String EMAIL = "email";
	public final static String BLACKLIST = "blacklist";
	public final static String CLOCKS = "clocks";
	public final static String CREATED = "created";
}
