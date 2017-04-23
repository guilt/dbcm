package org.karthikkumar.dbcm.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.karthikkumar.dbcm.ConnectionManager;
import org.karthikkumar.dbcm.conn.ConnectorFactory;
import org.karthikkumar.dbcm.util.InstantiatorUtil;
import static org.karthikkumar.dbcm.util.StringUtil.isEmpty;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML File based Configurator. <br>
 * It uses the System Property <b>dbcm.xmlfile</b> and defaults to
 * <b>"config/dbcm.xml"</b>. <br>
 * <br>
 * 
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public class XMLConfigurator extends AbstractConfigurator {

	private static final Logger logger = Logger.getLogger(XMLConfigurator.class.getCanonicalName());

	private static final String XMLFILE = "dbcm.xmlfile";
	private static final String DEFXMLFILE = "config/dbcm.xml";
	private String mXMLFileName = null;
	private Document mDoc = null;
	private static int FIRSTLISTELEMENT = 0;

	private String getXMLFileName() {
		return mXMLFileName;
	}

	private void setXMLFileName(String pXMLFileName) {
		if (!isEmpty(mXMLFileName))
			mXMLFileName = pXMLFileName;
		else {
			mXMLFileName = System.getProperty(XMLFILE, DEFXMLFILE);
		}
	}

	private Document getDocument() {
		return mDoc;
	}

	private void setDocument(Document pDoc) {
		mDoc = pDoc;
	}

	private Document readDocument(String pXMLFileName) {
		Document mDoc = null;
		DocumentBuilderFactory mDBFac = DocumentBuilderFactory.newInstance();
		mDBFac.setValidating(true);
		mDBFac.setNamespaceAware(true);
		try {
			DocumentBuilder mDocBuilder = mDBFac.newDocumentBuilder();
			mDoc = mDocBuilder.parse(new FileInputStream(new File(pXMLFileName)));
		} catch (ParserConfigurationException ePCE) {
			logger.severe(ePCE.getMessage());
			mDoc = null;
		} catch (SAXException eSAXE) {
			logger.severe(eSAXE.getMessage());
			mDoc = null;
		} catch (IOException eIOE) {
			logger.severe(eIOE.getMessage());
			mDoc = null;
		}
		return mDoc;
	}

	private Properties processSubProperties(Element pConnectionNode) {
		NodeList mTempNL, mTempNL2;
		Element mPropertiesNode, mPropertyNode, mNameNode, mValueNode;
		Node mTempNode;
		Properties mNewProp = null;

		if (pConnectionNode == null)
			return mNewProp;

		mPropertiesNode = null;
		mTempNL = pConnectionNode.getElementsByTagName("properties");
		if (mTempNL != null) {
			mPropertiesNode = (Element) mTempNL.item(FIRSTLISTELEMENT);
		}

		if (mPropertiesNode == null)
			return mNewProp;

		mNewProp = new Properties();
		mTempNL = mPropertiesNode.getElementsByTagName("property");
		if (mTempNL != null) {
			for (int mIter = 0; mIter < mTempNL.getLength(); ++mIter) {
				String mTemp, mTemp2, mTemp3, mTemp4;
				mPropertyNode = (Element) mTempNL.item(mIter);

				if (mPropertyNode == null)
					continue;

				mNameNode = null;
				mTempNL2 = mPropertyNode.getElementsByTagName("name");
				if (mTempNL2 != null) {
					mNameNode = (Element) mTempNL2.item(FIRSTLISTELEMENT);
				}
				mTempNode = null;
				if (mNameNode != null) {
					mTempNode = mNameNode.getFirstChild();
				}
				mTemp = null;
				if (mTempNode != null)
					mTemp = mTempNode.getNodeValue();

				mValueNode = null;
				mTempNL2 = mPropertyNode.getElementsByTagName("value");
				if (mTempNL2 != null) {
					mValueNode = (Element) mTempNL2.item(FIRSTLISTELEMENT);
				}
				mTempNode = null;
				if (mValueNode != null) {
					mTempNode = mValueNode.getFirstChild();
				}
				mTemp2 = null;
				if (mTempNode != null)
					mTemp2 = mTempNode.getNodeValue();

				mTemp3 = mTemp.trim();
				mTemp4 = mTemp2.trim();
				if (!isEmpty(mTemp3) && !isEmpty(mTemp4)) {
					mNewProp.put(mTemp3, mTemp4);
				}
			}
		}
		return mNewProp;
	}

	private void processDocument(Document pDoc, ConnectionManager pCM) {
		String mDefName;
		Properties mFactoryMap;
		Properties mConnectionMap;
		Map<String, Properties> mConnectionPropMap;
		Properties mSubProp;
		ConnectorFactory mCF;
		Enumeration<Object> mEnum;
		NodeList mTempNL, mTempNL2;
		Element mTopChild, mConfigurationNode, mDefConnectionNode, mFactoryNode, mConnectionNode, mNameNode,
				mClassNameNode;
		Node mTempNode;
		if (pDoc == null || pCM == null)
			return;

		mTopChild = pDoc.getDocumentElement();
		if (mTopChild == null)
			return;

		mConfigurationNode = mTopChild;

		mDefName = null;
		mDefConnectionNode = null;
		mTempNL = mConfigurationNode.getElementsByTagName("defconnection");
		if (mTempNL != null) {
			mDefConnectionNode = (Element) mTempNL.item(FIRSTLISTELEMENT);
		}
		if (mDefConnectionNode != null) {
			mNameNode = null;
			mTempNL = mDefConnectionNode.getElementsByTagName("name");
			if (mTempNL != null) {
				mNameNode = (Element) mTempNL.item(FIRSTLISTELEMENT);
			}
			mTempNode = null;
			if (mNameNode != null) {
				mTempNode = mNameNode.getFirstChild();
			}
			if (mTempNode != null)
				mDefName = mTempNode.getNodeValue();
		}

		mFactoryMap = new Properties();
		mTempNL = mConfigurationNode.getElementsByTagName("factory");
		if (mTempNL != null) {
			for (int mIter = 0; mIter < mTempNL.getLength(); ++mIter) {
				String mTemp, mTemp2, mTemp3, mTemp4;
				mFactoryNode = (Element) mTempNL.item(mIter);

				if (mFactoryNode == null)
					continue;

				mNameNode = null;
				mTempNL2 = mFactoryNode.getElementsByTagName("name");
				if (mTempNL2 != null) {
					mNameNode = (Element) mTempNL2.item(FIRSTLISTELEMENT);
				}
				mTempNode = null;
				if (mNameNode != null) {
					mTempNode = mNameNode.getFirstChild();
				}
				mTemp = null;
				if (mTempNode != null)
					mTemp = mTempNode.getNodeValue();

				mClassNameNode = null;
				mTempNL2 = mFactoryNode.getElementsByTagName("classname");
				if (mTempNL2 != null) {
					mClassNameNode = (Element) mTempNL2.item(FIRSTLISTELEMENT);
				}
				mTempNode = null;
				if (mClassNameNode != null) {
					mTempNode = mClassNameNode.getFirstChild();
				}
				mTemp2 = null;
				if (mTempNode != null)
					mTemp2 = mTempNode.getNodeValue();

				mTemp3 = mTemp.trim();
				mTemp4 = mTemp2.trim();
				if (!isEmpty(mTemp3) && !isEmpty(mTemp4)) {
					if (InstantiatorUtil.getInstance().isSubClass(InstantiatorUtil.getInstance().forName(mTemp4),
							ConnectorFactory.class)) {
						mFactoryMap.setProperty(mTemp3, mTemp4);
					}
				}
			}
		}

		mConnectionMap = new Properties();
		mConnectionPropMap = new HashMap<String, Properties>();
		mTempNL = mConfigurationNode.getElementsByTagName("connection");
		if (mTempNL != null) {
			for (int mIter = 0; mIter < mTempNL.getLength(); ++mIter) {
				String mTemp, mTemp2, mTemp3, mTemp4;
				mConnectionNode = (Element) mTempNL.item(mIter);

				if (mConnectionNode == null)
					continue;

				mNameNode = null;
				mTempNL2 = mConnectionNode.getElementsByTagName("name");
				if (mTempNL2 != null) {
					mNameNode = (Element) mTempNL2.item(FIRSTLISTELEMENT);
				}
				mTempNode = null;
				if (mNameNode != null) {
					mTempNode = mNameNode.getFirstChild();
				}
				mTemp = null;
				if (mTempNode != null)
					mTemp = mTempNode.getNodeValue();

				mClassNameNode = null;
				mTempNL2 = mConnectionNode.getElementsByTagName("factoryname");
				if (mTempNL2 != null) {
					mClassNameNode = (Element) mTempNL2.item(FIRSTLISTELEMENT);
				}
				mTempNode = null;
				if (mClassNameNode != null) {
					mTempNode = mClassNameNode.getFirstChild();
				}
				mTemp2 = null;
				if (mTempNode != null)
					mTemp2 = mTempNode.getNodeValue();

				mTemp3 = mTemp.trim();
				mTemp4 = mTemp2.trim();
				if (!isEmpty(mTemp3) && !isEmpty(mTemp4)) {
					if (mFactoryMap.containsKey(mTemp4)) {
						mConnectionMap.setProperty(mTemp3, mTemp4);
						mSubProp = processSubProperties(mConnectionNode);
						if (mSubProp != null)
							mConnectionPropMap.put(mTemp3, mSubProp);
					}
				}
			}
		}

		if (mDefName != null && !mConnectionMap.containsKey(mDefName))
			mDefName = null;

		mEnum = mConnectionMap.keys();
		while (mEnum.hasMoreElements()) {
			String mTemp;
			mTemp = (String) mEnum.nextElement();

			if (isEmpty(mTemp))
				continue;

			mSubProp = (Properties) mConnectionPropMap.get(mTemp);
			mCF = (ConnectorFactory) InstantiatorUtil.getInstance().createObject(
					InstantiatorUtil.getInstance().forName(mFactoryMap.getProperty(mConnectionMap.getProperty(mTemp))),
					ConnectorFactory.class);
			if (mCF != null)
				mCF.initializeValues(mSubProp);
			pCM.addConnectorFactory(mTemp, mCF);
			mCF = null;
		}

		pCM.setDefaultConnectionName(mDefName);
	}

	public XMLConfigurator() {
		setXMLFileName(null);
		setDocument(readDocument(getXMLFileName()));
	}

	public XMLConfigurator(String pPropFileName) {
		setXMLFileName(pPropFileName);
		setDocument(readDocument(getXMLFileName()));
	}

	public XMLConfigurator(Document pDoc) {
		setXMLFileName(null);
		setDocument(pDoc);
	}

	public void configure(ConnectionManager pCM) {
		if (pCM == null)
			return;
		synchronized (pCM) {
			processDocument(getDocument(), pCM);
		}
	}

}