package com.nextlabs.em.windchill.test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nextlabs.configuration.Obligation;

public class XmlReader {

	public static void JaxbWriteXml()
	{
		try
		{
			com.nextlabs.configuration.Configuration conf= new com.nextlabs.configuration.Configuration();
			com.nextlabs.configuration.Obligation ob1=new com.nextlabs.configuration.Obligation();
			ob1.setDisabled("Yes");
			ob1.setType("com.nextlabs.obligatioin1");
			com.nextlabs.configuration.Obligation ob2=new com.nextlabs.configuration.Obligation();
			ob2.setDisabled("no");
			ob2.setType("com.nextlabs.obligatioon2");
			
			List<Obligation> obs=new ArrayList<Obligation>();
			obs.add(ob1);
			obs.add(ob2);
			
			conf.setObligations(obs);
			JAXBContext jc = JAXBContext.newInstance(com.nextlabs.configuration.Configuration.class);
			Marshaller marshaller = jc.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.marshal(conf, System.out);
		}
		catch(Exception ex)
		{
			System.out.print(ex.getMessage());
			ex.printStackTrace();
		}
	
	}
	public static void JaxbReadXml()
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(com.nextlabs.configuration.Configuration.class);
	
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        File xml = new File("C:\\jjin\\windchillConfig.xml");
	        com.nextlabs.configuration.Configuration conf = (com.nextlabs.configuration.Configuration) unmarshaller.unmarshal(xml);
	        List<Obligation> obs=conf.getObligations();
	        for(int i=0;i<obs.size();i++)
	        {
	        	Obligation ob=obs.get(i);
	        	System.out.println("Obligation "+i+":");
	        	System.out.println("   Type="+ob.getType());
	        	System.out.println("   disabled="+ob.getDisabled());
	        	System.out.println("   name="+ob.getName());
	        	@SuppressWarnings("unchecked")
				Class<com.nextlabs.IObligation> ownClass=(Class<com.nextlabs.IObligation>) Class.forName(ob.getType());
	        	com.nextlabs.IObligation obligationClass=ownClass.newInstance();
	        	obligationClass.process(null,null, null,null,null);
	        	
	        }
	        System.out.print(conf.toString());
		}
		catch(Exception ex)
		{
			System.out.print(ex.getMessage());
			ex.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print("test");
		
		
		JaxbWriteXml();
		
		JaxbReadXml();
		try {
			  File file = new File("C:\\jjin\\test.xml");
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  Document doc = db.parse(file);
			  doc.getDocumentElement().normalize();
			  System.out.println("Root element " + doc.getDocumentElement().getNodeName());
			  NodeList nodeLst = doc.getElementsByTagName("employee");
			  System.out.println("Information of all employees");

			  for (int s = 0; s < nodeLst.getLength(); s++) {

			    Node fstNode = nodeLst.item(s);
			    
			    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
			  
			           Element fstElmnt = (Element) fstNode;
			      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("firstname");
			      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
			      NodeList fstNm = fstNmElmnt.getChildNodes();
			      System.out.println("First Name : "  + ((Node) fstNm.item(0)).getNodeValue());
			      NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("lastname");
			      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
			      NodeList lstNm = lstNmElmnt.getChildNodes();
			      System.out.println("Last Name : " + ((Node) lstNm.item(0)).getNodeValue());
			    }

			  }
			  } catch (Exception e) {
			    e.printStackTrace();
			  }

	}

}
