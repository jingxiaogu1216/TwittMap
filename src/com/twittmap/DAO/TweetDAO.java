package com.twittmap.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.twittmap.Model.Tweet;

public class TweetDAO {
	private static String URL = "jdbc:mysql://twittmap.cypqw0c5lsxk.us-east-1.rds.amazonaws.com:3306/mydb?connectTimeout=2000";
	private static String USER = "root";
	private static String PASS = "123456789";
	private static int queryLimit = 30;
	private final static Logger logger = Logger.getLogger(Class.class);

	/*******
	 * Create Tweet
	 * 
	 * @throws SQLException
	 **********/
	public static int createTweet(Tweet tweet) throws SQLException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASS);

			String query = "INSERT into Tweet (lag, longi, userName, text) values (?,?,?,?);";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			ps.setDouble(1, tweet.getLag());
			ps.setDouble(2, tweet.getLongi());
			ps.setString(3, tweet.getUserName());
			ps.setString(4, tweet.getText());

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException(
						"Creating tweet failed, no rows affected.");
			}

			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				tweet.setTweetId(rs.getInt(1));
			} else {
				throw new SQLException(
						"Creating tweet failed, no rows affected.");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		}
		return 0;

	}

	/*******
	 * 
	 * Query Tweets
	 * 
	 * @throws SQLException
	 * **********/
	public static JSONArray getTweetsFromDB() throws SQLException {
		JSONArray tweets = new JSONArray();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASS);

			String query = "SELECT * from Tweet ORDER BY tweetId DESC LIMIT "
					+ queryLimit;
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery(query);

			while (rs.next()) {
				JSONObject tweet = new JSONObject();
				tweet.put("tweetId", rs.getInt("tweetId"));
				tweet.put("lag", rs.getDouble("lag"));
				tweet.put("longi", rs.getDouble("longi"));
				tweet.put("userName", rs.getString("userName"));
				tweet.put("text", rs.getString("text"));
				tweets.put(tweet);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		}
		return tweets;
	}

	/*******
	 * Clear DB
	 * 
	 * @throws SQLException
	 **********/
	public static void clearDB() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASS);

			String query = "DELETE from Tweet";
			ps = conn.prepareStatement(query);

			int affectedRows = ps.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException(
						"Deleting tweet failed, no rows affected.");
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			if (conn != null)
				conn.close();
		}
	}
}
