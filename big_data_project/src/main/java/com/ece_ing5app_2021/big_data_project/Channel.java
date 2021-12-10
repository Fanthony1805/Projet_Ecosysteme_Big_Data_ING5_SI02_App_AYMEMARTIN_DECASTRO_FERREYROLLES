package com.ece_ing5app_2021.big_data_project;

public class Channel {
	private final long id;
	private final String name;
	private final String owners;
	
	public Channel(long id, String name, String owners) {
		super();
		this.id = id;
		this.name = name;
		this.owners = owners;
	}
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getOwners() {
		return owners;
	}
}
