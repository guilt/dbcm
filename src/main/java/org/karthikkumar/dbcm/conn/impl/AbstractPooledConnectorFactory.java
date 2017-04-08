package org.karthikkumar.dbcm.conn.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.PooledConnection;

import org.karthikkumar.dbcm.conn.PooledConnectorFactory;

abstract class AbstractPooledConnectorFactory extends AbstractConnectorFactory implements PooledConnectorFactory {

	 public Connection getConnection() {
		Connection mCon=null;
		PooledConnection mPCon=getPooledConnection();
		if(mPCon!=null) {
			try{
				mCon=mPCon.getConnection();
			} catch (SQLException eSQLE) {
				eSQLE.printStackTrace(); 
			}
		}
		return mCon;
	}
	
}
