package com.anthony.fernandez.nightly.enums;

public enum DaysOfWeek {
	
	SAMEDI("Samedi", 0),
	DIMANCHE("Dimanche", 1),
	LUNDI("Lundi", 2),
	MARDI("Mardi", 3),
	MERCREDI("Mercredi", 4),
	JEUDI("Jeudi", 5),
	VENDREDI("Vendredi", 6);
	
	public String nameOfDay;
	public int numOfDay;
	
	DaysOfWeek(String name, int num){
		this.nameOfDay = name;
		this.numOfDay = num;
	}

}
