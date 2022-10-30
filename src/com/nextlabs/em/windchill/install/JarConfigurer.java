package com.nextlabs.em.windchill.install;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.nextlabs.Install;



public class JarConfigurer {

	public static final String README_TXT_FNAME			="readme.txt";
	public static final String DENYPAGE_HTML_FNAME 		="denypage.html";
	public static final String DENYPAGE_MESSAGE_FNAME 	="denymessage.txt";
	public static final String DENYPAGE_STATUS_FNAME 	="setstate.html";
	public static final String NXL_PNG_FNAME	 		="nxl.png";
	public static final String OBLIGATIONX_XML_FNAME	="obligations.xml";
	public static final String PEP_PROP_FNAME		 	="pep.properties";
	public static final String RMPCCONFIG_PROP_FNAME	="rmpcconfig.properties";
	public static final String OPENAZ_PEP_PROP_FNAME	="openaz-pep-on-prem.properties";
	public static final String VERIFY_JSP_FNAME			="verify.jsp";
	public static final String KEYSTORE_JKS_FNAME		="rmskmc-keystore.jks";
	public static final String TRUSTSTORE_JKS_FNAME		="rmskmc-truststore.jks";
	public static final String ACTION_MAPPING_XML       ="actionMapping.xml";
	public static final String EHCACHE_CONFIG           ="nxlEMcache.xml";
	public static final String NXL_LOG					="NextLabsLogo.png";
	public static final String REJECT_ICON				="icon.png";
	
	public static double JAVA_VERSION = getVersion ();

	static double getVersion () {
	    String version = System.getProperty("java.version");
	    int pos = version.indexOf('.');
	    pos = version.indexOf('.', pos+1);
	    return Double.parseDouble (version.substring (0, pos));
	}

	@SuppressWarnings("serial")
	//target path is relative path to DOCBASE\com\nextlabs
	private static HashMap<String,String> confFilesMap=new HashMap<String,String>(){
		{
			put(README_TXT_FNAME,		README_TXT_FNAME);
			put(DENYPAGE_MESSAGE_FNAME, DENYPAGE_MESSAGE_FNAME);
			put(DENYPAGE_HTML_FNAME,	DENYPAGE_HTML_FNAME);
			put(DENYPAGE_STATUS_FNAME,	DENYPAGE_STATUS_FNAME);
			put(NXL_PNG_FNAME,			NXL_PNG_FNAME);
			put(OBLIGATIONX_XML_FNAME,	"conf\\"+OBLIGATIONX_XML_FNAME);
			put(PEP_PROP_FNAME,			"conf\\"+PEP_PROP_FNAME);
			put(RMPCCONFIG_PROP_FNAME,	"conf\\"+RMPCCONFIG_PROP_FNAME);
			put(OPENAZ_PEP_PROP_FNAME,	"conf\\"+OPENAZ_PEP_PROP_FNAME);
			put(KEYSTORE_JKS_FNAME,		"conf\\"+KEYSTORE_JKS_FNAME);
			put(TRUSTSTORE_JKS_FNAME,	"conf\\"+TRUSTSTORE_JKS_FNAME);
			put(ACTION_MAPPING_XML,     "conf\\"+ACTION_MAPPING_XML);
			put(EHCACHE_CONFIG    ,     "conf\\"+EHCACHE_CONFIG);
			put(VERIFY_JSP_FNAME,		VERIFY_JSP_FNAME);
			put(NXL_LOG,				NXL_LOG);
			put(REJECT_ICON,			REJECT_ICON);
		}
	};
	
	public static final String COMMONS_VFS_JAR	="commons-vfs2-2.0.jar";
	public static final String PDF_BOX_APP_JAR  ="pdfbox-app-2.0.0.jar";
	public static final String KMS_JAR			="KeyManagementService.jar";
	public static final String JNA_JAR			="jna-4.1.0.jar";
	public static final String NLJAVA_SDK_JAR	="nlJavaSDK2.jar";
	public static final String RMJAVA_SDK_JAR	="RMJavaSdk.jar";
	public static final String SLF4J_API_JAR	="slf4j-api-1.7.13.jar";
	public static final String EHCACHE_JAR      ="ehcache-2.10.1.jar";
	public static final String JTAGGER_JAR_1_6  ="nextlabs-jtagger1.6.jar";
	public static final String JTAGGER_JAR_1_7  ="nextlabs-jtagger1.7.jar";
	public static final String NXL_EM           ="NextLabs-WindchillPEP.jar";
	
