package com.anthony.fernandez.nightly.task.listener;

import com.anthony.fernandez.nightly.model.Message;

public interface OnMessageReceive {

	public void OnMessageReceived(Message message, long conversationID);

}
