package com.anthony.fernandez.nightly.task.listener;

public interface OnGoodNightReceive {
	
	public void OnGoodNightReceived(String message, int categorieID, long dateReceive, long expeditorID);

}
