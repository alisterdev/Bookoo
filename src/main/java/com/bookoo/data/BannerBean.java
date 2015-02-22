package com.bookoo.data;

import java.io.Serializable;
import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import com.bookoo.persistence.implementation.BannerDAOImpl;

/**
 * Banner Bean representing ads on home page
 * @author Jolan Cornevin, Darrel-Day Guerrero
 * 
 */

@Named("bannerBean")
@RequestScoped
public class BannerBean implements Serializable {

	private static final long serialVersionUID = 1475520952659809785L;

	private long id;
	private String image;
	private String description;
	private String link;
	private boolean isAvailable;

	@Inject
	BannerDAOImpl bdi;

	public BannerBean() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public void approuved(BannerBean banner) throws SQLException {
		if (banner.isAvailable)
			banner.setAvailable(false);
		else
			banner.setAvailable(true);

		bdi = new BannerDAOImpl();
		bdi.updateBanner(banner);
		bdi.getCurrentBanners();
	}

	/**
	 * Validate Ad's description
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateDescription(FacesContext fc, UIComponent c, Object value) {
		if ((((String) value).trim().length() > 40)) {
			throw new ValidatorException(new FacesMessage(
					"Your description size must be under 40 character"));
		}
		if (((String) value).isEmpty()) {
			throw new ValidatorException(new FacesMessage(
					"Your must put a description"));
		}
	}

	/**
	 * Validates Ad Link
	 * @param fc
	 * @param c
	 * @param value
	 */
	public void validateLink(FacesContext fc, UIComponent c, Object value) {

		if ((((String) value).trim().length() > 40)) {
			throw new ValidatorException(new FacesMessage(
					"Your link size must be under 40 character"));
		} else if (((String) value).isEmpty()) {
			throw new ValidatorException(new FacesMessage(
					"Your must put a link"));

		} else if (!((String) value)
				.matches("\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]")) {
			throw new ValidatorException(new FacesMessage(
					"Your must put a valide link"));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + (isAvailable ? 1231 : 1237);
		result = prime * result + ((link == null) ? 0 : link.hashCode());
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
		BannerBean other = (BannerBean) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;

		if (id != other.id)
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (isAvailable != other.isAvailable)
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BannerBean [id=" + id + ", image=" + image + ", description="
				+ description + ", link=" + link + ", isAvailable="
				+ isAvailable + "]";
	}

}
