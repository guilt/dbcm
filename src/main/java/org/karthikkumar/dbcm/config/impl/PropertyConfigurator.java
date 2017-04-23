package org.karthikkumar.dbcm.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import org.karthikkumar.dbcm.ConnectionManager;
import org.karthikkumar.dbcm.conn.ConnectorFactory;
import org.karthikkumar.dbcm.util.InstantiatorUtil;

import static org.karthikkumar.dbcm.util.StringUtil.isEmpty;

/**
 * Property File based Configurator. <br>
 * It uses the System Property <b>dbcm.propertyfile</b> and defaults to
 * <b>"config/dbcm.properties"</b>. <br>
 * <br>
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public class PropertyConfigurator extends AbstractConfigurator {

	private static final Logger logger = Logger.getLogger(PropertyConfigurator.class.getCanonicalName());
	
	private static final String PROPFILE = "dbcm.propertyfile";
	private static final String DEFPROPFILE = "config/dbcm.properties";
	private String mPropFileName = null;
	private Properties mProp = null;

	private String getPropertyFileName() {
		return mPropFileName;
	}

	private void setPropertyFileName(String pPropFileName) {
		if (!isEmpty(pPropFileName))
			mPropFileName = pPropFileName;
		else {
			mPropFileName = System.getProperty(PROPFILE, DEFPROPFILE);
		}
	}

	private Properties getProperties() {
		return mProp;
	}

	private void setProperties(Properties pProp) {
		mProp = pProp;
	}

	private Properties readProperties(String pPropFileName) {
		Properties mPropsNew = new Properties();
		try {
			mPropsNew.load(new FileInputStream(new File(pPropFileName)));
		} catch (FileNotFoundException eFNFE) {
			logger.severe(eFNFE.getMessage());
			mPropsNew = null;
		} catch (IOException eIOE) {
			logger.severe(eIOE.getMessage());
			mPropsNew = null;
		}
		return mPropsNew;
	}

	private List<String> getValues(String pSource) {
		boolean mDone = false;
		int mIndex = -1;
		List<String> mList = new ArrayList<String>();

		while (!mDone) {
			if (!isEmpty(pSource)) {
				mIndex = pSource.indexOf(",");
				if (mIndex == -1) {
					String mTemp;
					mTemp = pSource.trim();
					if (!isEmpty(mTemp))
						mList.add(mTemp);
					mDone = true;
				} else {
					String mTemp, mTemp2;
					mTemp2 = pSource.substring(0, mIndex);
					mTemp = null;
					if (!isEmpty(mTemp2))
						mTemp = mTemp2.trim();
					if (!isEmpty(mTemp))
						mList.add(mTemp);
					mTemp = pSource.substring(mIndex + 1);
					pSource = mTemp;
				}
			} else
				mDone = true;
		}
		return mList;
	}

	private Properties processSubProperties(Properties pProp, String pPrefix) {
		Properties mNewProp = null;
		Enumeration<Object> mEnum;
		if (pProp == null || isEmpty(pPrefix))
			return mNewProp;
		mNewProp = new Properties();
		mEnum = pProp.keys();
		// Inefficient Way. It Works though.
		while (mEnum.hasMoreElements()) {
			String mTemp, mTemp2, mTemp3;
			mTemp2 = (String) mEnum.nextElement();
			mTemp = null;
			if (!isEmpty(mTemp2))
				mTemp = mTemp2.trim();
			if (!isEmpty(mTemp) && mTemp.startsWith(pPrefix)) {
				mTemp3 = mTemp.substring(pPrefix.length());
				if (!isEmpty(mTemp3))
					mNewProp.put(mTemp3, pProp.get(mTemp2));
			}
		}
		return mNewProp;
	}

	private void processProperties(Properties pProp, ConnectionManager pCM) {
		List<String> mFactoryList;
		List<String> mConnectionList;
		String mDefName;
		Properties mFactoryMap;
		Properties mConnectionMap;
		Properties mSubProp;
		ConnectorFactory mCF;
		Enumeration<Object> mEnum;

		if (pProp == null || pCM == null)
			return;

		mFactoryList = getValues(pProp.getProperty("factories"));
		mConnectionList = getValues(pProp.getProperty("connections"));
		mDefName = pProp.getProperty("defconnection");
		mFactoryMap = new Properties();
		mConnectionMap = new Properties();
		for (int mIter = 0; mIter < mFactoryList.size(); ++mIter) {
			String mTemp, mTemp2;
			mTemp2 = pProp.getProperty("factory." + mFactoryList.get(mIter));
			mTemp = null;
			if (!isEmpty(mTemp2))
				mTemp = mTemp2.trim();
			if (!isEmpty(mTemp)) {
				if (InstantiatorUtil.getInstance().isSubClass(InstantiatorUtil.getInstance().forName(mTemp),
						ConnectorFactory.class)) {
					mFactoryMap.setProperty((String) mFactoryList.get(mIter), mTemp);
				}
			}
		}
		for (int mIter = 0; mIter < mConnectionList.size(); ++mIter) {
			String mTemp, mTemp2;
			mTemp2 = pProp.getProperty("connection." + mConnectionList.get(mIter));
			mTemp = null;
			if (!isEmpty(mTemp2))
				mTemp = mTemp2.trim();
			if (!isEmpty(mTemp)) {
				if (mFactoryMap.containsKey(mTemp)) {
					mConnectionMap.setProperty((String) mConnectionList.get(mIter), mTemp);
				}
			}
		}
		if (!isEmpty(mDefName) && !mConnectionMap.containsKey(mDefName))
			mDefName = null;
		mEnum = mConnectionMap.keys();
		while (mEnum.hasMoreElements()) {
			String mTemp, mTemp2;
			mTemp2 = (String) mEnum.nextElement();
			mTemp = null;
			if (!isEmpty(mTemp2))
				mTemp = mTemp2.trim();
			if (!isEmpty(mTemp)) {
				mSubProp = processSubProperties(pProp, "connection." + mTemp + ".");
				mCF = (ConnectorFactory) InstantiatorUtil.getInstance().createObject(
						InstantiatorUtil.getInstance().forName(mFactoryMap.getProperty(mConnectionMap.getProperty(mTemp))),
						ConnectorFactory.class);
				if (mCF != null)
					mCF.initializeValues(mSubProp);
				pCM.addConnectorFactory(mTemp, mCF);
				mCF = null;
			}
		}

		pCM.setDefaultConnectionName(mDefName);
	}

	public PropertyConfigurator() {
		setPropertyFileName(null);
		setProperties(readProperties(getPropertyFileName()));
	}

	public PropertyConfigurator(String pPropFileName) {
		setPropertyFileName(pPropFileName);
		setProperties(readProperties(getPropertyFileName()));
	}

	public PropertyConfigurator(Properties pProp) {
		setPropertyFileName(null);
		setProperties(pProp);
	}

	public void configure(ConnectionManager pCM) {
		if (pCM == null)
			return;
		synchronized (pCM) {
			processProperties(getProperties(), pCM);
		}
	}

}