	public static final String COMMONS_LANG="commons-lang-2.6.jar";
	public static final String COMMONS_LANG3="commons-lang3-3.3.2.jar";
	public static final String COMMONS_LOGGING="commons-logging-1.1.1.jar";
	public static final String GUAVA="guava-19.0.jar";
	public static final String HTTPCLIENT="httpclient-4.3.1.jar";
	public static final String HTTPCORE="httpcore-4.3.jar";
	public static final String JACKSON_ANNOTATIONS="jackson-annotations-2.6.0.jar";
	public static final String JACKSON_CORE="jackson-core-2.6.3.jar";
	public static final String JACKSON_DATABIND="jackson-databind-2.6.3.jar";
	public static final String NEXTLABS_OPENAZ="nextlabs-openaz-pep.jar";
	public static final String OPENAZ_PEP="openaz-pep-0.0.1-SNAPSHOT.jar";
	public static final String OPENAZ_XACML="openaz-xacml-0.0.1-SNAPSHOT.jar";
	public static final String CRYPT="crypt.jar";
	@SuppressWarnings("serial")
	private static HashMap<String,String> jarFilesMap_1_7=new HashMap<String,String>(){
		{
			put(COMMONS_VFS_JAR,COMMONS_VFS_JAR);
			put(KMS_JAR,KMS_JAR);
			put(JNA_JAR,JNA_JAR);
			put(NLJAVA_SDK_JAR,NLJAVA_SDK_JAR);
			put(RMJAVA_SDK_JAR,RMJAVA_SDK_JAR);
			put(SLF4J_API_JAR,SLF4J_API_JAR);
			put(EHCACHE_JAR,EHCACHE_JAR);
			put(JTAGGER_JAR_1_7,JTAGGER_JAR_1_7);
			put(PDF_BOX_APP_JAR,PDF_BOX_APP_JAR);
			put(COMMONS_LANG,COMMONS_LANG);
			put(COMMONS_LANG3,COMMONS_LANG3);
			put(COMMONS_LOGGING,COMMONS_LOGGING);
			put(GUAVA,GUAVA);
			put(HTTPCLIENT,HTTPCLIENT);
			put(HTTPCORE,HTTPCORE);
			put(JACKSON_ANNOTATIONS,JACKSON_ANNOTATIONS);
			put(JACKSON_CORE,JACKSON_CORE);
			put(JACKSON_DATABIND,JACKSON_DATABIND);
			put(NEXTLABS_OPENAZ,NEXTLABS_OPENAZ);
			put(OPENAZ_PEP,OPENAZ_PEP);
			put(OPENAZ_XACML,OPENAZ_XACML);
			put(CRYPT,CRYPT);
		
		}
	};
	
	private static HashMap<String,String> jarFilesMap_1_6=new HashMap<String,String>(){
		{
			put(COMMONS_VFS_JAR,COMMONS_VFS_JAR);
			put(KMS_JAR,KMS_JAR);
			put(JNA_JAR,JNA_JAR);
			put(NLJAVA_SDK_JAR,NLJAVA_SDK_JAR);
			put(RMJAVA_SDK_JAR,RMJAVA_SDK_JAR);
			put(SLF4J_API_JAR,SLF4J_API_JAR);
			put(EHCACHE_JAR,EHCACHE_JAR);
			put(JTAGGER_JAR_1_6,JTAGGER_JAR_1_6);
			put(PDF_BOX_APP_JAR,PDF_BOX_APP_JAR);
			put(COMMONS_LANG,COMMONS_LANG);
			put(COMMONS_LANG3,COMMONS_LANG3);
			put(COMMONS_LOGGING,COMMONS_LOGGING);
			put(GUAVA,GUAVA);
			put(HTTPCLIENT,HTTPCLIENT);
			put(HTTPCORE,HTTPCORE);
			put(JACKSON_ANNOTATIONS,JACKSON_ANNOTATIONS);
			put(JACKSON_CORE,JACKSON_CORE);
			put(JACKSON_DATABIND,JACKSON_DATABIND);
			put(NEXTLABS_OPENAZ,NEXTLABS_OPENAZ);
			put(OPENAZ_PEP,OPENAZ_PEP);
			put(OPENAZ_XACML,OPENAZ_XACML);
			put(CRYPT,CRYPT);
		}
	};
	
