package org.karthikkumar.dbcm.conn;

import javax.sql.XADataSource;

/**
 * XADataSource based ConnectorFactory.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public interface XADataSourceConnectorFactory extends XAConnectorFactory {
	public XADataSource getXADataSource();
}
