package com.ece_ing5app_2021.big_data_project;

public class User {
	private static long userID = 0;
	private static String name = "";
	private static String email = "";
	private static String password = "";
	
	public static long getUserID() {
		return userID;
	}
	public static void setUserID(long userID) {
		User.userID = userID;
	}
	public static String getName() {
		return name;
	}
	public static void setName(String name) {
		User.name = name;
	}
	public static String getEmail() {
		return email;
	}
	public static void setEmail(String email) {
		User.email = email;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		User.password = password;
	}
}
