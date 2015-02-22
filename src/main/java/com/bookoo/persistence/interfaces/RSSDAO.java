package com.bookoo.persistence.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import com.bookoo.data.RSSBean;

public interface RSSDAO {

	public ArrayList<RSSBean> getRssFeeds() throws SQLException;
	
	public int insertRSS() throws SQLException;
	
	public int removeRSS() throws SQLException;

	public ArrayList<RSSBean> getAllNewsFeeds() throws SQLException;

	public int updateRSS(RSSBean rss) throws SQLException;

	public int toggleAvailability(boolean availability, long id) throws SQLException;
	
	
}
