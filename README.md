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

Limitations
-----------

This software comes with no warranty, implicit or explicit. Use
this at your own risk. Suggestions/bug reports/fixes are always welcome.

Feedback
--------

* Author: Karthik Kumar Viswanathan
* Web   : http://karthikkumar.org
* Email : karthikkumar@gmail.com
