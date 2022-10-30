package com.nextlabs.em.windchill.install;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
//import com.nextlabs.Utilities;
import wt.util.WTProperties;

public class ConfigureHelper {
	public static final String WEB_XML_FNAME		="web.xml";
	public static final String DOC_MGR_ACTION_XML   ="DocumentManagement-actions.xml";
	public static final String BROWSER_JSP    		="browser.jsp";
	public static final String CREATE_JSP    		="create.jsp";
	public static final String CREATE_MULTI         ="createMulti.jsp";
	public static final String CREATE_JSPF    		="create.jspf";
	public static final String JAXWS_XML_FNAME 		="sun-jaxws.xml";
	public static final String WAPP_PROP_FNAME 		="apacheWebAppConfig.properties";
	public static final String WAPP_AJP_CONF_FNAME	="app-Windchill-AJP.conf";
	public static final String WAPP_AUTH_CONF_FNAME ="app-Windchill-Auth.conf";
	public static final String WAPP_WC_PROP_FNAME	="app-Windchill.properties";
	

	@SuppressWarnings("serial")
	private static HashMap<String,String> filesMap=new HashMap<String,String>(){
		{
			put("web.xml",							"codebase\\WEB-INF\\web.xml");
			put("DocumentManagement-actions.xml",	"codebase\\config\\actions\\DocumentManagement-actions.xml");
			put("browser.jsp",						"codebase\\netmarkets\\jsp\\attachments\\download\\browser.jsp");
			put("create.jsp",						"codebase\\netmarkets\\jsp\\document\\create.jsp");
			put("create.jspf",						"codebase\\netmarkets\\jsp\\document\\create.jspf");
			put("sun-jaxws.xml",					"codebase\\WEB-INF\\sun-jaxws.xml");
			put("apacheWebAppConfig.properties",	"apacheConf\\config\\apacheWebAppConfig.properties");
			put("app-Windchill-Auth.conf",			"conf\\extra\\app-Windchill-Auth.conf");
			put("app-Windchill-AJP.conf",			"conf\\extra\\app-Windchill-AJP.conf");
			put("app-Windchill.properties",			"conf\\extra\\app-Windchill.properties");
			put("createMulti.jsp",					"codebase\\netmarkets\\jsp\\document\\createMulti.jsp");
			
		}
	};

	@SuppressWarnings("serial")
	private static HashMap<String,String> ptc11filesMap=new HashMap<String,String>(){
		{
			put("app-Windchill-Auth.conf",			"conf\\conf.d\\30-app-Windchill-Auth.conf");
			put("app-Windchill-AJP.conf",			"conf\\conf.d\\30-app-Windchill-AJP.conf");
			put("app-Windchill.properties",			"conf\\app-Windchill.properties");			
		}
	};
	
	private String wtHome=null;
	private String wtVersion=null;
	private String webXml=null;
	private String docMgrActionXml=null;
	private String browserJSP=null;
	private String createJSP=null;
	private String createMultiJSP=null;
	private String createJSPF=null;
	private String jaxwsXml=null;
	private String webappProp=null;
	private String apacheHome=null;
	private String appName=null;
	private String docBase=null;
	private String webappWCAuth=null;
	private String webappWCAjp=null;
	private String webappWCProp=null;
	private String ajpWorker=null;


	

	public String getWTHome()
	{
		//wtHome = wtHome="C:\\ptc\\Windchill_10.2\\Windchill";
		if(wtHome==null)
		{
			try
			{
				WTProperties wtproperties = WTProperties.getLocalProperties();
				wtHome = wtproperties.getProperty("wt.home", "");
			}
			catch(Exception exp)
			{
				System.out.println(" Exception:"+exp.getMessage());
				exp.printStackTrace();
			}
		}
		return wtHome;
	}
	
