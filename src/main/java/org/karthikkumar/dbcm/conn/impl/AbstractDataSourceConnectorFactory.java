package org.karthikkumar.dbcm.conn.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.karthikkumar.dbcm.conn.DataSourceConnectorFactory;

abstract class AbstractDataSourceConnectorFactory extends AbstractConnectorFactory implements DataSourceConnectorFactory {

	private static final Logger logger = Logger.getLogger(AbstractDataSourceConnectorFactory.class.getCanonicalName());

	 public Connection getConnection() {
			Connection mCon=null;
			DataSource mDS=getDataSource();
			if(mDS!=null) {
				try{
					mCon=mDS.getConnection();
				} catch (SQLException eSQLE) {
					logger.severe(eSQLE.getMessage());
					mCon = null;
				}
			}
			return mCon;
	}
	
}
