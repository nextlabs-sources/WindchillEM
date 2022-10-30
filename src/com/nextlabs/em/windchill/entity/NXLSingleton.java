package com.nextlabs.em.windchill.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.nextlabs.em.windchill.install.ConfigureHelper;

public class NXLSingleton {
	private static  Logger logger = Logger
			.getLogger(NXLSingleton.class);
	private static NXLSingleton nxtSingleton = null;

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

	private static CacheManager cm;

	private Cache cache;

	private NodeList nList;

	static {
		try {
			nxtSingleton = new NXLSingleton();
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception occured initializing the singleton instance");
		} 
	}

	private NXLSingleton() throws SAXException, IOException, ParserConfigurationException {

		ConfigureHelper configHelp=new ConfigureHelper();
		String confPath=configHelp.getDocBase()+"\\com\\nextlabs\\conf\\";
		File actionMapping = new File(confPath+"actionMapping.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;

		dBuilder = dbFactory.newDocumentBuilder();

		Document doc = null;

		try{
			doc = dBuilder.parse(actionMapping);
		}catch(FileNotFoundException e){
			logger.info("#######################################################################################################################");
			logger.info("******************NXL Singleton instance could not be initialized: actionMapping.xml file is not found*****************");
			logger.info("#######################################################################################################################");
			e.printStackTrace();
		}catch(SAXParseException e){
			System.out.println("content of the actionMapping.xml file is wrong");
			logger.info("#######################################################################################################################");
			logger.info("******************NXL Singleton instance could not be initialized: content of the actionMapping.xml file is wrong******");
			logger.info("#######################################################################################################################");
			e.printStackTrace();
		}
		
		if(null != doc){
			
			doc.getDocumentElement().normalize();
			nList = doc.getElementsByTagName("entry");
			logger.info("#######################################################################################################################");
			logger.info("******************Number of entries found in the actionMapping.xml file : "+nList.getLength()+"******");
			logger.info("#######################################################################################################################");
			
		}
		
		String cachePath = confPath+"nxlEMcache.xml";
		try{
			cm = CacheManager.newInstance(cachePath);
		}catch(CacheException e){
			logger.info("#######################################################################################################################");
			logger.info("******************NXL Singleton instance could not be initialized: nxlEMcache.xml file is not found********************");
			logger.info("#######################################################################################################################");
			e.printStackTrace();
		}
		if(null != cm){
			cache = cm.getCache("nxlem");
		}
		
	
	}

	public static NXLSingleton getInstance() {
		return nxtSingleton;
	}

	public static void updateCache(Element e) {
		getInstance().cache.put(e);
	}

	public static Element getCache(String url) {
		return getInstance().cache.get(url);
	}

	public static void acquireReadLock() {
		getInstance().lock.readLock().lock();
	}

	public static void releaseReadLock() {
		getInstance().lock.readLock().unlock();
	}

	public static void acquireWriteLock() {
		getInstance().lock.writeLock().lock();
	}

	public static void releaseWriteLock() {
		getInstance().lock.writeLock().unlock();
	}

	public static NodeList getNodeList() {
		return getInstance().nList;
	}

	public static void setNodeList(NodeList nList) {
		getInstance().nList = nList;
	}

	public static void clearCache() {
		getInstance().cm.clearAll();
		
	}
	
	public static CacheManager getCM(){
		return getInstance().cm;
	}

}
