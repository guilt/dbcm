package org.karthikkumar.dbcm.conn.impl;

import javax.sql.DataSource;

import org.karthikkumar.dbcm.util.Instantiator;

import java.util.Properties;

/**
 * JNDI named DataSource based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public class NamedDataSourceConnectorFactory extends AbstractDataSourceConnectorFactory {
	
	private String mName=null;
	private DataSource mDS=null;
	
	public void initializeValues(Properties pProp) {
		if(pProp!=null){
			mName=pProp.getProperty("nameds");			
		}
	}
	
	public void destroy() {
		mDS=null;
		mName=null;		
	}
	
	public DataSource getDataSource() {
		if(mDS==null) mDS=(DataSource)Instantiator.getInstance().getNamedObject(mName,DataSource.class);
		return mDS;
	}
	
	public String toString() {		
		if(mName!=null)
			return getClass()+": {nameds="+mName+"}";
		return getClass()+": No Properties";
	}
}