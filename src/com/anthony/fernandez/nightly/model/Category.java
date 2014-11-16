package com.anthony.fernandez.nightly.model;

public class Category {

	private long _id;
	private long numberMessages;
	private String description;

	public Category(long _id, String description){
		super();
		this._id = _id;
		this.description = description;
	}

	public Category(String description){
		super();
		this.description = description;
	}

	public Category(){
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
	 * @return the numberMessages
	 */
	public long getNumberMessages() {
		return numberMessages;
	}
	/**
	 * @param numberMessages the numberMessages to set
	 */
	public void setNumberMessages(long numberMessages) {
		this.numberMessages = numberMessages;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


}
