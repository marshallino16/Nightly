package com.anthony.fernandez.nightly.task.listener;

public interface OnAccountCreated {
	
	public void OnAccountCreatedSucced();
	public void OnAccountCreatedFailed(String reason);

}
