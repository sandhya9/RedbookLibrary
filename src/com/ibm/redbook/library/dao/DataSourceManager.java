package com.ibm.redbook.library.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.ibm.redbook.library.exceptions.DataSourceException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Convenience class used to get access to the different data sources used in the application.
 */
public enum DataSourceManager {
	BOOK("DefaultDatasource"), MEMBER("jdbc/Members"), STYLE("style");
	Logger log = Logger.getLogger(getClass().getName());
	private String dsName = "";

	private DataSourceManager(String dsName) {
		this.dsName = dsName;
	}

	/**
	 * Gets the data source
	 * @return the data source
	 * @throws DataSourceException if the data source cannot be returned (due to InitialContext throwing a NamingException) 
	 */
	public DataSource getDs() throws DataSourceException {
		DataSource ds = null;
		InitialContext initContext = null;
		try {
			initContext = new InitialContext();
			Context env = (Context) initContext.lookup("java:comp/env");
			//$ANALYSIS-IGNORE
			ds = (javax.sql.DataSource) env.lookup(dsName);
		} catch (NamingException e) {
			log.log(Level.SEVERE, "failed to get the context. Error: " + e.getMessage(), e);
			throw new DataSourceException("Failed to get a datasource for "
					+ dsName, e);
		}
		return ds;
	}

	/**
	 * Returns the name of the data source, this is not the enum value, but the value used to get the data source from JNDI
	 * @return the data source name
	 */
	public String getDsName() {
		return dsName;
	}
}
