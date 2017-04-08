package org.karthikkumar.dbcm.conn;

import java.sql.Connection;
import java.util.Properties;

/**
 * An Interface to get a Connection from an underlying source.
 * <br>
 * <br>
 * Essentially, it exposes three methods that are called by the ConnectionManager to
 * get a Connection.
 * <br>
 * <br>
 * You can implement one yourself based on a any criterion, such as provided
 * by the container, a JNDI context, an IoC system etc.
 * <br>
 * <br>
 * Sample Usage:
 * <br>
 * <br>
 *  <pre>
 *  final ConnectionManager cm = new ConnectionManager();
 *  final ConnectorFactory cf = new MyImplementationOfConnectorFactory();
 *  cf.initializeValues(properties);
 *  cm.addConnectorFactory("myname",cf);
 *  final Connection conn = cm.getConnection("myname");
 *  </pre>
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public interface ConnectorFactory {
	public void initializeValues(Properties pProp);
	public Connection getConnection();
	public void destroy();
}
