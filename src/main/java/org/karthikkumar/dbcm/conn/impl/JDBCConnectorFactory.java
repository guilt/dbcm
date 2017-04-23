package org.karthikkumar.dbcm.conn.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import org.karthikkumar.dbcm.util.InstantiatorUtil;

/**
 * JDBC Driver based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public class JDBCConnectorFactory extends AbstractConnectorFactory {

	private static final Logger logger = Logger.getLogger(JDBCConnectorFactory.class.getCanonicalName());

	protected String mDriver=null;
	protected String mJDBCUrl=null;
	protected Properties mProp=null;
	
	public void initializeValues(Properties pProp) {
		if(pProp!=null){
			mProp=pProp;
			mDriver=mProp.getProperty("driverClassName");
			mJDBCUrl=mProp.getProperty("url");
		}
	}
	
	public void destroy() {
		mDriver=null;
		mJDBCUrl=null;
		mProp=null;
	}
	
	public Connection getConnection() {
		Connection mCon=null;
		if(mDriver!=null) {
			if(InstantiatorUtil.getInstance().createObject(mDriver)!=null) {
				try {
					mCon=DriverManager.getConnection(mJDBCUrl,mProp);
				} catch (SQLException eSQLE) {
					logger.severe(eSQLE.getMessage());
					mCon=null;
				}
			}				
		}		
		return mCon;
	}
	
	public String toString() {		
		if(mProp!=null)
			return getClass()+": "+mProp.toString();
		return getClass()+": No Properties";
	}

}