	public String getWTVersion()
	{
		//wtHome = wtHome="C:\\ptc\\Windchill_10.2\\Windchill";
		if(wtHome==null)
		{
			try
			{
				WTProperties wtproperties = WTProperties.getLocalProperties();
				wtHome = wtproperties.getProperty("wt.home", "");
			}
			catch(Exception exp)
			{
				System.out.println(" Exception:"+exp.getMessage());
				exp.printStackTrace();
			}
		}
		
		if(wtHome.contains("10.2")){
			wtVersion = "10.2";
		}else if(wtHome.contains("10.1")){
			wtVersion = "10.1";
		}else if(wtHome.contains("11")){
			wtVersion = "11";
		}
		System.out.println("Winchill Version = "+wtVersion);
		return wtVersion;
	}
	
	public String getWebXml()
	{
		if(webXml==null)
			webXml=getWTHome()+"\\"+filesMap.get(WEB_XML_FNAME);
		return webXml;
					
	}
	
	public String getDocMgrActionXML()
	{
		if(docMgrActionXml==null)
			docMgrActionXml=getWTHome()+"\\"+filesMap.get(DOC_MGR_ACTION_XML);
		return docMgrActionXml;
					
	}
	
	private String getBrowserJSP() {
		if(browserJSP==null)
			browserJSP=getWTHome()+"\\"+filesMap.get(BROWSER_JSP);
		return browserJSP;
	}
	
	private String getCreateJSP() {
		if(createJSP==null)
			createJSP=getWTHome()+"\\"+filesMap.get(CREATE_JSP);
		return createJSP;
	}

	private String getCreateJSPF() {
		if(createJSPF==null)
			createJSPF=getWTHome()+"\\"+filesMap.get(CREATE_JSPF);
		return createJSPF;
	}
	
	private String getCreateMultiJSP() {
		if(createMultiJSP==null)
			createMultiJSP=getWTHome()+"\\"+filesMap.get(CREATE_MULTI);
		return createMultiJSP;
	}
	
	public String getJaxwsXml()
	{
		if(jaxwsXml==null)
			jaxwsXml=getWTHome()+"\\"+filesMap.get(JAXWS_XML_FNAME);
		return jaxwsXml;
	}
	public String getWebappProp()
	{
		if(webappProp==null)
			webappProp=getWTHome()+"\\"+filesMap.get(WAPP_PROP_FNAME);
		return webappProp;
	}
	
	private void loadWebappProp()
	{
		InputStream is=null;
		Properties prop = new Properties(); 

		try
        {
			is = new FileInputStream(getWebappProp());
        	prop.load(is);
        	apacheHome=prop.getProperty("APACHE_HOME");
        	appName=prop.getProperty("appName");
        	docBase=prop.getProperty("docBase");
        	is.close();
        	prop.clear();
        }
        catch(IOException ioExp)
        {
        	System.out.println(" Exception:"+ioExp.getMessage());
        	ioExp.printStackTrace();
			return;
        }
	}
	public String getApacheHome()
	{
		if(apacheHome==null)
			loadWebappProp();
		return apacheHome;
	}
	public String getAppName()
	{
		if(appName==null)
			loadWebappProp();
		return appName;
	}
	public String getDocBase()
	{
		if(docBase==null)
			loadWebappProp();
		return docBase;
	}
	
	public String getWebappWCAuth()
	{
		if(webappWCAuth==null) {
			if(wtVersion.equals("11")) {
				webappWCAuth=getApacheHome()+"\\"+ptc11filesMap.get(WAPP_AUTH_CONF_FNAME);
			}else {
				webappWCAuth=getApacheHome()+"\\"+filesMap.get(WAPP_AUTH_CONF_FNAME);
			}
		}
		return webappWCAuth;
	}
	public String getWebappWCAjp()
	{
		if(webappWCAjp==null) {
			if(wtVersion.equals("11")) {
				webappWCAjp=getApacheHome()+"\\"+ptc11filesMap.get(WAPP_AJP_CONF_FNAME);
			}
			else {
				webappWCAjp=getApacheHome()+"\\"+filesMap.get(WAPP_AJP_CONF_FNAME);
			}		
		}
		return webappWCAjp;
	}
	public String getWebappWCProp()
	{
		if(webappWCProp==null) {
			if(wtVersion.equals("11")) {
				webappWCProp=getApacheHome()+"\\"+ptc11filesMap.get(WAPP_WC_PROP_FNAME);
			}
			else{
			webappWCProp=getApacheHome()+"\\"+filesMap.get(WAPP_WC_PROP_FNAME);
			}
		}
		return webappWCProp;
	}
	private void loadWebappWCProp()
	{
		InputStream is=null;
		Properties prop = new Properties(); 

		try
        {
			is = new FileInputStream(getWebappWCProp());
        	prop.load(is);
        	ajpWorker=prop.getProperty("ajpWorker");
        	is.close();
        	prop.clear();
        }
        catch(IOException ioExp)
        {
        	System.out.println(" Exception:"+ioExp.getMessage());
        	ioExp.printStackTrace();
			return;
        }
	}
	public String getAjpWorker()
	{
		if(ajpWorker==null)
			loadWebappWCProp();
		return ajpWorker;
	}
	