	private static HashMap<String,String> nxlEM = new HashMap<String,String> (){
		{
			put(NXL_EM,NXL_EM);
		}
	};
	private String wtVersion;
	public JarConfigurer(String wtVersion)
	{
		nlEmJar=JarConfigurer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		//nlEmJar="D:\\workspace\\nextlabs\\EntitlementManagers\\testing-ant-build\\Nextlabs.WindchillPEP\\dist\\Nextlabs.WindchillPEP.jar";
		if(nlEmJar!=null&&nlEmJar.isEmpty()==false)
			System.out.println(ConfigurerUtils.formatString(" NextLabs Entitlement Jar file "+nlEmJar,"found"));
		this.wtVersion=wtVersion;
	}
	private String nlEmJar=null;
	public void installFile(String targetPath,HashMap<String,String> filesMap)
	{
		try
		{
			java.util.jar.JarFile jar = new java.util.jar.JarFile(nlEmJar);
	    	java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
	    	
	    	entries = jar.entries();
	    	while (entries.hasMoreElements())
	    	{
	    		java.util.jar.JarEntry curfile = (java.util.jar.JarEntry) entries.nextElement();
	    		String targetFile=filesMap.get(curfile.getName());
	    		if(targetFile!=null&&targetFile.isEmpty()==false)
	    		{
	    			java.io.File f = new java.io.File(targetPath+ targetFile);
	    			java.io.InputStream is = jar.getInputStream(curfile); // get the input stream
		    		java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
		    		while (is.available() > 0) {  // write contents of 'is' to 'fos'
		    			fos.write(is.read());
		    		}
		    		fos.close();
		    		is.close();
		    		
		    		System.out.println(ConfigurerUtils.formatString(" "+curfile.getName()+" -------> "+f.getCanonicalPath(),curfile.getName()+" copy ok"));
	    		}
	    		else
	    		{
	    			System.out.println(ConfigurerUtils.formatString(" "+curfile.getName()+" -------> "+targetFile,curfile.getName()+" copy ok"));
	    		}
	    	}
	    	jar.close();
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}
	public void installJar()
	{
		ConfigureHelper configHelp=new ConfigureHelper();
		String libPath=configHelp.getDocBase()+"\\WEB-INF\\lib\\";
		File targetFile=new File(libPath);
		if(targetFile.exists()==false)
			targetFile.mkdirs();
		if(wtVersion.equals("11")) {
			jarFilesMap_1_6.remove(GUAVA);	
			jarFilesMap_1_7.remove(GUAVA);
		}
		if(JAVA_VERSION==1.6){
			installFile(libPath,jarFilesMap_1_6);
		}else {
			installFile(libPath,jarFilesMap_1_7);
		}
		
		
		
		//File srcFile=new File("D:\\workspace\\nextlabs\\EntitlementManagers\\testing-ant-build\\Nextlabs.WindchillPEP\\dist\\Nextlabs.WindchillPEP.jar");
		
		File srcFile=new File(Install.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String targetPath=libPath+srcFile.getName();
		String srcPath=null;
		try
		{
			System.out.println(ConfigurerUtils.formatString(" Start copy Nextlabs Entitlement to "+targetPath,"ok"));
			srcPath=srcFile.getCanonicalPath();
			//Utilities.backupTo(srcPath, targetPath);
			InputStream inStream = null;
	    	OutputStream outStream = null;
	        
	        File afile =new File(srcPath);
	        File bfile =new File(targetPath);
	        
	        inStream = new FileInputStream(afile);
	        outStream = new FileOutputStream(bfile);
	          	
	        byte[] buffer = new byte[1024];
	        int length;
	        //copy the file content in bytes 
	        while ((length = inStream.read(buffer)) > 0)
	        {
	        	outStream.write(buffer, 0, length);
	        }
	        outStream.flush(); 
	        inStream.close();
	        outStream.close();
	        
		} 
		catch(Exception exp)
		{
			System.out.println("InstallJar failed:"+exp.getMessage());
			exp.printStackTrace();
		}
		System.out.println(ConfigurerUtils.formatString("############################################ All jar files copied ###################################################","done"));
	}
	public void installConf()
	{
		ConfigureHelper configHelp=new ConfigureHelper();
		String targetPath=configHelp.getDocBase()+"\\com\\nextlabs\\";
		File targetFile=new File(targetPath);
		if(targetFile.exists()==false)
			targetFile.mkdirs();
		
		String confPath=targetPath+"conf\\";
		File confFile=new File(confPath);
		if(confFile.exists()==false)
			confFile.mkdirs();
		
		installFile(targetPath,confFilesMap);
	}
	public void uninstall()
	{
		 File f = new File(JarConfigurer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		 String deleteBatFile =  f.getAbsolutePath().replace(f.getName(), "")+"removeNXLJars.bat";
		 File remoteBat = new File(deleteBatFile);
		 FileWriter fw=null;
		try {
			fw = new FileWriter(remoteBat.getAbsoluteFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 BufferedWriter bw = new BufferedWriter(fw);
		 String echoOff = "@echo off";
		 
		ConfigureHelper configHelp=new ConfigureHelper();
		String libPath=configHelp.getDocBase()+"\\WEB-INF\\lib\\";
		libPath=libPath.replace("/", "\\");
		try
		{
			//remove jar files
			HashMap<String,String> map=null;
			if(JAVA_VERSION==1.6){
				map = jarFilesMap_1_6;
			}else if(JAVA_VERSION==1.7){
				map=jarFilesMap_1_7;
			}
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			bw.write(echoOff+System.getProperties().getProperty("line.separator"));
			int i=1;
		    while (it.hasNext()) 
		    {
		        Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
		        
		        String value=pairs.getValue();
		        File file=new File(libPath+value);
		        if(file.exists())
		        {

		        	if(file.exists()){

		        		bw.write("del "+"\""+file.getAbsolutePath()+"\""+System.getProperties().getProperty("line.separator"));
		    		}else{

		    			System.out.println(ConfigurerUtils.formatString(" "+libPath+value,"delete failed"));
		    		}

		        	i++;
		        }
		    }
		    
		    File emJar=new File(libPath+nxlEM.get(NXL_EM));
		    
		    if(emJar.exists()){

		    	bw.write("del "+"\""+libPath+nxlEM.get(NXL_EM)+"\""+System.getProperties().getProperty("line.separator"));
		    	
    		}else{
    			//System.out.println("Delete operation is failed.");
    			System.out.println(ConfigurerUtils.formatString(" "+nlEmJar,"delete failed"));
    		}
		    //String deletebat = "DEL \"%~f0\"";
		    //bw.write(deletebat);
		    bw.close();
		    //System.out.println("*  "+i+": "+ emJar.getName());
		    //System.out.println("*********************************************************");
		    //remove [codebase]\com\nextlabs
		    //Thread.sleep(5000);
		    String nlPath=configHelp.getDocBase()+"\\com\\nextlabs\\";
		    File nlFile=new File(nlPath);
		    if(nlFile.exists())
		    {
		    	org.apache.commons.io.FileUtils.forceDelete(nlFile);
			    System.out.println(ConfigurerUtils.formatString("Remove folder "+nlPath,"ok"));
		    }
		    System.out.println(ConfigurerUtils.formatString("############################################ All jar files and configuration files removed ##########################","done"));
			
		}
		catch(Exception exp)
		{
			//System.out.println(" Exception message:"+exp.getMessage());
			System.out.println(ConfigurerUtils.formatString("############################################ All jar files and configuration files removed ##########################","failed"));
			exp.printStackTrace();
		}
		
	}
}
