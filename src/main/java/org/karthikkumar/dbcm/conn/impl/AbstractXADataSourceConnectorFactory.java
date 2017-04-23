package org.karthikkumar.dbcm.conn.impl;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.XAConnection;
import javax.sql.XADataSource;

import org.karthikkumar.dbcm.conn.XADataSourceConnectorFactory;

abstract class AbstractXADataSourceConnectorFactory extends AbstractXAConnectorFactory implements XADataSourceConnectorFactory {

	private static final Logger logger = Logger.getLogger(AbstractXADataSourceConnectorFactory.class.getCanonicalName());

	 public XAConnection getXAConnection() {
			XAConnection mXACon=null;
			XADataSource mXADS=getXADataSource();
			if(mXADS!=null) {
				try{
					mXACon=mXADS.getXAConnection();
				} catch (SQLException eSQLE) {
					logger.severe(eSQLE.getMessage());
					mXACon=null;
				}
			}
			return mXACon;
		}		 
		
}