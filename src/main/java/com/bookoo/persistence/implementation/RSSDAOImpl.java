package com.bookoo.persistence.implementation;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import com.bookoo.data.RSS;
import com.bookoo.data.RSSBean;
import com.bookoo.persistence.interfaces.RSSDAO;
import com.bookoo.util.RSSFeedParser;

/**
 * RSS Data Access Routines implementation for RSS Beans.
 * 
 * @author Darrel-Day Guerrero
 * 
 */
@Named("rssAction")
@RequestScoped
public class RSSDAOImpl implements RSSDAO, Serializable {

	private static final long serialVersionUID = 4411713582317191714L;

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	@Inject
	private RSSBean rss;

	private RSS feed;
	private RSSFeedParser parser;

	public RSSDAOImpl() {
		super();
	}

	/**
	 * Retrieves all news feeds from the database
	 * 
	 */
	@Override
	public ArrayList<RSSBean> getAllNewsFeeds() throws SQLException {

		ArrayList<RSSBean> results = new ArrayList<RSSBean>();

		String preparedQuery = "SELECT * FROM NEWSFEED";

		if (ds == null)
			throw new SQLException("Can't get data source");

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {

			try (ResultSet resultSet = pStatement.executeQuery()) {
				while (resultSet.next()) {
					RSSBean newsFeedBean = new RSSBean();
					newsFeedBean.setId(resultSet.getLong("FEED_ID"));
					newsFeedBean.setTitle(resultSet.getString("FEED_TITLE"));
					newsFeedBean.setLink(resultSet.getString("FEED_LINK"));
					newsFeedBean
							.setAvailable(resultSet.getBoolean("AVAILABLE"));
					results.add(newsFeedBean);
				}
			}
		}

		return results;
	}

	/**
	 * Retrieves RSS feeds that are available for broadcast on home page
	 */
	@Override
	public ArrayList<RSSBean> getRssFeeds() throws SQLException {

		String url = "";
		ArrayList<RSS> rssList = new ArrayList<RSS>();

		if (ds == null)
			throw new SQLException("Can't get data source");

		// RSS must be available in order to display
		String preparedQuery = "SELECT * FROM NEWSFEED WHERE AVAILABLE = 1";

		try (Connection connection = ds.getConnection();
				PreparedStatement pStatement = connection
						.prepareStatement(preparedQuery);) {
			try (ResultSet rs = pStatement.executeQuery()) {
				while (rs.next()) {
					RSS rss = new RSS();
					rss.setLink(rs.getString("FEED_LINK"));
					rssList.add(rss);
				}
			}
		}

		// Randomize the list
		Collections.shuffle(rssList);

		// Always select first RSS link from the randomized list
		url = rssList.get(0).getLink();

		parser = new RSSFeedParser(url);
		feed = parser.readFeed();
		return feed.getMessages();
	}

	@Override
	public int insertRSS() throws SQLException {

		String preparedQuery = "INSERT INTO NEWSFEED (FEED_TITLE, FEED_LINK, AVAILABLE)"
				+ "VALUES (?, ?, 0)";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setString(1, rss.getTitle());
			ps.setString(2, rss.getLink());

			return ps.executeUpdate();

		}
	}

	@Override
	public int updateRSS(RSSBean rss) throws SQLException {
		String preparedQuery = "UPDATE NEWSFEED SET FEED_TITLE = ?, FEED_LINK = ?, AVAILABLE = 1 WHERE FEED_ID = ?;";

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {

			ps.setString(1, rss.getTitle());
			ps.setString(2, rss.getLink());
			ps.setLong(3, rss.getId());

			return ps.executeUpdate();
		}

	}

	@Override
	public int toggleAvailability(boolean availability, long id)
			throws SQLException {
		String preparedQuery = "UPDATE NEWSFEED SET AVAILABLE = ? WHERE FEED_ID = ?";

		if (ds == null)
			throw new SQLException("Can't get datasource.");

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery);) {
			ps.setBoolean(1, availability);
			ps.setLong(2, id);

			return ps.executeUpdate();
		}

	}

	@Override
	public int removeRSS() throws SQLException {
		return 0;
	}

	public String checkRssInsert() throws SQLException {

		long result = this.insertRSS();

		if (result == 1)
			return "null";
		else
			return "null";
	}

	public String checkRssEdit(RSSBean rss) throws SQLException {

		// For some reason, update only works when passed as a parameter.
		// Application loses state of the Injected RSS before commandButton
		// action is executed.
		long result = this.updateRSS(rss);

		if (result == 1)
			return "null";
		else
			return "null";
	}

	public String checkRssAvailability(RSSBean rss) throws SQLException {
		int test = 0;
		if (rss.isAvailable()) {
			test = toggleAvailability(false, rss.getId());
		} else {
			test = toggleAvailability(true, rss.getId());
		}
		if (test == 1) {
			return null;
		} else
			return null;
	}

}
