package com.nextlabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import wt.util.WTProperties;

import com.nextlabs.configuration.Obligation;

public class ConfigurationManager
{
	private static String 								confFiles[]={com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME , com.nextlabs.Property.ACTION_MAPPING_FILE_NAME	};
	private static java.util.Map<String, Properties> 	confs=new java.util.HashMap<String, Properties>();
	private static ConfigurationManager 				instance=null;
	private static java.util.Map<String, Object> 		dirtyStatus=new java.util.HashMap<String,Object>();
	private static String 								password=null;
	private static  Logger logger = Logger
			.getLogger(ConfigurationManager.class);
	private ConfigurationManager(){}
	public static String 		getConfPath()
	{
		String path=null;
		try
		{
			WTProperties wtproperties = WTProperties.getLocalProperties();  
		    String wthome = wtproperties.getProperty("wt.home", "");
		    if(wthome==null ||wthome.isEmpty()==true)
		    {
				//Normally, the currentDir will be refer to [WindchillInstallPath]\Windchill\bin
				//The NextLabs conf path will be [WindchillInstallPath]\Windchill\codebase\com\nextlabs\conf
				File currentDir = new File(new File(".\\..\\codebase\\com\\nextlabs\\conf\\.").getAbsolutePath());
				if(currentDir!=null)
					path=currentDir.getCanonicalPath();
				if(path!=null && !path.isEmpty())
					path+="\\";
		    }
		    else
		    {
		    	logger.info(" **wt.home="+wthome);
		    	path=wthome+"\\codebase\\com\\nextlabs\\conf\\";
		    }
		}
		catch(Exception exp)
		{
			
		}
		return path;
	}

	private static Properties 	readConf(String fileName)
	{
		InputStream is=null;
		try
		{
			String confPath=getConfPath();
			String filePath=confPath+fileName;
			is = new FileInputStream(filePath);
		}
		catch(FileNotFoundException exp)
		{
		
		}
		if(is==null)
			return null;
		
        Properties prop = new Properties(); 
        try
        {
        	if(fileName.endsWith(".properties"))
        		prop.load(is); 
        	else if(fileName.endsWith(".xml"))
        		prop.loadFromXML(is);
        	else
        		prop=null;
        }
        catch(IOException ioExp)
        {
         
        }
        return prop;
	}
	
	private static void 		load()
	{
		for(int i=0;i<confFiles.length;i++)
		{
			Properties prop=readConf(confFiles[i]);
			if(prop!=null)
			{
				synchronized(confs)
				{
					confs.put(confFiles[i], prop);
					dirtyStatus.put(confFiles[i], (Object)false);
				}
			}
		}
	}
	
	public static void 			reset()
	{
		synchronized(confs)
		{
			confs.clear();
		}
	}
	public static String[] 		getConfigureFiles()
	{
		return confFiles;
	}
	public static synchronized ConfigurationManager getInstance() {
		if(instance == null)
		{
			instance =new ConfigurationManager();
			load();
		}
		return instance;
	}	
	public String getProperty(String fileName,String pn)
	{
		String val=null;
		if(confs.size()==0)
		{
			synchronized(confs)
			{
				load();
			}
		}
		synchronized(confs)
		{
			Properties prop=confs.get(fileName);
			if(prop!=null)
				val=(String)prop.get(pn);
		}
		return val;
	}
	public String getQueryAgentPassword()
	{
		if(password==null)
		{
			String encryptedPassword=getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,com.nextlabs.Property.PEP_WINDCHILL_QUERYAGENT_PASSWORD);
			TextProtector textProtector=new TextProtector();
			password=textProtector.decryptText(encryptedPassword);
		}
		return password;
	}
	public void setProperty(String fileName, String pn, String pv)
	{
		Properties prop=confs.get(fileName);
		if(prop!=null)
		{
			prop.setProperty(pn, pv);
			dirtyStatus.put(fileName, true);
		}
	}
	public void saveDirty()
	{
		
	}
	
	private static com.nextlabs.configuration.Configuration obligationsConf=null;
	public com.nextlabs.configuration.Configuration getObligationConfiguration()
	{
		if(obligationsConf==null)
		{
			String obXmlFileName=getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,com.nextlabs.Property.PEP_WINDCHILL_OBLIGATION_XML);
			if(obXmlFileName!=null && obXmlFileName.isEmpty()==false)
			{
				try
				{
					JAXBContext jc = JAXBContext.newInstance(com.nextlabs.configuration.Configuration.class);
			
			        Unmarshaller unmarshaller = jc.createUnmarshaller();
			        File xml = new File(obXmlFileName);
			        obligationsConf = (com.nextlabs.configuration.Configuration) unmarshaller.unmarshal(xml);
			        List<Obligation> obs=obligationsConf.getObligations();
			        for(int i=0;i<obs.size();i++)
			        {
			        	Obligation ob=obs.get(i);
			        	if(ob.getDisabled()==null)
			        		ob.setDisabled("no");
			        	if(ob.getType()==null)
			        		ob.setDisabled("yes");
			        }
				}
				catch(Exception ex)
				{
					System.out.print(ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		return obligationsConf;
	}
	
}
