package com.nextlabs.em.windchill.install;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WebxmlConfigurer {
	private String xmlPath;
	private Document document = null;
	private boolean bWriteNeeded=false;
	public String getXmlPath() {
		return xmlPath;
	}
	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}
	public WebxmlConfigurer(String path)
	{
		this.xmlPath=path;
	}
	public void load()
	{
		document=getDocument();
	}
	private Document getDocument()
	{
		if(document!=null)
			return document;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
		builderFactory.setNamespaceAware(true);
		Document doc = null;
		try 
		{ 
	         //DOM parser instance 
	         DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
	         //parse an XML file into a DOM tree 
	         //System.out.println(ConfigurerUtils.formatString(xmlPath+" opened","file opend"));
	         doc = builder.parse(new File(xmlPath)); 

		} catch (ParserConfigurationException e) { 
	         e.printStackTrace();  
		} catch (SAXException e) { 
	         e.printStackTrace(); 
		} catch (IOException e) { 
	         e.printStackTrace(); 
		} 
		return doc;
	}
	public boolean unload()
	{
		if(this.bWriteNeeded==false)
			return false;
		try 
		{
        	TransformerFactory tFactory =TransformerFactory.newInstance();
        
        	Transformer transformer =tFactory.newTransformer();
        	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        	DOMSource source = new DOMSource(document);
        	StreamResult result = new StreamResult(new java.io.File(xmlPath));
        	    
        	transformer.transform(source, result);
        	result.getOutputStream().close();
        }
        catch(Exception exp)
        {
        	exp.printStackTrace();
        }
		return true;
	}
	private boolean validateNodeNameValue(Node filterNode,String nodeName,String nodeValue)
	{
		/*
	    	<nodeName>nodeValue</nodeName>
		 */
		boolean bRet=false;
		NodeList nodeList=filterNode.getChildNodes();
		if(nodeList==null||nodeList.getLength()==0)
			return false;
		
		for(int i=0;i<nodeList.getLength();i++)
		{
			Node node=nodeList.item(i);
			if(node!=null&&node.getNodeName().equalsIgnoreCase(nodeName))
			{
				Node classNode=node.getFirstChild();
				if(classNode!=null
						&&classNode.getNodeName().toLowerCase().equalsIgnoreCase("#text")
						&&classNode.getNodeValue().toLowerCase().equalsIgnoreCase(nodeValue))
				{
					bRet=true;
					break;
				}
			}
		}
		return bRet;
	}
	
	private Node isNodeExist(Node firstNode,String parentNodeName,String nodeName,String nodeValue)
	{
		Node currnode=firstNode;
		while(true)
		{
			if(currnode!=null)
        	{
        		if(currnode.getNodeName().equalsIgnoreCase(parentNodeName))
        		{
        			if(validateNodeNameValue(currnode,nodeName,nodeValue))
        			{
        				System.out.println(ConfigurerUtils.formatString(" "+parentNodeName+" with "+nodeName+"="+nodeValue,"nxl "+parentNodeName+" config found"));
        				break;
        			}
        		}
        		currnode=currnode.getNextSibling();
        	}
        	else
        		break;
		}
		return currnode;
	}
	
	private Node findFirstNode(Node nodeHead,String nodeName)
	{
		Node currnode=nodeHead;
		while(true)
    	{
    		if(currnode!=null)
    		{
		        if(currnode.getNodeName().equalsIgnoreCase(nodeName))
		        	break;

		        currnode=currnode.getNextSibling();
    		}
    		else
    			break;
    	}
		return currnode;
	}
	

	private Node createFilter(String ns)
	{
		/*
		<filter>
	    	<description>NextLabs Entitlement Manager filter based on policy</description>
	    	<filter-name>NextLabsEntitlementManagerFilter</filter-name>
	    	<filter-class>com.nextlabs.em.windchill.EntitlementManagerFilter</filter-class>
		</filter>
		 */
		Element filter=document.createElementNS(ns, "filter");
		
		Node desc=document.createElementNS(ns,"description");
		desc.appendChild(document.createTextNode("NextLabs Entitlement Manager filter based on policy"));
		filter.appendChild(desc);
		
		Node filterName=document.createElementNS(ns,"filter-name");
		filterName.appendChild(document.createTextNode("NextLabsEntitlementManagerFilter"));
		filter.appendChild(filterName);
		
		Node filterClass=document.createElementNS(ns,"filter-class");
		filterClass.appendChild(document.createTextNode("com.nextlabs.em.windchill.EntitlementManagerFilter"));
		filter.appendChild(filterClass);

		return filter;
	}
	public void addFilter()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"filter","filter-class","com.nextlabs.em.windchill.EntitlementManagerFilter");
        
        Node refnode=null;
        if(currnode==null)
        {
        	System.out.println(ConfigurerUtils.formatString(" filter with filter-class=com.nextlabs.em.windchill.EntitlementManagerFilter","not found"));
        	currnode=rootElement.getFirstChild();
        	refnode=findFirstNode(currnode,"filter-mapping");
        	rootElement.insertBefore(createFilter(rootElement.getNamespaceURI()), refnode);
        	bWriteNeeded=true;
        }
        
	}
	public void removeFilter()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"filter","filter-class","com.nextlabs.em.windchill.EntitlementManagerFilter");
        if(currnode!=null)
        {
        	currnode.getParentNode().removeChild(currnode);
        	bWriteNeeded=true;
        }
	}
	private Node createFilterMapping(String ns)
	{
		/*
		<filter-mapping>
	    	<filter-name>NextLabsEntitlementManagerFilter</filter-name>
	    	<url-pattern>/*</url-pattern>
		</filter-mapping>
		*/
		Element filterMapping=document.createElementNS(ns, "filter-mapping");
		
		Node filterName=document.createElementNS(ns,"filter-name");
		filterName.appendChild(document.createTextNode("NextLabsEntitlementManagerFilter"));
		filterMapping.appendChild(filterName);
		
		Node urlPattern=document.createElementNS(ns,"url-pattern");
		urlPattern.appendChild(document.createTextNode("/*"));
		filterMapping.appendChild(urlPattern);
		

		return filterMapping;
	}
	public void addFilterMapping()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"filter-mapping","filter-name","NextLabsEntitlementManagerFilter");
        
        Node refnode=null;
        if(currnode==null)
        {
        	System.out.println(ConfigurerUtils.formatString(" filter-mapping for NextLabsEntitlementManagerFilter","not found"));
        	currnode=rootElement.getFirstChild();
        	refnode=findFirstNode(currnode,"listener");
        	rootElement.insertBefore(createFilterMapping(rootElement.getNamespaceURI()), refnode);
        	bWriteNeeded=true;
        }
	}
	
	public void removeFilterMapping()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"filter-mapping","filter-name","NextLabsEntitlementManagerFilter");
        
        if(currnode!=null)
        {
        	currnode.getParentNode().removeChild(currnode);
        	bWriteNeeded=true;
        }
	}
	private Node createListener(String ns)
	{
		/*
		<listener>
			<description>NextLabs Entitlement Listener</description>
			<listener-class>com.nextlabs.em.windchill.EntitlementManagerServletContextListener</listener-class>
		</listener>
		*/
		Element listener=document.createElementNS(ns, "listener");
		
		Node desc=document.createElementNS(ns,"description");
		desc.appendChild(document.createTextNode("NextLabs Entitlement Listener"));
		listener.appendChild(desc);
		
		Node listenerClass=document.createElementNS(ns,"listener-class");
		listenerClass.appendChild(document.createTextNode("com.nextlabs.em.windchill.EntitlementManagerServletContextListener"));
		listener.appendChild(listenerClass);
		return listener;
		
	}
	public void addListener()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"listener","listener-class","com.nextlabs.em.windchill.EntitlementManagerServletContextListener");
        
        Node refnode=null;
        if(currnode==null)
        {
        	System.out.println(ConfigurerUtils.formatString(" listener with class com.nextlabs.em.windchill.EntitlementManagerServletContextListener","not found"));
        	currnode=rootElement.getFirstChild();
        	refnode=findFirstNode(currnode,"servlet");
        	rootElement.insertBefore(createListener(rootElement.getNamespaceURI()), refnode);
        	bWriteNeeded=true;
        }
	}
	public void removeListener()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"listener","listener-class","com.nextlabs.em.windchill.EntitlementManagerServletContextListener");
        
        if(currnode!=null)
        {
        	currnode.getParentNode().removeChild(currnode);
        	bWriteNeeded=true;
        }
	}
	private Node createServlet(String ns)
	{
		/*
		<servlet>
			<description>NextLabs QueryAgent Servlet</description>
			<servlet-name>QueryAgent</servlet-name>
			<servlet-class>com.sun.xml.ws.transport.http.servlet.WSServlet</servlet-class>  
			<load-on-startup>1</load-on-startup>
		</servlet>
		*/
		Element servlet=document.createElementNS(ns, "servlet");
		
		Node desc=document.createElementNS(ns,"description");
		desc.appendChild(document.createTextNode("NextLabs QueryAgent Servlet"));
		servlet.appendChild(desc);
		
		Node servletName=document.createElementNS(ns,"servlet-name");
		servletName.appendChild(document.createTextNode("QueryAgent"));
		servlet.appendChild(servletName);
		
		Node servletClass=document.createElementNS(ns,"servlet-class");
		servletClass.appendChild(document.createTextNode("com.sun.xml.ws.transport.http.servlet.WSServlet"));
		servlet.appendChild(servletClass);
		
		Node loadOnStartup=document.createElementNS(ns,"load-on-startup");
		loadOnStartup.appendChild(document.createTextNode("1"));
		servlet.appendChild(loadOnStartup);

		return servlet;
	}
	public void addServlet()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"servlet","servlet-name","QueryAgent");
        
        Node refnode=null;
        if(currnode==null)
        {
        	System.out.println(ConfigurerUtils.formatString(" servlet with servlet-name=QueryAgent","not found"));
        	currnode=rootElement.getFirstChild();
        	refnode=findFirstNode(currnode,"servlet-mapping");
        	rootElement.insertBefore(createServlet(rootElement.getNamespaceURI()), refnode);
        	bWriteNeeded=true;
        }
	}
	
	public void removeServlet()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"servlet","servlet-name","QueryAgent");
        
        if(currnode!=null)
        {
        	currnode.getParentNode().removeChild(currnode);
        	bWriteNeeded=true;
        }
	}
	
	private Node createServletMapping(String ns)
	{
		/*
		<servlet-mapping>
			<servlet-name>QueryAgent</servlet-name>
			<url-pattern>/com/nextlabs/QueryAgent</url-pattern>
		</servlet-mapping>
		*/
		Element servlet=document.createElementNS(ns, "servlet-mapping");
		
		Node servletName=document.createElementNS(ns,"servlet-name");
		servletName.appendChild(document.createTextNode("QueryAgent"));
		servlet.appendChild(servletName);
		
		Node urlPattern=document.createElementNS(ns,"url-pattern");
		urlPattern.appendChild(document.createTextNode("/com/nextlabs/QueryAgent"));
		servlet.appendChild(urlPattern);

		return servlet;
	}
	
	public void addServletMapping()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"servlet-mapping","servlet-name","QueryAgent");
        
        Node refnode=null;
        if(currnode==null)
        {
        	System.out.println(ConfigurerUtils.formatString(" servlet-mapping for servlet QueryAgent","not found"));
        	currnode=rootElement.getFirstChild();
        	refnode=findFirstNode(currnode,"session-config");
        	rootElement.insertBefore(createServletMapping(rootElement.getNamespaceURI()), refnode);
        	bWriteNeeded=true;
        }
	}
	
	public void removeServletMapping()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"servlet-mapping","servlet-name","QueryAgent");
        
        if(currnode!=null)
        {
        	currnode.getParentNode().removeChild(currnode);
        	bWriteNeeded=true;
        }
	}
	
	public void updateServletMapping(){

		NodeList servlet = document.getElementsByTagName("servlet");
		
		for (int i = 0; i < servlet.getLength(); i++) {
			Node node = servlet.item(i);
			Element eElement = (Element) node;
			String urlPattern = eElement.getElementsByTagName("servlet-name").item(0).getTextContent();
			
			if ("AttachmentsDownloadDirectionServlet".equals(urlPattern)) {
				Node servletClass = eElement.getElementsByTagName("servlet-class").item(0);
				if(servletClass.getTextContent().equals("com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet")){
					System.out.println(ConfigurerUtils.formatString(" servlet-class com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet","NXL AttachmentsDownloadDirectionServlet config found"));
				}else{
					System.out.println(ConfigurerUtils.formatString(" servlet-class com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet","NXL AttachmentsDownloadDirectionServlet config not found"));
					servletClass.setTextContent("com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet");
					bWriteNeeded=true;
				}
				
			}

		}
		
	}
	
	public void undoServletMapping(){
		
		NodeList servlet = document.getElementsByTagName("servlet");
		for (int i = 0; i < servlet.getLength(); i++) {
			Node node = servlet.item(i);
			Element eElement = (Element) node;
			String urlPattern = eElement.getElementsByTagName("servlet-name").item(0).getTextContent();
			if ("AttachmentsDownloadDirectionServlet".equals(urlPattern)) {
				Node servletClass = eElement.getElementsByTagName("servlet-class").item(0);
				//servletClass.setTextContent("com.ptc.windchill.enterprise.servlets.AttachmentsDownloadDirectionServlet");
				if(servletClass.getTextContent().equals("com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet")){
					System.out.println(ConfigurerUtils.formatString(" servlet-class com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet","NXL AttachmentsDownloadDirectionServlet config found"));
					servletClass.setTextContent("com.ptc.windchill.enterprise.servlets.AttachmentsDownloadDirectionServlet");
					bWriteNeeded=true;
				}else{
					System.out.println(ConfigurerUtils.formatString(" servlet-mapping for servlet com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet","config not found"));
					
				}
			}

		}
		
	}
}
