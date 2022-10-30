package com.nextlabs.em.windchill;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.nextlabs.ConfigurationManager;
import com.nextlabs.em.windchill.entity.NXLSingleton;
import com.nextlabs.em.windchill.install.ConfigureHelper;

public class FileMonitorThread implements Runnable,FileListener { //extends Thread{ //
	private static  Logger logger = Logger
			.getLogger(FileMonitorThread.class);
	private int counter=0;
	private String ctxId=null;
	private DefaultFileMonitor fileMonitor = null;
	public FileMonitorThread(String id)
	{
		ctxId=id;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try
		{
			while(true)
			{
				logger.info(ctxId+"  output from FileMonitorThread.run()");

				FileSystemManager fsManager = VFS.getManager();
				if (fileMonitor == null) 
				{
					fileMonitor = new DefaultFileMonitor(this);
					String[] files=ConfigurationManager.getConfigureFiles();
					for(int i=0;i<files.length;i++)
					{
						String confPath=ConfigurationManager.getConfPath();
						String filePath=confPath+files[i];
						System.out.println(filePath);
						logger.info(ctxId+"  output from FileMonitorThread.run() monitor on file="+filePath);
						FileObject fileObject = fsManager.toFileObject(new File(filePath));
						fileMonitor.addFile(fileObject);
					}
					fileMonitor.start();
				}
				synchronized (this) {
					this.wait();
				}
				//fileMonitoring.wait(); //wait fileChange
				//startListening(fileMonitoring); //recursive call
				
			}
		}
		catch(FileSystemException fsExp){}
		catch(InterruptedException intExp){}

	}
	public void doShutdown()
	{
		logger.info(ctxId+"  output from FileMonitorThread.doShutdown()");
	}

	public void fileCreated(FileChangeEvent fce) throws Exception {
		throw new NotImplementedException();
	}
	 
	public void fileDeleted(FileChangeEvent fce) throws Exception {
		throw new NotImplementedException();
	}
 
	public void fileChanged(FileChangeEvent fce) throws Exception
	{
		logger.info(ctxId+"  fileChanged:version-->" + ++counter);
		FileName fileName = fce.getFile().getName();
		logger.info(ctxId+" file being chagned-->" +fce.getFile().getName());
		int lastIndex = fce.getFile().getName().toString().lastIndexOf("/");
		logger.info(ctxId+" action mapping file has been modified-->" +fce.getFile().getName().toString().substring(lastIndex+1));
		if(fileName.toString().contains("actionMapping.xml")){
			logger.info(ctxId+" action mapping file has been modified-->" +fce.getFile().getName());
			ConfigureHelper configHelp=new ConfigureHelper();
			String confPath=configHelp.getDocBase()+"\\com\\nextlabs\\conf\\";
			File actionMapping = new File(confPath+"actionMapping.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = null;

			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = null;

			doc = dBuilder.parse(actionMapping);

			doc.getDocumentElement().normalize();
			NXLSingleton.setNodeList(doc.getElementsByTagName("entry"));
			logger.info(ctxId+"  ##########################CALLING CLEAR CACHE###########################################################");
			NXLSingleton.clearCache();
			
		}
		ConfigurationManager.reset();
		synchronized (this) {
			this.notify();
		}
	}
}
