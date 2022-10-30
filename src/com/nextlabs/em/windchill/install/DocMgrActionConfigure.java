package com.nextlabs.em.windchill.install;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DocMgrActionConfigure {
	
	private String xmlPath;
	private Document document = null;
	private boolean bWriteNeeded=false;
	public DocMgrActionConfigure(String path)
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
	        // System.out.println(ConfigurerUtils.formatString(xmlPath+" opened","file opend"));
	         doc = builder.parse(xmlPath); 

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
	
	public void updateServletClassPath() {
		NodeList servlet = document.getElementsByTagName("objecttype");


		for (int i = 0; i < servlet.getLength(); i++) {
			NamedNodeMap attr = servlet.item(i).getAttributes();

			if (attr.getNamedItem("name").toString().contains("document")) {
				NodeList nodelist = servlet.item(i).getChildNodes();
				
				for (int j = 0; j < nodelist.getLength(); j++) {
					if (nodelist.item(j).getNodeType() == Node.ELEMENT_NODE) {
						NamedNodeMap atttr = nodelist.item(j).getAttributes();
						if (atttr.getNamedItem("name").getNodeValue().equals("downloadDocumentsToCompressedFile") || atttr.getNamedItem("name").getNodeValue().equals("downloadDocsToCompressedFile") ) {
							
							Element eElement = (Element) nodelist.item(j);
							NodeList childNodes = nodelist.item(j).getChildNodes();
						
							for (int k = 0; k < childNodes.getLength(); k++) {
								
								if (childNodes.item(k).getNodeName().equals("command")) {
									NamedNodeMap attrr = childNodes.item(k).getAttributes();
									Node nodeAttr = attrr.getNamedItem("class");
									if(!attrr.getNamedItem("class").getNodeValue().equals("com.nextlabs.em.windchill.netmarkets.model.NmObjectCommands")){
										System.out.println(ConfigurerUtils.formatString(" download action with action-class com.ptc.netmarkets.model.NmObjectCommands","found"));
										nodeAttr.setTextContent("com.nextlabs.em.windchill.netmarkets.model.NmObjectCommands");
										bWriteNeeded=true;
									}else{
										System.out.println(ConfigurerUtils.formatString(" download action with action-class com.ptc.netmarkets.model.NmObjectCommands","not found"));
									}
									
									
								}
							}

						}

					}

				}
			}
		
		}

		
	}
	
	public void undoServletClassPath() {
		NodeList servlet = document.getElementsByTagName("objecttype");// .item(0);
		
		for (int i = 0; i < servlet.getLength(); i++) {
			NamedNodeMap attr = servlet.item(i).getAttributes();
			
			if (attr.getNamedItem("name").toString().contains("document")) {
				NodeList nodelist = servlet.item(i).getChildNodes();
				
				for (int j = 0; j < nodelist.getLength(); j++) {
					if (nodelist.item(j).getNodeType() == Node.ELEMENT_NODE) {
						NamedNodeMap atttr = nodelist.item(j).getAttributes();
						if (atttr.getNamedItem("name").getNodeValue().equals("downloadDocumentsToCompressedFile") || atttr.getNamedItem("name").getNodeValue().equals("downloadDocsToCompressedFile") ) {
						
							Element eElement = (Element) nodelist.item(j);
							NodeList childNodes = nodelist.item(j).getChildNodes();
							
							for (int k = 0; k < childNodes.getLength(); k++) {
								
								if (childNodes.item(k).getNodeName().equals("command")) {
									
									NamedNodeMap attrr = childNodes.item(k).getAttributes();
									Node nodeAttr = attrr.getNamedItem("class");
									if(!attrr.getNamedItem("class").getNodeValue().equals("com.ptc.netmarkets.model.NmObjectCommands")){
										System.out.println(ConfigurerUtils.formatString(" download action with action-class com.nextlabs.em.windchill.netmarkets.model.NmObjectCommands","found"));
										nodeAttr.setTextContent("com.ptc.netmarkets.model.NmObjectCommands");
										bWriteNeeded=true;
									}else{
										System.out.println(ConfigurerUtils.formatString(" download action with action-class com.nextlabs.em.windchill.netmarkets.model.NmObjectCommands","not found"));
									}
								}
							}

						}

					}

				}
			}
		
		}
		
	}

}
