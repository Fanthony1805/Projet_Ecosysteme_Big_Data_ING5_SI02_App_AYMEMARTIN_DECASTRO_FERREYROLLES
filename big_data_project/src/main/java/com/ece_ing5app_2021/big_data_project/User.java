package com.ece_ing5app_2021.big_data_project;

public class User {
	private final long userID;
	private final String name;
	private final String email;
	private final String password;
	private final String avatarID;
	
	public User(long userID, String name, String email, String password, String avatarID) {
		super();
		this.userID = userID;
		this.name = name;
		this.email = email;
		this.password = password;
		this.avatarID = avatarID;
	}
	public long getUserID() {
		return userID;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getAvatarID() {
		return avatarID;
	}
}
