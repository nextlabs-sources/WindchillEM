package com.nextlabs.em.windchill.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import wt.access.AccessControlHelper;
import wt.access.NotAuthorizedException;
import wt.access.SecurityLabeled;
import wt.access.SecurityLabeledDownloadAcknowledgment;
import wt.access.configuration.SecurityLabel;
import wt.access.configuration.SecurityLabelsConfiguration;
import wt.access.configuration.SecurityLabelsHelper;
import wt.audit.AuditHelper;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.fc.EnumeratedType;
import wt.fc.EnumeratedTypeUtil;
import wt.fc.ObjectNoLongerExistsException;
import wt.fc.Persistable;
import wt.fc.ReferenceFactory;
import wt.fc.WTReference;
import wt.fc.collections.WTKeyedHashMap;
import wt.httpgw.URLFactory;
import wt.log4j.LogR;
import wt.type.TypedUtility;
import wt.util.HTMLEncoder;
import wt.util.LocalizableMessage;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

import com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionRequest;
import com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionRequestHolder;
import com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse;
import com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseHolder;
import com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsHelper;
import com.ptc.core.meta.common.AttributeTypeIdentifier;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.beans.HTTPRequestData;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.windchill.enterprise.servlets.DownloadStateManager;
import com.ptc.windchill.enterprise.util.AttachmentsWebHelper;

public final class AttachmentsDownloadDirectionServlet extends HttpServlet {
	private static final Logger clientLogger = LogR.getLogger("com.ptc.windchill.enterprise.servlets");
	private static final long serialVersionUID = 1L;
	public static final String EXTRA_PATH_ALL = "extra_path_all";
	public static final String EXTRA_PATH_PRIMARY = "extra_path_primary";
	public static final String EXTRA_PATH_SELECTED = "extra_path_selected";
	public static final String EXTRA_PATH_SINGLE = "extra_path_single";
	public static final String FORWARD_DIRECT_DOWNLOAD = "forward-direct-download";
	public static final String FORWARD_DETECT_DTI = "forward-detect-dti";
	public static final String FORWARD_CONFIRM_DTI = "forward-confirm-dti";
	public static final String FORWARD_DTI_DOWNLOAD = "forward-dti-download";
	public static final String FORWARD_DETECT_JRE = "forward-detect-jre";
	public static final String FORWARD_CONFIRM_JRE = "forward-confirm-jre";
	public static final String FORWARD_APPLET_DOWNLOAD = "forward-applet-download";
	public static final String FORWARD_NONE_DOWNLOADABLE = "forward-none-downloadable";
	public static final String FORWARD_NOT_EXIST = "forward-not-exist";
	public static final String FORWARD_ERROR = "forward-error";
	public static final String PARA_DTI_STATUS = "adti";
	public static final String PARA_JRE_STATUS = "ajre";
	public static final String PARA_ACK_STATUS = "ack";
	public static final String PARA_STATUS_INSTALLED = "installed";
	public static final String PARA_STATUS_NOT_INSTALLED = "notinstalled";
	public static final String PARA_STATUS_INSTALLATION_REJECTED = "rejected";
	public static final String PARA_STATUS_INSTALLATION_REJECTED_ONCE = "rejectedonce";
	public static final String PARA_STATUS_INSTALLATION_FAILED = "failed";
	public static final String PARA_ACK_STATUS_ACCEPT = "true";
	public static final String PARA_ACK_STATUS_REJECTED = "false";
	public static final String PARA_ACK_DOWNLOAD = "true";
	public static final String PARA_CACHING_URL = "url";
	public static final String PARA_CONTENT_OID_PREFIX = "coid_";
	public static final String PARA_CONTENT_HOLDER_OID = "oid";
	public static final String PARA_CONTENT_HOLDER_CHOID = "chOid";
	public static final String PARA_CONTENT_ITEM_OIDS = "cioids";
	public static final String PARA_SELECTED_WINDCHILL_REFERENCES = "soid";
	public static final String PARA_TYPE_PRIMARY = "primary";
	public static final String PARA_TYPE_ALL = "all";
	public static final String PARA_DELIM_OID = "||";
	public static final String REQUEST_KEY_DOWNLOAD_DIRECTION = "attachements-download-direction";
	public static final String REQUEST_KEY_ATTACHMENTS_EXCEPTION = "attachements-exception";
	public static final String REQUEST_KEY_URI = "attachments-download-uri";
	public static final String SECURITY_LABEL_MAP = "security-label-map";
	public static final String DOWNLOAD_STATE_FORMAT = "downloadStateFormat";
	private static final String PREF_KEY_MECHANISM = "/com/ptc/windchill/enterprise/attachments/downloadMechanism";
	private static final String SESSION_ACK = "sessiomAcknowledge";
	private static ReferenceFactory refFactory = new ReferenceFactory();
	private String paraACK = "";
	private static SecurityLabelsConfiguration labelConfig = null;

