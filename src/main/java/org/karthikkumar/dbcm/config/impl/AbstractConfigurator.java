package org.karthikkumar.dbcm.config.impl;

import org.karthikkumar.dbcm.ConnectionManager;
import org.karthikkumar.dbcm.config.Configurator;

abstract class AbstractConfigurator implements Configurator {
	
	public void reconfigure(ConnectionManager pCM){
		if(pCM!=null) {
			pCM.destroy();
			configure(pCM);
		}
	}

}