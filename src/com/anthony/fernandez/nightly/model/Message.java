package com.anthony.fernandez.nightly.model;

import com.anthony.fernandez.nightly.enums.MessageDirection;

public class Message {

	private long _id;
	private long dateSend;
	private long dateReceived;
	private boolean isReceived;
	private boolean isPending = false; //0 = false, 1 = true IF true MUST BE RESENT, ONLY USE IN LOCAL
	private String message;
	private MessageDirection direction;
	
	public Message(long _id, String message){
		this._id = _id;
		this.message = message;
	}
	
	public Message(String message){
		super();
		this.message = message;
	}
	
	public Message() {
		super();
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
	 * @return the dateSend
	 */
	public long getDateSend() {
		return dateSend;
	}
	/**
	 * @param dateSend the dateSend to set
	 */
	public void setDateSend(long dateSend) {
		this.dateSend = dateSend;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public MessageDirection getDirection() {
		return direction;
	}

	public void setDirection(MessageDirection direction) {
		this.direction = direction;
	}

	public boolean isPending() {
		return isPending;
	}

	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}

	/**
	 * @return the dateReceived
	 */
	public long getDateReceived() {
		return dateReceived;
	}

	/**
	 * @param dateReceived the dateReceived to set
	 */
	public void setDateReceived(long dateReceived) {
		this.dateReceived = dateReceived;
	}

	/**
	 * @return the isReceived
	 */
	public boolean isReceived() {
		return isReceived;
	}

	/**
	 * @param isReceived the isReceived to set
	 */
	public void setReceived(boolean isReceived) {
		this.isReceived = isReceived;
	}
}
