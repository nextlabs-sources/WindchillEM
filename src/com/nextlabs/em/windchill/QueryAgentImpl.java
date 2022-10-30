package com.nextlabs.em.windchill;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Logger;
import org.apache.openaz.pepapi.Action;
import org.apache.openaz.pepapi.PepResponse;
import org.apache.openaz.pepapi.Resource;
import org.apache.openaz.pepapi.Subject;
import org.apache.openaz.xacml.api.AttributeAssignment;
import org.apache.openaz.xacml.api.Decision;
import org.apache.openaz.xacml.api.Obligation;
import wt.doc.WTDocument;
import wt.fc.Persistable;
import wt.fc.ReferenceFactory;
import wt.fc.WTReference;
import wt.inf.container.WTContainerHelper;
import wt.org.WTGroup;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.util.WTException;
import com.nextlabs.ConfigurationManager;
import com.nextlabs.EntitlementManagerContext;
import com.nextlabs.EvaluationResult;
import com.nextlabs.EvaluationResult.RuntimeObligation;
import com.nextlabs.OPENAZSdkConstants;
import com.nextlabs.ObjectAttrCollection;
import com.nextlabs.cache.CacheEngine;
import com.nextlabs.cache.PolicyDecisionObject;
import com.nextlabs.openaz.utils.Constants;
import com.nextlabs.windchil.em.restapi.JPCRestCall;

