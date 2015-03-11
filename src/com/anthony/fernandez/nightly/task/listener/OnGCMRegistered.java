package com.anthony.fernandez.nightly.task.listener;

public interface OnGCMRegistered {
	
	public void OnGCMRegister();
	public void OnGCMRegisterFailed(String reason);

}
