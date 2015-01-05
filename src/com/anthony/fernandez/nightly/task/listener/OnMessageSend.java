package com.anthony.fernandez.nightly.task.listener;

public interface OnMessageSend {
	
	public void OnMessageSendSucced();
	public void OnMessageSendFailed(String reason);

}
