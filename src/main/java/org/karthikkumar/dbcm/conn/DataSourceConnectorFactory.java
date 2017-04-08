package org.karthikkumar.dbcm.conn;

import javax.sql.DataSource;

/**
 * DataSource based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public interface DataSourceConnectorFactory extends ConnectorFactory {
	public DataSource getDataSource();
}
