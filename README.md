About
-----
Java Database Connection Manager.

Usage
-----

Modern Java Usage:

    try(Connection con = ConnectionManagerHelper.getConnection()) {
    	//Blah.
    }

Traditional Java Usage:

    try {
    	Connection con = ConnectionManagerHelper.getConnection();
    	//Blah.
    } catch(SQLException sqle) {
    	//Handle Errors.
    } finally {
    	ConnectionManagerHelper.closeConnection(con);
    }

Installing
----------

mvn install -DperformRelease
