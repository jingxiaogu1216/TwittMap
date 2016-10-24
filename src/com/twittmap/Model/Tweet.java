package com.twittmap.Model;

public class Tweet {
	private long tweetId;
	private double lag;
	private double longi;
	private String userName;
	private String text;

	public Tweet(double lag, double longi, String userName, String text) {
		super();
		this.lag = lag;
		this.longi = longi;
		this.userName = userName;
		this.text = text;
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public double getLag() {
		return lag;
	}

	public void setLag(double lag) {
		this.lag = lag;
	}

	public double getLongi() {
		return longi;
	}

	public void setLongi(double longi) {
		this.longi = longi;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
