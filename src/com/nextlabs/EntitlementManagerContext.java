package com.nextlabs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EntitlementManagerContext {
	private String requestId;
	private String refererUrl;
	private String source;
	private String target;
	private String userName;
	public EntitlementManagerContext()
	{
		requestId=com.nextlabs.Utilities.RandomString(10);
	}
	
	public void setRequestId(String requestId)
	{
		this.requestId=requestId;
	}
	
	public String getRequestId()
	{
		return requestId;
	}
	public void setRefererUrl(String refererUrl)
	{
		this.refererUrl=refererUrl;
	}
	public String getRefererUrl()
	{
		return this.refererUrl;
	}
	public void setUserName(String userName)
	{
		this.userName=userName;
	}
	public String getUserName()
	{
		return this.userName;
	}
	public void setSource(String source)
	{
		this.source=source;
	}
	public String getSource()
	{
		return this.source;
	}
	public void setTarget(String target)
	{
		this.target=target;
	}
	public String getTarget()
	{
		return this.target;
	}
/*	public void log(LogLevel lvl,String msg)
	{
		String strLoglevel=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, com.nextlabs.Property.PEP_LOGLEVEL);
		LogLevel logLevelSetting=LogLevel.parse(strLoglevel);
		//com.nextlabs.Utilities.OutputDebugStringA("loglevel="+lvl+"(current setting="+logLevelSetting+")"+"str loglvl setting="+strLoglevel);
		if(lvl.compareTo(logLevelSetting)>=0)
		{
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
	    	String strMsg=String.valueOf(Thread.currentThread().getId());
	    	strMsg+=" WCEM "+this.requestId+" "+LogLevel.format(lvl)+" "+sdf.format(cal.getTime())+" ";
	    	strMsg+=msg;
			com.nextlabs.Utilities.OutputDebugStringA(strMsg);
		}
	}*/
/*	public void log(LogLevel lvl,Exception exp)
	{
		String strLoglevel=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, com.nextlabs.Property.PEP_LOGLEVEL);
		LogLevel logLevelSetting=LogLevel.parse(strLoglevel);
		//com.nextlabs.Utilities.OutputDebugStringA("loglevel="+lvl+"(current setting="+logLevelSetting+")"+"str loglvl setting="+strLoglevel);
		if(lvl.compareTo(logLevelSetting)>=0)
		{			
	    	String strMsg=String.valueOf(Thread.currentThread().getId());
	    	
	    	strMsg+=" WCEM "+this.requestId+" "+LogLevel.format(lvl)+" "+Utilities.getDayTimeString("yyyy/MM/dd HH:mm:ss")+" ";
	    	strMsg+=exp.getMessage();
	    	
	    	StringWriter sw = new StringWriter();
	    	PrintWriter pw = new PrintWriter(sw);
	    	exp.printStackTrace(pw);
	    	strMsg+=sw.toString();
			com.nextlabs.Utilities.OutputDebugStringA(strMsg);
		}
	}*/
/*	public void log(String requestid,String msg)
	{
		String strMsg=String.valueOf(Thread.currentThread().getId());
    	strMsg+=" "+requestid;
    	strMsg+=msg;
		com.nextlabs.Utilities.OutputDebugStringA(strMsg);
	}*/
}
