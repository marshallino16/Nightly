package com.anthony.fernandez.nightly.model;

import com.anthony.fernandez.nightly.enums.CountryPhoneCode;

public class Contact {
	
	private long _id;
	private boolean isInfosVisible = false;
	private boolean gender = false;
	private String firstname;
	private String lastname;
	private String phoneNumber;
	private CountryPhoneCode countryCode;
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
	 * @return the isInfosVisible
	 */
	public boolean isInfosVisible() {
		return isInfosVisible;
	}
	/**
	 * @param isInfosVisible the isInfosVisible to set
	 */
	public void setInfosVisible(boolean isInfosVisible) {
		this.isInfosVisible = isInfosVisible;
	}
	/**
	 * @return the gender
	 */
	public boolean isGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the countryCode
	 */
	public CountryPhoneCode getCountryCode() {
		return countryCode;
	}
	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(CountryPhoneCode countryCode) {
		this.countryCode = countryCode;
	}
}
