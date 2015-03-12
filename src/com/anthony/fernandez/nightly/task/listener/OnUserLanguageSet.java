package com.anthony.fernandez.nightly.task.listener;

public interface OnUserLanguageSet {
	
	public void OnLanguageSet();
	public void OnLanguageSetFailed(String reason);
	
}
