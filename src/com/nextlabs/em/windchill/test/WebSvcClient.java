package com.nextlabs.em.windchill.test;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.nextlabs.ObjectAttrCollection;
import com.nextlabs.em.windchill.QueryAgent;
import com.nextlabs.em.windchill.wsclient.QueryAgent_Service;

public class WebSvcClient {

	public static void main(String[] args) {
		URL wsdlUrl=null;
		try
		{
			wsdlUrl = new URL("http://localhost:8070/nextlabs/QueryAgent?wsdl");
		}
		catch(java.net.MalformedURLException  exp)
		{
			
		}
		
		QName qName = new QName("http://www.nextlabs.com/windchill/QueryAgent", "QueryAgent");
		Service service = Service.create(wsdlUrl,qName);
		QueryAgent port = service.getPort(QueryAgent.class);
		//Credentials credentials=new UsernamePasswordCredentials("admin1","admin");
		//credentials.setUserName("admin1");
		//credentials.setPassword("admin");
		String strRet=new String();
		ObjectAttrCollection attrCol= port.QueryTest("this is a test oid",null,null,null);
		if(attrCol!=null)
		{
			System.out.println("is not null");
		}
		////////////////////////////////////////
		QueryAgent_Service queryAgentService = new QueryAgent_Service();
		com.nextlabs.em.windchill.wsclient.QueryAgent queryAgentPort = queryAgentService.getQueryAgentImplPort();
		com.nextlabs.em.windchill.wsclient.ObjectAttrCollection testAttrCol = queryAgentPort.queryTest("test", String.valueOf(Thread.currentThread().getId()), null, null);
		if(testAttrCol!=null)
		{
			System.out.println(" testAttrCol is not null");
		}
		System.out.println(strRet);
		System.exit(0);

	}
}
