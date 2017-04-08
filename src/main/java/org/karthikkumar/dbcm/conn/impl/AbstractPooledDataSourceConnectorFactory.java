package org.karthikkumar.dbcm.conn.impl;

import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import org.karthikkumar.dbcm.conn.PooledDataSourceConnectorFactory;

abstract class AbstractPooledDataSourceConnectorFactory extends AbstractPooledConnectorFactory implements PooledDataSourceConnectorFactory {

	 public PooledConnection getPooledConnection() {
			PooledConnection mPCon=null;
			ConnectionPoolDataSource mPDS=getPooledDataSource();
			if(mPDS!=null) {
				try{
					mPCon=mPDS.getPooledConnection();
				} catch (SQLException eSQLE) {
					eSQLE.printStackTrace(); 
				}
			}
			return mPCon;
		}		 
	
}