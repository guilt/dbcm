package org.karthikkumar.dbcm.conn;

import javax.sql.XAConnection;

/**
 * XAConnection based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public interface XAConnectorFactory extends PooledConnectorFactory {
	public XAConnection getXAConnection();
}
