package org.karthikkumar.dbcm.conn;

import javax.sql.ConnectionPoolDataSource;

/**
 * PooledDataSource based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public interface PooledDataSourceConnectorFactory extends PooledConnectorFactory {
	public ConnectionPoolDataSource getPooledDataSource();
}
