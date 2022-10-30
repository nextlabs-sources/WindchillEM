package com.nextlabs;

import org.apache.log4j.Logger;

public class DenyPageHelper {
	private static String denyPageContent=readDenyPage();
	private static  Logger logger = Logger
			.getLogger(DenyPageHelper.class);
	private static String denyMessageContent = readDenyMessage();
	
	private static String statusPageContent = readStatusPage();
	
	//private static String defaultDenyPageContent=readDefaultDenyPageContent();
	
	/*private static String readDefaultDenyPageContent(){
		String defaultfileContent=null;
		String defaultMessageFile=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, com.nextlabs.Property.PEP_DENY_DEFAULT_MESSAGE);

		if(defaultMessageFile==null||defaultMessageFile.isEmpty())
			return null;
		try
		{
			java.io.File file=new java.io.File(defaultMessageFile);
			defaultfileContent=Utilities.readToString(file, "");
		}
		catch(Exception exp)
		{
			logger.info("  readDenyPage exception:"+exp.getMessage());
		}
		
		return defaultfileContent;
	}
	*/
	private static String readDenyPage()
	{
		String fileContent=null;
		String defMessageFile=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, com.nextlabs.Property.PEP_DENY_MESSAGE_FILE);

		if(defMessageFile==null||defMessageFile.isEmpty())
			return null;
		try
		{
			java.io.File file=new java.io.File(defMessageFile);
			fileContent=Utilities.readToString(file, "");
		}
		catch(Exception exp)
		{
			logger.info("  readDenyPage exception:"+exp.getMessage());
		}
		
		return fileContent;
	}
	
	private static String readStatusPage() {
		// TODO Auto-generated method stub
		String fileContent=null;
		String defStatusMessageFile=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, com.nextlabs.Property.PEP_DENY_STATUS_FILE);

		if(defStatusMessageFile==null||defStatusMessageFile.isEmpty())
			return null;
		try
		{
			java.io.File file=new java.io.File(defStatusMessageFile);
			fileContent=Utilities.readToString(file, "");
		}
		catch(Exception exp)
		{
			logger.info("  readDenyPage exception:"+exp.getMessage());
		}
		
		return fileContent;
	}

	private static String readDenyMessage()
	{
		String fileContent=null;
		String defMessageFile=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, com.nextlabs.Property.PEP_DENY_MESSAGE_INFO);

		if(defMessageFile==null||defMessageFile.isEmpty())
			return null;
		try
		{
			java.io.File file=new java.io.File(defMessageFile);
			fileContent=Utilities.readToString(file, "");
		}
		catch(Exception exp)
		{
			logger.info("  readDenyPage exception:"+exp.getMessage());
		}
		
		return fileContent;
	}
	
	public static String getPage(String policyName,String policyMessage,String refererUrl,String imageURL)
	{
		String result=null;

		if(denyPageContent==null ||denyPageContent.isEmpty())
			result=readDenyPage();
			//ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, com.nextlabs.Property.PEP_DENY_MESSAGE_DEFAULT);
		else
			result=denyPageContent;
		
		
		logger.info("======================1 deny page content ="+result);
		//logger.info("======================2==>"+policyName+"<=000");
		/*if(policyName!=null && policyName.isEmpty()==false){		
			result=result.replaceAll("@POLICY_NAME@", "Policy Name : ");
			result=result.replaceAll("@POLICY_NAME_PLACEHOLDER@", policyName+"<br/></br>");
		}else{
			result=result.replaceAll("@POLICY_NAME@", "");
			result=result.replaceAll("@POLICY_NAME_PLACEHOLDER@", "This policy is enforced by NextLabs Policy Controller");
		}
		logger.info("======================3 policy name "+policyName);*/
		if(policyMessage!=null && policyMessage.isEmpty()==false){
			result=result.replaceAll("@POLICY_MESSAGE@", "<strong>Policy Message : </strong>");
			result=result.replaceAll("@POLICY_MESSAGE_PLACEHOLDER@", policyMessage);
		}else{
			result=result.replaceAll("@POLICY_MESSAGE@", "");
			result=result.replaceAll("@POLICY_MESSAGE_PLACEHOLDER@", "");
		}
		logger.info("======================4 policy message "+policyMessage);
		if(refererUrl!=null && refererUrl.isEmpty()==false)
			result=result.replaceAll("@REFERER_URL_PLACEHOLDER@", refererUrl);
		String imageIconURL = imageURL+"/com/nextlabs/icon.png";
		String imageLogoURL = imageURL+"/com/nextlabs/NextLabsLogo.png";		
		logger.info("======================4 policy image url"+imageURL);
		result=result.replaceAll("@NXT_LOGO@", imageLogoURL);
		result=result.replaceAll("@ICON@", imageIconURL);
		logger.info("======================5 policy icon url"+imageIconURL);
		return result;
	}
	
	public static String getPage(String policyName,String policyMessage,String refererUrl)
	{
		String result=null;
		logger.info("======================1");
		if(denyPageContent==null ||denyPageContent.isEmpty())
			result=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, com.nextlabs.Property.PEP_DENY_MESSAGE_DEFAULT);
		else
			result=denyPageContent;
		logger.info("======================1 result="+result);
		logger.info("======================2==>"+policyName+"<=000");
		if(policyName!=null && policyName.isEmpty()==false)
			result=result.replaceAll("@POLICY_NAME_PLACEHOLDER@", policyName);
		logger.info("======================3");
		if(policyMessage!=null && policyMessage.isEmpty()==false)
			result=result.replaceAll("@POLICY_MESSAGE_PLACEHOLDER@", policyMessage);
		logger.info("======================4");
		if(refererUrl!=null && refererUrl.isEmpty()==false)
			result=result.replaceAll("@REFERER_URL_PLACEHOLDER@", refererUrl);
		result.replaceAll("@NXT_LOGO@", "com/nextlabs/icon.png");
		result.replaceAll("@ICON@", "com/nextlabs/icon.png");
		logger.info("======================5");
		return result;
	}
	public static String getMessage(String policyName, String policyMessage) {
		String result=null;
		logger.info("======================1");
		if(denyMessageContent==null ||denyMessageContent.isEmpty())
			result=readDenyMessage();
		else
			result=denyMessageContent;
		logger.info("======================1 result="+result);
		logger.info("======================2==>"+policyName+"<=000");
		if(policyName!=null && policyName.isEmpty()==false)
			result=result.replaceAll("@POLICY_NAME_PLACEHOLDER@", policyName);
		logger.info("======================3");
		if(policyMessage!=null && policyMessage.isEmpty()==false)
			result=result.replaceAll("@POLICY_MESSAGE_PLACEHOLDER@", policyMessage);
		logger.info("======================4");
		
		return result;
	}

	public static String getStatusPage(String policyName, String policyMessage) {
		String result=null;
		logger.info("======================1");
		if(statusPageContent==null ||statusPageContent.isEmpty())
			result=readStatusPage();
		else
			result=statusPageContent;
		logger.info("======================1 result="+result);
		logger.info("======================2==>"+policyName+"<=000");
		if(policyName!=null && policyName.isEmpty()==false)
			result=result.replaceAll("@POLICY_NAME_PLACEHOLDER@", policyName);
		logger.info("======================3");
		if(policyMessage!=null && policyMessage.isEmpty()==false)
			result=result.replaceAll("@POLICY_MESSAGE_PLACEHOLDER@", policyMessage);
		logger.info("======================4");
		
		return result;
	}
}
