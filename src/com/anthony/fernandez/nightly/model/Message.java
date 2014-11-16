package com.anthony.fernandez.nightly.model;

public class Message {

	private long _id;
	private long dateSend;
	private int numberLove;
	private String message;
	
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
	 * @return the numberLove
	 */
	public int getNumberLove() {
		return numberLove;
	}
	/**
	 * @param numberLove the numberLove to set
	 */
	public void setNumberLove(int numberLove) {
		this.numberLove = numberLove;
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
}
