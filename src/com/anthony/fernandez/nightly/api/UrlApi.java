package com.anthony.fernandez.nightly.api;

public interface UrlApi {

	public final static String URL_API_BASE = "https://nightly-api-dev.cheezecake.ovh";
	
	public final static String GET_CATEGORIES = "/api/categories";
	public final static String ADD_USER_CLOCK = "/api/clock";
	public final static String GET_USER_CLOCKS = "/api/clock";
	public final static String GET_RANDOM_CLOCK = "/api/random_clock";
	public final static String SEND_GOOD_NIGHT = "/api/oodnight";
	public final static String DISLIKE_GOODNIGHT ="/api/dislike/goodnight";
	public final static String GET_GOODNIGHT = "/api/goodnight";
	public final static String LIKE_GOODNIGHT = "/api/like/goodnight/:id_goodnight";
	public final static String SET_FACEBOOK_TOKEN = "/auth/facebook/token";
	public final static String GET_OAUTH_TOKEN = "/oauth/token";
	public final static String CREATE_USER = "/api/signup";
	public final static String GET_USER_INFOS = "/api/user_info";
	public final static String SET_USER_LANGUAGE = "/api/language";
	
}