@WebService(endpointInterface = "com.nextlabs.em.windchill.QueryAgent", serviceName = "QueryAgent", targetNamespace = "http://www.nextlabs.com/windchill/QueryAgent")
public class QueryAgentImpl implements QueryAgent {
	public static ConcurrentLinkedQueue<WTDocument> documents = new ConcurrentLinkedQueue<WTDocument>();
	public static long time = 0;
	private Logger logger = Logger.getLogger(QueryAgentImpl.class);
	private String userId = ConfigurationManager.getInstance().getProperty(
			com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
			com.nextlabs.Property.PEP_USER_ID);
	private String defaultDecision = ConfigurationManager.getInstance()
			.getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PEP_DEFAULT_ACTION);
	private String dontcareDecision = ConfigurationManager.getInstance()
			.getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PEP_DEFAULT_DONTCARE_DECISION);
	private String indeterminateDecision = ConfigurationManager.getInstance()
			.getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PEP_DEFAULT_INDETERMINATE_DECISION);
	private String userAttributesToSend = ConfigurationManager.getInstance()
			.getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PEP_DEFAULT_USER_ATTRIBUTES_TO_SEND);
	private String resourceAttributesToSend = ConfigurationManager
			.getInstance()
			.getProperty(
					com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PEP_DEFAULT_RESOURCE_ATTRIBTES_TO_SEND);

	@Override
	public List<String> Eval(@WebParam(name = "requestId") String requestId,
			@WebParam(name = "paras") String[] paras,
			@WebParam(name = "reserved") String reserved)
			throws QueryAgentException {
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		emCtx.setRequestId(requestId);
		java.util.List<String> retList = new java.util.ArrayList<String>();

		try {
			String username = null, oids = null, policyaction = null, url = null, referer = null, querystring = null;
			HashMap<String, String> specialResourceAttributes = new HashMap<String, String>();
			for (int i = 0; i < paras.length; i++) {
				if (i == paras.length - 1) {
					logger.debug("Para " + i / 2 + ":" + paras[i]
							+ " is discarded");
				} else {
					logger.debug("Para " + i / 2 + ":" + paras[i] + "="
							+ paras[i + 1]);
					if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME))
						username = paras[++i];
					else if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_OIDS))
						oids = paras[++i];
					else if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_POLICYACTION))
						policyaction = paras[++i];
					else if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_URL))
						url = paras[++i];
					else if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_REFERER))
						referer = paras[++i];
					else if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_QUERYSTRING))
						querystring = paras[++i];
					else if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_ACTIONNAME))
						specialResourceAttributes.put("actionName", paras[++i]);
					else if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_WIZARDACTIONCLASS))
						specialResourceAttributes.put("wizardActionClass",
								paras[++i]);
					else if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_TABLEID))
						specialResourceAttributes.put("tableID", paras[++i]);
				}
			}
			emCtx.setUserName(username);
			logger.debug("Getting CloudAZ File Path Property");
			EvaluationResult result = null;
			String cloudAzFilePath = ConfigurationManager
					.getInstance()
					.getProperty(
							com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
							com.nextlabs.Property.NEXTLABS_CLOOUDAZ_PROPERTIES_FILE_PATH);
			logger.debug("CloudAzFilePath:" + cloudAzFilePath);

			if (specialResourceAttributes.size() > 0) {
				result = callRestApiEvaluator(username, emCtx, policyaction,
						url, referer, querystring, null, cloudAzFilePath,
						specialResourceAttributes);

			} else {
				String[] oidsArray = oids
						.split(com.nextlabs.em.windchill.Constants.PARANAME_SEPARATOR);
				logger.debug("After  original url oidsArray:" + oidsArray);
				com.nextlabs.ObjectAttrCollection objAttrs = null;
				for (int idx = 0; idx < oidsArray.length; idx++) {
					String oid = oidsArray[idx];
					logger.debug(" EntitlementManagerFilter queryOid(" + oid
							+ ")");
					objAttrs = QueryOid(oid, emCtx.getRequestId(), null, null);
					result = callRestApiEvaluator(username, emCtx,
							policyaction, url, referer, querystring, objAttrs,
							cloudAzFilePath, specialResourceAttributes);
					if (result != null && result.isAllowed() == false) {
						logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
						break;
					}
				}
				com.nextlabs.ObjectAttrCollection objAttr[] = new com.nextlabs.ObjectAttrCollection[1];
				com.nextlabs.ObjectAttrCollection userAttrCol = null;
				if (result != null)
					result.print(emCtx);

				if (result != null) {
					logger.debug(" Start to execute obligation(s)");
					objAttr[0] = objAttrs;
					userAttrCol = this.QueryUser(username,
							emCtx.getRequestId(), null, null);
					result.ExecObligation(emCtx, oidsArray, userAttrCol,
							objAttr);
				}
			}

			String policyName = result.getPolicyName();
			logger.debug("policy name:" + policyName);
			String policyMessage = result.getPolicyMessage();
			logger.debug("policy message:" + policyMessage);
			if (policyName != null && policyName.isEmpty() == false) {
				retList.add(com.nextlabs.em.windchill.Constants.PARANAME_POLICYNAME);
				retList.add(policyName);
			}
			if (policyMessage != null && policyMessage.isEmpty() == false) {
				retList.add(com.nextlabs.em.windchill.Constants.PARANAME_POLICYMESSAGE);
				retList.add(policyMessage);
			}
			if (result != null && result.isAllowed() == false) {

				retList.add(com.nextlabs.em.windchill.Constants.PARANAME_POLICYEFFECT);
				retList.add("DENY");
			} else {
				retList.add(com.nextlabs.em.windchill.Constants.PARANAME_POLICYEFFECT);
				retList.add("ALLOW");
			}
			if (result.getObligations() != null) {
				RuntimeObligation[] obligations = result.getObligations();
				for (RuntimeObligation obligation : obligations) {
					if (obligation != null) {
						if (obligation.getName() != null) {
							retList.add(com.nextlabs.em.windchill.Constants.PARANAME_OBJNAME);
							retList.add(obligation.getName());
						}
						if (obligation.getParameters() != null) {
							Iterator<String> iterator = obligation
									.getParameters().keySet().iterator();
							retList.add(com.nextlabs.em.windchill.Constants.PARANAME_OBJPARAMNAME);
							while (iterator.hasNext()) {
								String paramName = iterator.next();
								retList.add(paramName);
								retList.add(obligation.getParameters().get(
										paramName));
							}
							retList.add(com.nextlabs.em.windchill.Constants.PARANAME_OBJPARAMVAlUE);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Exception in evaluation:" + e.getMessage());
			logger.error(e);
		}

		return retList;
	}

	private EvaluationResult callRestApiEvaluator(String username,
			EntitlementManagerContext emCtx, String policyaction, String url,
			String referer, String querystring, ObjectAttrCollection objAttrs,
			String cloudAzFilePath,
			HashMap<String, String> specialResourceAttributes) {
		EvaluationResult result = new EvaluationResult();

		HashSet<String> userSet = new HashSet<String>();
		HashSet<String> resourceSet = new HashSet<String>();
		userSet.add(userId);
		boolean allUserAttributes = true;
		boolean allResourceAttributes = true;

		if (userAttributesToSend != null
				&& !userAttributesToSend.equalsIgnoreCase("all")) {
			allUserAttributes = false;
			StringTokenizer userTokens = new StringTokenizer(
					userAttributesToSend, ",");
			while (userTokens.hasMoreTokens()) {
				String token = userTokens.nextToken();
				token = token.toLowerCase();
				userSet.add(token.replace("_", " "));
			}
		}

		if (resourceAttributesToSend != null
				&& !resourceAttributesToSend.equalsIgnoreCase("all")) {
			allResourceAttributes = false;
			StringTokenizer resourceTokens = new StringTokenizer(
					resourceAttributesToSend, ",");
			while (resourceTokens.hasMoreTokens()) {
				String token = resourceTokens.nextToken();
				token = token.toLowerCase();
				resourceSet.add(token.replace("_", " "));
			}
		}

		if (defaultDecision != null
				&& defaultDecision.equalsIgnoreCase("allow")) {
			result.setAllowed(true);
		} else {
			result.setAllowed(false);
		}

		try {
			JPCRestCall jpcRestCall = JPCRestCall.getInstance(cloudAzFilePath);
			Action action = Action.newInstance(policyaction);

			Subject user = Subject.newInstance(username);
			com.nextlabs.ObjectAttrCollection userAttrCol = this.QueryUser(
					username, emCtx.getRequestId(), null, null);
			if (userAttrCol != null) {
				HashMap<String, String> map = userAttrCol.getAttrs();
				Iterator<Map.Entry<String, String>> it = map.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> pairs = (Map.Entry<String, String>) it
							.next();
					String key = pairs.getKey();
					String value = pairs.getValue();
					if (allUserAttributes
							|| userSet.contains(key.replace("_", " ")
									.toLowerCase())) {
						if (key != null && value != null) {
							value = trimValueWithoutNull(value);
							if (key.equals(userId)) {

								logger.info(
										"inside if block UserID  user-->" + key
												+ "=" + value);

								logger.info( "JPC  user id----->="
										+ value);
								user.addAttribute(Subject.SUBJECT_ID_KEY, value);
							} else {	
								if (key.equalsIgnoreCase(OPENAZSdkConstants.USER_GROUPS_KEY)
										|| key.equalsIgnoreCase(OPENAZSdkConstants.USER_PROFILE_KEY)) {
									logger.info("JPC  " + key + " value----->=" + value);
									String[] values = value.split(OPENAZSdkConstants.MULTIVAL_SEPARATOR);
									user.addAttribute(key, values);
								} else {
									user.addAttribute(key.replace(" ", "_"), value);
								}
							
							}
						}
					}
				}
			} else
				logger.info(" QueryAgentImpl.Eval user attributes is null");
			
			logger.info( "JPC  user attributes----->"
					+ user.getAttributeMap());
			logger.info("JPC resource url-->=" + url);
			Resource resource = Resource.newInstance(url);
			String id = url;
			String type = null;
			String no;
			HashSet<String> idSet = new HashSet<String>();
			idSet.add("number");
			idSet.add("product number");
			idSet.add("display identifier");

			resource.addAttribute(
					Constants.ID_RESOURCE_RESOURCE_TYPE.stringValue(),
					"windchill");
			if (allResourceAttributes || resourceSet.contains("referer")) {
				resource.addAttribute("referer", referer);
			}
			if (allResourceAttributes || resourceSet.contains("original url")) {
				resource.addAttribute("original_url", url + "?" + querystring);
			}
			if (specialResourceAttributes != null
					&& specialResourceAttributes.size() > 0) {
				Iterator<String> mapIterator = specialResourceAttributes
						.keySet().iterator();
				while (mapIterator.hasNext()) {

					String key = (String) mapIterator.next();
					if (allResourceAttributes
							|| resourceSet.contains(key.replace("_", " ")
									.toLowerCase())) {
						String value = specialResourceAttributes.get(key);
						if (value != null) {
							resource.addAttribute(key, value);
						}
					}
				}
			}
			if (objAttrs != null) {
				HashMap<String, String> map = objAttrs.getAttrs();
				Iterator<Map.Entry<String, String>> it = map.entrySet()
						.iterator();
				logger.info("JPC resourceSet-->" + resourceSet);
				if (map.keySet().contains("type")) {
					type = map.get("type");
					if (type != null
							&& (type.equalsIgnoreCase("Part") || type
									.equalsIgnoreCase("Document"))) {
						no = map.get("number");
						if (no != null) {
							id = no;
						}
					}
					if (type != null && type.equalsIgnoreCase("Folder")) {
						no = getId(url + "?" + querystring, type);
						resourceSet.add("url");
						if (no != null) {
							id = no;
						}
					}
					if (type != null && type.equalsIgnoreCase("Cabinet")
							|| type.equalsIgnoreCase("Library")) {
						no = getId(url + "?" + querystring, type);
						if (no != null) {
							id = no;
						}
					}
					if (type != null && type.equalsIgnoreCase("Product")) {
						no = map.get("product number");
						if (no != null) {
							id = no;
						} else {
							no = getId(url + "?" + querystring, type);
							if (no != null) {
								id = no;
							}
						}
					}
					if (type != null
							&& type.equalsIgnoreCase("Application Data")) {
						String displayidentiy = map.get("display identity");
						no = getAppDataId(displayidentiy);
						if (no != null) {
							id = no;
						}
					}

				}

				while (it.hasNext()) {
					Map.Entry<String, String> pairs = (Map.Entry<String, String>) it
							.next();
					String key = pairs.getKey();
					String value = pairs.getValue();
					
					if (allResourceAttributes
							|| resourceSet.contains(key.replace("_", " ")
									.toLowerCase())) {
						if (key != null && value != null) {
							resource.addAttribute(key.replace(" ", "_"), value);
						}
					}
				}

			} else
				logger.info("  QueryAgentImpl.Eval resource attributes is null");
			
			logger.info("JPC resource id-->" + id);
			logger.info("JPC resource attributes-->" + resource.getAttributeMap());
			resource.addAttribute(Resource.RESOURCE_ID_KEY, id);

			jpcRestCall.setAction(action);
			jpcRestCall.setUser(user);
			jpcRestCall.setResource(resource);

			// Verify that evaluation request is present in the evaluation cache
			PolicyDecisionObject pdo;
			pdo = getPolicyDecisionObject(user);
			if (pdo != null
					&& pdo.getAction().equalsIgnoreCase(
							action.getActionIdValue())
					&& pdo.getResourceID().equalsIgnoreCase(id)) {
				return pdo.getDecision();
			}

			PepResponse pepResponse = jpcRestCall.evaluate();
			Decision decision = pepResponse.getWrappedResult().getDecision();
			String effectValue;
			if (decision == Decision.NOTAPPLICABLE) {
				if (dontcareDecision != null
						&& dontcareDecision.equalsIgnoreCase("allow")) {
					effectValue = "allow";
				} else {
					effectValue = "deny";
				}
			} else if (decision == Decision.PERMIT) {
				effectValue = "allow";
			} else if (decision == Decision.DENY) {
				effectValue = "deny";
			} else {
				if (indeterminateDecision != null
						&& indeterminateDecision.equalsIgnoreCase("allow")) {
					effectValue = "allow";
				} else {
					effectValue = "deny";
				}
			}

			logger.info(" REST API  response=" + effectValue);
			if (effectValue.equalsIgnoreCase("allow")) {
				logger.info(" Rest API allowed");
				result.setAllowed(true);
			} else
				result.setAllowed(false);
			Collection<Obligation> obligations = pepResponse.getWrappedResult()
					.getObligations();
			if (obligations != null) {
				for (Obligation obligation : obligations) {
					if (obligation != null && obligation.getId() != null) {
						String obligationname = obligation.getId()
								.stringValue();
						logger.info(" Rest API Eval Obligation Name:"
								+ obligationname);
						if (obligationname != null
								&& obligationname
										.equalsIgnoreCase(OPENAZSdkConstants.OBLIGATION_MESSAGE_NOTIFY)) {
							Collection<AttributeAssignment> attributeAssignments = obligation
									.getAttributeAssignments();
							if (attributeAssignments != null) {
								for (AttributeAssignment aa : attributeAssignments) {
									if (aa.getAttributeId() != null
											&& aa.getAttributeId()
													.stringValue()
													.equalsIgnoreCase(
															OPENAZSdkConstants.OBLIGATION_CENOTIFY_MESSAGE)) {

										result.setPolicyMessage(aa
												.getAttributeValue().getValue()
												.toString());
										logger.info(" Rest API Eval Obligation message:"
												+ result.getPolicyMessage());
									}
								}
							}
						}
					}
				}

				pdo = new PolicyDecisionObject(id, action.getActionIdValue(),
						result);
				CacheEngine.getInstance().insertIntoCache(
						user.getSubjectIdValue(), pdo);

			}

		} catch (IOException e) {
			logger.debug("Exception in callRestApiEvaluator Method. Returning Default Decision configured in PEP Properties file:"
					+ e.getMessage());
			logger.error(e);
		}

		return result;
	}

	private PolicyDecisionObject getPolicyDecisionObject(Subject user) {
		String userID = user.getSubjectIdValue();
		CacheEngine ce = CacheEngine.getInstance();
		return ce.getFromCache(userID);
	}

	private String getAppDataId(String displayidentity) {
		String id = null;
		int obIndex = 0, cbIndex = 0;

		if (displayidentity.contains("("))
			obIndex = displayidentity.indexOf("(");
		if (displayidentity.contains(")"))
			cbIndex = displayidentity.indexOf(")");
		if (obIndex > 0 && cbIndex > 0) {
			String processText = displayidentity
					.substring(obIndex + 1, cbIndex);
			if (processText.contains(",")) {
				processText = processText
						.substring(0, processText.indexOf(","));
				if (processText.contains("-"))
					processText = processText.substring(processText
							.indexOf("-") + 1);
			}
			id = processText.trim();
		}
		return id;
	}

	private String trimValueWithoutNull(String value) {
		if (value.startsWith("null")) {
			int index = value.indexOf("null");
			value = value.substring(index + 4);
		}
		value = value.trim();
		if (value.contains(";")) {
			value = value.substring(0, value.lastIndexOf(";"));
		}
		return value;
	}

	@Override
	public ObjectAttrCollection QueryOid(String oid, String requestId,
			String reserved1, String reserved2) throws QueryAgentException {
		EntitlementManagerContext ctx = new EntitlementManagerContext();
		ctx.setRequestId(requestId);
		logger.info("  QueryAgent -> QueryOid (" + oid + ")");
		ObjectAttrCollection attrCol = new ObjectAttrCollection();
		Persistable wcObj = WindchillObjectHelper.getObject(oid);
		if (wcObj != null) {
			if (WindchillObjectHelper.getAttrs(attrCol, wcObj, ctx) == false) {

				return null;
			}
		} else
			return null;
		return attrCol;
	}

	@Override
	public ObjectAttrCollection QueryUser(String userName, String requestId,
			String reserved1, String reserved2) throws QueryAgentException {
		EntitlementManagerContext ctx = new EntitlementManagerContext();
		ctx.setRequestId(requestId);
		logger.info( " QueryAgent -> QueryUser (" + userName + ")");
		ObjectAttrCollection attrCol = new ObjectAttrCollection();
		WTUser user = WindchillObjectHelper.getUser(userName);
		if (user != null) {
			if (WindchillObjectHelper.getAttrs(attrCol, user, ctx) == false) {
				return null;
			}
		} else {
			return null;
		}
		try {
			String groups = getGroups(user);
			String profiles = getProfiles(user);
			attrCol.add(OPENAZSdkConstants.USER_GROUPS_KEY, groups);
			attrCol.add(OPENAZSdkConstants.USER_PROFILE_KEY, profiles);
		} catch (WTException e) {
			logger.error("Error occured while getting user groups: ", e);
		}
		return attrCol;

	}
	

	private String getProfiles(WTUser user) throws WTException {
		StringBuilder sb = new StringBuilder();
		ReferenceFactory rff = new ReferenceFactory();
		WTReference ref = rff.getReference(user);
		WTPrincipal principal = (WTPrincipal) ref.getObject();

		Map usermap = WTContainerHelper.service.getProfileGroupsForRoleAccessCheck((WTUser) principal);
		Set profileGroups = (Set) usermap.get(Boolean.TRUE);

		Iterator iter = profileGroups.iterator();

		while (iter.hasNext()) {
			WTGroup grp = (WTGroup) iter.next();
			sb.append(grp.getName());
			sb.append(OPENAZSdkConstants.MULTIVAL_SEPARATOR);
		}

		logger.debug("QueryAgent user profiles:" + sb.toString());
		return sb.toString();
	}

	private String getGroups(WTUser user) throws WTException {
		StringBuilder sb = new StringBuilder();
		ReferenceFactory rff = new ReferenceFactory();
		WTReference ref = rff.getReference(user);
		WTPrincipal principal = (WTPrincipal) ref.getObject();
		System.out.println("User Name is :" + principal.getName());
		Map usermap = WTContainerHelper.service.getProfileGroupsForRoleAccessCheck((WTUser) principal);
		Set profileGroups = (Set) usermap.get(Boolean.TRUE);
		Iterator iter = profileGroups.iterator();
		while (iter.hasNext()) {
			WTGroup grp = (WTGroup) iter.next();
			sb.append(grp.getName());
			sb.append(OPENAZSdkConstants.MULTIVAL_SEPARATOR);
		}
		logger.debug("QueryAgent user Groups:" + sb.toString());
		return sb.toString();
	}

	public String getId(String url, String type) {
	
		String id = null;

		try {
			String decodedURL = URLDecoder.decode(url,
					StandardCharsets.UTF_8.name());
			type = type + ":";
			int typelength = type.length();
			if (decodedURL.contains(type)) {
				int typeindex = decodedURL.lastIndexOf(type);
				String temp = decodedURL.substring(typeindex + typelength);
				if (temp.contains("&"))
					id = cleanSpecialCharacters(temp.substring(0,
							temp.indexOf("&")));
				else
					id = temp;
			}

		} catch (UnsupportedEncodingException e) {
			logger.info(
					"Exception in evaluation:" + e.getMessage());
			logger.error(e);
		}

		return id;
	}

	private String cleanSpecialCharacters(String cleanSpecialcharString) {

		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add("!");
		hashSet.add("$");
		hashSet.add("*");
		hashSet.add("?");

		for (String str : hashSet) {
			if (cleanSpecialcharString.contains(str)) {
				cleanSpecialcharString = cleanSpecialcharString
						.replace(str, "");
			}
		}
		return cleanSpecialcharString;
	}

	@Override
	public ObjectAttrCollection QueryTest(String para1, String para2,
			String para3, String para4) throws QueryAgentException {
		logger.info("QueryTest be called");
		ObjectAttrCollection attrCol = new ObjectAttrCollection();
		attrCol.add("name1", "value1");
		attrCol.add("name2", "value2");
		attrCol.add("name3", "value3");
		return attrCol;
	}

}
