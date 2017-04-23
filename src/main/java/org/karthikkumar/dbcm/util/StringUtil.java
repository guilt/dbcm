package org.karthikkumar.dbcm.util;


/**
 * Utilities for String.
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */

public class StringUtil {

	public static boolean isEmpty(String pGiven) {
		return (null == pGiven || "".equals(pGiven));
	}

}
