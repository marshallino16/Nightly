package com.anthony.fernandez.nightly.model;

import java.util.ArrayList;

public class Conversation {

	private long _id;
	private long dateCreation;
	private Contact owner;
	private Contact participant;
	private boolean isBlackListed;
	private boolean isModerated;
	private boolean isRead = false;
	private ArrayList<Message> listMessagesReceived;
	private ArrayList<Message> listMessagesSent;

	public Conversation(long _id){
		super();
		this._id = _id;
		this.listMessagesReceived = new ArrayList<Message>();
		this.listMessagesSent = new ArrayList<Message>();
	}

	public Conversation(long _id, long dateCreation){
		super();
		this._id = _id;
		this.dateCreation = dateCreation;
		this.listMessagesReceived = new ArrayList<Message>();
		this.listMessagesSent = new ArrayList<Message>();
	}

	/**
	 * @return the _id
	 */
	public long get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(long _id) {
		this._id = _id;
	}
	/**
	 * @return the dateCreation
	 */
	public long getDateCreation() {
		return dateCreation;
	}
	/**
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation(long dateCreation) {
		this.dateCreation = dateCreation;
	}
	/**
	 * @return the owner
	 */
	public Contact getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Contact owner) {
		this.owner = owner;
	}
	/**
	 * @return the participant
	 */
	public Contact getParticipant() {
		return participant;
	}
	/**
	 * @param participant the participant to set
	 */
	public void setParticipant(Contact participant) {
		this.participant = participant;
	}
	/**
	 * @return the isBlackListed
	 */
	public boolean isBlackListed() {
		return isBlackListed;
	}
	/**
	 * @param isBlackListed the isBlackListed to set
	 */
	public void setBlackListed(boolean isBlackListed) {
		this.isBlackListed = isBlackListed;
	}
	/**
	 * @return the isModerated
	 */
	public boolean isModerated() {
		return isModerated;
	}
	/**
	 * @param isModerated the isModerated to set
	 */
	public void setModerated(boolean isModerated) {
		this.isModerated = isModerated;
	}
	/**
	 * @return the listMessages
	 */
	public ArrayList<Message> getListMessages() {
		return listMessagesReceived;
	}
	/**
	 * @param listMessages the listMessages to set
	 */
	public void setListMessages(ArrayList<Message> listMessages) {
		this.listMessagesReceived = listMessages;
	}

	public ArrayList<Message> getListMessagesSent() {
		return listMessagesSent;
	}

	public void setListMessagesSent(ArrayList<Message> listMessagesSent) {
		this.listMessagesSent = listMessagesSent;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}


}
