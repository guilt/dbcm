package org.karthikkumar.dbcm.conn.impl;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import org.karthikkumar.dbcm.util.InstantiatorUtil;
import static org.karthikkumar.dbcm.util.StringUtil.isEmpty;

/**
 * Derby based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public class DerbyJDBCConnectorFactory extends JDBCConnectorFactory {

	private static final Logger logger = Logger.getLogger(DerbyJDBCConnectorFactory.class.getCanonicalName());

	private String mJDBCDBName = null;
	private String mJDBCEndUrl = null;
	private String mErrorLog = null;

	public static final OutputStream DERBY_ERROR_LOG_NULL = new OutputStream() {
        public void write(int b) {}
    };

	public void initializeValues(Properties pProp) {
		if (pProp != null) {
			mProp = pProp;
			mDriver = "org.apache.derby.jdbc.EmbeddedDriver";
			mJDBCDBName = mProp.getProperty("db");
			if (isEmpty(mJDBCDBName))
				mJDBCDBName = "derby";
			mErrorLog = mProp.getProperty("errorfile");
			if (isEmpty(mErrorLog))
				System.setProperty("derby.stream.error.field", DerbyJDBCConnectorFactory.class.getCanonicalName()+".DERBY_ERROR_LOG_NULL");
			else
				System.setProperty("derby.stream.error.file", mErrorLog);
			mJDBCUrl = "jdbc:derby:" + mJDBCDBName + ";create=true";
			mJDBCEndUrl = "jdbc:derby:;shutdown=true";
		}
	}

	public void destroy() {
		if (InstantiatorUtil.getInstance().createObject(mDriver) != null) {
			Connection mCon=null;
			try {
				mCon = DriverManager.getConnection(mJDBCEndUrl);
				mCon.close();
			} catch (SQLException eSQLE) {
				logger.severe(eSQLE.getMessage());
			} finally {
				mCon = null;
			}
		}
		mJDBCEndUrl = null;
		mJDBCDBName = null;
		super.destroy();
	}

}