package com.ece_ing5app_2021.big_data_project;

public class Message {
	private final long messageID;
	private final long channelID;
	private final String content;
	private final String author;
	private final String created_at;
	private final String message;
	
	public Message(long messageID, long channelID, String content, String author, String created_at, String message) {
		super();
		this.messageID = messageID;
		this.channelID = channelID;
		this.content = content;
		this.author = author;
		this.created_at = created_at;
		this.message = message;
	}
	public long getMessageID() {
		return messageID;
	}
	public long getChannelID() {
		return channelID;
	}
	public String getContent() {
		return content;
	}
	public String getAuthor() {
		return author;
	}
	public String getCreated_at() {
		return created_at;
	}
	public String getMessage() {
		return message;
	}
}
