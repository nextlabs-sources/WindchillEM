package com.nextlabs.em.windchill;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

//import wt.util.WTProperties;

public class EntitlementManagerServletContextListener implements ServletContextListener 
{
	private static  Logger logger = Logger
			.getLogger(EntitlementManagerServletContextListener.class);
	private String ctxId=null;
	public EntitlementManagerServletContextListener()
	{
		ctxId=com.nextlabs.Utilities.RandomString(10);
	}
	private Thread fileMonitorThread = null;
	private FileMonitorThread innerThread=null;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		logger.info(ctxId+" Nextlabs Entitlement Manager servlet context destroyed");
		try
		{
			if(fileMonitorThread!=null&&innerThread!=null)
			{
				innerThread.doShutdown();
				fileMonitorThread.interrupt();
			}
        }
		catch (Exception ex) 
		{
        }
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
		logger.info(ctxId+" Nextlabs Entitlement Manager servlet context initialized ("+arg0.getServletContext().getContextPath()+")");
		try
		{
			String loc=WindchillObjectHelper.getWTTempPath();
		    loc+="\\nxldrm";
		    java.io.File dir = new java.io.File(loc);
		    if(dir.exists()==false)
		    {
		    	dir.mkdirs();
		    	logger.info(ctxId+" NXL DRM temp folder \""+loc+"\" created");
		    }
		    else
		    	logger.info(ctxId+" NXL DRM temp folder \""+loc+"\" existing");
		    	
		    
		}
		catch(Exception exp)
		{
			
		}
		
		//arg0.getServletContext().setAttribute("servletScope Context", "value from contextInitialized");
		if ((fileMonitorThread == null) || (!fileMonitorThread.isAlive())) 
		{
			innerThread=new FileMonitorThread(ctxId);
			fileMonitorThread = new Thread(innerThread);
			fileMonitorThread.start();
	    }

	}

}
