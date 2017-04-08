package org.karthikkumar.dbcm.conn.impl;

import javax.sql.PooledConnection;

import org.karthikkumar.dbcm.conn.XAConnectorFactory;

abstract class AbstractXAConnectorFactory extends AbstractPooledConnectorFactory implements XAConnectorFactory {

	public PooledConnection getPooledConnection() {		
		return getXAConnection();
	}
	
}
