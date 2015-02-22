package com.bookoo.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Genre bean used to store genres from database and provide them to user in
 * menus and lists of genres
 * 
 * @author Alex Ilea
 */
@Named
@RequestScoped
public class GenreBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String genre;

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Resource(name = "jdbc/g2w14")
	private DataSource ds;

	public GenreBean() {
		super();
	}

	public ArrayList<GenreBean> getAllGenres() throws SQLException {

		ArrayList<GenreBean> genreList = new ArrayList<GenreBean>();

		if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/g2w14");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

		String preparedQuery = "SELECT * FROM GENRES";

		try (Connection connection = ds.getConnection();
				PreparedStatement ps = connection
						.prepareStatement(preparedQuery)) {

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					GenreBean genre = new GenreBean();
					genre.setGenre(rs.getString("GENRE"));
					genreList.add(genre);
				}
				return genreList;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
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
		GenreBean other = (GenreBean) obj;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GenreBean [genre=" + genre + "]";
	}

}
