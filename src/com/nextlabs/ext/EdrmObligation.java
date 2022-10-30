package com.nextlabs.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import wt.fc.Persistable;

import com.nextlabs.ConfigurationManager;
//import com.nextlabs.CESdkConstants;
import com.nextlabs.EntitlementManagerContext;
import com.nextlabs.EvaluationResult.RuntimeObligation;
import com.nextlabs.IObligation;
import com.nextlabs.LogLevel;
import com.nextlabs.ObjectAttrCollection;
import com.nextlabs.em.windchill.WindchillObjectHelper;

public class EdrmObligation implements IObligation {

	private final static String ATTR_NAME_TAGGING	="Tagging";
	private final static String ATTR_NAME_NAME		="Name";
	private final static String ATTR_NAME_VALUE		="Value";
	private  Logger logger = Logger
			.getLogger(EdrmObligation.class);
	private final static String ATTR_VALUE_TAGGING_ALL		="All-Properties";
	private final static String ATTR_VALUE_TAGGING_USERDEF	="User-Defined";
	private final static String ATTR_VALUE_TAGGING_SYSTEM	="System-Properties";
	private final static String ATTR_VALUE_TAGGING_SPECIFIC	="Specific-Property";
	
	public static final String PEP_WINDCHILL_OBLIGATION_EDRM_REMOVE ="pep.windchill.obligation.edrm.remove.temp";
	
 	public void outputParams(String requestId,String[] oids, List<RuntimeObligation> oblist,ObjectAttrCollection userAttrs, ObjectAttrCollection[] objAttrs)
	{
		EntitlementManagerContext ctx=new EntitlementManagerContext();
		ctx.setRequestId(requestId);
		int idx=0;
		logger.debug(" ==>oid(s):");
		for(idx=0;idx<oids.length;idx++)
			logger.debug( " ====>oid "+(idx+1)+":"+oids[idx]);

		
		for(idx=0;idx<oblist.size();idx++)
		{
			logger.debug(" ==>obligation "+(idx+1)+":");
			RuntimeObligation ob=oblist.get(idx);
			Map<String,String> params=ob.getParameters();
			Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
			
			while (it.hasNext())
		    {
		        Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
		        String key=pairs.getKey();
		        String value=pairs.getValue();
		        if(key!=null&&value!=null)
				{
		        	logger.debug( "     ==>"+key+"="+value);
				}
		    }
			
		}
		
		
		logger.debug(" ==>User attribute(s):");
		if(userAttrs!=null)
		{
			HashMap<String,String> map=userAttrs.getAttrs();
			Iterator<Map.Entry<String, String>> userit = map.entrySet().iterator();
		    while (userit.hasNext()) 
		    {
		        Map.Entry<String,String> pairs = (Map.Entry<String,String>)userit.next();
		        String key=pairs.getKey();
		        String value=pairs.getValue();
		        if(key!=null&&value!=null)
				{
		        	logger.debug( "     ==>"+key+"="+value);
				}
		    }
		}
		logger.debug(" ==>Object attribute(s):");
		for(idx=0;idx<objAttrs.length;idx++)
      	{
    		if(objAttrs[idx]!=null)
    		{
    			HashMap<String,String> map=objAttrs[idx].getAttrs();
    			Iterator<Map.Entry<String, String>> objit = map.entrySet().iterator();
    			while (objit.hasNext())
    		    {
    		        Map.Entry<String,String> pairs = (Map.Entry<String,String>)objit.next();
    		        String key=pairs.getKey();
    		        String value=pairs.getValue();
    		        if(key!=null&&value!=null)
    				{
    		        	logger.debug( "     ==>"+(idx+1)+key+"="+value);
    				}
    		    }
    		}
      	}
	}

 	private void insertTag(HashMap<String, List<String>> tags, String name,String value)
 	{
 		if(tags==null||name==null||name.isEmpty()==true||value==null||value.isEmpty()==true)
 			return;
 			
 		if(tags.containsKey(name))
		{
			List<String> vals=tags.get(name);
			vals.add(value);
		}
		else
		{
			List<String> vals=new ArrayList<String>();
			vals.add(value);
			tags.put(name, vals);
		}
 		return;
 	}
 	private void insertTag(HashMap<String, List<String>> tags, ObjectAttrCollection attrCol)
 	{
 		if(tags==null||attrCol==null||attrCol.size()==0)
 			return;
 		
 		HashMap<String,String> map=attrCol.getAttrs();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
	        String key=pairs.getKey();
	        String value=pairs.getValue();
	        insertTag(tags,key,value);
	    }
 		
