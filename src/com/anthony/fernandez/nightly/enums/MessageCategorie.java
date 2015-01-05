package com.anthony.fernandez.nightly.enums;

public enum MessageCategorie {
	
	HUMOUR("Humour", 0),
	ROMANCE("Romance", 1),
	HORREUR("Horreur", 2),
	MEDEVIAL("Médiéval", 3),
	DECALE("Décalé", 4),
	POESIE("Poésie", 5),
	CLASSIQUE("Classique", 6);
	
	public String type;
	public int serverID;
	
	MessageCategorie(String type, int serverID){
		this.type = type;
		this.serverID = serverID;
	}

}
