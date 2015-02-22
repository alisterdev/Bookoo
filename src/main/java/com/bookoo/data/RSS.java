package com.bookoo.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holds a RSS feed broadcasted from the RSS link. Each feed consists of a
 * title, link, description, language, copyright and publish date. RSS links
 * must have at least one or more of these attributes in order to be parsed
 * correctly. Links with extra XML tags will cause an error.
 * 
 * @author Darrel-Day Guerrero
 * 
 */
public class RSS implements Serializable {

	private static final long serialVersionUID = -5772662626500409104L;
	
	private long id;
	private String title;
	private String link;
	private String description;
	private String language;
	private String copyright;
	private String pubDate;
	
	// List of RSS feed entries
	private final ArrayList<RSSBean> entries = new ArrayList<RSSBean>();

	public RSS() {
		super();
	}

	public RSS(String title, String link, String description, String language,
			String copyright, String pubDate) {
		super();
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
		this.copyright = copyright;
		this.pubDate = pubDate;
	}

	public ArrayList<RSSBean> getMessages() {
		return entries;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	@Override
	public String toString() {
		return "Feed [copyright=" + copyright + ", description=" + description
				+ ", language=" + language + ", link=" + link + ", pubDate="
				+ pubDate + ", title=" + title + "]";
	}

}
