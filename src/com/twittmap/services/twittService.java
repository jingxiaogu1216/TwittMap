package com.twittmap.services;

import java.sql.SQLException;

import org.json.JSONArray;

import com.twittmap.DAO.TweetDAO;
import com.twittmap.Model.Tweet;

public class twittService {

	public static void insertTweetFromStream(Tweet tweet) throws SQLException {
		TweetDAO.createTweet(tweet);
	}

	public static void resetTweetDB() throws SQLException {
		TweetDAO.clearDB();
	}

	public static JSONArray queryTweetsFromDB()
			throws SQLException {
		JSONArray tweets = TweetDAO.getTweetsFromDB();
		return tweets;
	}
}
