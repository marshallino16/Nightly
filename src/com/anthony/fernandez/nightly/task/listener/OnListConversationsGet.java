package com.anthony.fernandez.nightly.task.listener;

import java.util.ArrayList;

import com.anthony.fernandez.nightly.model.Conversation;

public interface OnListConversationsGet {

	public void OnListConversationGet(ArrayList<Conversation> listConversation);
}