	protected void processRequest(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse) throws ServletException, IOException {
		paramHttpServletRequest.setAttribute("attachments-download-uri", paramHttpServletRequest.getRequestURI());

		String str1 = HTMLEncoder.encodeForHTMLAttribute(paramHttpServletRequest.getParameter("adti"));
		String str2 = HTMLEncoder.encodeForHTMLAttribute(paramHttpServletRequest.getParameter("ajre"));

		String str3 = null;
		try {
			AttachmentsDownloadDirectionRequest localAttachmentsDownloadDirectionRequest = null;
			AttachmentsDownloadDirectionResponse localAttachmentsDownloadDirectionResponse = null;

			DownloadStateManager localDownloadStateManager = DownloadStateManager
					.getDownloadStateManager(paramHttpServletRequest);
			if ((str1 != null) && (str1.length() > 0)) {
				localDownloadStateManager.setDTIStatus(DownloadStateManager.transformStatus(str1));
			}
			if ((str2 != null) && (str2.length() > 0)) {
				localDownloadStateManager.setJREStatus(DownloadStateManager.transformStatus(str2));
			}
			int i = getUserDownloadPreference(paramHttpServletRequest);
			HashMap localHashMap;
			HttpSession localHttpSession;
			Object localObject1;
			URLFactory localURLFactory;
			String str4;
			String str5;
			String str6;
			String str7;
			if (i == 0) {
				localHashMap = new HashMap();

				localAttachmentsDownloadDirectionRequest = decodeRequest(paramHttpServletRequest);
				localAttachmentsDownloadDirectionRequest.setDownloadType(0);
				localAttachmentsDownloadDirectionResponse = AttachmentsHelper.service
						.downloadAttachmentDirections(localAttachmentsDownloadDirectionRequest);
				if (isSecurityLableInstalled()) {
					localHttpSession = paramHttpServletRequest.getSession(true);
					localObject1 = (Set) localHttpSession.getAttribute("sessiomAcknowledge");
					if (localObject1 == null) {
						localObject1 = new HashSet();
					}
					if ((this.paraACK != null) && (this.paraACK.equals("true"))
							&& (((Set) localObject1).contains(paramHttpServletRequest.getQueryString()))) {
						((Set) localObject1).remove(paramHttpServletRequest.getQueryString());
						localHttpSession.setAttribute("sessiomAcknowledge", localObject1);
						if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
							paramHttpServletRequest.setAttribute("attachements-download-direction",
									localAttachmentsDownloadDirectionResponse);
							str3 = getInitParameter("forward-direct-download",
									"/netmarkets/jsp/attachments/download/browser.jsp");

							dispatchSecurityLabelDownloadAckEvent(paramHttpServletRequest);
						} else {
							str3 = getInitParameter("forward-none-downloadable",
									"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
						}
					} else if ((this.paraACK != null) && (this.paraACK.equals("false"))) {
						((Set) localObject1).remove(paramHttpServletRequest.getQueryString());
						localHttpSession.setAttribute("sessiomAcknowledge", localObject1);
						str3 = getInitParameter("forward-none-downloadable",
								"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
					} else if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
						paramHttpServletRequest.setAttribute("attachements-download-direction",
								localAttachmentsDownloadDirectionResponse);

						localURLFactory = new URLFactory();
						str4 = paramHttpServletRequest.getRequestURI();
						str5 = localURLFactory.determineResource(str4);
						str6 = paramHttpServletRequest.getQueryString();
						str7 = localURLFactory.getHREF(str5, str6);
						clientLogger.debug("resource:" + str5 + "  queryString:" + str6 + "  finalURL:" + str7);

						paramHttpServletRequest.setAttribute("url", str7);
						securityLabelSet(paramHttpServletRequest, localHashMap);
						paramHttpServletRequest.setAttribute("security-label-map", localHashMap);
						paramHttpServletRequest.setAttribute("downloadStateFormat", Integer.valueOf(i));
						((Set) localObject1).add(paramHttpServletRequest.getQueryString());
						localHttpSession.setAttribute("sessiomAcknowledge", localObject1);
						if (localHashMap.isEmpty()) {
							str3 = getInitParameter("forward-direct-download",
									"/netmarkets/jsp/attachments/download/browser.jsp");
						} else {
							str3 = getInitParameter("forward-direct-download",
									"/netmarkets/jsp/attachments/download/openAcknowledgeConfirm.jsp");
						}
					} else {
						str3 = getInitParameter("forward-none-downloadable",
								"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
					}
				} else if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
					paramHttpServletRequest.setAttribute("attachements-download-direction",
							localAttachmentsDownloadDirectionResponse);
					str3 = getInitParameter("forward-direct-download",
							"/netmarkets/jsp/attachments/download/browser.jsp");
				} else {
					str3 = getInitParameter("forward-none-downloadable",
							"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
				}
			} else if (i == 1) {
				switch (localDownloadStateManager.getDTIStatus()) {
				case 1:
					localAttachmentsDownloadDirectionRequest = decodeRequest(paramHttpServletRequest);
					localAttachmentsDownloadDirectionRequest.setDownloadType(1);
					localAttachmentsDownloadDirectionResponse = AttachmentsHelper.service
							.downloadAttachmentDirections(localAttachmentsDownloadDirectionRequest);
					if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
						populatePreferenceValues(paramHttpServletRequest, localAttachmentsDownloadDirectionResponse);
						paramHttpServletRequest.setAttribute("attachements-download-direction",
								localAttachmentsDownloadDirectionResponse);
						paramHttpServletRequest.getSession(true).setAttribute("attachements-download-direction",
								localAttachmentsDownloadDirectionResponse);

						str3 = getInitParameter("forward-dti-download", "/netmarkets/jsp/attachments/download/dti.jsp");
					} else {
						str3 = getInitParameter("forward-none-downloadable",
								"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
					}
					break;
				case 0:
					str3 = getInitParameter("forward-detect-dti",
							"/netmarkets/jsp/attachments/download/detect_dti.jsp");

					break;
				case 2:
					localDownloadStateManager.setDTIStatus(0);
					str3 = getInitParameter("forward-confirm-dti",
							"/netmarkets/jsp/attachments/download/confirm_dti.jsp");

					break;
				case 3:
				case 4:
					localAttachmentsDownloadDirectionRequest = decodeRequest(paramHttpServletRequest);
					localAttachmentsDownloadDirectionRequest.setDownloadType(0);
					localAttachmentsDownloadDirectionResponse = AttachmentsHelper.service
							.downloadAttachmentDirections(localAttachmentsDownloadDirectionRequest);
					if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
						paramHttpServletRequest.setAttribute("attachements-download-direction",
								localAttachmentsDownloadDirectionResponse);
						str3 = getInitParameter("forward-direct-download",
								"/netmarkets/jsp/attachments/download/browser.jsp");
					} else {
						str3 = getInitParameter("forward-none-downloadable",
								"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
					}
					break;
				case 5:
					localAttachmentsDownloadDirectionRequest = decodeRequest(paramHttpServletRequest);
					localAttachmentsDownloadDirectionRequest.setDownloadType(0);
					localAttachmentsDownloadDirectionResponse = AttachmentsHelper.service
							.downloadAttachmentDirections(localAttachmentsDownloadDirectionRequest);
					if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
						paramHttpServletRequest.setAttribute("attachements-download-direction",
								localAttachmentsDownloadDirectionResponse);
						str3 = getInitParameter("forward-direct-download",
								"/netmarkets/jsp/attachments/download/browser.jsp");
					} else {
						str3 = getInitParameter("forward-none-downloadable",
								"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
					}
					localDownloadStateManager.setDTIStatus(2);
				}
			} else if (i == 2) {
				switch (localDownloadStateManager.getJREStatus()) {
				case 1:
					localHashMap = new HashMap();

					localAttachmentsDownloadDirectionRequest = decodeRequest(paramHttpServletRequest);
					localAttachmentsDownloadDirectionRequest.setDownloadType(2);
					localAttachmentsDownloadDirectionResponse = AttachmentsHelper.service
							.downloadAttachmentDirections(localAttachmentsDownloadDirectionRequest);
					if (isSecurityLableInstalled()) {
						localHttpSession = paramHttpServletRequest.getSession(true);
						localObject1 = (Set) localHttpSession.getAttribute("sessiomAcknowledge");
						if (localObject1 == null) {
							localObject1 = new HashSet();
						}
						if ((this.paraACK != null) && (this.paraACK.equals("true"))
								&& (((Set) localObject1).contains(paramHttpServletRequest.getQueryString()))) {
							((Set) localObject1).remove(paramHttpServletRequest.getQueryString());
							localHttpSession.setAttribute("sessiomAcknowledge", localObject1);
							if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
								populatePreferenceValues(paramHttpServletRequest,
										localAttachmentsDownloadDirectionResponse);
								paramHttpServletRequest.setAttribute("attachements-download-direction",
										localAttachmentsDownloadDirectionResponse);
								str3 = getInitParameter("forward-applet-download",
										"/servlet/AttachmentsDownloadAppletServlet");

								dispatchSecurityLabelDownloadAckEvent(paramHttpServletRequest);
							} else {
								str3 = getInitParameter("forward-none-downloadable",
										"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
							}
						} else if ((this.paraACK != null) && (this.paraACK.equals("false"))) {
							((Set) localObject1).remove(paramHttpServletRequest.getQueryString());
							localHttpSession.setAttribute("sessiomAcknowledge", localObject1);
							str3 = getInitParameter("forward-none-downloadable",
									"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
						} else {
							((Set) localObject1).add(paramHttpServletRequest.getQueryString());
							localHttpSession.setAttribute("sessiomAcknowledge", localObject1);
							if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
								populatePreferenceValues(paramHttpServletRequest,
										localAttachmentsDownloadDirectionResponse);
								paramHttpServletRequest.setAttribute("attachements-download-direction",
										localAttachmentsDownloadDirectionResponse);

								localURLFactory = new URLFactory();
								str4 = paramHttpServletRequest.getRequestURI();
								str5 = localURLFactory.determineResource(str4);
								str6 = paramHttpServletRequest.getQueryString();
								str7 = localURLFactory.getHREF(str5, str6);
								clientLogger.debug("resource:" + str5 + "  queryString:" + str6 + "  finalURL:" + str7);

								paramHttpServletRequest.setAttribute("url", str7);
								securityLabelSet(paramHttpServletRequest, localHashMap);
								paramHttpServletRequest.setAttribute("security-label-map", localHashMap);
								paramHttpServletRequest.setAttribute("downloadStateFormat", Integer.valueOf(i));
								if (localHashMap.isEmpty()) {
									str3 = getInitParameter("forward-applet-download",
											"/servlet/AttachmentsDownloadAppletServlet");
								} else {
									str3 = getInitParameter("forward-direct-download",
											"/netmarkets/jsp/attachments/download/openAcknowledgeConfirm.jsp");
								}
							} else {
								str3 = getInitParameter("forward-none-downloadable",
										"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
							}
						}
					} else if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
						populatePreferenceValues(paramHttpServletRequest, localAttachmentsDownloadDirectionResponse);
						paramHttpServletRequest.setAttribute("attachements-download-direction",
								localAttachmentsDownloadDirectionResponse);
						str3 = getInitParameter("forward-applet-download", "/servlet/AttachmentsDownloadAppletServlet");
					} else {
						str3 = getInitParameter("forward-none-downloadable",
								"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
					}
					break;
				case 0:
					str3 = getInitParameter("forward-detect-jre",
							"/netmarkets/jsp/attachments/download/detect_jre.jsp");

					break;
				case 2:
					localDownloadStateManager.setJREStatus(0);
					str3 = getInitParameter("forward-confirm-jre",
							"/netmarkets/jsp/attachments/download/confirm_jre.jsp");

					break;
				case 3:
				case 4:
					localAttachmentsDownloadDirectionRequest = decodeRequest(paramHttpServletRequest);
					localAttachmentsDownloadDirectionRequest.setDownloadType(0);
					localAttachmentsDownloadDirectionResponse = AttachmentsHelper.service
							.downloadAttachmentDirections(localAttachmentsDownloadDirectionRequest);
					if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
						paramHttpServletRequest.setAttribute("attachements-download-direction",
								localAttachmentsDownloadDirectionResponse);
						str3 = getInitParameter("forward-direct-download",
								"/netmarkets/jsp/attachments/download/browser.jsp");
					} else {
						str3 = getInitParameter("forward-none-downloadable",
								"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
					}
					break;
				case 5:
					localAttachmentsDownloadDirectionRequest = decodeRequest(paramHttpServletRequest);
					localAttachmentsDownloadDirectionRequest.setDownloadType(0);
					localAttachmentsDownloadDirectionResponse = AttachmentsHelper.service
							.downloadAttachmentDirections(localAttachmentsDownloadDirectionRequest);
					if (getNumberOfItems(localAttachmentsDownloadDirectionResponse) > 0) {
						paramHttpServletRequest.setAttribute("attachements-download-direction",
								localAttachmentsDownloadDirectionResponse);
						str3 = getInitParameter("forward-direct-download",
								"/netmarkets/jsp/attachments/download/browser.jsp");
					} else {
						str3 = getInitParameter("forward-none-downloadable",
								"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
					}
					localDownloadStateManager.setJREStatus(2);
				}
			}
		} catch (ServletException localServletException) {
			str3 = getInitParameter("forward-none-downloadable",
					"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
		} catch (NotAuthorizedException localNotAuthorizedException) {
			str3 = getInitParameter("forward-none-downloadable",
					"/netmarkets/jsp/attachments/download/none_downloadable.jsp");
		} catch (ObjectNoLongerExistsException localObjectNoLongerExistsException) {
			str3 = getInitParameter("forward-not-exist", "/netmarkets/jsp/attachments/download/not_exist.jsp");
		} catch (Exception localException) {
			clientLogger.error("attachements-exception", localException);
			str3 = getInitParameter("forward-error", "/netmarkets/jsp/attachments/download/error.jsp");
		} finally {
			paramHttpServletResponse.setHeader("Cache-Control", "no-cache");
			paramHttpServletRequest.getRequestDispatcher(str3).forward(paramHttpServletRequest,
					paramHttpServletResponse);
		}
	}

	private boolean isSecurityLableInstalled() throws IOException, WTException {
		return SecurityLabelsHelper.isSecurityLabelFeatureEnabled();
	}

	private void securityLabelSet(HttpServletRequest paramHttpServletRequest, Map<String, Object[]> paramMap)
			throws IOException, WTException {
		String[] arrayOfString = paramHttpServletRequest.getParameterValues("oid");
		getSecrutiyLabels(arrayOfString, paramMap, paramHttpServletRequest);
	}

	private void getSecrutiyLabels(String[] paramArrayOfString, Map<String, Object[]> paramMap,
			HttpServletRequest paramHttpServletRequest) throws WTException, IOException {
		if (labelConfig == null) {
			labelConfig = SecurityLabelsConfiguration.getSecurityLabelsConfiguration();
		}
		Map localMap = labelConfig.getSecurityLabels();
		for (String str1 : paramArrayOfString) {
			WTReference localWTReference = null;

			localWTReference = refFactory.getReference(str1);
			localWTReference.refresh();

			Persistable localPersistable = localWTReference.getObject();

			if ((localPersistable instanceof ContentItem)) {
				String localObject = paramHttpServletRequest.getParameter("chOid");
				if (localObject != null) {
					localWTReference = refFactory.getReference((String) localObject);
					localWTReference.refresh();
					localPersistable = localWTReference.getObject();
				}
			}
			if ((localPersistable instanceof SecurityLabeled)) {
				ArrayList<SecurityLabeledDownloadAcknowledgment> localObject1 = AccessControlHelper.manager
						.getSecurityLabeledDownloadAcknowledgments((SecurityLabeled) localPersistable);
				for (SecurityLabeledDownloadAcknowledgment localSecurityLabeledDownloadAcknowledgment : localObject1) {
					String str2 = localSecurityLabeledDownloadAcknowledgment.getSecurityLabelName();
					SecurityLabel localSecurityLabel = (SecurityLabel) localMap.get(str2);
					String str3 = localSecurityLabeledDownloadAcknowledgment.getSecurityLabelValue();
					if ((!str3.equals("NULL")) && (paramMap.get(str2) == null)) {
						Object[] arrayOfObject = { localSecurityLabel, str3,
								localSecurityLabeledDownloadAcknowledgment.getLocalizableMessage() };
						paramMap.put(str2, arrayOfObject);
					}
				}
			}
		}
	}

	private Set<String> getLabelsToAcknowledge(String paramString) {
		HashSet localHashSet = new HashSet();
		if (paramString != null) {
			String[] arrayOfString1 = paramString.split(",");
			for (String str : arrayOfString1) {
				localHashSet.add(str);
			}
		}
		return localHashSet;
	}

	public static Map getSecurityLabelMap(String paramString, Map<String, Object[]> paramMap, Locale paramLocale)
			throws WTException, WTPropertyVetoException {
		HashMap localHashMap = new HashMap();

		Set<String> localSet = paramMap.keySet();
		for (String str1 : localSet) {
			SecurityLabel localSecurityLabel = (SecurityLabel) ((Object[]) paramMap.get(str1))[0];
			String str2 = (String) ((Object[]) paramMap.get(str1))[1];
			LocalizableMessage localLocalizableMessage1 = (LocalizableMessage) ((Object[]) paramMap.get(str1))[2];
			try {
				Object localObject1 = null;

				NmCommandBean localObject2 = new NmCommandBean();

				HTTPRequestData localHTTPRequestData = new HTTPRequestData();
				localHTTPRequestData.setParameterMap(new HashMap());
				localObject2.setRequestData(localHTTPRequestData);

				NmOid localNmOid = new NmOid(paramString);
				ArrayList localArrayList = new ArrayList();
				localArrayList.add(localNmOid);

				AttributeTypeIdentifier localAttributeTypeIdentifier = localSecurityLabel
						.getSecurityLabelAttributeTypeId();
				LocalizableMessage localLocalizableMessage2 = TypedUtility
						.getLocalizableMessage(localAttributeTypeIdentifier);
				String str3 = localLocalizableMessage2.getLocalizedMessage(paramLocale);

				String str4 = localSecurityLabel.getSecurityLabelValueResourceClass();
				EnumeratedType localEnumeratedType = EnumeratedTypeUtil.toEnumeratedType(str4, str2);
				String str5 = localEnumeratedType.getDisplay(paramLocale);
				String str6 = localLocalizableMessage1.getLocalizedMessage(paramLocale);

				localHashMap.put(str3 + " - " + str5, str6);
			} catch (WTException localWTException) {
				Object localObject2 = "Exception occurred getting security label description in AttachmentsDownloadDirectionServlet.java.";
				throw new WTException(localWTException, (String) localObject2);
			}
		}
		return localHashMap;
	}

	private AttachmentsDownloadDirectionRequest decodeRequest(HttpServletRequest paramHttpServletRequest)
			throws WTException, ServletException {
		String str1 = paramHttpServletRequest.getParameter("oid");
		String str2 = paramHttpServletRequest.getParameter("cioids");
		String str3 = paramHttpServletRequest.getParameter("soid");

		AttachmentsDownloadDirectionRequest localAttachmentsDownloadDirectionRequest = new AttachmentsDownloadDirectionRequest();
		//Object localObject1;
		//Object localObject2;
		int i;
		//Object localObject4;
		if ((str3 != null) && (getInitParameter("extra_path_selected", "/DOCSBselected")
				.equalsIgnoreCase(paramHttpServletRequest.getPathInfo()))) {
			String[] localObject1 = str3.split(";");
			if (localObject1.length < 1) {
				throw new ServletException(
						getClass().getName() + " error: multi-select has not selected any downloadable content");
			}
			String[] localObject2 = new String[localObject1.length];
			for (i = 0; i < localObject1.length; i++) {
				NmOid localObject4 = NmOid.newNmOid("OR:" + localObject1[i].trim());
				localObject2[i] = ((NmOid) localObject4).getReferenceString();
				AttachmentsDownloadDirectionRequestHolder localAttachmentsDownloadDirectionRequestHolder = new AttachmentsDownloadDirectionRequestHolder(
						0, null);

				localAttachmentsDownloadDirectionRequest.setAttachmentsDownloadDirectionRequestHolder(localObject2[i],
						localAttachmentsDownloadDirectionRequestHolder);
			}
			return localAttachmentsDownloadDirectionRequest;
		}
		//Object localObject6;
		if ((str1 != null) && (str1.length() > 0)) {
			Object localObject1 = null;
			if (getInitParameter("extra_path_primary", "/primary")
					.equalsIgnoreCase(paramHttpServletRequest.getPathInfo())) {
				String[] localObject2 = paramHttpServletRequest.getParameterValues("oid");
				for (i = 0; i < localObject2.length; i++) {
					localObject1 = new AttachmentsDownloadDirectionRequestHolder(0, null);

					localAttachmentsDownloadDirectionRequest.setAttachmentsDownloadDirectionRequestHolder(
							localObject2[i], (AttachmentsDownloadDirectionRequestHolder) localObject1);
				}
			} else if (getInitParameter("extra_path_single", "/single")
					.equalsIgnoreCase(paramHttpServletRequest.getPathInfo())) {
				String[] localObject2 = new String[1];
				localObject2[0] = str1;
				localObject1 = new AttachmentsDownloadDirectionRequestHolder(2, (String[]) localObject2);

				localAttachmentsDownloadDirectionRequest.setAttachmentsDownloadDirectionRequestHolder(
						"unknown_holder_oid", (AttachmentsDownloadDirectionRequestHolder) localObject1);
			} else if (getInitParameter("extra_path_selected", "/selected")
					.equalsIgnoreCase(paramHttpServletRequest.getPathInfo())) {
				NmCommandBean localObject2 = new NmCommandBean();
				((NmCommandBean) localObject2).setRequest(paramHttpServletRequest);
				ArrayList localArrayList = ((NmCommandBean) localObject2).getSelectedOidForPopup();
				if (localArrayList.size() < 1) {
					throw new ServletException(
							getClass().getName() + " error: multi-select has not selected any downloadable content");
				}
				String[] localObject4 = new String[localArrayList.size()];
				for (int m = 0; m < localArrayList.size(); m++) {
					NmOid localObject6 = (NmOid) localArrayList.get(m);
					localObject4[m] = ((NmOid) localObject6).getReferenceString();
				}
				localObject1 = new AttachmentsDownloadDirectionRequestHolder(2, (String[]) localObject4);

				localAttachmentsDownloadDirectionRequest.setAttachmentsDownloadDirectionRequestHolder(str1,
						(AttachmentsDownloadDirectionRequestHolder) localObject1);
			} else if (getInitParameter("extra_path_all", "/all")
					.equalsIgnoreCase(paramHttpServletRequest.getPathInfo())) {
				String[] localObject2 = paramHttpServletRequest.getParameterValues("oid");
				for (int j = 0; j < localObject2.length; j++) {
					localObject1 = new AttachmentsDownloadDirectionRequestHolder(1, null);

					localAttachmentsDownloadDirectionRequest.setAttachmentsDownloadDirectionRequestHolder(
							localObject2[j], (AttachmentsDownloadDirectionRequestHolder) localObject1);
				}
			} else {
				if ((str2 == null) || ("primary".equalsIgnoreCase(str2))) {
					localObject1 = new AttachmentsDownloadDirectionRequestHolder(0, null);
				} else if ((str2.length() == 0) || ("all".equalsIgnoreCase(str2))) {
					localObject1 = new AttachmentsDownloadDirectionRequestHolder(1, null);
				} else {
					String[] localObject2 = paramHttpServletRequest.getParameterValues("cioids");
					localObject2 = splitValues((String[]) localObject2, "||");
					localObject1 = new AttachmentsDownloadDirectionRequestHolder(2, (String[]) localObject2);
				}
				localAttachmentsDownloadDirectionRequest.setAttachmentsDownloadDirectionRequestHolder(str1,
						(AttachmentsDownloadDirectionRequestHolder) localObject1);
			}
		} else {
			//Object localObject3;
			//Object localObject5;
			if (getInitParameter("extra_path_selected", "/selected")
					.equalsIgnoreCase(paramHttpServletRequest.getPathInfo())) {
				NmCommandBean localObject1 = new NmCommandBean();
				((NmCommandBean) localObject1).setRequest(paramHttpServletRequest);
				ArrayList localObject2 = localObject1.getSelectedOidForPopup();
				if (((List) localObject2).size() < 1) {
					throw new ServletException(getClass().getName()
							+ " error: primary multi-select has not selected any downloadable content");
				}
				String[] localObject3 = new String[((List) localObject2).size()];
				for (int k = 0; k < ((List) localObject2).size(); k++) {
					NmOid localObject5 = (NmOid) ((List) localObject2).get(k);
					localObject3[k] = ((NmOid) localObject5).getReferenceString();
					AttachmentsDownloadDirectionRequestHolder localObject6 = new AttachmentsDownloadDirectionRequestHolder(0, null);

					localAttachmentsDownloadDirectionRequest.setAttachmentsDownloadDirectionRequestHolder(
							localObject3[k], (AttachmentsDownloadDirectionRequestHolder) localObject6);
				}
				if (((NmCommandBean) localObject1).getSessionBean() != null) {
					((NmCommandBean) localObject1).getSessionBean().getStorage().remove("multiSelect");
				}
			} else {
				Enumeration<String> localObject1 = paramHttpServletRequest.getParameterNames();
				while (((Enumeration) localObject1).hasMoreElements()) {
					String localObject2 = (String) ((Enumeration) localObject1).nextElement();
					if (localObject2.startsWith("coid_")) {
						String localObject3 = localObject2.substring("coid_".length());
						String str4 = paramHttpServletRequest.getParameter((String) localObject2);
						AttachmentsDownloadDirectionRequestHolder localObject5 = null;
						if ((str4 == null) || ("primary".equalsIgnoreCase(str4))
								|| (getInitParameter("extra_path_primary", "/primary")
										.equalsIgnoreCase(paramHttpServletRequest.getPathInfo()))) {
							localObject5 = new AttachmentsDownloadDirectionRequestHolder(0, null);
						} else if ((str4.length() == 0) || ("all".equalsIgnoreCase(str4))
								|| (getInitParameter("extra_path_all", "/all")
										.equalsIgnoreCase(paramHttpServletRequest.getPathInfo()))) {
							localObject5 = new AttachmentsDownloadDirectionRequestHolder(1, null);
						} else {
							String[] localObject6 = paramHttpServletRequest.getParameterValues((String) localObject2);
							localObject6 = splitValues(localObject6, "||");
							localObject5 = new AttachmentsDownloadDirectionRequestHolder(2, (String[]) localObject6);
						}
						localAttachmentsDownloadDirectionRequest.setAttachmentsDownloadDirectionRequestHolder(
								(String) localObject3, (AttachmentsDownloadDirectionRequestHolder) localObject5);
					}
				}
			}
		}
		return localAttachmentsDownloadDirectionRequest;
	}

	private int getUserDownloadPreference(HttpServletRequest paramHttpServletRequest) throws WTException {
		String str1 = AttachmentsWebHelper.getPreferenceValue(
				"/com/ptc/windchill/enterprise/attachments/downloadMechanism", paramHttpServletRequest);

		int i = 0;
		if (str1.equalsIgnoreCase("BROWSER")) {
			i = 0;
		} else if (str1.equalsIgnoreCase("DTI")) {
			String str2 = paramHttpServletRequest.getParameter("oid");

			int j = -1;
			if (str2 != null) {
				j = str2.indexOf("wt.doc.WTDocument");
			}
			if (j >= 0) {
				String str3 = HTMLEncoder.encodeForHTMLAttribute(paramHttpServletRequest.getParameter("role"));
				if (((str3 != null) && (str3.equals(ContentRoleType.PRIMARY.toString())))
						|| (getInitParameter("extra_path_primary", "/primary")
								.equalsIgnoreCase(paramHttpServletRequest.getPathInfo()))) {
					i = 1;
				} else {
					i = 0;
				}
			} else {
				i = 0;
			}
		} else if (str1.equals("APPLET")) {
			i = 2;
		}
		return i;
	}

	private AttachmentsDownloadDirectionResponse populatePreferenceValues(HttpServletRequest paramHttpServletRequest,
			AttachmentsDownloadDirectionResponse paramAttachmentsDownloadDirectionResponse) throws WTException {
		String str1 = AttachmentsWebHelper.getPreferenceValue(
				"/com/ptc/windchill/enterprise/attachments/downloadOperation", paramHttpServletRequest);

		String str2 = AttachmentsWebHelper.getPreferenceValue(
				"/com/ptc/windchill/enterprise/attachments/defaultLocalDirectory", paramHttpServletRequest);

		paramAttachmentsDownloadDirectionResponse
				.setAssociatedPreference("/com/ptc/windchill/enterprise/attachments/downloadOperation", str1);
		paramAttachmentsDownloadDirectionResponse
				.setAssociatedPreference("/com/ptc/windchill/enterprise/attachments/defaultLocalDirectory", str2);

		return paramAttachmentsDownloadDirectionResponse;
	}

	private String getInitParameter(String paramString1, String paramString2) {
		String str = getInitParameter(paramString1);
		if ((str == null) || (str.length() < 1)) {
			str = paramString2;
		}
		return paramString2;
	}

	private int getNumberOfItems(AttachmentsDownloadDirectionResponse paramAttachmentsDownloadDirectionResponse) {
		int i = 0;
		String[] arrayOfString = paramAttachmentsDownloadDirectionResponse.getAllContentHolderOIDs();
		for (int j = 0; (arrayOfString != null) && (j < arrayOfString.length); j++) {
			AttachmentsDownloadDirectionResponseHolder localAttachmentsDownloadDirectionResponseHolder = paramAttachmentsDownloadDirectionResponse
					.getHolder(arrayOfString[j]);
			i += localAttachmentsDownloadDirectionResponseHolder.getNumberOfItems();
		}
		return i;
	}

	protected void doGet(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
			throws ServletException, IOException {
		this.paraACK = "";
		processRequest(paramHttpServletRequest, paramHttpServletResponse);
	}

	protected void doPost(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
			throws ServletException, IOException {
		this.paraACK = paramHttpServletRequest.getParameter("ack");
		processRequest(paramHttpServletRequest, paramHttpServletResponse);
	}

	public static String[] splitValues(String[] paramArrayOfString, String paramString) {
		if (paramArrayOfString == null) {
			return null;
		}
		Vector localVector = new Vector();
		for (int i = 0; i < paramArrayOfString.length; i++) {
			StringTokenizer localStringTokenizer = new StringTokenizer(paramArrayOfString[i], paramString);
			while (localStringTokenizer.hasMoreTokens()) {
				localVector.add(localStringTokenizer.nextToken());
			}
		}
		String[] arrayOfString = new String[localVector.size()];
		arrayOfString = (String[]) localVector.toArray(arrayOfString);
		return arrayOfString;
	}

	private void dispatchSecurityLabelDownloadAckEvent(HttpServletRequest paramHttpServletRequest) throws WTException {
		String[] arrayOfString1 = paramHttpServletRequest.getParameterValues("oid");
		WTKeyedHashMap localWTKeyedHashMap = new WTKeyedHashMap();
		for (String str : arrayOfString1) {
			WTReference localWTReference = null;
			localWTReference = refFactory.getReference(str);
			Persistable localPersistable = localWTReference.getObject();
			Object localObject;
			if ((localPersistable instanceof ContentItem)) {
				localObject = paramHttpServletRequest.getParameter("chOid");
				if (localObject != null) {
					localWTReference = refFactory.getReference((String) localObject);
					localWTReference.refresh();
					localPersistable = localWTReference.getObject();
				}
			}
			if ((localPersistable instanceof SecurityLabeled)) {
				localObject = AccessControlHelper.manager
						.getSecurityLabeledDownloadAcknowledgments((SecurityLabeled) localPersistable);
				localWTKeyedHashMap.put(localPersistable, localObject);
			}
		}
		AuditHelper.service.dispatchSecurityLabelDownloadAckEvent(localWTKeyedHashMap);
	}
}
