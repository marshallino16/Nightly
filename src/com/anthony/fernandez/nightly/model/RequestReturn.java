package com.anthony.fernandez.nightly.model;

public class RequestReturn {

	public String json;
	public int code;
	
	public RequestReturn(String json, int code) {
		this.json = json;
		this.code = code;
	}
}