 		return;
 		
 		
 	}
 	private HashMap<String, List<String>> mergeTags(EntitlementManagerContext ctx,String oid, List<RuntimeObligation> oblist, ObjectAttrCollection userAttrs, ObjectAttrCollection objAttrs)
 	{
 		HashMap<String,List<String>> tags=new HashMap<String,List<String>>();
 		for(int idxOb=0;idxOb<oblist.size();idxOb++)
		{
			RuntimeObligation ob=oblist.get(idxOb);
			String tagging=ob.getParameter(ATTR_NAME_TAGGING);
			String name=ob.getParameter(ATTR_NAME_NAME);
			String value=ob.getParameter(ATTR_NAME_VALUE);
			
			if(value!=null&&value.isEmpty()==false)
				value=value.toLowerCase();
			if(tagging==null)
				continue;
			
			if(tagging.equalsIgnoreCase(ATTR_VALUE_TAGGING_ALL))
			{
				if(name.equalsIgnoreCase("$all"))
				{
					insertTag(tags,userAttrs);
					insertTag(tags,objAttrs);
				}
				else if(name.equalsIgnoreCase("$user"))
				{
					insertTag(tags,userAttrs);
				}
				else//object
				{
					insertTag(tags,objAttrs);
				}
			}
			else if(tagging.equalsIgnoreCase(ATTR_VALUE_TAGGING_SYSTEM))
			{
				
			}
			else if(tagging.equalsIgnoreCase(ATTR_VALUE_TAGGING_SPECIFIC))
			{
				ObjectAttrCollection srcAttrCol=null;
				String attrName=null;
				if(value.startsWith("$user."))
				{
					srcAttrCol=userAttrs;
					attrName=value.substring(6);
				}
				else if(value.startsWith("$object."))
				{
					srcAttrCol=objAttrs;
					attrName=value.substring(8);
				}
				else
				{
					srcAttrCol=objAttrs;
					attrName=value;
				}
				String attrValue=srcAttrCol.getAttr(attrName);
				insertTag(tags,attrName,attrValue);
			}
			else if(tagging.equalsIgnoreCase(ATTR_VALUE_TAGGING_USERDEF))
			{
				String tagname=ob.getParameter(ATTR_NAME_NAME);
				String tagvalue=ob.getParameter(ATTR_NAME_VALUE);
				if(tagvalue!=null)
				{
					tagvalue=tagvalue.toLowerCase();
					ObjectAttrCollection srcAttrCol=null;
					String attrName=null;
					if(tagvalue.startsWith("$user.")==false&&tagvalue.startsWith("$object.")==false)
					{
						insertTag(tags,tagname,tagvalue);
					}
					else
					{
						if(tagvalue.startsWith("$user."))
						{
							srcAttrCol=userAttrs;
							attrName=tagvalue.substring(6);
						}
						else if(tagvalue.startsWith("$object."))
						{
							srcAttrCol=objAttrs;
							attrName=tagvalue.substring(8);
						}
						String attrValue=srcAttrCol.getAttr(attrName);
						insertTag(tags,tagname,attrValue);
					}
				}
				else
					logger.warn(" Obligation "+ob.getName()+" value for attribute \"Value\" is incorrect when it is User Defined tagging");
			}
			else
			{
				logger.warn(" Obligation "+ob.getName()+" value for attribute \"Tagging\" is incorrect");
				continue;
			}
		}
 		return tags;
 	}
 	private void printTags(EntitlementManagerContext ctx, HashMap<String,List<String>> tags)
 	{
 		HashMap<String,List<String>> map=tags;
		Iterator<Map.Entry<String, List<String>>> it = map.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry<String,List<String>> pairs = (Map.Entry<String,List<String>>)it.next();
	        String name=pairs.getKey();
	        String value="";
	        List<String> vallist=pairs.getValue();
	        for(int idx=0;idx<vallist.size();idx++)
	        {
	        	value+=vallist.get(idx)+";";
	        }
	        logger.debug( "   "+name+"="+value);
	    }
 	}
 	@Override
	public NextAction process(String requestId,String[] oids, List<RuntimeObligation> oblist,ObjectAttrCollection userAttrs, ObjectAttrCollection[] objAttrs) 
	{
		EntitlementManagerContext ctx=new EntitlementManagerContext();
		ctx.setRequestId(requestId);
		logger.debug(" ==>EDRM obligation running");
		
		String strLoglevel=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, com.nextlabs.Property.PEP_LOGLEVEL);
		LogLevel logLevelSetting=LogLevel.parse(strLoglevel);
		LogLevel debuglvl=LogLevel.DEBUG;
		if(debuglvl.compareTo(logLevelSetting)>=0)
			outputParams(requestId,oids,oblist,userAttrs,objAttrs);
		
		
		for(int idx=0;idx<oids.length;idx++)
		{
			Persistable wcObj=WindchillObjectHelper.getObject(oids[idx]);
			if(wcObj instanceof wt.doc.WTDocument)
			{
				wt.doc.WTDocument wtDoc=(wt.doc.WTDocument) wcObj;
				try
				{
					HashMap<String,List<String>> tags=this.mergeTags(ctx, oids[idx], oblist, userAttrs, objAttrs[idx]);
					if(debuglvl.compareTo(logLevelSetting)>=0)
						printTags(ctx,tags);
					String workingpath=WindchillObjectHelper.nxlDrmDoc(ctx, wtDoc,tags);
					
					String removetemp=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, PEP_WINDCHILL_OBLIGATION_EDRM_REMOVE);
					if(removetemp==null||(removetemp.compareToIgnoreCase("No")!=0&&removetemp.compareToIgnoreCase("false")!=0))
						org.apache.commons.io.FileUtils.forceDelete(new java.io.File(workingpath));
				}
				catch(Exception exp)
				{
					logger.warn(exp);
				}
			} 
		}
		return NextAction.CONTINUE;
	}

}