	/*
	 * install= ture, install; 
	 * install=false, uninstall
	 */
	public void configure(String[] args, boolean install)
	{
		boolean bDebug=false;
		for(int i=0;i<args.length;i++)
		{
			if(args[i].equalsIgnoreCase("-debug"))
				bDebug=true;
		}
		ConfigureHelper configHelper=new ConfigureHelper();
		String wtHome=configHelper.getWTHome();
		String wtVersion=configHelper.getWTVersion();
		
		if(wtHome==null||wtHome.isEmpty())
		{
			System.out.println(ConfigurerUtils.formatString(" wt.home","property not found"));
			return;
		}
		else
			System.out.println(ConfigurerUtils.formatString(" wt.home="+wtHome,"property found"));
		String webXml=null;			//web.xml
		String docMgrActionXml=null;
		String browserJSP=null;
		String createJSP=null;
		String createJSPF=null;
		String createMultiJSP=null;
		String jaxwsXml=null;		//sum-jaxws.xml
		String webappProp=null;		//apacheWebAppConfig.properties
		String apacheHome=null;
		String docBase=null;
		String appName=null;
		String webappWCAuth=null; 	//app-Windchill-Auth.conf
		String webappWCAjp=null;	//app-Windchill-AJP.conf
		String webappWCProp=null;	//app-Windchill.properties
		String ajpWorker=null;
	
		webXml=configHelper.getWebXml();

		if((new java.io.File(webXml)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+webXml,"web.xml file found"));
		else
			System.out.println(ConfigurerUtils.formatString(" "+webXml,"web.xml file not found"));
		
		docMgrActionXml=configHelper.getDocMgrActionXML();
		
		if((new java.io.File(docMgrActionXml)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+docMgrActionXml,"DocumentManagement-actions.xml file found"));
		else
			System.out.println(ConfigurerUtils.formatString(" "+docMgrActionXml,"DocumentManagement-actions.xml file not found"));
		
		browserJSP=configHelper.getBrowserJSP();
		
		if((new java.io.File(browserJSP)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+browserJSP,"browser.jsp file found"));
		else
			System.out.println(ConfigurerUtils.formatString(" "+browserJSP,"browser.jsp file not found"));

		//if(wtVersion.equals("10.1")){
			
		//	createJSP=configHelper.getCreateJSP();
		//	if((new java.io.File(createJSP)).exists())
		//		System.out.println(ConfigurerUtils.formatString(" "+createJSP,"create.jsp file found"));
		//	else
		//		System.out.println(ConfigurerUtils.formatString(" "+createJSP,"create.jsp file not found"));
			
		//}else 
		//	if(wtVersion.equals("10.2")){
			
			createJSPF=configHelper.getCreateJSPF();
			if((new java.io.File(createJSPF)).exists())
				System.out.println(ConfigurerUtils.formatString(" "+createJSPF,"create.jspf file found"));
			else
				System.out.println(ConfigurerUtils.formatString(" "+createJSPF,"create.jspf file not found"));
			
			
			createMultiJSP=configHelper.getCreateMultiJSP();
			if((new java.io.File(createMultiJSP)).exists())
				System.out.println(ConfigurerUtils.formatString(" "+createMultiJSP,"create_multi.jsp file found"));
			else
				System.out.println(ConfigurerUtils.formatString(" "+createMultiJSP,"create_multi.jsp file not found"));
			
		//}else if(wtVersion.equals("11")){
			
		//	createJSPF=configHelper.getCreateJSPF();
		//	if((new java.io.File(createJSPF)).exists())
		//		System.out.println(ConfigurerUtils.formatString(" "+createJSPF,"create.jspf file found"));
		//	else
		//		System.out.println(ConfigurerUtils.formatString(" "+createJSPF,"create.jspf file not found"));
			
		//}
		
		jaxwsXml=configHelper.getJaxwsXml();
		if((new java.io.File(jaxwsXml)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+jaxwsXml,"sun-jaxws.xml file found"));
		else
			System.out.println(ConfigurerUtils.formatString(" "+jaxwsXml,"sun-jaxws.xml file not found"));
		
		/*
		 * normally, the Apache Home path stored in apacheConf\\config\\apacheWebAppConfig.properties as property APACHE_HOME
		 * in order to get the path for file app-Windchill-Auth.conf and app-Windchill-AJP.conf, the apache home path need to be located first.
		 */
		webappProp=configHelper.getWebappProp();
		if((new java.io.File(webappProp)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+webappProp,"apacheWebAppConfig.properties file found"));
		else
			System.out.println(ConfigurerUtils.formatString(" "+webappProp,"apacheWebAppConfig.properties file not found"));
		
		
        apacheHome=configHelper.getApacheHome();
        appName=configHelper.getAppName();
        if(appName!=null&&appName.isEmpty()==false)
			System.out.println(ConfigurerUtils.formatString(" Application with name \""+appName+"\"","Windchill Application found"));
		else
			System.out.println(ConfigurerUtils.formatString(" Application with name \""+appName+"\"","Windchill Application file not found"));
        
        docBase=configHelper.getDocBase();

		if((new java.io.File(apacheHome)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+apacheHome,"Apache Home found"));
		
		if((new java.io.File(docBase)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+docBase,"Windchill Codebase found"));
		
		webappWCAuth=configHelper.getWebappWCAuth();
		if((new java.io.File(webappWCAuth)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+webappWCAuth,"app-Windchill-Auth.conf file found"));
		else
			System.out.println(ConfigurerUtils.formatString(" "+webappWCAuth,"app-Windchill-Auth.conf file not found"));
		
		webappWCAjp=configHelper.getWebappWCAjp();
		if((new java.io.File(webappWCAjp)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+webappWCAjp,"app-Windchill-AJP.conf file found"));
		else
			System.out.println(ConfigurerUtils.formatString(" "+webappWCAjp,"app-Windchill-AJP.conf file not found"));
		
		webappWCProp=configHelper.getWebappWCProp();
		if((new java.io.File(webappWCProp)).exists())
			System.out.println(ConfigurerUtils.formatString(" "+webappWCProp,"app-Windchill.properties file found"));
		else
			System.out.println(ConfigurerUtils.formatString(" "+webappWCProp,"app-Windchill.properties file not found"));
		
		
        ajpWorker=configHelper.getAjpWorker();
        
		if(ajpWorker!=null&&ajpWorker.isEmpty()==false)
			System.out.println(ConfigurerUtils.formatString(" AJP Worker with name \""+ajpWorker+"\"","ajpWorker property found"));
		else
			System.out.println(ConfigurerUtils.formatString(" AJP Worker with name \""+ajpWorker+"\"","ajpWorker property not found"));

		System.out.println(ConfigurerUtils.formatString("############################################ Configuration Check Completed ##########################################","done"));
		//////////////////////////////////////////////////////////////
		//  Start to copy/remove file
		/////////////////////////////////////////////////////////////
		JarConfigurer jarConfig=new JarConfigurer(wtVersion);
		if(install==true)
		{
			jarConfig.installConf();
			System.out.println(ConfigurerUtils.formatString("############################################ All Config Files Copied ################################################","done"));
			jarConfig.installJar();
		}
		else
		{
			jarConfig.uninstall();
		}
		
		///////////////////////////////////////////////////////////////
		//  web.xml.
		//  including <filter><filter-mapping><servlet> etc.
		///////////////////////////////////////////////////////////////
		System.out.println(ConfigurerUtils.formatString("############################################ web.xml ################################################################","start modify"));
		String back_webXml=ConfigurerUtils.backup(webXml);
		String work_webXml=ConfigurerUtils.backup(webXml,"working");
		if(back_webXml==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.WEB_XML_FNAME,"fail"));
			return;
		}
		if(work_webXml==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.WEB_XML_FNAME,"fail"));
			return;
		}
		
		WebxmlConfigurer webXmlConfig=new WebxmlConfigurer(work_webXml);
		webXmlConfig.load();
		if(install==true)
		{
			webXmlConfig.addFilter();
			webXmlConfig.addFilterMapping();
			webXmlConfig.addListener();
			webXmlConfig.addServlet();
			webXmlConfig.addServletMapping();
			//webXmlConfig.updateServletMapping();
		}
		else
		{
			webXmlConfig.removeFilter();
			webXmlConfig.removeFilterMapping();
			webXmlConfig.removeListener();
			webXmlConfig.removeServlet();
			webXmlConfig.removeServletMapping();
			//webXmlConfig.undoServletMapping();
		}
		//webXmlConfig.setXmlPath(webXml);
		boolean bOverwriteWebXml=webXmlConfig.unload();
		
		///////////////////////////////////////////////////////////////
		//  DocumentManagement-actions.xml.
		//  including com.nextlabs.em.windchill.netmarkets.model.NmObjectCommands
		///////////////////////////////////////////////////////////////
		System.out.println(ConfigurerUtils.formatString("############################################ DocumentManagement-actions.xml #########################################","start modify"));
		String back_docMgrActionXml=ConfigurerUtils.backup(docMgrActionXml);
		String work_docMgrActionXml=ConfigurerUtils.backup(docMgrActionXml,"working");
		if(back_docMgrActionXml==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.DOC_MGR_ACTION_XML,"fail"));
			return;
		}
		if(work_docMgrActionXml==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.DOC_MGR_ACTION_XML,"fail"));
			return;
		}
		
		DocMgrActionConfigure docMgrActionConfig = new DocMgrActionConfigure(work_docMgrActionXml);
		docMgrActionConfig.load();

		if(install==true){
			docMgrActionConfig.updateServletClassPath();
		}else{
			docMgrActionConfig.undoServletClassPath();
		}
		boolean bOverwriteDocMgrActionXML=docMgrActionConfig.unload();
		
		
		///////////////////////////////////////////////////////////////
		//  browser.jsp
		//  including com.nextlabs.em.windchill.netmarkets.model.NmObjectCommands
		///////////////////////////////////////////////////////////////
		if(!wtVersion.equals("11")){
		System.out.println(ConfigurerUtils.formatString("############################################ browser.jsp ############################################################","start modify"));
		String back_browserJSP=ConfigurerUtils.backup(browserJSP);
		String work_browserJSP=ConfigurerUtils.backup(browserJSP,"working");
		if(back_browserJSP==null)
		{
		System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.BROWSER_JSP,"fail"));
		return;
		}
		if(work_browserJSP==null)
		{
		System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.BROWSER_JSP,"fail"));
		return;
		}
		
		JSPConfigure jspConfigure = new JSPConfigure(work_browserJSP);
		jspConfigure.load();
		
		if(install==true){
			jspConfigure.updateJSPContent();
		}else{
			jspConfigure.undoJSPContent();
		}
		boolean bOverwriteBrowserJSP=jspConfigure.unload();
		
		
		
		///////////////////////////////////////////////////////////////
		//  create.jsp
		//  including com.nextlabs.em.windchill.netmarkets.model.NmObjectCommands
		///////////////////////////////////////////////////////////////
		
		System.out.println(ConfigurerUtils.formatString("############################################ create.jsp #############################################################","start modify"));
		String back_createJSP=null;
		String work_createJSP=null;
		//if(wtVersion.equals("10.1")){
		//	back_createJSP=ConfigurerUtils.backup(createJSP);
		//	work_createJSP=ConfigurerUtils.backup(createJSP,"working");
		//}else if(wtVersion.equals("10.2")){
			back_createJSP=ConfigurerUtils.backup(createJSPF);
			work_createJSP=ConfigurerUtils.backup(createJSPF,"working");
		//}

		//if(wtVersion.equals("10.1")){
			
		//	if(back_createJSP==null)
		//	{
		//		System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.CREATE_JSP,"fail"));
		//		return;
		//	}
		//	if(work_createJSP==null)
		//	{
		//		System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.CREATE_JSP,"fail"));
		//		return;
		//	}
			
		//}else if(wtVersion.equals("10.2")){
			
			if(back_createJSP==null)
			{
				System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.CREATE_JSPF,"fail"));
				return;
			}
			if(work_createJSP==null)
			{
				System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.CREATE_JSPF,"fail"));
				return;
			}	
		//}
		
		
		JSPConfigure jspConfigure2 = new JSPConfigure(work_createJSP);
		jspConfigure2.load();
		
		if(install==true){
			jspConfigure2.updateJSPContentForCreatePage();
		}else{
			jspConfigure2.undoJSPContentForCreatePage();
		}
		boolean bOverwriteCreateJSP=jspConfigure2.unload();
		
		///////////////////////////////////////////////////////////////
		//  createMulti.jsp
		//  including com.nextlabs.em.windchill.netmarkets.model.NmObjectCommands
		///////////////////////////////////////////////////////////////
		System.out.println(ConfigurerUtils.formatString("############################################ createMulti.jsp #############################################################","start modify"));
		String back_createMultiJSP=null;
		String work_createMultiJSP=null;
		//if(wtVersion.equals("10.1")){
		//	back_createJSP=ConfigurerUtils.backup(createJSP);
		//	work_createJSP=ConfigurerUtils.backup(createJSP,"working");
		//}else if(wtVersion.equals("10.2")){
			back_createMultiJSP=ConfigurerUtils.backup(createMultiJSP);
			work_createMultiJSP=ConfigurerUtils.backup(createMultiJSP,"working");
		//}

		//if(wtVersion.equals("10.1")){
			
		//	if(back_createJSP==null)
		//	{
		//		System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.CREATE_JSP,"fail"));
		//		return;
		//	}
		//	if(work_createJSP==null)
		//	{
		//		System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.CREATE_JSP,"fail"));
		//		return;
		//	}
			
		//}else if(wtVersion.equals("10.2")){
			
			if(back_createMultiJSP==null)
			{
				System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.CREATE_MULTI,"fail"));
				return;
			}
			if(work_createMultiJSP==null)
			{
				System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.CREATE_MULTI,"fail"));
				return;
			}	
		//}
		
		
		JSPConfigure jspConfigure3 = new JSPConfigure(work_createMultiJSP);
		jspConfigure3.load();
		
		if(install==true){
			jspConfigure3.updateJSPContentForCreatePage();
		}else{
			jspConfigure3.undoJSPContentForCreatePage();
		}
		boolean bOverwriteCreateMultiJSP=jspConfigure3.unload();		

		String targetCraeteJSP=null;
		String targetCreateMultiJSP=null;
		//if(wtVersion.equals("10.1")){
		//	targetCraeteJSP=createJSP;
		//}else if(wtVersion.equals("10.2")){
			targetCraeteJSP=createJSPF;
		//}
			targetCreateMultiJSP=createMultiJSP;
			String targetBrowseJSP = browserJSP;
					
		if(bOverwriteCreateJSP){
			System.out.println(ConfigurerUtils.formatString(" overwrite create.jsp=",""+bOverwriteCreateJSP));
			java.io.File fileWorkCreateJSP=new java.io.File(work_createJSP);
			java.io.File fileTargetCreateJSP=new java.io.File(targetCraeteJSP);
			fileTargetCreateJSP.delete();
			fileWorkCreateJSP.renameTo(fileTargetCreateJSP);
		}
		//
		if(bOverwriteCreateMultiJSP){
			System.out.println(ConfigurerUtils.formatString(" overwrite createMulti.jsp=",""+bOverwriteCreateMultiJSP));
			java.io.File fileWorkCreateMultiJSP=new java.io.File(work_createMultiJSP);
			java.io.File fileTargetCreateMultiJSP=new java.io.File(targetCreateMultiJSP);
			fileTargetCreateMultiJSP.delete();
			fileWorkCreateMultiJSP.renameTo(fileTargetCreateMultiJSP);
		}
		if(bOverwriteBrowserJSP){
			System.out.println(ConfigurerUtils.formatString(" overwrite browser.jsp=",""+bOverwriteBrowserJSP));
			java.io.File fileWorkBrowserJSP=new java.io.File(work_browserJSP);
			java.io.File fileTargetBrowserJSP=new java.io.File(targetBrowseJSP);
			fileTargetBrowserJSP.delete();
			fileWorkBrowserJSP.renameTo(fileTargetBrowserJSP);
		}
	}
		
		
		//////////////////////////////////////////////////////////////
		//   sun-jaxws.xml
		////////////////////////////////////////////////////////////////
		System.out.println(ConfigurerUtils.formatString("############################################ sun-jaxws.xml ##########################################################","start modify"));
		String back_jaxwsXml=ConfigurerUtils.backup(jaxwsXml);
		String work_jaxwsXml=ConfigurerUtils.backup(jaxwsXml,"working");
		if(back_jaxwsXml==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.JAXWS_XML_FNAME,"fail"));
			return;
		}
		if(work_jaxwsXml==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.JAXWS_XML_FNAME,"fail"));
			return;
		}
		
		JaxwsConfigurer jaxwsXmlConfig=new JaxwsConfigurer(work_jaxwsXml);
		jaxwsXmlConfig.load();
		if(install==true)
			jaxwsXmlConfig.addEndPoint();
		else
			jaxwsXmlConfig.removeEndPoint();
		
			//jaxwsXmlConfig.setXmlPath(jaxwsXml);
		boolean bOverwriteJaxwsXml=jaxwsXmlConfig.unload();
		System.out.println(ConfigurerUtils.formatString("############################################ app-Windchill-AJP.conf #################################################","start modify"));
		//////////////////////////////////////////////////////////////
		//  app-Windchill-AJP.conf
		/////////////////////////////////////////////////////////////
		String back_webappWCAjp=ConfigurerUtils.backup(webappWCAjp);
		String work_webappWCAjp=ConfigurerUtils.backup(webappWCAjp,"working");
		if(back_webappWCAjp==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.WAPP_AJP_CONF_FNAME,"fail"));
			return;
		}
		if(work_webappWCAjp==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.WAPP_AJP_CONF_FNAME,"fail"));
			return;
		}
		
		ApacheConfigurer ajpConfig=new ApacheConfigurer(work_webappWCAjp);
		ajpConfig.load();
		if(install==true)
			ajpConfig.addAjp(ajpWorker);
		else
			ajpConfig.removeAjp(ajpWorker);
		boolean bOverwriteAjp=ajpConfig.unload();
		
		System.out.println(ConfigurerUtils.formatString("############################################ app-Windchill-Auth.conf ################################################","start modify"));
		//////////////////////////////////////////////////////////////
		//  app-Windchill-Auth.conf
		/////////////////////////////////////////////////////////////
		String back_webappWCAuth=ConfigurerUtils.backup(webappWCAuth);
		String work_webappWCAuth=ConfigurerUtils.backup(webappWCAuth,"working");
		if(back_webappWCAuth==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Backup "+ConfigureHelper.WAPP_AUTH_CONF_FNAME,"fail"));
			return;
		}
		if(work_webappWCAuth==null)
		{
			System.out.println(ConfigurerUtils.formatString(" Create working copy for "+ConfigureHelper.WAPP_AUTH_CONF_FNAME,"fail"));
			return;
		}
		
		ApacheConfigurer authConfig=new ApacheConfigurer(work_webappWCAuth);
		authConfig.load();
		if(install==true)
			authConfig.addAuth(appName);
		else
			authConfig.removeAuth(appName);

		boolean bOverwriteAuth=authConfig.unload();
		if(install==true){
			System.out.println(ConfigurerUtils.formatString("############################################ config the pep.properties ##############################################","start modify"));
		}
		
		////////////////////////////////////////////////////////////////
		//   config the pep.properties
		////////////////////////////////////////////////////////////////
		if(install==true)
		{
			PepPropConfigurer pepConf=new PepPropConfigurer();
			pepConf.load();
			pepConf.install();
			pepConf.unload();
		}
		
		//////////////////////////////////////////////////////////////
		//
		////////////////////////////////////////////////////////////
		String targetWebXml		=webXml;
		String targetDocMgrActionXML = docMgrActionXml; 
	
	
		String targetJaxwsXml	=jaxwsXml;
		String targetAjp		=webappWCAjp;
		String targetAuth		=webappWCAuth;
		
		if(bDebug)
		{
			targetWebXml	=ConfigurerUtils.backup(webXml,		"target");
			targetJaxwsXml	=ConfigurerUtils.backup(jaxwsXml,		"target");
			targetAjp		=ConfigurerUtils.backup(webappWCAjp,	"target");
			targetAuth		=ConfigurerUtils.backup(webappWCAuth,	"target");
		}

		if(bOverwriteWebXml)
		{
			System.out.println(ConfigurerUtils.formatString(" overwrite web xml=",""+bOverwriteWebXml));
			java.io.File fileWorkWebXml=new java.io.File(work_webXml);
			java.io.File fileTargetWebXml=new java.io.File(targetWebXml);
			fileTargetWebXml.delete();
			fileWorkWebXml.renameTo(fileTargetWebXml);
		}
		
		if(bOverwriteDocMgrActionXML){
			System.out.println(ConfigurerUtils.formatString(" overwrite DocumentManagement-actions xml=",""+bOverwriteDocMgrActionXML));
			java.io.File fileWorkDocMgrActionXML=new java.io.File(work_docMgrActionXml);
			java.io.File fileTargetDocMgrActionXML=new java.io.File(targetDocMgrActionXML);
			fileTargetDocMgrActionXML.delete();
			fileWorkDocMgrActionXML.renameTo(fileTargetDocMgrActionXML);
		}
		

		if(bOverwriteJaxwsXml)
		{
			System.out.println(ConfigurerUtils.formatString(" overwrite jaxws.xml=",""+bOverwriteJaxwsXml));
			java.io.File fileWorkJaxwsXml=new java.io.File(work_jaxwsXml);
			java.io.File fileTargetJaxwsXml=new java.io.File(targetJaxwsXml);
			fileTargetJaxwsXml.delete();
			fileWorkJaxwsXml.renameTo(fileTargetJaxwsXml);
		}
		if(bOverwriteAjp)
		{
			System.out.println(ConfigurerUtils.formatString(" overwrite ajp=",""+bOverwriteAjp));
			java.io.File fileWorkAjp=new java.io.File(work_webappWCAjp);
			java.io.File fileTargetAjp=new java.io.File(targetAjp);
			fileTargetAjp.delete();
			fileWorkAjp.renameTo(fileTargetAjp);
		}
		if(bOverwriteAuth)
		{
			System.out.println(ConfigurerUtils.formatString(" overwrite auth=",""+bOverwriteAuth));
			java.io.File fileWorkAuth=new java.io.File(work_webappWCAuth);
			java.io.File fileTargetAuth=new java.io.File(targetAuth);
			fileTargetAuth.delete();
			fileWorkAuth.renameTo(fileTargetAuth);
		}
		
		return;
	}


	public void install(String [] args)
	{
		configure(args,true);
	}
	public void uninstall(String [] args)
	{
		configure(args,false);
	}
	
	public boolean verifyConfig()
	{
		return true;
	}
}
