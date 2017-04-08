package org.karthikkumar.dbcm.conn.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.karthikkumar.dbcm.util.Instantiator;

/**
 * JDBC Driver based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public class JDBCConnectorFactory extends AbstractConnectorFactory {
	
	private String mDriver=null;
	private String mJDBCUrl=null;
	private Properties mProp=null;
	
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
			if(Instantiator.getInstance().createObject(mDriver)!=null) {
				try {
					mCon=DriverManager.getConnection(mJDBCUrl,mProp);					
				} catch (SQLException eSQLE) {
					eSQLE.printStackTrace();
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