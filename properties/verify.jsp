<%@ page import="com.nextlabs.em.windchill.WindchillObjectHelper" %>
<%@ page import="com.nextlabs.em.windchill.QueryAgent" %>
<%@ page import="wt.org.*" %>
<%@ page import="wt.util.WTException" %>
<%@ page import="wt.method.AuthenticationException" %>
<%@ page import="java.util.*" %>
<%@ page import="wt.fc.*" %>
<%@ page import="com.nextlabs.*" %>
<%@ page import="wt.iba.value.*" %>
<%@ page import="wt.part.WTPart" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.Authenticator" %>
<%@ page import="java.net.PasswordAuthentication" %>
<%@ page import="javax.xml.namespace.QName" %>
<%@ page import="javax.xml.ws.*" %>
<%@ page import="wt.method.*" %>
<%@ page import="wt.fc.WTObject" %>
<%@ page import="wt.session.SessionHelper" %>
<%@ page import="wt.iba.value.AttributeContainer" %>
<%@ page import="wt.iba.value.service.IBAValueHelper" %>
<%@ page import="wt.iba.value.IBAValueUtility" %>
<%@ page import="wt.iba.definition.litedefinition.AttributeDefDefaultView" %>
<%@ page import="wt.iba.value.DefaultAttributeContainer" %>
<%@ page import="wt.iba.definition.service.StandardIBADefinitionService" %>
<%@ page import="wt.iba.value.litevalue.AbstractValueView" %>
<%@ page import="wt.introspection.ClassInfo" %>
<%@ page import="wt.doc.WTDocument" %>
<jsp:useBean id="wtcontext" class="wt.httpgw.WTContextBean" scope="request">
 <jsp:setProperty name="wtcontext" property="request" value="<%=request%>"/>
</jsp:useBean>
<%
//VR:wt.part.WTPart:117435
//VR:wt.doc.WTDocument:127457

	
	wt.util.WTProperties wtproperties = wt.util.WTProperties.getLocalProperties(); 
	String wthome = wtproperties.getProperty("wt.home",""); 
	out.println("wt.home : " + wthome+"<br />");
	com.nextlabs.Win32API.OutputDebugStringA("111111111111111111111");
	Persistable pObj=WindchillObjectHelper.getObject("VR:wt.doc.WTDocument:127457");
	com.ptc.core.lwc.server.LWCNormalizedObject obj =
			 new com.ptc.core.lwc.server.LWCNormalizedObject(pObj,null, java.util.Locale.US, new com.ptc.core.meta.common.DisplayOperationIdentifier());

	//obj.getTypeDescriptor();
	/* Get value of IBAName soft attribute */
	obj.load("INTERNAL_CLASSIFICATION");  
	java.lang.String string_value = (java.lang.String) obj.get("INTERNAL_CLASSIFICATION");    
	out.println("Soft attibute value : " + string_value+"<br />");

	Persistable pObjPart=WindchillObjectHelper.getObject("VR:wt.part.WTPart:117435");
	if(pObjPart instanceof wt.inf.container.WTContained)
	{
		String containerStr=null;
		wt.inf.container.WTContained contained=(wt.inf.container.WTContained)pObjPart;
		out.println("contained.getPersistInfo().getObjectIdentifier().getStringValue() <br />");
		out.println(" string value:"+contained.getPersistInfo().getObjectIdentifier().getStringValue());
		containerStr=contained.toString();
		out.println("the object is a WTContainer <br />");
		wt.inf.container.WTContainer preContainer=null;
		wt.inf.container.WTContainer curContainer=wt.inf.container.WTContainerHelper.getContainer(contained);
		int i=0;
		while(true)
		{
			i++;
			if(curContainer!=null)
			{
				containerStr=curContainer.getName()+"\\"+containerStr;
				out.println(" container String:"+containerStr+" <br />");
			}
			preContainer=curContainer;
			curContainer=preContainer.getContainer();
			if(curContainer==null)
			{
				out.println(" break curContainer is null <br />");
				break;
			}
			
			if(preContainer.equals(curContainer)==true)
			{
				out.println(" break preContainer==curContainer<br />");
			
				break;
			}
			if(i>10)
			{
				out.println(" break i>10 <br />");
				break;
			}
		}
		out.println(" container String:"+containerStr+" <br />");
	}
	else
		out.println("the object is not a WTContainer <br />");
	if(pObj instanceof WTDocument)
	{
		WTDocument doc=(WTDocument)pObj;
		if(doc.getContainer()!=null)
			out.println(" container 1:"+doc.getContainer().getName()+" <br />");
		if(doc.getContainer().getContainer()!=null)
			out.println(" container 2:"+doc.getContainer().getContainer().getName()+" <br />");
		if(doc.getContainer().getContainer().getContainer()!=null)
			out.println(" container 3:"+doc.getContainer().getContainer().getContainer().getName()+" <br />");
		if(doc.getContainer().getContainer().getContainer().getContainer()!=null)
			out.println(" container 4:"+doc.getContainer().getContainer().getContainer().getContainer().getName()+" <br />");
		if(doc.getContainer().getContainer().getContainer().getContainer().getContainer()!=null &&
				doc.getContainer().getContainer().getContainer().getContainer().getContainer().equals
				(doc.getContainer().getContainer().getContainer().getContainer())==false)
			out.println(" container 5:"+doc.getContainer().getContainer().getContainer().getContainer().getContainer().getName()+" <br />");
		
	}
	if(pObj instanceof _WTObject)
	{
		_WTObject _wtObj=(_WTObject)pObj;
		out.println("the object is a _WTObject <br />");
		out.println("display type:"+_wtObj.getDisplayType().toString()+"<br />");
	}
	else
		out.println("the object is not a _WTObject");
	
	if(pObj instanceof wt.access.SecurityLabeled)
	{
		wt.access.SecurityLabeled sLs=(wt.access.SecurityLabeled)pObj;
		out.println("the object is a SecurityLabeled:"+sLs.getSecurityLabels().toString()+" <br />");
	}
	IBAHolder ibaholder=(IBAHolder)pObj;
	{
		{
			WTDocument doc=(WTDocument)pObj;
			if(doc!=null)
				out.println(" doc name:"+doc.getName()+"<br />");
			try
			{
				ClassInfo ci=doc.getClassInfo();
				if(ci!=null)
				{
					out.println("classinfo display name:"+ci.getDisplayName()+"<br />");
					out.println("classinfo open icon:"+ci.getOpenIcon()+"<br />");
					
					ci.getAttributeValueSet("Classification");
					ClassInfo cis[]=ci.getDescendentInfos();
					if(cis!=null)
					{
						for(int i=0;i<cis.length;i++)
						{
							out.println("classinfo display name "+i+":"+cis[i].getDisplayName()+"<br />");
							
						}
					}
					
					out.println("classinfo 1:"+ci.getDisplayName()+"<br />");
					
					wt.introspection.ColumnDescriptor desc[]=ci.getAttributeColumnDescriptors("");
					out.println("classinfo:2"+ci.getDisplayName()+"<br />");
					if(desc!=null)
						out.println("column description:"+"<br />");
				}
				else
					out.println("classinfo is null"+"<br />");
			}
			catch(WTException wtExp)
			{
				out.println(" WTException:"+wtExp.getMessage());
			}
			catch(java.lang.NullPointerException nullExp)
			{
				out.println("null exception");
				
			}
			
		}
	}
	out.println("================<br/>");
	out.println("<br/>");
	out.println("<br/>");
	
  	
%>