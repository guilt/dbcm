package org.karthikkumar.dbcm.conn.impl;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

/**
 * DBCP based DataSourceConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public class DBCPDataSourceConnectorFactory extends AbstractDataSourceConnectorFactory {
		
	private Properties mProp=null;
	private DataSource mDS=null;
	
	public void initializeValues(Properties pProp) {
		if(pProp!=null){
			mProp=pProp;
		}
	}
	
	public void destroy() {
		mDS=null;
		mProp=null;
	}
	
	public DataSource getDataSource() {
		if(mDS==null) {
			try {
				mDS=(DataSource) BasicDataSourceFactory.createDataSource(mProp);
			} catch (Exception eE) {
				eE.printStackTrace();
				mDS=null;
			}
		}
		return mDS;
	}
	
	public String toString() {	
		if(mProp!=null)
			return getClass()+": "+mProp.toString();
		return getClass()+": No Properties";
	}
}