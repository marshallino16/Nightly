package com.anthony.fernandez.nightly.task.listener;

public interface OnConnectListener {
	
	public void onConnectionAccepted();
	public void onConnectionRefused(String reason);

}
