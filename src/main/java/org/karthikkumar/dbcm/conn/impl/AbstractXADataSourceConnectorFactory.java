package org.karthikkumar.dbcm.conn.impl;

import java.sql.SQLException;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

import org.karthikkumar.dbcm.conn.XADataSourceConnectorFactory;

abstract class AbstractXADataSourceConnectorFactory extends AbstractXAConnectorFactory implements XADataSourceConnectorFactory {

	 public XAConnection getXAConnection() {
			XAConnection mXACon=null;
			XADataSource mXADS=getXADataSource();
			if(mXADS!=null) {
				try{
					mXACon=mXADS.getXAConnection();
				} catch (SQLException eSQLE) {
					eSQLE.printStackTrace(); 
				}
			}
			return mXACon;
		}		 
		
}