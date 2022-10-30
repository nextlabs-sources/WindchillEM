package com.nextlabs;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.nextlabs.EvaluationResult.RuntimeObligation;
import com.nextlabs.destiny.sdk.CEApplication;
import com.nextlabs.destiny.sdk.CEAttributes;
import com.nextlabs.destiny.sdk.CEEnforcement;
import com.nextlabs.destiny.sdk.CEResource;
import com.nextlabs.destiny.sdk.CESdk;
import com.nextlabs.destiny.sdk.CESdkException;
import com.nextlabs.destiny.sdk.CEUser;
import com.nextlabs.destiny.sdk.ICESdk;


public class PolicyEvaluator {
	private Logger logger = Logger.getLogger(PolicyEvaluator.class);
	private long 			ceConnectionHandler;
	private CESdk 			ceSdk;
	
	private CEApplication 	ceApp;
	private CEAttributes    ceAttrApp;
	
	private CEUser 			ceUser;
	private CEAttributes	ceAttrUser;
	
	private CEResource		ceSource;
	private CEAttributes    ceAttrSource;
	private CEResource		ceTarget;
	private CEAttributes	ceAttrTarget;
	
	private String 			pcIpAddr;
	private String			action;
	private int 			ms_timeout;
	private int				noiseLevel;
	private ArrayList<String>		recipients;
	
