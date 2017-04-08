package org.karthikkumar.dbcm.conn.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.karthikkumar.dbcm.util.Instantiator;

/**
 * Derby based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public class DerbyJDBCConnectorFactory extends AbstractConnectorFactory {

	private String mDriver=null;
	private String mJDBCDBName=null;
	private String mJDBCStartUrl=null;
	private String mJDBCEndUrl=null;
	private Properties mProp=null;

	public void initializeValues(Properties pProp) {
		if(pProp!=null){
			mProp=pProp;
			mDriver="org.apache.derby.jdbc.EmbeddedDriver";
			mJDBCDBName=mProp.getProperty("db");
			if(mJDBCDBName==null||"".equals(mJDBCDBName)) mJDBCDBName="derby";
			mJDBCStartUrl="jdbc:derby:"+mJDBCDBName+";create=true";
			mJDBCEndUrl="jdbc:derby:;shutdown=true";
		}
	}

	public void destroy() {
		if(Instantiator.getInstance().createObject(mDriver)!=null) {
			try {
				DriverManager.getConnection(mJDBCEndUrl);					
			} catch (SQLException eSQLE) {
				//Ignore:	eSQLE.printStackTrace();
			}						
		}
		mDriver=null;
		mJDBCDBName=null;
		mJDBCStartUrl=null;
		mJDBCEndUrl=null;
		mProp=null;
	}
	
	public Connection getConnection() {
		Connection mCon=null;
		if(mDriver!=null) {
			if(Instantiator.getInstance().createObject(mDriver)!=null) {
				try {
					mCon=DriverManager.getConnection(mJDBCStartUrl,mProp);					
				} catch (SQLException eSQLE) {
					eSQLE.printStackTrace();
					mCon=null;
				}						
			}				
		}		
		return mCon;
	}
	
}