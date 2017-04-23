package org.karthikkumar.dbcm.conn.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.PooledConnection;

import org.karthikkumar.dbcm.conn.PooledConnectorFactory;

abstract class AbstractPooledConnectorFactory extends AbstractConnectorFactory implements PooledConnectorFactory {

	private static final Logger logger = Logger.getLogger(AbstractPooledConnectorFactory.class.getCanonicalName());

	 public Connection getConnection() {
		Connection mCon=null;
		PooledConnection mPCon=getPooledConnection();
		if(mPCon!=null) {
			try{
				mCon=mPCon.getConnection();
			} catch (SQLException eSQLE) {
				logger.severe(eSQLE.getMessage());
				mCon = null;
			}
		}
		return mCon;
	}
	
}
