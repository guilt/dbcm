package org.karthikkumar.dbcm.conn.impl;

import org.karthikkumar.dbcm.conn.ConnectorFactory;

abstract class AbstractConnectorFactory implements ConnectorFactory {

	public String toString() {		
		return getClass()+": No Connection";
	}	
	
}
