package com.ece_ing5app_2021.big_data_project;

public class Counter {
	private static int nb_user = 0;
	private static int nb_channel = 0;
	private static int nb_message = 0;

	public static int getNb_user() {
		return nb_user;
	}

	public static void setNb_user(int nb_user) {
		Counter.nb_user = nb_user;
	}

	public static int getNb_channel() {
		return nb_channel;
	}

	public static void setNb_channel(int nb_channel) {
		Counter.nb_channel = nb_channel;
	}

	public static int getNb_message() {
		return nb_message;
	}

	public static void setNb_message(int nb_message) {
		Counter.nb_message = nb_message;
	}
}
