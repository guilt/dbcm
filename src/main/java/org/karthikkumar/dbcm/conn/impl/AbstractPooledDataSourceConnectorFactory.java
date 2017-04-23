package org.karthikkumar.dbcm.conn.impl;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import org.karthikkumar.dbcm.conn.PooledDataSourceConnectorFactory;

abstract class AbstractPooledDataSourceConnectorFactory extends AbstractPooledConnectorFactory implements PooledDataSourceConnectorFactory {

	private static final Logger logger = Logger.getLogger(AbstractPooledDataSourceConnectorFactory.class.getCanonicalName());

	 public PooledConnection getPooledConnection() {
			PooledConnection mPCon=null;
			ConnectionPoolDataSource mPDS=getPooledDataSource();
			if(mPDS!=null) {
				try{
					mPCon=mPDS.getPooledConnection();
				} catch (SQLException eSQLE) {
					logger.severe(eSQLE.getMessage());
					mPCon = null;
				}
			}
			return mPCon;
		}		 
	
}