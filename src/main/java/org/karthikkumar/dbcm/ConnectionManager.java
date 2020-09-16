package org.karthikkumar.dbcm;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.karthikkumar.dbcm.conn.ConnectorFactory;
import static org.karthikkumar.dbcm.util.StringUtil.isEmpty;

/**
 * The Basic Building Unit of the library - The Connection Manager. <br>
 * <br>
 * Allows you to specify and use connections across many DBs in the same
 * application. <br>
 * <br>
 * Sample Usage: <br>
 * <br>
 *
 * <pre>
 * final ConnectionManager cm = new ConnectionManager();
 * // ... Configure cm ...
 * final Connection conn = cm.getDefaultConnection();
 * </pre>
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public final class ConnectionManager {

	private static final Logger logger = Logger.getLogger(ConnectionManager.class.getCanonicalName());
	private final Map<String, ConnectorFactory> mConnectorFactories = new HashMap<String, ConnectorFactory>();
	private String mDefName = "default";

	/**
	 * Destroy the existing Connection giving Factories explicitly. This method
	 * is called by the finalize() method automatically.
	 */
	public void destroy() {
		ConnectorFactory mCF = null;
		if (mConnectorFactories != null) {
			for (final String mName : mConnectorFactories.keySet()) {
				mCF = (ConnectorFactory) mConnectorFactories.get(mName);
				if (mCF != null) {
					mCF.destroy();
					mConnectorFactories.remove(mName);
				}
			}
		}
		mCF = null;
		mDefName = null;
	}

	/**
	 * Constructs an Instance of the Connection Manager.
	 */
	public ConnectionManager() {
	}

	/**
	 * Return a Specific Connection from the Database Connection Factory.
	 * 
	 * @param pName
	 *            Name of Connection to retrieve
	 * @return Database Connection
	 */
	public Connection getConnection(String pName) {
		ConnectorFactory mCF = null;
		Connection mCon = null;
		if(pName == null) {
			logger.severe("Connection Name cannot be null.");
			return mCon;
		}
		mCF = (ConnectorFactory) mConnectorFactories.get(pName);
		if (mCF != null)
			mCon = mCF.getConnection();
		else
			logger.severe("Unable to find ConnectorFactory: "+pName+". Ensure you have registered this ConnectorFactory.");
		mCF = null;
		return mCon;
	}

	/**
	 * Return the Default Connection Name.
	 * 
	 * @return
	 */
	private String getDefaultConnectionName() {
		return mDefName;
	}

	/**
	 * Override the Default Connection Factory Name. Default Connection's Name
	 * is 'default'
	 * 
	 * @param pDefName
	 *            the New Default Connection Factory Name to Set.
	 */
	public void setDefaultConnectionName(String pDefName) {
		if (!isEmpty(pDefName))
			mDefName = pDefName;
		else
			logger.severe("Default Connection Name cannot be null.");
	}

	/**
	 * Return a Connection from the Default Database Connection Factory.
	 * 
	 * @return Default Database Connection
	 */
	public Connection getDefaultConnection() {
		return getConnection(getDefaultConnectionName());
	}

	/**
	 * Add a new Connection giving Factory. It is usually used when you want to
	 * dynamically change the list of Connection giving factories.
	 * 
	 * @param pName
	 *            Name associated with the Connection giving Factory.
	 * @param pCF
	 *            An instance of a Connection giving Factory.
	 */
	public void addConnectorFactory(String pName, ConnectorFactory pCF) {
		if (!isEmpty(pName) && pCF != null) {
			logger.config("Adding ConnectorFactory: "+pName);
			mConnectorFactories.put(pName, pCF);
		}
	}

}
