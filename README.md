About
-----
Java Database Connection Manager.

Usage
-----

Modern Java Usage:

    try(Connection con = ConnectionWrapper.getConnection()) {
    	//Blah.
    }

Traditional Java Usage:

    try {
    	Connection con = ConnectionWrapper.getConnection();
    	//Blah.
    } catch(SQLException sqle) {
    	//Handle Errors.
    } finally {
    	ConnectionWrapper.closeConnection(con);
    }

Installing
----------

mvn install -DperformRelease
