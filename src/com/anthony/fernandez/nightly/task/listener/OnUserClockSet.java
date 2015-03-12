package com.anthony.fernandez.nightly.task.listener;

public interface OnUserClockSet {
	
	public void OnClockSet();
	public void OnClockSetFailed(String reason);

}