	private EntitlementManagerContext ctx;
	public EntitlementManagerContext getContext()
	{
		return ctx;
	}
	public void setContext(EntitlementManagerContext ctx)
	{
		this.ctx=ctx;
	}
	public PolicyEvaluator(String ipAddr) {
		this.pcIpAddr = ipAddr;
		noiseLevel=ICESdk.CE_NOISE_LEVEL_USER_ACTION;
		ms_timeout=10000;
		ceSdk = new CESdk();
		ceApp = new CEApplication("Windchill_EM", "N/A");
		String strTimeout=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, Property.PC_CONNECT_TIMEOUT);
		setTimeout(Integer.parseInt(strTimeout));
	}

	private CEAttributes setAttr(CEAttributes attrs,String attrName, String attrVal)
	{
		if(attrVal==null||attrVal.isEmpty())
			return attrs;
		if(attrs==null)
			attrs=new CEAttributes();
		attrs.add(attrName, attrVal);
		return attrs;
	}
 	public void setSource(String resourceName,String resourceType)
	{
		ceSource=new CEResource(resourceName,resourceType);
	}
	public void setTarget(String resourceName,String resourceType)
	{
		ceTarget=new CEResource(resourceName,resourceType);
	}
	public void setSourceAttr(String attrName,String attrVal)
	{
		ceAttrSource=setAttr(ceAttrSource,attrName,attrVal);
	}
	public void setTargetAttr(String attrName,String attrVal)
	{
		ceAttrTarget=setAttr(ceAttrTarget,attrName,attrVal);
	}
	public void setUser(String name,String id)
	{
		ceUser=new CEUser(name,id);
	}
	public void setApp(String name,String path,String url) 	
	{
		ceApp=new CEApplication(name,path,url);
	}
	public void setApp(String name,String path)
	{
		ceApp=new CEApplication(name,path);
	}
	public void setAppAttr(String attrName,String attrVal)
	{
		ceAttrApp=setAttr(ceAttrApp,attrName,attrVal);
	}
	public void setUserAttr(String attrName,String attrVal)
	{
		ceAttrUser=setAttr(ceAttrUser,attrName,attrVal);
	}
	public void setAction(String action){
		this.action=action;
	}
	public void addRecipient(String recipient)
	{
		if(recipients==null)
			recipients=new ArrayList<String>();
		recipients.add(recipient);
	}
	public void setTimeout(int ms)
	{
		this.ms_timeout=ms;
	}
	public void setIpAddr(String ipAddr)
	{
		pcIpAddr=ipAddr;
	}
	public void setNoiseLevel(int level)
	{
		this.noiseLevel=level;
	}
	public EvaluationResult eval()
	{
		logger.debug(" PolicyEvaluator start...");

		EvaluationResult result=new EvaluationResult();
		String defaultAction=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, Property.PEP_DEFAULT_ACTION);
		defaultAction=defaultAction.toLowerCase();
		ceConnectionHandler=com.nextlabs.CEConnectionPool.getInstance().getCeSdkConnection(pcIpAddr,ctx);
		if(ceConnectionHandler==0)
			logger.debug(" PolicyEvaluator ceConnectionHandler is null");
		else
			logger.debug(" PolicyEvaluator ceConnectionHandler ="+ceConnectionHandler);
		result.setAllowed(defaultAction.equalsIgnoreCase("deny")?false:true);
		CEEnforcement enforcement=null;
		try 
		{
			String arrayRec[]=null;
			if(recipients!=null)
				arrayRec=recipients.toArray(new String[recipients.size()]);

			enforcement = ceSdk.CheckResources(ceConnectionHandler, action, 
					ceSource, ceAttrSource, 
					ceTarget, ceAttrTarget,
					ceUser, ceAttrUser, 
					ceApp, ceAttrApp,
					arrayRec,
					(int) com.nextlabs.Utilities.ipToInt(pcIpAddr), 
					true, 
					this.noiseLevel, 
					ms_timeout);
			
		}
		catch (CESdkException e) 
		{
			e.printStackTrace(System.out);
			// If not cause by error connection, set ce handler to 0
			// so that connection will be re-initialize at again at next call
		logger.warn(" PolicyEvaluator Set ceHandler to 0");
			com.nextlabs.CEConnectionPool.getInstance().resetConnection(ceConnectionHandler);
		}
		catch(Exception ex)
		{
		logger.warn(" PolicyEvaluator exception:"+ex.getMessage());
			ex.printStackTrace();
			
		}
		if (enforcement == null) {
			logger.debug(" PolicyEvaluator enforcement == null");
			return result;
		}
		logger.info(" PolicyEvaluator response="+enforcement.getResponseAsString());
		if(enforcement.getResponseAsString().toLowerCase().equalsIgnoreCase("allow"))
		{
			logger.info(" PolicyEvaluator allowed");
			result.setAllowed(true);
		}
		else
			result.setAllowed(false);
		
		CEAttributes ceObligations = enforcement.getObligations();

		String[] sArrObligations = ceObligations.toArray();
		logger.debug( "size of obligations : "+sArrObligations.length);
		logger.debug( "CE::Notify obligation is not null");
		parseObligation(result,sArrObligations);
		List<RuntimeObligation> notifyOb=result.getObligation(CESdkConstants.CE_OBLIGATION_NAME_CENOTIFY);
		if(notifyOb!=null&&notifyOb.size()>0)
		{
			//logger.debug( "CE::Notify obligation is not null");
			result.setPolicyMessage(notifyOb.get(0).getParameter(CESdkConstants.CE_CENOTIFY_ATTR_NAME_NOTIFY_MESSAGE));
			result.setPolicyName(notifyOb.get(0).getPolicy());
		}
		else
		{
			//logger.debug( "CE::Notify obligation is null");
		}
		return result;
	}
	
	public int parseObligation(EvaluationResult result,String [] obStrings)
	{
		int obCount=0;
		EvaluationResult.RuntimeObligation currOb=null;
		String currName=null;
		for(int i=0;i<obStrings.length;i++)
		{
			if(obStrings[i].equalsIgnoreCase(CESdkConstants.CE_ATTR_OBLIGATION_COUNT))
			{
				obCount=Integer.parseInt(obStrings[++i]);
				continue;
			}
			else if(obStrings[i].startsWith(CESdkConstants.CE_ATTR_OBLIGATION_NAME))
			{
				String obName=obStrings[++i];
				if(obName!=null && obName.isEmpty()==false)
				{
					currOb=result.new RuntimeObligation();
					currOb.setName(obName);
					result.addObligation(currOb);
				}
				
				continue;
			}
			else if(obStrings[i].startsWith(CESdkConstants.CE_ATTR_OBLIGATION_POLICY))
			{
				if(currOb!=null)
				{
					currOb.setPolicy(obStrings[++i]);
				}
				else
					i++;
				continue;
			}
			else if(obStrings[i].startsWith(CESdkConstants.CE_ATTR_OBLIGATION_VALUE))
			{
				if(currName==null)
					currName=obStrings[++i];
				else
				{
					if(currOb!=null)
					{
						if(currOb.getName().equalsIgnoreCase(CESdkConstants.CE_OBLIGATION_NAME_CENOTIFY))
							currOb.setParameter(CESdkConstants.CE_CENOTIFY_ATTR_NAME_NOTIFY_MESSAGE, obStrings[++i]);
						else
							currOb.setParameter(currName, obStrings[++i]);
						currName=null;
					}
					else
						i++;
				}
				continue;
			}
		}
		return obCount;
	}
	
}
