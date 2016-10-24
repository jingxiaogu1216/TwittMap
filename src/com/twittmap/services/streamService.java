package com.twittmap.services;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.twittmap.Model.Tweet;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.*;

public class streamService {
	private static final String consumerKey = "0QXj6VbWYB13HAe5suhx1uJJm";
	private static final String consumerSecret = "ZvQlBw6bCYyRFZR1eo4BBmonDjJGe2RIrtaTQeSpGN1k9dqsWg";
	private static final String oAuthAccessToken = "2796804192-7lLIw7X5XX1tQTHqqzgQuDiFBph7uM7sew66Z6B";
	private static final String oAuthAccessTokenSecret = "FqxOBJMgo5AAA5kMrInsc1FCKKCY7fxDmtzWYDB7WqTNB";

	private static ConfigurationBuilder cb;
	private static TwitterStream twitterStream;
	private static StatusListener listener;
	private static FilterQuery filtre;
	private static boolean initialized = false;
	
	private final static Logger logger = Logger.getLogger(Class.class);

	public static void startStream(String keyWord) {
		if (!initialized) {
			initialization();
			initialized = true;
		}
		String[] keywordsArray = { keyWord };
		filtre.track(keywordsArray);
		// begin streaming
		twitterStream.filter(filtre);
	}

	private static void initialization() {
		// cb
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(consumerKey);
		cb.setOAuthConsumerSecret(consumerSecret);
		cb.setOAuthAccessToken(oAuthAccessToken);
		cb.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
		// twitterStream
		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		// listener
		listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				if (status.getGeoLocation() != null) {					
					Tweet tweet = new Tweet(status.getGeoLocation()
							.getLatitude(), status.getGeoLocation()
							.getLongitude(), status.getUser().getScreenName(),
							status.getText());
					try {
						twittService.insertTweetFromStream(tweet);
					} catch (SQLException e) {
						logger.error(e.getMessage());
					}
				}
			}

			@Override
			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:"
						+ statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:"
						+ numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId
						+ " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		// add listener
		twitterStream.addListener(listener);
		// filterQuery
		filtre = new FilterQuery();
	}

	public static void cleanUp(boolean reset) {
		if (twitterStream != null) {
			twitterStream.cleanUp();
		}
		if (!reset) return;
		try {
			twittService.resetTweetDB();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

}