package com.nextlabs.em.windchill;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import wt.fc.Persistable;
import wt.org.WTPrincipal;
import wt.org.WTUser;

import com.ibm.icu.util.StringTokenizer;
import com.nextlabs.CESdkConstants;
import com.nextlabs.ConfigurationManager;
import com.nextlabs.DenyPageHelper;
import com.nextlabs.EntitlementManagerContext;
import com.nextlabs.EvaluationResult;
import com.nextlabs.EvaluationResult.RuntimeObligation;
import com.nextlabs.ObjectAttrCollection;
import com.nextlabs.PolicyEvaluator;
import com.nextlabs.em.windchill.tools.Url2Action;
import com.nextlabs.em.windchill.wsclient.MapConvertor;
import com.nextlabs.em.windchill.wsclient.MapEntry;
import com.nextlabs.em.windchill.wsclient.QueryAgent_Service;

public class EntitlementManagerFilter implements Filter {
	private Logger logger = Logger.getLogger(EntitlementManagerFilter.class);
	ServletContext servletContext = null;

	private class EMAuthenticator extends Authenticator {
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			String user = ConfigurationManager.getInstance().getProperty(
					com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PEP_WINDCHILL_QUERYAGENT_USER);
			String password = ConfigurationManager.getInstance()
					.getQueryAgentPassword();
			return new PasswordAuthentication(user, password.toCharArray());
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
		// String
		// value=(String)servletContext.getAttribute("servletScope Context");
		// logger.info("EntitlementManagerFilter.init...
		// "+value);

	}

	public HashMap<String, Object> doUploadPolicyEvaluation(
			EntitlementManagerContext emCtx, WTPrincipal principal, String oid) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean isAllowed = true;
		HashMap<String, String> primaryTags = new HashMap<String, String>();
		HashMap<String, String> secondaryTags = new HashMap<String, String>();
		String policyName = null, policyMessage = null;

		long lStartTime = System.currentTimeMillis();
		logger.info("UPLOAD EVENT start at " + lStartTime);
		try {
			Authenticator myAuth = new EMAuthenticator();
			Authenticator.setDefault(myAuth);
			logger.info("UPLOAD EVENT Authentiator set");

			List<String> params = new ArrayList<String>();
			logger.info("UPLOAD EVENT principal at " + principal);
			params.add(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME);
			params.add(principal.getName());
			logger.info("UPLOAD EVENT"
					+ com.nextlabs.em.windchill.Constants.PARANAME_USERNAME
					+ "=" + principal.getName());

			params.add(com.nextlabs.em.windchill.Constants.PARANAME_OIDS);
			params.add(oid);

			params.add(com.nextlabs.em.windchill.Constants.PARANAME_POLICYACTION);
			params.add("UPLOAD");
			logger.info("UPLOAD EVENT calling for evaluation ");
			/*
			 * QueryAgent_Service queryAgentService = new QueryAgent_Service();
			 * logger.info( "UPLOAD EVENT queryAgentService at " +
			 * queryAgentService); QueryAgent queryAgentPort = queryAgentService
			 * .getQueryAgentImplPort(); logger.info(
			 * "UPLOAD EVENT queryAgentPort at " + queryAgentPort);
			 */

			String[] paramss = new String[params.size()];
			int in = 0;
			for (String param : params) {
				paramss[in] = new String(param);
				in++;

			}
			List<String> resultStrList = new QueryAgentImpl().Eval(
					emCtx.getRequestId(), paramss,
					"Entitlement Manager for Windchill");
			logger.info("after evaluation");
			logger.info("UPLOAD EVENT  Web service call done resultStrList.length="
					+ resultStrList.size());
			logger.info("UPLOAD EVENT  Web service call done resultStrList.length="
					+ resultStrList);
			for (int i = 0; i < resultStrList.size(); i++) {
				if (i == resultStrList.size() - 1) {
					logger.info("UPLOAD EVENT ret " + i + ":"
							+ resultStrList.get(i) + " is discarded");
				} else {
					logger.info("ret " + i + ":" + resultStrList.get(i) + "="
							+ resultStrList.get(i + 1));
					if (resultStrList
							.get(i)
							.equalsIgnoreCase(
									com.nextlabs.em.windchill.Constants.PARANAME_POLICYNAME))
						policyName = resultStrList.get(++i);
					else if (resultStrList
							.get(i)
							.equalsIgnoreCase(
									com.nextlabs.em.windchill.Constants.PARANAME_POLICYMESSAGE))
						policyMessage = resultStrList.get(++i);
					else if (resultStrList
							.get(i)
							.equalsIgnoreCase(
									com.nextlabs.em.windchill.Constants.PARANAME_OBJNAME)) {
						String obligationName = resultStrList.get(++i);
						if (!(obligationName.equalsIgnoreCase("WDRM") || obligationName
								.equalsIgnoreCase("TAGUPLOAD"))) {
							continue;
						}
						if (obligationName.equalsIgnoreCase("WDRM")) {
							resultMap.put("DRM", true);
						}
						logger.info("UPLOAD EVENT  SASERVICE Policy obligationName:"
								+ obligationName);
						resultMap.put("obligationame", obligationName);
						if (resultStrList
								.get(++i)
								.equalsIgnoreCase(
										com.nextlabs.em.windchill.Constants.PARANAME_OBJPARAMNAME)) {
							String tagname = null;
							String contentType = null;
							String securitylabel = null;
							String doctagname = null;
							for (int j = 0;; j++) {
								if (resultStrList
										.get(i)
										.equalsIgnoreCase(
												com.nextlabs.em.windchill.Constants.PARANAME_OBJPARAMVAlUE)) {
									logger.info("UPLOAD EVENT  SASERVICE Exiting Obligation");
									break;
								}
								if (j % 2 == 0) {
									tagname = resultStrList.get(++i);
									if (tagname
											.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_OBJPARAMVAlUE)) {
										logger.info("UPLOAD EVENT  SASERVICE Exiting Obligation");
										break;
									}
									logger.info("UPLOAD EVENT  SASERVICE Policy tagname:"
											+ tagname);

								} else {
									String tagValue = resultStrList.get(++i);

									logger.info("UPLOAD EVENT  SASERVICE Policy tagValue:"
											+ tagValue);
									if (tagname
											.equalsIgnoreCase("Security Label")) {
										securitylabel = tagValue;
									}
									if (tagname.equalsIgnoreCase("Tag Names")) {
										doctagname = tagValue;

									}
									if (tagname
											.equalsIgnoreCase("Retain Attachment Tags")) {
										contentType = tagValue;

									}

								}
							}
							primaryTags.putAll(getTags(doctagname));
							if (contentType != null) {
								if (contentType.equalsIgnoreCase("YES")) {
									if (securitylabel != null)
										secondaryTags.put(doctagname,
												securitylabel);
									else {
										secondaryTags
												.putAll(getTags(doctagname));
									}
								}
							}
						}
					} else if (resultStrList
							.get(i)
							.equalsIgnoreCase(
									com.nextlabs.em.windchill.Constants.PARANAME_POLICYEFFECT)) {
						if (resultStrList.get(++i).equalsIgnoreCase("allow")) {
							isAllowed = true;
							logger.info("UPLOAD EVENT  SASERVICE Policy evaluated to allow:");
						} else
							isAllowed = false;
					}
				}
			}
			long lEndTime = System.currentTimeMillis();
			resultMap.put(
					com.nextlabs.em.windchill.Constants.PARANAME_POLICYEFFECT,
					isAllowed);
			resultMap.put("tags", primaryTags);
			resultMap.put("sectags", secondaryTags);

