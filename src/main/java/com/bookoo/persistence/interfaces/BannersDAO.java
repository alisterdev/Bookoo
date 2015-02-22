package com.bookoo.persistence.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import com.bookoo.data.BannerBean;

/**
 * Data Access routine interface for the Banner data bean.
 * 
 * @author Jolan Cornevin
 * @author Alex Ilea
 * @version 1.0
 */
public interface BannersDAO {

	public int insertBanner(BannerBean banner) throws SQLException;

	public int updateBanner(BannerBean banner) throws SQLException;

	public ArrayList<BannerBean> getAllBanners() throws SQLException;

	public ArrayList<BannerBean> getCurrentBanners() throws SQLException;
	
	public BannerBean getLeftCurrentBanner() throws SQLException;
	public BannerBean getRightCurrentBanner() throws SQLException;

	public BannerBean getBannerById(long id) throws SQLException;

	int updateBannerBean(int id) throws SQLException;

}
