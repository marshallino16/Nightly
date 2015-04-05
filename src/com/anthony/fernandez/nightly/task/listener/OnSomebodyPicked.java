package com.anthony.fernandez.nightly.task.listener;

public interface OnSomebodyPicked {
	
	public void OnSomebodyPick(String _idClock, int hour, int minutes, String categoryName);
	public void OnSomebodyPickFailed(String reason);
}
