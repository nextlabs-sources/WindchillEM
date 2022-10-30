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
import org.xml.sax.SAXException;
public class JaxwsConfigurer {
	private String xmlPath;
	private Document document = null;
	private boolean bWriteNeeded=false;
	public JaxwsConfigurer(String path)
	{
		this.xmlPath=path;
	}
	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
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
		if(bWriteNeeded==false)
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
	
	private Node isNodeExist(Node header, String nodeName, String attrName,String attrValue)
	{
		Node currnode=header;

		while(true)
		{
			if(currnode!=null)
        	{
				if(currnode.getNodeType()==Node.ELEMENT_NODE)
				{
					Element element=(Element)currnode;
					String val=element.getAttribute(attrName);
					//System.out.println(ConfigurerUtils.formatString("Node "+nodeName+" with "+attrName+"="+val,"test"));
					if(val!=null&&val.equalsIgnoreCase(attrValue.toLowerCase()))
					{
						System.out.println(ConfigurerUtils.formatString(" Node "+nodeName+" with "+attrName+"="+attrValue,"found"));
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
	private Node createEndPoint(String ns)
	{
		/*
		<endpoint
			name="QueryAgent"
			implementation="com.nextlabs.em.windchill.QueryAgentImpl"
			url-pattern="/com/nextlabs/QueryAgent" />
		 */
		Element endPoint=document.createElementNS(ns, "endpoint");
		endPoint.setAttribute("name", "QueryAgent");
		endPoint.setAttribute("implementation", "com.nextlabs.em.windchill.QueryAgentImpl");
		endPoint.setAttribute("url-pattern", "/com/nextlabs/QueryAgent");
		return endPoint;
	}
	public void addEndPoint()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"endpoint","name","QueryAgent");

        if(currnode==null)
        {
        	System.out.println(ConfigurerUtils.formatString(" endpoint node for QueryAgent","not found"));
        	rootElement.appendChild(createEndPoint(rootElement.getNamespaceURI()));
        	bWriteNeeded=true;
        }else{
        	System.out.println(ConfigurerUtils.formatString(" endpoint node for QueryAgent","found"));
        }
	}
	public void removeEndPoint()
	{
		Element rootElement = document.getDocumentElement();
        Node currnode=rootElement.getFirstChild();
        currnode=isNodeExist(currnode,"endpoint","name","QueryAgent");

        if(currnode!=null)
        {
        	System.out.println(ConfigurerUtils.formatString(" endpoint node for QueryAgent","found"));
        	currnode.getParentNode().removeChild(currnode);
        	bWriteNeeded=true;
        }else{
        	System.out.println(ConfigurerUtils.formatString(" endpoint node for QueryAgent","not found"));
        }
	}
	
	public static void main(String args[])
	{
		String path="C:/Source/sun-jaxws.xml";
		JaxwsConfigurer jc=new JaxwsConfigurer(path);
		jc.load();
	}
}
