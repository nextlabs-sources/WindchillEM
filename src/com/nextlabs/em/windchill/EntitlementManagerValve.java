package com.nextlabs.em.windchill;

import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;

/*
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
*/
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.ServletOutputStream;  

import org.apache.catalina.Request;
//import org.apache.catalina.connector.Request;
import org.apache.catalina.Response;
import org.apache.catalina.ValveContext;
//import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.log4j.Logger;

import wt.httpgw.GatewayAuthenticator;
import wt.method.RemoteMethodServer;
import wt.org.StandardOrganizationServicesManager;
import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.WTException;

public class EntitlementManagerValve extends ValveBase {

	private static  Logger logger = Logger
			.getLogger(EntitlementManagerValve.class);
    public static WTUser getUserFromName(String name) throws WTException {  

        //Enumeration<WTUser> enumUser = OrganizationServicesHelper.manager.findUser(WTUser.NAME, name);  

    	logger.info("  Entry getUserFromName for user "+name);
    	logger.info("  Entry getUserFromName for user 10===="+name);
    	RemoteMethodServer.getDefault().setUserName("wcadmin");  
    	logger.info("  Entry getUserFromName for user 11===="+name);
    	RemoteMethodServer.getDefault().setPassword("123blue!");  
    	logger.info("  Entry getUserFromName for user 12===="+name);
    	wt.method.MethodContext wtcontext=wt.method.MethodContext.getContext();
    	logger.info("  Entry getUserFromName for user 10"+name);
    	GatewayAuthenticator auth = new GatewayAuthenticator();
    	logger.info("  Entry getUserFromName for user 1a"+name);
    	wt.org.WTPrincipal admin = SessionHelper.manager.getAdministrator();
    	logger.info("  Entry getUserFromName for user 1b"+name);
    	auth.setRemoteUser(admin.getName());
    	logger.info("  Entry getUserFromName for user 1c"+name);
    	wtcontext.setAuthentication(auth);
    	SessionHelper.manager.setAdministrator();
    	logger.info("  Entry getUserFromName for user 1d"+name);
    	
    	StandardOrganizationServicesManager orgSvcMng=StandardOrganizationServicesManager.newStandardOrganizationServicesManager();
    	
    	WTUser user = orgSvcMng.getAuthenticatedUser(name);//OrganizationServicesHelper.manager.getAuthenticatedUser(name);
    	logger.info("  Entry getUserFromName for user 1"+name);
		
         if (user == null) {  
        	 logger.info("WTUser cannot found for "+name);
            //throw new WTException("There is not existing a user who named'" + name + "'");  
        } 
        else
        {
        	logger.info("WTUser found for "+name);
        	logger.info(WTUser.ATTRIBUTES);
        	/*HashMap attrMap=user.getAdditionalAttributes();
        	
        	Iterator iter = attrMap.entrySet().iterator(); 
        	while (iter.hasNext()) { 
        	    Map.Entry entry = (Map.Entry) iter.next(); 
        	    Object key = entry.getKey(); 
        	    Object val = entry.getValue(); 
        	} */
        
        }

        return user;  
    }  
    public void invoke(Request request, Response response,ValveContext context) throws IOException, ServletException {
    	try
    	{
	        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getRequest();
	        //HttpServletResponse httpServletResponse=response.getResponse();
	        
	        logger.info("EntitlementManagerValve be called (");
	        logger.info("    url:"+httpServletRequest.getRequestURI());
	        logger.info("    path info:"+httpServletRequest.getPathInfo());
	        logger.info("    path translated:"+httpServletRequest.getPathTranslated());
	        logger.info("    servlet path:"+httpServletRequest.getServletPath());
	        
	        logger.info("    query string:"+httpServletRequest.getQueryString());
	        logger.info("    method:"+httpServletRequest.getMethod());
	        logger.info("    protocol:"+httpServletRequest.getProtocol());
	        logger.info("    remote addr:"+httpServletRequest.getRemoteAddr());
	        logger.info("    remote user:"+httpServletRequest.getRemoteUser());
	        logger.info("    auth type:"+httpServletRequest.getAuthType());
	        logger.info("    oid:"+httpServletRequest.getParameter("oid"));
	        
	        Principal principal=httpServletRequest.getUserPrincipal();
	        logger.info("    principal name:"+principal.getName());
	        String username=principal.getName();
	        if(username!=null && !username.isEmpty())
	        	getUserFromName(username);
	        
	        @SuppressWarnings("unchecked")
			Enumeration<String> headerNames =(Enumeration<String>) httpServletRequest.getHeaderNames();
	
	        while (headerNames.hasMoreElements()) {
	            String header = headerNames.nextElement();
	            String strMessage="    Header:"+header+" --> "+httpServletRequest.getHeader(header);
	            logger.info(strMessage);
	            //logger.log(Level.INFO, "Header --> {0} Value --> {1}", new Object[]{header, httpServletRequest.getHeader(header)});
	        }
    	}
    	catch(Exception ex)
    	{
    		logger.info("  exceptioin:"+ex.getMessage());
    		logger.info("  exceptioin:stacktrace:"+ex.getStackTrace());
    	}
        //ServletOutputStream stream=httpServletResponse.getOutputStream();
        //stream.write("This page is not allowed to access".getBytes());
        context.invokeNext(request, response);
        //getNext().invoke(request, response);
    }
}