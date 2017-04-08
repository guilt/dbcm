package org.karthikkumar.dbcm.conn.impl;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.karthikkumar.dbcm.conn.DataSourceConnectorFactory;

abstract class AbstractDataSourceConnectorFactory extends AbstractConnectorFactory implements DataSourceConnectorFactory {

	 public Connection getConnection() {
			Connection mCon=null;
			DataSource mDS=getDataSource();
			if(mDS!=null) {
				try{
					mCon=mDS.getConnection();
				} catch (SQLException eSQLE) {
					eSQLE.printStackTrace(); 
				}
			}
			return mCon;
	}
	
}
