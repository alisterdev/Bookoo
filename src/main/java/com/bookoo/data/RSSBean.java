package com.bookoo.data;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

import org.apache.commons.validator.UrlValidator;

/**
 * RSS Feed item Bean
 * 
 * @author Darrel-Day Guerrero
 * 
 */
@Named("rssBean")
@RequestScoped
public class RSSBean implements Serializable {

	private static final long serialVersionUID = -5683589707782433961L;

	private long id;
	private String title;
	private String description;
	private String link;
	private String author;
	private boolean isAvailable;
	private String guid;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((guid == null) ? 0 : guid.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (isAvailable ? 1231 : 1237);
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RSSBean other = (RSSBean) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (guid == null) {
			if (other.guid != null)
				return false;
		} else if (!guid.equals(other.guid))
			return false;
		if (id != other.id)
			return false;
		if (isAvailable != other.isAvailable)
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RSSBean [id=" + id + ", title=" + title + ", description="
				+ description + ", link=" + link + ", author=" + author
				+ ", isAvailable=" + isAvailable + ", guid=" + guid + "]";
	}

	/**
	 * Validate RSS Title AJAX
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateRssTitle(FacesContext fc, UIComponent c, Object value) {
		if (!((String) value)
				.matches("^([a-zA-Z0-9]{1,}[\\-\\s]{0,1}[A-Za-z0-9]{0,}){3,35}$")) {
			throw new ValidatorException(
					new FacesMessage(
							"Please use between 3 and 50 characters, alphanumeric only."));
		}
	}

	/**
	 * Validate Download Link AJAX, not the best validation for RSS
	 * 
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateRssLink(FacesContext fc, UIComponent c, Object value) {
		String url = ((String) value).trim();
		UrlValidator urlValidator = new UrlValidator();
		if (!urlValidator.isValid(url)) {
			throw new ValidatorException(new FacesMessage(
					"Please enter a valid RSS URL"));
		}
	}
}