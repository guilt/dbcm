About
-----
Java Database Connection Manager.

Usage
-----

Modern Java Usage:

```java
    try(Connection con = ConnectionManagerHelper.getConnection()) {
    	//Blah.
    }
```

Traditional Java Usage:

```java
    try {
    	Connection con = ConnectionManagerHelper.getConnection();
    	//Blah.
    } catch(SQLException sqle) {
    	//Handle Errors.
    } finally {
    	ConnectionManagerHelper.closeConnection(con);
    }
```

Installing
----------

```bash
mvn install -DperformRelease
```
