package com.nextlabs.em.windchill.tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nextlabs.EntitlementManagerContext;
import com.nextlabs.em.windchill.entity.Entry;
import com.nextlabs.em.windchill.entity.NXLSingleton;

public class Url2Action {
	private static Logger logger = Logger.getLogger(Url2Action.class);

	/**
	 * 
	 * @param url
	 *            URL to be matched with entries in the action mapping
	 * @return action
	 */
	public Url2Action() {

	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	/*
	 * public static String url2Action(EntitlementManagerContext emCtx, String
	 * url) {
	 * 
	 * String action = "OPEN"; List<Entry> entries = url2Entries(emCtx, url); if
	 * (null != entries) { emCtx.log(LogLevel.INFO,
	 * "******************NUMBER OF MATCHING ENTRIES FOR URL : " + url +
	 * "****************** size of the entries to search= " + entries.size() +
	 * " "); action = doSearch(entries); } return action; }
	 */
	public static String url2Action(EntitlementManagerContext emCtx,
			String url, String httpMethod, String queryString) {
		// TODO Auto-generated method stub
		// get the matching entry list
		String action = "OPEN";
		try {
			// if(null != httpMethod || !"".equals(httpMethod)){
			// httpMethod = java.net.URLDecoder.decode(httpMethod, "UTF-8");
			// }
			if (null != queryString && !"".equals(queryString)) {
				queryString = java.net.URLDecoder.decode(queryString, "UTF-8");
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Entry> entries = url2Entries(emCtx, url);
		if (null != entries) {
			/**
			 * INFO** : URL =
			 * Windchill/ptc1/document/downloadDocumentsToCompressedFile size of
			 * the entries to search = 0 httpMethod of the request = GET
			 * queryString =
			 * ContainerOid=OR%3Awt.pdmlink.PDMLinkProduct%3A100136
			 * &u8=1&unique_page_number
			 * =58493180640673_7&AjaxEnabled=component&wizardActionClass
			 * =com.nextlabs.em.windchill.netmarkets.model.NmObjectCommands&
			 * wizardActionMethod=downloadFolderContentFiles&tableID=
			 * table__folderbrowser_PDM_TABLE
			 * &actionName=downloadDocumentsToCompressedFile
			 * &portlet=poppedup&context
			 * =comp%24folderbrowser_table%24OR%3Awt.folder
			 * .SubFolder%3A160019%24
			 * &oid=OR%3Awt.folder.SubFolder%3A160019&soid=
			 * comp%24folderbrowser_table
			 * %24OR%3Awt.folder.SubFolder%3A160019%24VR
			 * %3Awt.doc.WTDocument%3A276719
			 * !*&CSRF_NONCE=4C%2BGNy%2FLYKrMHyZ44GrfE
			 * %2FN3pFendYXxtKPGbPC4vpk%3D
			 * %3A1466662362336%3AHKNyzOyj66hlYlPAUwvU6A%3D%3D
			 */
			logger.info("**INFO**  URL =" + url
					+ "; size of the entries to search = " + entries.size()
					+ "; httpMethod of the request = " + httpMethod
					+ "; queryString = " + queryString);
			Map<String, List<String>> qS = null;
			if (null != queryString) {
				qS = processQueryString(queryString, emCtx);
			} else {
				qS = new HashMap<String, List<String>>() {
					{
						put("empty", new ArrayList<String>() {
							{
								add("empty");
							}
						});
					}
				};
			}
			logger.info("$$$$$$$$$$$$$$$$ starting to search  $$$$$$$$$$$$$$$$");
			action = doSearch(emCtx, entries, httpMethod, qS);
		}
		return action;
	}

	private static Map<String, List<String>> processQueryString(String a,
			EntitlementManagerContext emCtx) {
		String t[] = a.split("\\&");
		Map<String, List<String>> queryString = new HashMap<String, List<String>>();

		for (String string : t) {
			String b[] = string.split("=");
			if (b != null && b.length == 1) {
				continue;
			}
			String[] c = null;
			// System.out.println(b[0]+" "+b[1]);
			// emCtx.log(LogLevel.INFO,"$$$$$$$$$$$$$$$$ query string key : "+b[0]+"  $$$$$$$$$$$$$$$$");
			if (b[1].contains(":")) {

				c = b[1].split(":");

			} else {
				c = new String[1];
				c[0] = b[1];
			}
			// emCtx.log(LogLevel.INFO,"$$$$$$$$$$$$$$$$ query string key : "+b[0]+" $$$$$$$$$$$$$$$$ query strinv value : "+Arrays.toString(c));
			queryString.put(b[0], Arrays.asList(c));

		}

		return queryString;
	}

	/**
	 * 
	 * @param entries
	 * @return
	 */
	public static String doSearch(EntitlementManagerContext emCtx,
			List<Entry> entries, String httpMethod,
			Map<String, List<String>> map) {
		// TODO Auto-generated method stub

		List<Entry> httpMatchs = new ArrayList<Entry>();
		if (null != entries && entries.size() > 0) {

			for (Entry entry : entries) {
				// System.out.println("parameter size in the entry :" +
				// entry.getQueryParameters().size());
				if (entry.getHttpMethod().equals(httpMethod)) {
					logger.info("$$$$$$$$$$$$$$$$ fond http match  $$$$$$$$$$$$$$$$");
					httpMatchs.add(entry);
				}
			}

			if (null != httpMatchs && httpMatchs.size() > 0) {

				// searching for entries with matching parameters
				for (Entry httpMatch : httpMatchs) {

					// here I need to do the queryParameters2Entries
					// entries are contained in the http matches hash map
					// query parameters are in the map object

					if (queryString2EntryParameters(emCtx,
							httpMatch.getQueryParameters(), map)) {
						logger.info("$$$$$$$$$$$$$$$$" + httpMatch.getAction()
								+ "$$$$$$$$$$$$$$$$");
						return httpMatch.getAction();
					}
					/*
					 * 
					 * if (httpMatch.getQueryParameters().keySet().size() !=
					 * map.keySet().size()) { continue; } else {
					 * emCtx.log(LogLevel.INFO,
					 * "$$$$$$$$$$$$$$$$ para map and query para map size match"
					 * );
					 * 
					 * if
					 * (httpMatch.getQueryParameters().keySet().equals(map.keySet
					 * ())) { if ((new
					 * HashSet(httpMatch.getQueryParameters().values())
					 * .equals(new HashSet(map.values())))) { return
					 * httpMatch.getAction(); }
					 * 
					 * } }
					 */

				}

				return "OPEN";

			} else {
				// no matching entries found for httpMethod
				logger.info("$$$$$$$$$$$$$$$$ http match did not find, param search is not doing. action of  is entry is :"
						+ entries.get(0).getAction()
						+ " no need to search for parameters  $$$$$$$$$$$$$$$$");
				return "OPEN";
			}

		} else {
			logger.info("$$$$$$$$$$$$$$$$ there is no matching entries  $$$$$$$$$$$$$$$$");
			return "OPEN";
		}

	}

	private static boolean queryString2EntryParameters(
			EntitlementManagerContext emCtx,
			Map<String, List<String>> entryParametersMap,
			Map<String, List<String>> queryParametersMap) {

		for (Map.Entry<String, List<String>> entryParameter : entryParametersMap
				.entrySet()) {
			logger.info("$$$$$$$$$$$$$$$$queryString2EntryParameters key = "
					+ entryParameter.getKey() + " $$$$$$$$$$$$$$$$");

			// check if entry parameters contains the query parameter key
			if (queryParametersMap.containsKey(entryParameter.getKey())) {

				logger.info("$$$$$$$$$$$$$$$$ queryString2EntryParameters key contained inside the query parameters map $$$$$$$$$$$$$$$$");
				// now it's time to check if both map entries has equal values
				// as a first step of this comparison we will compare the size
				// of the two value lists
				List<String> entryValues = entryParametersMap
						.get(entryParameter.getKey());
				List<String> queryValues = queryParametersMap
						.get(entryParameter.getKey());
				// see if entry values query values
				for (String value : entryValues) {
					if (queryValues.contains(value)) {
						continue;
					} else {
						logger.info("$$$$$$$$$$$$$$$$queryString2EntryParameters value = "
								+ queryValues.contains(value)
								+ "$$$$$$$$$$$$$$$$");
						return false;
					}
				}
			} else {
				// key value of the parameter in the entry does not contain in
				// the query string
				return false;
			}

		}
		logger.info("$$$$$$$$$$$$$$$$queryString2EntryParameters !!! match found !!! $$$$$$$$$$$$$$$$");
		return true;
	}

	/**
	 * 
	 * @param entries
	 * @return
	 * 
	 *         public static String doSearch(List<Entry> entries) {
	 * 
	 *         if (null != entries) { if (entries.size() > 0) return
	 *         entries.get(0).getAction(); else return "OPEN"; } else { return
	 *         "OPEN"; }
	 * 
	 *         }
	 */
	/**
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Entry> url2Entries(EntitlementManagerContext emCtx,
			String url) {

		List<Entry> entryList;
		try {
			NXLSingleton nxlSingleton = NXLSingleton.getInstance();
		} catch (Throwable t) {
			logger.info("#######################################################################################################################");
			logger.info("******************NXL Singleton instance could not be initialized: read the logs for more information******************");
			logger.info("#######################################################################################################################");
			t.printStackTrace();
			return null;
		}

		net.sf.ehcache.Element el = null;

		try {
			NXLSingleton.acquireReadLock();
			el = (net.sf.ehcache.Element) NXLSingleton.getCache(url);
		} finally {
			NXLSingleton.releaseReadLock();
		}

		if (null != el) {
			/* match found in the cache */
			entryList = (List<Entry>) NXLSingleton.getCache(url)
					.getObjectValue();
			if (null != entryList) {
				logger.info("******************NUMBER OF MATCHING ENTREIS FROM CACHE : ******************"
						+ entryList.size());
			} else {
				logger.info("******************NUMBER OF MATCHING ENTREIS FROM CACHE : ******************"
						+ null);
			}
			return entryList;
		} else {
			/* match did not find in the cache */
			NodeList nList = NXLSingleton.getNodeList();

			if (null == nList) {
				logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ nList is null ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				return null;
			}

			logger.info("#############################################################################"
					+ nList.getLength());
			entryList = url2UrlPattern(nList, url);
			if (null != entryList) {
				logger.info("******************NUMBER OF MATCH FOUND IN THE DOM : "
						+ url + "******************" + entryList.size());
			} else {
				logger.info("******************NUMBER OF MATCH FOUND IN THE DOM : "
						+ url + "******************" + null);
			}

			try {
				NXLSingleton.acquireWriteLock();
				NXLSingleton.updateCache(new net.sf.ehcache.Element(url,
						entryList));
			} finally {
				NXLSingleton.releaseWriteLock();
			}
			return entryList;

		}
	}

	/**
	 * Method perform the matching of URL to entries in the XML
	 * 
	 * @param nList
	 *            entries in the XML
	 * @param url
	 *            URL to be matched with
	 * @return list of matched entries
	 */

	/*
	 * public static List<Entry> url2UrlPattern(NodeList nList, String url) {
	 * List<Entry> entryList = new ArrayList<Entry>(); for (int temp = 0; temp <
	 * nList.getLength(); temp++) { Node nNode = nList.item(temp);
	 * 
	 * if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 * 
	 * Element eElement = (Element) nNode; String urlPattern =
	 * eElement.getElementsByTagName("url").item(0).getTextContent();
	 * 
	 * if (RegexMatches.matcher(url, Pattern.compile(urlPattern))) {
	 * 
	 * String httpMethod =
	 * eElement.getElementsByTagName("httpMethod").item(0).getTextContent();
	 * String action =
	 * eElement.getElementsByTagName("action").item(0).getTextContent();
	 * 
	 * Entry e = new Entry(); e.setURLPattern(urlPattern);
	 * e.setHttpMethod(httpMethod); e.setAction(action); entryList.add(e);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } return entryList; }
	 */
	public static List<Entry> url2UrlPattern(NodeList nList, String url) {
		List<Entry> entryList = new ArrayList<Entry>();
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				String urlPattern = eElement.getElementsByTagName("url")
						.item(0).getTextContent();

				if (RegexMatches.matcher(url, Pattern.compile(urlPattern))) {

					String httpMethod = eElement
							.getElementsByTagName("httpMethod").item(0)
							.getTextContent();

					String action = eElement.getElementsByTagName("action")
							.item(0).getTextContent();

					NodeList paramNodesList = eElement
							.getElementsByTagName("parameter");

					// System.out.println("number of parameters" +
					// paramNodesList.getLength());

					Map<String, List<String>> paraMap = new HashMap<String, List<String>>();
					for (int i = 0; i < paramNodesList.getLength(); i++) {
						Node node = paramNodesList.item(i);

						NamedNodeMap atttr = paramNodesList.item(i)
								.getAttributes();

						NodeList valueList = node.getChildNodes();

						String[] sa = null;
						List<String> vList = new ArrayList<String>();
						for (int k = 0; k < valueList.getLength(); k++) {

							Node val = valueList.item(k);

							if (val.getNodeType() == Node.ELEMENT_NODE) {

								Element vnode = (Element) val;
								sa = new String[vnode.getElementsByTagName(
										"value").getLength()];

								vList.add(val.getTextContent());
							}

						}
						// System.out.println("&&&&" + vList.size());
						paraMap.put(atttr.getNamedItem("name").getNodeValue(),
								vList);
					}

					Entry e = new Entry();
					e.setQueryParameters(paraMap);
					e.setURLPattern(urlPattern);
					e.setHttpMethod(httpMethod);
					e.setAction(action);
					entryList.add(e);

				}

			}

		}
		return entryList;
	}

}
