package org.karthikkumar.dbcm.qr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

import org.karthikkumar.dbcm.ConnectionManagerHelper;

/**
 * An Utility to execute Queries in a batch fashion against any Database.
 * <br>
 * <br>
 * It reads queries from <b>"data/query.txt"</b>, uses ConnectionManagerHelper for the
 * default Connection. It reads queries line by line, executes them and runs them against
 * the database. Finally, errors and output are written to <b>"data/output.txt"</b>.
 *  
 * @author Karthik Kumar Viswanathan &lt;karthikkumar@gmail.com&gt;
 *
 */
public class QueryRunner {

	private static final String DEFINPUTFILE="data/query.txt";
	private static final String DEFOUTPUTFILE="data/output.txt";
	private static final String INPUTFILE="db.query.inputfile";
	private static final String OUTPUTFILE="db.query.outputfile";
	
	private QueryRunner() {
	}

	public static void main(String[] pArgs) {
		String mInputFile=null;
		String mOutputFile=null;

		File mIFile=null;
		File mOFile=null;		

		BufferedReader mIBR=null;
		BufferedWriter mOBW=null;

		String mSQLQuery=null;
		String mTemp=null;

		Connection mCon=null;

		QueryOutput mQO=new QueryOutput();

		if(pArgs.length>0) {
			mInputFile=pArgs[0];
			if(pArgs.length>1) {
				mOutputFile=pArgs[1]; 
			}
		} 

		if(mInputFile==null) mInputFile=System.getProperty(INPUTFILE, DEFINPUTFILE);  
		if(mOutputFile==null) mOutputFile=System.getProperty(OUTPUTFILE, DEFOUTPUTFILE);

		if(mCon==null) {
			mCon=ConnectionManagerHelper.getDefaultConnection();
		}

		if(mCon==null) {
			System.err.println("Can't connect to database.");
			return;
		}			

		System.out.println("Query File: "+mInputFile);

		mIFile=new File(mInputFile);
		try{
			mIBR=new BufferedReader(new FileReader(mIFile));
		} catch(FileNotFoundException eFNFE){
			System.err.println("Query File not found: "+mInputFile);
			return;	
		}

		System.out.println("Query Output File: "+mOutputFile);

		mOFile=new File(mOutputFile);
		try{
			mOBW=new BufferedWriter(new FileWriter(mOFile));
		} catch(IOException eIOE){
			eIOE.printStackTrace();
			System.err.println("Output File not Writable: "+mOutputFile);
			try{
				mIBR.close();
			} catch(IOException eIOE2){
			}
			return;
		}	

		try{
			while((mTemp=mIBR.readLine())!=null) {  			
				mSQLQuery=mTemp.trim();
				if(mSQLQuery!=null&&!("".equals(mSQLQuery))&&!mSQLQuery.startsWith("#")&&!mSQLQuery.startsWith("//")) {
					System.out.println("\nExecuting Command: "+mSQLQuery);
					mQO.executeQuery(mCon,mSQLQuery,mOBW);
					mOBW.newLine();
					System.out.println("DONE");	
				}
			}
		} catch(IOException eIOE){
		}

		ConnectionManagerHelper.closeConnection(mCon);
		mCon=null;

		try{
			mIBR.close();
			mIBR=null;
			mOBW.close();
			mOBW=null;
		} catch(IOException eIOE){
		}	

	}

}