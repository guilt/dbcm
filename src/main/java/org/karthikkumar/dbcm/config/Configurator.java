package org.karthikkumar.dbcm.config;

import org.karthikkumar.dbcm.ConnectionManager;

/**
 * An Interface to Configure The Connection Manager.
 * <br>
 * <br>
 * Essentially, it exposes two methods that are called by the ConnectionManagerHelper to
 * configure or reconfigure dynamically the Database Connections used by ConnectionManager.
 * <br>
 * <br>
 * You can implement one yourself based on a certain dynamic configuration, such as provided
 * by ZooKeeper or any other Configuration Management System.
 * <br>
 * <br>
 * Sample Usage:
 * <br>
 * <br>
 *  <pre>
 *  final ConnectionManager cm = new ConnectionManager();
 *  final Configurator conf = new MyImplementationOfConfigurator();
 *  conf.configure(cm);
 *  //Time elapses, you decide to change ...
 *  conf.reconfigure(cm);
 *  </pre>
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public interface Configurator {
	public void configure(ConnectionManager pCM);
	public void reconfigure(ConnectionManager pCM);
}
