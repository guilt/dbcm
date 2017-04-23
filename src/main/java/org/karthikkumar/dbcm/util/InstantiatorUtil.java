package org.karthikkumar.dbcm.util;

import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Utilities for Instantiating Objects.
 *
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */

public class InstantiatorUtil {

	private static final Logger logger = Logger.getLogger(InstantiatorUtil.class.getCanonicalName());

	private static InstantiatorUtil mInst=new InstantiatorUtil();
	private Context mContext=null;

	private InstantiatorUtil(){
		try {
			mContext=new InitialContext();
		} catch (NamingException eNE) {
			logger.severe(eNE.getMessage());
			mContext=null;
		}
	}

	public static InstantiatorUtil getInstance(){
		return mInst;
	}

	public boolean isSubClass(Class<?> pSubClass, Class<?> pSuperClass){
		if(pSubClass!=null&&pSuperClass!=null){
			if(pSuperClass.isAssignableFrom(pSubClass)) return true;
		}
		return false;
	}

	public Class<?> forName(String pName){
		Class<?> mClass=null;
		try {
			mClass=Class.forName(pName);
		} catch (ClassNotFoundException eCNFE) {
			logger.severe(eCNFE.getMessage());
			mClass=null;
		}
		return mClass;
	}

	public Object createObject(Class<?> pClass, Class<?> pSuperClass){
		Object mObj=null;
		if(pClass==null) return mObj;
		if(pSuperClass==null||isSubClass(pClass,pSuperClass)) {
			try {
				mObj=pClass.newInstance();
			} catch (InstantiationException eIE) {
				logger.severe(eIE.getMessage());
				mObj=null;
			} catch (IllegalAccessException eIAE) {
				logger.severe(eIAE.getMessage());
				mObj=null;
			}
		}
		return mObj;
	}

	public Object createObject(String pSubName, Class <?> pSuperClass){
		return createObject(forName(pSubName),pSuperClass);
	}

	public Object createObject(String pSubName, String pSuperName){
		return createObject(pSubName,forName(pSuperName));
	}

	public Object createObject(Class<?> pClass){
		return createObject(pClass,null);
	}

	public Object createObject(String pName){
		return createObject(forName(pName));
	}

	public Object getNamedObject(String pName, Class <?>pSuperClass){
		Object mObj=null;
		if(pName==null) return mObj;
		if(mContext==null) return mObj;
		try {
			mObj=mContext.lookup(pName);
		} catch (NamingException eNE) {
			logger.severe(eNE.getMessage());
			mObj=null;
		}
		if(mObj!=null) {
			if(pSuperClass==null||isSubClass(mObj.getClass(),pSuperClass)) ;
			else mObj=null;
		}
		return mObj;
	}

	public Object getNamedObject(String pName){
		return getNamedObject(pName,null); 
	}

}