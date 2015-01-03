package com.anthony.fernandez.nightly.enums;

public enum MessageDirection {
	
	INCOMMING(0),
	OUTCOMMING(1);
	
	public int status;
	
	private MessageDirection(int status) {
		this.status = status;
	}

}
