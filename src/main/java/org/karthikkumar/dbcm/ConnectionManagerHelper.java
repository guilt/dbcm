package org.karthikkumar.dbcm;

import java.sql.Connection;
import java.sql.SQLException;

import org.karthikkumar.dbcm.config.Configurator;
import org.karthikkumar.dbcm.config.impl.PropertyConfigurator;
import org.karthikkumar.dbcm.config.impl.XMLConfigurator;

/**
 * The Entry Point into the library - The Connection Manager Helper.
 * <br>
 * <br>
 * It first searches for the <b>"config/dbcm.properties"</b> and then for 
 * the XML file <b>"config/dbcm.xml"</b>.
 * <br>
 * <br>
 * Sample Usage:
 * <br>
 * <br>
 *  <pre>
 *  ConnectionManagerHelper.getDefaultConnection();
 *  </pre>
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
final public class ConnectionManagerHelper{
	
	private static ConnectionManagerHelper mCW=new ConnectionManagerHelper();	
	private ConnectionManager mCM;
	private boolean mInited=false;
	

	private void init(boolean pReconf){
		 Configurator mConf=null;
		 Connection mCon=null;
		 
		 mInited=false;
		 
		 if(mCM == null) {
			mCM = new ConnectionManager();
		 }
		 
		 if(mCon==null) {
			mConf=new PropertyConfigurator();
		 	if(pReconf) mConf.reconfigure(mCM);
		 	else mConf.configure(mCM); 
		 	if((mCon=mCM.getDefaultConnection())!=null) mInited=true;
		 }

		 if(mCon==null) {			 
			mConf=new XMLConfigurator();
		 	if(pReconf) mConf.reconfigure(mCM);
		 	else mConf.configure(mCM); 
		 	if((mCon=mCM.getDefaultConnection())!=null) mInited=true;
		 }
		 
		 if(mCon!=null) {
			 instCloseConnection(mCon);
			 mCon=null;
		 }	 
	}
	
	/**
	 * Constructs an Instance of the Connection Manager.
	 */
	private ConnectionManagerHelper(){
			init(false);
	}

	/**
	 * Returns the Default Instance of the Connection Manager.
	 * 
	 * @return the Default Instance of the Connection Manager.
	 */
	private static ConnectionManagerHelper getInstance(){
		return mCW;
	}
	
	/**
	 * Return a Connection from the Default Database Connection Factory.
	 * 
	 * @return Default Database Connection
	 */
	private Connection instGetDefaultConnection(){
		if(!mInited) init(false);	
		return mCM.getDefaultConnection();
	}
	
	/**
	 * Return a Specific Connection from the Database Connection Factory.
	 * 
	 * @param pName Name of Connection to retrieve
	 * @return Database Connection
	 */
	private Connection instGetConnection(String pName){
		if(!mInited) init(false);	
		return mCM.getConnection(pName);
	}
	
	/**
	 * Close a Connection.
	 * 
	 * @param pCon Connection to Close.
	 */
	private void instCloseConnection(Connection pCon){
		if(pCon!=null) {
			try {
				pCon.close();
			} catch (SQLException eSQLE) {
				eSQLE.printStackTrace();		
			}
		}
	}
	
	/**
	 * Reconfigure the Database Connections.
	 */
	private void instReconfigure(){
		init(true);
	}
	
	/**
	 * Return a Connection from the Default Database Connection Factory.
	 * 
	 * @return Default Database Connection
	 */
	public static Connection getDefaultConnection(){
		return getInstance().instGetDefaultConnection();
	}
	
	/**
	 * Return a Specific Connection from the Database Connection Factory.
	 * 
	 * @param pName Name of Connection to retrieve
	 * @return Database Connection
	 */
	public static Connection getConnection(String pName){
		return getInstance().instGetConnection(pName);
	}
	
	/**
	 * Close a Connection.
	 * 
	 * @param pCon Connection to Close.
	 */
	public static void closeConnection(Connection pCon){
		getInstance().instCloseConnection(pCon);
		pCon=null;
	}

	/**
	 * Reconfigure the Database Connections.
	 */
	public static void reconfigure(){
		getInstance().instReconfigure();
	}
	
}