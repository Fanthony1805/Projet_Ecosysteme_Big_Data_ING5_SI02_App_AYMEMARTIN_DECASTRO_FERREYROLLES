package com.ece_ing5app_2021.big_data_project;

public class Channel {
	private static long id;
	private static String name;
	
	public static long getId() {
		return id;
	}
	public static void setId(long id) {
		Channel.id = id;
	}
	public static String getName() {
		return name;
	}
	public static void setName(String name) {
		Channel.name = name;
	}
}
