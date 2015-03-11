package com.anthony.fernandez.nightly.model;

public class AlarmClock {

	private long _id;
	private int hours;
	private int minutes;
	private int dayOfWeek;
	private Category category;
	private boolean isActive = true;

	public AlarmClock(int hours, int minutes, int dayOfWeek){
		super();
		this.hours = hours;
		this.minutes = minutes;
		this.dayOfWeek = dayOfWeek;
	}

	public AlarmClock(int hours, int minutes){
		super();
		this.hours = hours;
		this.minutes = minutes;
	}

	public AlarmClock(int dayOfWeek){
		super();
		this.dayOfWeek = dayOfWeek;
	}

	public AlarmClock(){
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
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}
	/**
	 * @param hours the hours to set
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}
	/**
	 * @return the minutes
	 */
	public int getMinutes() {
		return minutes;
	}
	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	/**
	 * @return the daysOfWeek
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	/**
	 * @param daysOfWeek the daysOfWeek to set
	 */
	public void setDayOfWeek(int daysOfWeek) {
		this.dayOfWeek = daysOfWeek;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