			logger.info("UPLOAD EVENT  doFilter end at " + lStartTime + "("
					+ (lEndTime - lStartTime) + ")");
		} catch (Exception e) {
			logger.info("UPLOAD EVENT  exception " + e.getMessage());
			logger.error(e);
		}
		return resultMap;
	}

	private HashMap<String, String> getTags(String doctagname) {
		HashMap<String, String> tags = new HashMap<String, String>();
		StringTokenizer tokens = new StringTokenizer(doctagname, ";");
		while (tokens.hasMoreTokens()) {
			String tagName = tokens.nextToken();
			tags.put(tagName, tagName);
		}
		return tags;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		long lStartTime = System.currentTimeMillis();
		logger.info("EntitlementManagerFilter doFilter start at " + lStartTime);
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		httpServletRequest.setAttribute("requestId", emCtx.getRequestId());
		String url = httpServletRequest.getRequestURI();
		String fullUrl = httpServletRequest.getRequestURL().toString();
		// emCtx.setSource(url);
		emCtx.setSource(fullUrl);
		logger.info("EntitlementManagerFilter doFilter start...url=("
				+ httpServletRequest.getMethod() + ")" + fullUrl);

		String referer = httpServletRequest.getHeader("Referer");
		logger.info("EntitlementManagerFilter doFilter start...referer="
				+ referer);

		String authType = httpServletRequest.getAuthType();
		logger.info("EntitlementManagerFilter doFilter start...AuthType="
				+ authType);

		String queryString = httpServletRequest.getQueryString();
		if (null == queryString) {
			if (request.getParameterValues("popupActionMethod") != null) {
				queryString = "popupActionMethod="
						+ request.getParameterValues("popupActionMethod")[0]
						+ "&" + "popupActionClass="
						+ request.getParameterValues("popupActionClass")[0];
			}
		}
		logger.info("EntitlementManagerFilter doFilter start...QueryString = "
				+ queryString);
		String username = httpServletRequest.getRemoteUser();
		emCtx.setUserName(username);

		if (url.contains("/Windchill/gwt/com.ptc.windchill.wncgwt.WncGWT/")) {

			EntitlementManagerRequestWrapper myRequestWrapper = new EntitlementManagerRequestWrapper(
					(HttpServletRequest) request);
			String body = myRequestWrapper.getBody();
			logger.info("@@@@@@@@@@@@@@@@@@@@@^^^^^^^^^^^^^^^^" + body
					+ "^^^^^^^^@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			String bodyData[] = body.split("\\|");
			logger.info("@@@@@@@@@@@@@@@@@@@@@^^^^^^^^^^^^^^^^" + bodyData[2]
					+ "^^^^^^^^@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			// if(bodyData[2].equals("18")&& bodyData[11].equals("renameGWT")){
			if (bodyData[2].equals("19") && bodyData[11].equals("renameGWT")
					|| bodyData[2].equals("18")
					&& bodyData[11].equals("renameGWT")) {

				Authenticator myAuth = new EMAuthenticator();
				Authenticator.setDefault(myAuth);

				QueryAgent_Service queryAgentService = new QueryAgent_Service();
				com.nextlabs.em.windchill.wsclient.QueryAgent queryAgentPort = queryAgentService
						.getQueryAgentImplPort();

				List<String> params = new ArrayList<String>();

				// The current user name
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME);
				params.add(username);
				logger.info(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME
						+ "=" + username);

				// //
				// for (int i = 0; i < bodyData.length; i++) {
				// System.out.println(i+"  : "+bodyData[i]);
				// logger.info(
				// "oid  ^^^^^^^^^^^^^^^^"+bodyData[i]+"^^^^^^^^@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				// if(bodyData[2].equals("19")&&
				// bodyData[11].equals("renameGWT")){
				// if(i==15)
				// logger.info(
				// i+"    ^^^^^^^^^^^^^^^^"+bodyData[15]+":"+bodyData[17]+"^^^^^^^^@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				// }else if(bodyData[2].equals("18")&&
				// bodyData[11].equals("renameGWT")){
				// if(i==15)
				// logger.info(
				// i+"    ^^^^^^^^^^^^^^^^"+bodyData[15]+":"+bodyData[16]+"^^^^^^^^@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				// }

				// }

				String[] oids = new String[1];
				// oids[0] = bodyData[15]+":"+bodyData[17];

				if (bodyData[2].equals("19")
						&& bodyData[11].equals("renameGWT")) {
					oids[0] = bodyData[15] + ":" + bodyData[17];
				} else if (bodyData[2].equals("18")
						&& bodyData[11].equals("renameGWT")) {
					oids[0] = bodyData[15] + ":" + bodyData[16];
				}

				logger.info("oid@@@@@@@@@@@@@@@@@@@@@^^^^^^^^^^^^^^^^"
						+ oids[0]
						+ "^^^^^^^^@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
				{
					// the target object id
					params.add(com.nextlabs.em.windchill.Constants.PARANAME_OIDS);
					StringBuilder sb = new StringBuilder();
					sb.append(oids[0]);
					for (int idx = 1; idx < oids.length; idx++) {
						if (sb.indexOf(oids[idx]) == -1) {
							sb.append(com.nextlabs.em.windchill.Constants.PARANAME_SEPARATOR);
							sb.append(oids[idx]);
						}
					}
					params.add(sb.toString());
					logger.info(com.nextlabs.em.windchill.Constants.PARANAME_OIDS
							+ "=" + sb.toString());
				}

				// the request URL
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_URL);
				params.add(fullUrl);
				String action = "EDIT";
				// action = Url2Action.url2Action(emCtx, url,
				// httpServletRequest.getMethod(), queryString);
				logger.info(com.nextlabs.em.windchill.Constants.PARANAME_POLICYACTION
						+ "=" + action);
				// the policy action
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_POLICYACTION);
				params.add(action);

				// the referer URL
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_REFERER);
				params.add(referer);

				List<String> resultStrList = queryAgentPort.eval(
						emCtx.getRequestId(), params,
						"Entitlement Manager for Windchill");

				boolean isAllowed = true;
				String policyName = null;
				String policyMessage = null;

				for (int i = 0; i < resultStrList.size(); i++) {
					if (i == resultStrList.size() - 1) {
						logger.info("ret " + i + ":" + resultStrList.get(i)
								+ " is discarded");
					} else {
						logger.info("ret " + i + ":" + resultStrList.get(i)
								+ "=" + resultStrList.get(i + 1));
						if (resultStrList
								.get(i)
								.equalsIgnoreCase(
										com.nextlabs.em.windchill.Constants.PARANAME_POLICYNAME))
							policyName = resultStrList.get(++i);
						else if (resultStrList
								.get(i)
								.equalsIgnoreCase(
										com.nextlabs.em.windchill.Constants.PARANAME_POLICYMESSAGE))
							policyMessage = resultStrList.get(++i);
						else if (resultStrList
								.get(i)
								.equalsIgnoreCase(
										com.nextlabs.em.windchill.Constants.PARANAME_POLICYEFFECT)) {
							if (resultStrList.get(++i)
									.equalsIgnoreCase("allow"))
								isAllowed = true;
							else
								isAllowed = false;
						}
					}
				}

				if (isAllowed == false) {

					String refererUrl = httpServletRequest.getHeader("referer");
					String imageURL = request.getScheme() + "://"
							+ request.getServerName() + ""
							+ servletContext.getContextPath();
					httpServletResponse
							.setContentType("application/json;charset=UTF-8");
					String msg = "//EX[2,2,1,[\"com.ptc.core.appsec.GWTApplicationSecurityException/1640787063\",\"\\x20Access\\x20Denied\\x20-\\x20NextLabs\\x20\\x0b\\x0A ATTENTION: You can not modify the object because you do not have permission\"],0,5]";
					// Access Denied - NextLabs Inc \n ATTENTION: You can not
					// modify the object because you do not have permission
					httpServletResponse.getWriter().write(msg);
					return;
				}

			}

			filterChain.doFilter(myRequestWrapper, response);
			return;

		}

		String oids[] = null;

		if (request.getParameterValues("taskid") != null
				&& request.getParameterValues("taskid")[0]
						.equals("SETLCSTATE_TASK")
				&& request.getParameterValues("SetStateJSTable_objOID") != null) {
			String soid[] = request
					.getParameterValues("SetStateJSTable_objOID");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[0];
			}
		}

		if (request.getParameterValues("taskid") != null
				&& request.getParameterValues("taskid")[0]
						.equals("SetStateTask")
				&& request.getParameterValues("SetStateJSTable_objOID") != null) {
			String soid[] = request
					.getParameterValues("SetStateJSTable_objOID");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[0];
			}
		}

		if (request.getParameterValues("actionName") != null
				&& request.getParameterValues("actionName")[0]
						.equals("list_delete")
				&& request.getParameterValues("soid") != null) {
			String soid[] = request.getParameterValues("soid");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}

		if (request.getParameterValues("actionName") != null
				&& request.getParameterValues("actionName")[0]
						.equals("multiObjManageSecurity")
				&& request.getParameterValues("soid") != null) {
			String soid[] = request.getParameterValues("soid");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}

		if (request.getParameterValues("actionName") != null
				&& request.getParameterValues("actionName")[0].equals("export")
				&& request.getParameterValues("soid") != null) {
			String soid[] = request.getParameterValues("soid");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}

		if (request.getParameterValues("actionName") != null
				&& request.getParameterValues("actionName")[0]
						.equals("editMultiObjects")
				&& request.getParameterValues("soid") != null) {
			String soid[] = request.getParameterValues("soid");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}

		if (request.getParameterValues("actionName") != null
				&& request.getParameterValues("actionName")[0]
						.equals("downloadDocumentsToCompressedFile")
				&& request.getParameterValues("soid") != null) {
			String soid[] = request.getParameterValues("soid");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}
		/*
		 * if (request.getParameterValues("actionName")!= null &&
		 * request.getParameterValues("actionName")[0].equals("list_copy") &&
		 * request.getParameterValues("soid")!=null) { String soid[] =
		 * request.getParameterValues("soid"); oids = new String[soid.length];
		 * for(int i=0;i<soid.length;i++){ logger.info( "@@@@@@@@@@@@@@@@@@@@@"
		 * + soid[i]); logger.info( "@@@@@@@@@@@@@@@@@@@@@" +
		 * soid[i].replace("!*", "")); logger.info( "@@@@@@@@@@@@@@@@@@@@@" +
		 * soid[i].replace("!*", "").split("\\$").length);
		 * 
		 * oids[i] = soid[i].replace("!*", "").split("\\$")[3]; } }
		 * 
		 * if (request.getParameterValues("actionName")!= null &&
		 * request.getParameterValues("actionName")[0].equals("copy") &&
		 * request.getParameterValues("soid")!=null) { String soid[] =
		 * request.getParameterValues("soid"); oids = new String[soid.length];
		 * for(int i=0;i<soid.length;i++){ logger.info( "@@@@@@@@@@@@@@@@@@@@@"
		 * + soid[i]); logger.info( "@@@@@@@@@@@@@@@@@@@@@" +
		 * soid[i].replace("!*", "")); logger.info( "@@@@@@@@@@@@@@@@@@@@@" +
		 * soid[i].replace("!*", "").split("\\$").length);
		 * 
		 * oids[i] = soid[i].replace("!*", "").split("\\$")[3]; } }
		 * 
		 * if (request.getParameterValues("actionName")!= null &&
		 * request.getParameterValues("actionName")[0].equals("cut") &&
		 * request.getParameterValues("soid")!=null) { String soid[] =
		 * request.getParameterValues("soid"); oids = new String[soid.length];
		 * for(int i=0;i<soid.length;i++){ logger.info( "@@@@@@@@@@@@@@@@@@@@@"
		 * + soid[i]); logger.info( "@@@@@@@@@@@@@@@@@@@@@" +
		 * soid[i].replace("!*", "")); logger.info( "@@@@@@@@@@@@@@@@@@@@@" +
		 * soid[i].replace("!*", "").split("\\$").length);
		 * 
		 * oids[i] = soid[i].replace("!*", "").split("\\$")[3]; } }
		 * 
		 * if (request.getParameterValues("actionName")!= null &&
		 * request.getParameterValues("actionName")[0].equals("list_cut") &&
		 * request.getParameterValues("soid")!=null) { String soid[] =
		 * request.getParameterValues("soid"); oids = new String[soid.length];
		 * for(int i=0;i<soid.length;i++){ logger.info( "@@@@@@@@@@@@@@@@@@@@@"
		 * + soid[i]); logger.info( "@@@@@@@@@@@@@@@@@@@@@" +
		 * soid[i].replace("!*", "")); logger.info( "@@@@@@@@@@@@@@@@@@@@@" +
		 * soid[i].replace("!*", "").split("\\$").length);
		 * 
		 * oids[i] = soid[i].replace("!*", "").split("\\$")[3]; } }
		 */
		if (request.getParameterValues("popupActionMethod") != null
				&& request.getParameterValues("popupActionMethod")[0]
						.equals("list_cut")
				&& request.getParameterValues("pjl_selPJLsa1__1") != null) {
			String soid[] = request.getParameterValues("pjl_selPJLsa1__1");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}

		if (request.getParameterValues("popupActionMethod") != null
				&& request.getParameterValues("popupActionMethod")[0]
						.equals("cut")
				&& request.getParameterValues("ACTION") == null) {
			if (request.getParameterValues("pjl_selPJLsa1__1") != null) {
				String soid[] = request.getParameterValues("pjl_selPJLsa1__1");
				oids = new String[soid.length];
				for (int i = 0; i < soid.length; i++) {
					logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", ""));
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", "").split("\\$").length);

					oids[i] = soid[i].replace("!*", "").split("\\$")[3];
				}
			} else if (request.getParameterValues("elemAddress") != null) {// elemAddress
				String soid[] = request.getParameterValues("elemAddress");
				oids = new String[soid.length];
				for (int i = 0; i < soid.length; i++) {
					logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", ""));
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", "").split("\\$").length);

					oids[i] = soid[i].replace("!*", "").split("\\$")[0];
				}
			} else {

				String soid[] = request.getParameterValues("oid");
				oids = new String[soid.length];
				for (int i = 0; i < soid.length; i++) {
					logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", ""));
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", "").split("\\$").length);

					oids[i] = soid[i].replace("!*", "").split("\\$")[0];
				}
			}

		}

		if (request.getParameterValues("popupActionMethod") != null
				&& request.getParameterValues("popupActionMethod")[0]
						.equals("list_copy")
				&& request.getParameterValues("pjl_selPJLsa1__1") != null) {
			String soid[] = request.getParameterValues("pjl_selPJLsa1__1");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}

		if (request.getParameterValues("popupActionMethod") != null
				&& request.getParameterValues("popupActionMethod")[0]
						.equals("copy")
				&& request.getParameterValues("ACTION") == null) {
			if (request.getParameterValues("pjl_selPJLsa1__1") != null) {
				String soid[] = request.getParameterValues("pjl_selPJLsa1__1");
				oids = new String[soid.length];
				for (int i = 0; i < soid.length; i++) {
					logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", ""));
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", "").split("\\$").length);

					oids[i] = soid[i].replace("!*", "").split("\\$")[3];
				}
			} else if (request.getParameterValues("elemAddress") != null) {// elemAddress
				String soid[] = request.getParameterValues("elemAddress");
				oids = new String[soid.length];
				for (int i = 0; i < soid.length; i++) {
					logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", ""));
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", "").split("\\$").length);

					oids[i] = soid[i].replace("!*", "").split("\\$")[0];
				}
			} else {
				String soid[] = request.getParameterValues("oid");
				oids = new String[soid.length];
				for (int i = 0; i < soid.length; i++) {
					logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", ""));
					logger.info("@@@@@@@@@@@@@@@@@@@@@"
							+ soid[i].replace("!*", "").split("\\$").length);

					oids[i] = soid[i].replace("!*", "").split("\\$")[0];
				}
			}
		}

		if (request.getParameterValues("ACTION") != null
				&& request.getParameterValues("ACTION")[0]
						.equals("CONTAINERMOVE")
				&& request.getParameterValues("pjl_selPJLsa1__1") != null) {
			String soid[] = request.getParameterValues("pjl_selPJLsa1__1");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}

		if (request.getParameterValues("ACTION") != null
				&& request.getParameterValues("ACTION")[0].equals("SETSTATE")
				&& request.getParameterValues("pjl_selPJLsa1__1") != null) {
			String soid[] = request.getParameterValues("pjl_selPJLsa1__1");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}
		if (request.getParameterValues("Action") != null
				&& request.getParameterValues("Action")[0]
						.equals("ReviseItems")
				&& request.getParameterValues("pjl_selPJLsa1__1") != null) {
			String soid[] = request.getParameterValues("pjl_selPJLsa1__1");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}
		if (request.getParameterValues("Action") != null
				&& request.getParameterValues("Action")[0]
						.equals("EDITSECURITYLABELS")
				&& request.getParameterValues("pjl_selPJLsa1__1") != null) {
			String soid[] = request.getParameterValues("pjl_selPJLsa1__1");
			oids = new String[soid.length];
			for (int i = 0; i < soid.length; i++) {
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i]);
				logger.info("@@@@@@@@@@@@@@@@@@@@@" + soid[i].replace("!*", ""));
				logger.info("@@@@@@@@@@@@@@@@@@@@@"
						+ soid[i].replace("!*", "").split("\\$").length);

				oids[i] = soid[i].replace("!*", "").split("\\$")[3];
			}
		}

		if (oids == null || oids.length == 0) {

			oids = request.getParameterValues("oid");// VR:wt.doc.WTDocument:262818

		}
		if (oids == null || oids.length == 0) {
			// if(request.getParameterValues("oid")!=null){
			if (request.getParameterValues("ContainerOid") != null) {
				if (request.getParameterValues("actionName") != null) {
					String action[] = request.getParameterValues("actionName");
					if (action[0].equals("create")) {
						oids = request.getParameterValues("ContainerOid");
					}

				}
			}
			// }
		}

		if (oids == null || oids.length == 0) {
			oids = request.getParameterValues("ContentHolder");
		}

		if (oids == null || oids.length == 0)
			oids = request.getParameterValues("OID");
		if (oids != null) {
			for (int i = 0; i < oids.length; i++) {
				logger.info("EntitlementManagerFilter doFilter =*=oids[" + i
						+ "]=*=" + oids[i]);
			}
		}

		boolean isAllowed = true;
		String policyName = null, policyMessage = null;
		if (oids != null && oids.length > 0 && username != null
				&& !username.isEmpty()
				&& url.contains("/Windchill/com/nextlabs/QueryAgent") == false) {
			Authenticator myAuth = new EMAuthenticator();
			Authenticator.setDefault(myAuth);

			QueryAgent_Service queryAgentService = new QueryAgent_Service();
			com.nextlabs.em.windchill.wsclient.QueryAgent queryAgentPort = queryAgentService
					.getQueryAgentImplPort();
			List<String> params = new ArrayList<String>();

			// The current user name
			params.add(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME);
			params.add(username);
			logger.info(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME
					+ "=" + username);

			{
				// the target object id
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_OIDS);
				StringBuilder sb = new StringBuilder();
				sb.append(oids[0]);
				for (int idx = 1; idx < oids.length; idx++) {
					if (sb.indexOf(oids[idx]) == -1) {
						sb.append(com.nextlabs.em.windchill.Constants.PARANAME_SEPARATOR);
						sb.append(oids[idx]);
					}
				}
				params.add(sb.toString());
				logger.info(com.nextlabs.em.windchill.Constants.PARANAME_OIDS
						+ "=" + sb.toString());
			}
			// the request URL
			params.add(com.nextlabs.em.windchill.Constants.PARANAME_URL);
			params.add(fullUrl);
			String action = "OPEN";
			action = Url2Action.url2Action(emCtx, url,
					httpServletRequest.getMethod(), queryString);
			logger.info(com.nextlabs.em.windchill.Constants.PARANAME_POLICYACTION
					+ "=" + action);
			// the policy action
			params.add(com.nextlabs.em.windchill.Constants.PARANAME_POLICYACTION);
			params.add(action);

			// the referer URL
			params.add(com.nextlabs.em.windchill.Constants.PARANAME_REFERER);
			params.add(referer);

			// the referer quertystring
			params.add(com.nextlabs.em.windchill.Constants.PARANAME_QUERYSTRING);
			params.add(queryString);
			logger.info("EFILTER Query Agent Port" + queryAgentPort);
			logger.info("EMCTX request id=" + emCtx.getRequestId());
			List<String> resultStrList = queryAgentPort.eval(
					emCtx.getRequestId(), params,
					"Entitlement Manager for Windchill");
			logger.info("Web service call done resultStrList.length="
					+ resultStrList.size());

			logger.info("scheme name : " + request.getScheme());
			logger.info("server name : " + request.getServerName());
			logger.info("sevlet context path : "
					+ servletContext.getContextPath());

			for (int i = 0; i < resultStrList.size(); i++) {
				if (i == resultStrList.size() - 1) {
					logger.info("ret " + i + ":" + resultStrList.get(i)
							+ " is discarded");
				} else {
					logger.info("ret " + i + ":" + resultStrList.get(i) + "="
							+ resultStrList.get(i + 1));
					if (resultStrList
							.get(i)
							.equalsIgnoreCase(
									com.nextlabs.em.windchill.Constants.PARANAME_POLICYNAME))
						policyName = resultStrList.get(++i);
					else if (resultStrList
							.get(i)
							.equalsIgnoreCase(
									com.nextlabs.em.windchill.Constants.PARANAME_POLICYMESSAGE))
						policyMessage = resultStrList.get(++i);
					else if (resultStrList
							.get(i)
							.equalsIgnoreCase(
									com.nextlabs.em.windchill.Constants.PARANAME_POLICYEFFECT)) {
						if (resultStrList.get(++i).equalsIgnoreCase("allow"))
							isAllowed = true;
						else
							isAllowed = false;
					}
				}
			}

			/*
			 * Following code will lead to "no context" error. This indicate
			 * that any Windchill object cannot be used in the filter. That's
			 * why we call the evaluation via web service call. The web service
			 * will be run in the windchill context. So can do anything in the
			 * web service including query windchill object, make operation on
			 * the objects.
			 * 
			 * WTUser wtUser=WindchillObjectHelper.getUser(username);
			 * emCtx.log("wtUser="+wtUser.getFullName());
			 */
		} else {

			if ((url.contains("product/createProductWizard")
					&& request.getParameterValues("actionName")[0]
							.equals("createProductWizard")
					&& request.getParameterValues("wizardActionClass")[0]
							.equals("com.ptc.windchill.enterprise.product.forms.CreateProductFormProcessor")
					&& username != null && !username.isEmpty())
					|| (url.contains("library/createLibraryWizard")
							&& request.getParameterValues("actionName")[0]
									.equals("createLibraryWizard")
							&& request.getParameterValues("wizardActionClass")[0]
									.equals("com.ptc.windchill.enterprise.library.forms.CreateLibraryFormProcessor")
							&& username != null && !username.isEmpty())) {
				Authenticator myAuth = new EMAuthenticator();
				Authenticator.setDefault(myAuth);

				QueryAgent_Service queryAgentService = new QueryAgent_Service();
				com.nextlabs.em.windchill.wsclient.QueryAgent queryAgentPort = queryAgentService
						.getQueryAgentImplPort();

				List<String> params = new ArrayList<String>();

				// The current user name
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME);
				params.add(username);
				logger.info(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME
						+ "=" + username);

				// //

				// the request URL
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_URL);
				params.add(fullUrl);
				String action = "OPEN";
				action = Url2Action.url2Action(emCtx, url,
						httpServletRequest.getMethod(), queryString);
				logger.info(com.nextlabs.em.windchill.Constants.PARANAME_POLICYACTION
						+ "=" + action);
				// the policy action
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_POLICYACTION);
				params.add(action);

				// the referer URL
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_REFERER);
				params.add(referer);

				// the referer quertystring
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_QUERYSTRING);
				params.add(queryString);
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_ACTIONNAME);
				params.add(request.getParameter("actionName"));
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_WIZARDACTIONCLASS);
				params.add(request.getParameter("wizardActionClass"));
				params.add(com.nextlabs.em.windchill.Constants.PARANAME_TABLEID);
				params.add(request.getParameter("tableID"));

				// reading query parameters
				// List<String> resultStrList = null;
				List<String> resultStrList = queryAgentPort.eval(
						emCtx.getRequestId(), params,
						"Entitlement Manager for Windchill");

				for (int i = 0; i < resultStrList.size(); i++) {
					if (i == resultStrList.size() - 1) {
						logger.info("ret " + i + ":" + resultStrList.get(i)
								+ " is discarded");
					} else {
						logger.info("ret " + i + ":" + resultStrList.get(i)
								+ "=" + resultStrList.get(i + 1));
						if (resultStrList
								.get(i)
								.equalsIgnoreCase(
										com.nextlabs.em.windchill.Constants.PARANAME_POLICYNAME))
							policyName = resultStrList.get(++i);
						else if (resultStrList
								.get(i)
								.equalsIgnoreCase(
										com.nextlabs.em.windchill.Constants.PARANAME_POLICYMESSAGE))
							policyMessage = resultStrList.get(++i);
						else if (resultStrList
								.get(i)
								.equalsIgnoreCase(
										com.nextlabs.em.windchill.Constants.PARANAME_POLICYEFFECT)) {
							if (resultStrList.get(++i)
									.equalsIgnoreCase("allow"))
								isAllowed = true;
							else
								isAllowed = false;
						}
					}
				}

			} else {
				logger.info("no need enforcement for url=" + url);
			}

		}
		long lEndTime = System.currentTimeMillis();
		if (isAllowed == false) {

			if (request.getParameterValues("popupActionMethod") != null
					&& request.getParameterValues("popupActionMethod")[0]
							.equals("list_cut")
					&& request.getParameterValues("pjl_selPJLsa1__1") != null) {
				if (request.getParameterValues("ACTION") == null) {
					String refererUrl = httpServletRequest.getHeader("referer");
					String imageURL = request.getScheme() + "://"
							+ request.getServerName() + ""
							+ servletContext.getContextPath();
					httpServletResponse
							.setContentType("text/html;charset=UTF-8");
					httpServletResponse.getWriter().write(
							DenyPageHelper
									.getMessage(policyName, policyMessage));
				} else {
					String refererUrl = httpServletRequest.getHeader("referer");
					String imageURL = request.getScheme() + "://"
							+ request.getServerName() + ""
							+ servletContext.getContextPath();
					httpServletResponse
							.setContentType("text/html;charset=UTF-8");
					httpServletResponse.getWriter().write(
							DenyPageHelper.getPage(policyName, policyMessage,
									refererUrl, imageURL));
				}/*
				 * } else if(!request.getParameterValues("ACTION")[0].equals(
				 * "CONTAINERMOVE")){ String refererUrl =
				 * httpServletRequest.getHeader("referer"); String imageURL =
				 * request
				 * .getScheme()+"://"+request.getServerName()+""+servletContext
				 * .getContextPath();
				 * httpServletResponse.setContentType("text/html;charset=UTF-8"
				 * );
				 * httpServletResponse.getWriter().write(DenyPageHelper.getMessage
				 * (policyName, policyMessage)); }
				 */

			} else if (request.getParameterValues("popupActionMethod") != null
					&& request.getParameterValues("popupActionMethod")[0]
							.equals("list_copy")
					&& request.getParameterValues("pjl_selPJLsa1__1") != null) {
				if (request.getParameterValues("ACTION") == null) {
					String refererUrl = httpServletRequest.getHeader("referer");
					String imageURL = request.getScheme() + "://"
							+ request.getServerName() + ""
							+ servletContext.getContextPath();
					httpServletResponse
							.setContentType("text/html;charset=UTF-8");
					httpServletResponse.getWriter().write(
							DenyPageHelper
									.getMessage(policyName, policyMessage));
				} else {
					String refererUrl = httpServletRequest.getHeader("referer");
					String imageURL = request.getScheme() + "://"
							+ request.getServerName() + ""
							+ servletContext.getContextPath();
					httpServletResponse
							.setContentType("text/html;charset=UTF-8");
					httpServletResponse.getWriter().write(
							DenyPageHelper.getPage(policyName, policyMessage,
									refererUrl, imageURL));
				}
				/*
				 * else if(!request.getParameterValues("ACTION")[0].equals(
				 * "CONTAINERMOVE")){ String refererUrl =
				 * httpServletRequest.getHeader("referer"); String imageURL =
				 * request
				 * .getScheme()+"://"+request.getServerName()+""+servletContext
				 * .getContextPath();
				 * httpServletResponse.setContentType("text/html;charset=UTF-8"
				 * );
				 * httpServletResponse.getWriter().write(DenyPageHelper.getMessage
				 * (policyName, policyMessage)); }
				 */

			} else if (request.getParameterValues("popupActionMethod") != null
					&& request.getParameterValues("popupActionMethod")[0]
							.equals("copy")) {
				if (request.getParameterValues("ACTION") == null) {
					String refererUrl = httpServletRequest.getHeader("referer");
					String imageURL = request.getScheme() + "://"
							+ request.getServerName() + ""
							+ servletContext.getContextPath();
					httpServletResponse
							.setContentType("text/html;charset=UTF-8");
					httpServletResponse.getWriter().write(
							DenyPageHelper
									.getMessage(policyName, policyMessage));
				} else {
					String refererUrl = httpServletRequest.getHeader("referer");
					String imageURL = request.getScheme() + "://"
							+ request.getServerName() + ""
							+ servletContext.getContextPath();
					httpServletResponse
							.setContentType("text/html;charset=UTF-8");
					httpServletResponse.getWriter().write(
							DenyPageHelper.getPage(policyName, policyMessage,
									refererUrl, imageURL));
				}

			} else if (request.getParameterValues("popupActionMethod") != null
					&& request.getParameterValues("popupActionMethod")[0]
							.equals("cut")) {
				if (request.getParameterValues("ACTION") == null) {
					String refererUrl = httpServletRequest.getHeader("referer");
					String imageURL = request.getScheme() + "://"
							+ request.getServerName() + ""
							+ servletContext.getContextPath();
					httpServletResponse
							.setContentType("text/html;charset=UTF-8");
					httpServletResponse.getWriter().write(
							DenyPageHelper
									.getMessage(policyName, policyMessage));
				}

			} else if (request.getParameterValues("taskid") != null
					&& request.getParameterValues("taskid")[0]
							.equals("SETLCSTATE_TASK")
					&& request.getParameterValues("SetStateJSTable_objOID") != null) {
				String refererUrl = httpServletRequest.getHeader("referer");
				String imageURL = request.getScheme() + "://"
						+ request.getServerName() + ""
						+ servletContext.getContextPath();
				httpServletResponse.setContentType("text/html;charset=UTF-8");
				httpServletResponse.getWriter()
						.write(DenyPageHelper.getStatusPage(policyName,
								policyMessage));
				// if (request.getParameterValues("taskid")!= null &&
				// request.getParameterValues("taskid")[0].equals("SetStateTask")
				// &&
				// request.getParameterValues("SetStateJSTable_objOID")!=null) {
			} else if (request.getParameterValues("taskid") != null
					&& request.getParameterValues("taskid")[0]
							.equals("SetStateTask")
					&& request.getParameterValues("SetStateJSTable_objOID") != null) {
				String refererUrl = httpServletRequest.getHeader("referer");
				String imageURL = request.getScheme() + "://"
						+ request.getServerName() + ""
						+ servletContext.getContextPath();
				httpServletResponse.setContentType("text/html;charset=UTF-8");
				httpServletResponse.getWriter()
						.write(DenyPageHelper.getStatusPage(policyName,
								policyMessage));
			} else {
				String refererUrl = httpServletRequest.getHeader("referer");
				String imageURL = request.getScheme() + "://"
						+ request.getServerName() + ""
						+ servletContext.getContextPath();
				httpServletResponse.setContentType("text/html;charset=UTF-8");
				httpServletResponse.getWriter().write(
						DenyPageHelper.getPage(policyName, policyMessage,
								refererUrl, imageURL));

			}

		} else {
			filterChain.doFilter(request, response);
		}

		logger.info("EntitlementManagerFilter doFilter end at " + lStartTime
				+ "(" + (lEndTime - lStartTime) + ")");
		return;

	}

	public ObjectAttrCollection QueryUser(String userName, String requestId,
			String reserved1, String reserved2) throws QueryAgentException {
		EntitlementManagerContext ctx = new EntitlementManagerContext();
		ctx.setRequestId(requestId);
		logger.info(" QueryAgent -> QueryUser (" + userName + ")");
		ObjectAttrCollection attrCol = new ObjectAttrCollection();
		WTUser user = WindchillObjectHelper.getUser(userName);
		if (user != null) {
			if (WindchillObjectHelper.getAttrs(attrCol, user, ctx) == false) {
				return null;
			}
		} else {
			return null;
		}

		return attrCol;

	}

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

	private List<String> Eval(String requestId, String[] paras,
			HashMap<String, String> queryStringentries, String reserved)
			throws QueryAgentException {
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		emCtx.setRequestId(requestId);
		java.util.List<String> retList = new java.util.ArrayList<String>();

		try {
			String username = null, policyaction = null, url = null, referer = null, querystring = null;
			for (int i = 0; i < paras.length; i++) {
				if (i == paras.length - 1) {
					logger.info(" para " + i / 2 + ":" + paras[i]
							+ " is discarded");
				} else {
					logger.info(" para " + i / 2 + ":" + paras[i] + "="
							+ paras[i + 1]);
					if (paras[i]
							.equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME))
						username = paras[++i];
					// else if
					// (paras[i].equalsIgnoreCase(com.nextlabs.em.windchill.Constants.PARANAME_OIDS))
					// oids = paras[++i];
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
				}
			}
			emCtx.setUserName(username);
			logger.info("getting property");
			String ipaddr = ConfigurationManager.getInstance().getProperty(
					com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PC_IP_ADDRESS);
			String userId = ConfigurationManager.getInstance().getProperty(
					com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PEP_USER_ID);
			PolicyEvaluator evaluator = new PolicyEvaluator(ipaddr);
			evaluator.setContext(emCtx);
			logger.info("seting context");
			evaluator.setUser(username, username);
			evaluator.setAction(policyaction);
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
					if (key != null && value != null) {

						if (key.equals(userId)) {
							if (value.startsWith("null")) {
								int index = value.indexOf("null");
								value = value.substring(index + 4);
							}
							value = value.trim();
							if (value.contains(";")) {
								value = value.substring(0,
										value.lastIndexOf(";"));
								logger.info("inside if block UserID  user-->"
										+ key + "=" + value);
							}
							logger.info("UserID  user----->" + key + "="
									+ value);
							evaluator.setUser(username, value);
						}
						/*
						 * else evaluator.setUserAttr(key, value);
						 */
					}
				}
			} else
				logger.info("  QueryAgentImpl.Eval user attributes is null");
			logger.info("after  user attributes");
			evaluator.setSource(url, "Portal");
			evaluator.setSourceAttr("referer", referer);
			evaluator.setSourceAttr("original url", url + "?" + querystring);

			evaluator.setSourceAttr("actionName",
					queryStringentries.get("actionName"));
			evaluator.setSourceAttr("wizardActionClass",
					queryStringentries.get("wizardActionClass"));
			evaluator.setSourceAttr("tableID",
					queryStringentries.get("tableID"));

			EvaluationResult result = evaluator.eval();

			if (result != null)
				result.print(emCtx);

			if (result != null) {
				logger.info(" Start to execute obligation(s)");
				// result.ExecObligation(emCtx, oidsArray, userAttrCol,
				// objAttrs);
			}
			String policyName = result.getPolicyName();
			logger.info("policy name:" + policyName);
			String policyMessage = result.getPolicyMessage();
			logger.info("policy message:" + policyMessage);
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
			logger.info("Exception in evaluation:" + e.getMessage());
			logger.error(e);
		}
		return retList;
	}

	public void doFilterBak(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		httpServletRequest.setAttribute("requestId", emCtx.getRequestId());
		String url = httpServletRequest.getRequestURI();
		emCtx.setSource(url);
		logger.info("EntitlementManagerFilter doFilter start...url=" + url);
		String username = httpServletRequest.getRemoteUser();
		emCtx.setUserName(username);
		EvaluationResult result = null;
		logger.info("EntitlementManagerFilter doFilter start...requestid="
				+ emCtx.getRequestId());
		logger.info("EntitlementManagerFilter doFilter start...requestid2="
				+ (String) httpServletRequest.getAttribute("requestId"));
		// String oid=request.getParameter("oid");
		String oids[] = request.getParameterValues("oid");
		if (oids != null) {
			for (int i = 0; i < oids.length; i++) {
				logger.info("EntitlementManagerFilter doFilter =*=oids=*="
						+ oids[i]);
			}
		}
		if (oids != null && oids.length > 0 && username != null
				&& !username.isEmpty()
				&& url != "/Windchill/com/nextlabs/QueryAgent") {
			try {
				Authenticator myAuth = new EMAuthenticator();
				Authenticator.setDefault(myAuth);

				// URL wsdlUrl = new
				// URL("http://localhost/Windchill/com/nextlabs/QueryAgent?wsdl");
				// QName qName = new
				// QName("http://www.nextlabs.com/windchill/QueryAgent",
				// "QueryAgent");
				// Service service = Service.create(wsdlUrl,qName);
				// QueryAgent port = service.getPort(QueryAgent.class);
				QueryAgent_Service queryAgentService = new QueryAgent_Service();
				com.nextlabs.em.windchill.wsclient.QueryAgent queryAgentPort = queryAgentService
						.getQueryAgentImplPort();
				// All kinds type oid:
				// ContainerID OR:wt.pdmlink.PDMLinkProduct:100136
				// oid OR:wt.org.WTUser:41884
				// oid VR:wt.part.WTPart:100772
				// oid OR:wt.inf.container.OrgContainer:110115
				// oid OR:wt.folder.SubFolder:110177
				// oid VR:com.ptc.wpcfg.doc.VariantSpec:100015
				// oid VR:wt.doc.WTDocument:122535
				// oid OR:wt.content.ApplicationData:126248
				// oid OR:com.ptc.core.lwc.server.LWCTypeDefinition:9905
				String ipaddr = ConfigurationManager.getInstance().getProperty(
						com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
						com.nextlabs.Property.PC_IP_ADDRESS);
				PolicyEvaluator evaluator = new PolicyEvaluator(ipaddr);
				evaluator.setContext(emCtx);

				// test for runtime sync for configuration
				// String
				// testProp=ConfigurationManager.getInstance().getProperty("pep.properties",
				// com.nextlabs.Property.PEP_WINDCHILL_TEST_PROPERTY);
				// emCtx.log(" +++++++++++++++++="+testProp+"=+++++");

				// set user component for evaluation
				evaluator.setUser(username, username);
				if (username != null && !username.isEmpty()) {
					logger.info("EntitlementManagerFilter queryUser("
							+ username + ")");
					com.nextlabs.em.windchill.wsclient.ObjectAttrCollection userAttrCol = queryAgentPort
							.queryUser(username, emCtx.getRequestId(), null,
									null);
					if (userAttrCol != null) {
						MapConvertor mapConvertor = userAttrCol.getAttrs();
						java.util.List<MapEntry> mapEntries = mapConvertor
								.getEntries();

						for (int i = 0; i < mapEntries.size(); i++) {
							MapEntry entry = mapEntries.get(i);
							String key = entry.getKey();
							String value = entry.getValue();
							if (key != null && value != null) {
								if (key.equalsIgnoreCase("uid"))
									evaluator.setUser(username, value);
								else
									evaluator.setUserAttr(key, value);
							}
						}
					} else
						logger.info("EntitlementManagerFilter user attributes is null");
				}

				// set source component for evaluation
				evaluator.setSource(url, "Portal");
				for (int idx = 0; idx < oids.length; idx++) {
					String oid = oids[idx];
					logger.info("EntitlementManagerFilter queryOid(" + oid
							+ ")");
					com.nextlabs.em.windchill.wsclient.ObjectAttrCollection sourceAttrCol = queryAgentPort
							.queryOid(oid, emCtx.getRequestId(), null, null);
					if (sourceAttrCol != null) {
						MapConvertor mapConvertor = sourceAttrCol.getAttrs();
						java.util.List<MapEntry> mapEntries = mapConvertor
								.getEntries();
						for (int i = 0; i < mapEntries.size(); i++) {
							MapEntry entry = mapEntries.get(i);
							String key = entry.getKey();
							String value = entry.getValue();
							if (key != null && value != null) {
								if (key.equalsIgnoreCase(CESdkConstants.RESOURCE_ATTR_KEY) == true)
									evaluator.setSource(value, "Portal");
								else
									evaluator.setSourceAttr(key, value);
							}
						}
					}
				}

				// policy action
				evaluator.setAction("OPEN");

				result = evaluator.eval();
				// emCtx.log(" EntitlementManagerFilter evalulation
				// finished............."+result.isAllowed());

			} catch (Exception exp) {
				logger.warn("EntitlementManagerFilter>exception: "
						+ exp.getMessage());
				exp.printStackTrace(httpServletResponse.getWriter());
			}
		} else {
			logger.info("EntitlementManagerFilter skip " + url);
		}

		if (result != null)
			result.print(emCtx);

		if (result != null && result.isAllowed() == false) {
			String refererUrl = httpServletRequest.getHeader("referer");
			String policyName = result.getPolicyName();
			String policyMessage = result.getPolicyMessage();
			httpServletResponse.getWriter().write(
					DenyPageHelper.getPage(policyName, policyMessage,
							refererUrl));
		} else {
			filterChain.doFilter(request, response);
		}
		logger.info("EntitlementManagerFilter doFilter end...");
		return;

	}

	public void destroy() {
		logger.info("EntitlementManagerFilter.destroy... ");
	}
}