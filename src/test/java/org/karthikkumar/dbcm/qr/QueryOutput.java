package org.karthikkumar.dbcm.qr;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

class QueryOutput {

	private static final int ALIGNLEFT=0;
	private static final int ALIGNRIGHT=1;
	private static final String COLSEP="  "; //No non-printable characters.
	private static final char FILLERCHAR=' ';
	private static final char ROWSEPCHAR='-';
	private static final char REPLACECHAR=' ';
	private static final String Replaceable="\\s";
	private static final String Replacedby=""+REPLACECHAR;
	private static final String UPDMSG=" row(s) updated.";
	private static int DATELENGTH=20;
	
	public void executeQuery(Connection pCon, String pQuery, BufferedWriter pOutBW) {
		
		boolean mRsOut=false;
		
		StringBuffer mDummy=null;
		Object mFieldObj;
		String mField,mPrintField;
		
		Statement mStmt=null;
		ResultSet mRs=null;
		ResultSetMetaData mRM=null;
		
		String mCNames[]=null;
		String mCFormats[]=null;
		int mCSizes[]=null;
		int mCAlign[]=null;
		
		int i,j,type;
		int cols=0,totallen=0;
		
		if(pCon==null) return;
		
		try {			
			mStmt=pCon.createStatement();
			mRsOut=mStmt.execute(pQuery);
			
			if(!mRsOut) {
				pOutBW.write(COLSEP);
				pOutBW.write(mStmt.getUpdateCount()+UPDMSG);
				pOutBW.write(COLSEP);
				pOutBW.newLine(); 
				return;
			}
			
			mRs=mStmt.getResultSet();  
			mRM=mRs.getMetaData();
			cols=mRM.getColumnCount();
			if(cols>0) {
				mCNames=new String[cols];
				mCFormats=new String[cols];
				mCSizes=new int[cols];
				mCAlign=new int[cols];
			}			
			for(i=1;i<=cols;i++){					
				type=mRM.getColumnType(i);
				mCNames[i-1]=mRM.getColumnLabel(i);
				mCSizes[i-1]=mRM.getColumnDisplaySize(i);
				if(mCSizes[i-1]<mCNames[i-1].length()) mCSizes[i-1]=mCNames[i-1].length(); //Fit length
				if(
						type==java.sql.Types.DATE      ||
						type==java.sql.Types.TIME      ||
						type==java.sql.Types.TIMESTAMP
				)
					if(mCSizes[i-1]<DATELENGTH) mCSizes[i-1]=DATELENGTH;  
				
				mDummy=new StringBuffer();
				for(j=0;j<mCSizes[i-1];j++)
					mDummy.append(FILLERCHAR);
				mCFormats[i-1]=mDummy.toString();
				
				mCAlign[i-1]=ALIGNLEFT;  
				if(
						type==java.sql.Types.BIGINT	 ||
						type==java.sql.Types.DECIMAL ||
						type==java.sql.Types.DOUBLE  ||
						type==java.sql.Types.FLOAT   ||
						type==java.sql.Types.INTEGER ||
						type==java.sql.Types.NUMERIC ||
						type==java.sql.Types.REAL    ||
						type==java.sql.Types.SMALLINT||
						type==java.sql.Types.TINYINT						
				)
				mCAlign[i-1]=ALIGNRIGHT;	
			}
			totallen=0;
			mPrintField=COLSEP;	
			pOutBW.write(mPrintField); totallen+=mPrintField.length();
			for(i=1;i<=cols;i++){
				mPrintField=(mCNames[i-1]+mCFormats[i-1]).substring(0,mCSizes[i-1])+COLSEP;
				pOutBW.write(mPrintField); totallen+=mPrintField.length();
			}
			pOutBW.newLine();  
			for(i=0;i<=totallen;i++){
				pOutBW.write(ROWSEPCHAR);
			}
			pOutBW.newLine();
			while(mRs.next()){
				mPrintField=COLSEP;	
				pOutBW.write(mPrintField); 
				for(i=1;i<=cols;i++){					
					mFieldObj=mRs.getObject(i);
					if(mFieldObj==null) mField="NULL";
					else {
						mField=mFieldObj.toString().replaceAll(Replaceable,Replacedby);
					}
					if(mField==null) mField="NULL";
					if(mCAlign[i-1]==ALIGNRIGHT) {
						mPrintField=mCFormats[i-1]+mField;
						pOutBW.write(mPrintField.substring(mPrintField.length()-mCSizes[i-1]));
					} else {
						mPrintField=mField+mCFormats[i-1];
						pOutBW.write(mPrintField.substring(0,mCSizes[i-1]));
					}
					pOutBW.write(COLSEP);										 
				}
				pOutBW.newLine();
			}
			mRM=null;
			mRs.close(); mRs=null;
			mStmt.close(); mStmt=null;
			pOutBW.flush(); 	
		} catch (SQLException eSQLE) {
			try {
				pOutBW.write("Error in SQL: "+pQuery);
				pOutBW.newLine();
				pOutBW.write("Details: "+eSQLE.getMessage());
				pOutBW.newLine(); 	
				pOutBW.flush();
			} catch (IOException eIOE) {
			}
		} catch (IOException eIOE) {
		}			
	}

}