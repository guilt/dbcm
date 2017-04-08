package org.karthikkumar.dbcm.conn;

import javax.sql.PooledConnection;

/**
 * PooledConnection based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public interface PooledConnectorFactory extends ConnectorFactory {
	public PooledConnection getPooledConnection();
}
