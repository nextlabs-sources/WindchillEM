package com.nextlabs.em.windchill.enterprise.attachments.server;

import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import wt.access.SecurityLabels;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.content.ExternalStoredData;
import wt.content.FormatContentHolder;
import wt.content.Streamed;
import wt.content.URLData;
import wt.doc.WTDocument;
import wt.epm.EPMDocument;
import wt.epm.familytable.EPMSepFamilyTable;
import wt.epm.util.EPMContentHelper;
import wt.facade.scm.ScmApplicationData;
import wt.facade.scm.ScmFacade;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTReference;
import wt.fv.FvProperties;
import wt.fv.master.RedirectDownload;
import wt.fv.uploadtocache.CachedContentDescriptor;
import wt.ixb.clientAccess.IXBJarWriter;
import wt.ixb.clientAccess.StandardIXBService;
import wt.log4j.LogR;
import wt.org.WTPrincipal;
import wt.services.StandardManager;
import wt.session.SessionMgr;
import wt.util.InstalledProperties;
import wt.util.WTException;
import wt.util.WTProperties;
import wt.util.WTPropertyVetoException;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

import com.nextlabs.ConfigurationManager;
import com.nextlabs.EntitlementManagerContext;
import com.nextlabs.RightsManagerHelper;
import com.nextlabs.em.windchill.WindchillObjectHelper;
import com.nextlabs.em.windchill.tools.UtilityTools;
import com.nextlabs.em.windchill.wsclient.QueryAgent;
import com.nextlabs.em.windchill.wsclient.QueryAgent_Service;
import com.nextlabs.nxl.crypt.RightsManager;
import com.ptc.netmarkets.model.NmObjectHelper;
import com.ptc.netmarkets.model.NmOid;
//import com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionRequest;
//import com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionRequestHolder;
//import com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse;
//import com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseHolder;
//import com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem;
import com.ptc.windchill.enterprise.attachments.validators.AttachmentsValidationHelper;

public class StandardAttachmentsService extends StandardManager implements
		AttachmentsService, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  Logger logger = Logger
			.getLogger(StandardAttachmentsService.class);

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

	private static final String RESOURCE = "com.ptc.windchill.enterprise.attachments.server.serverResource";
	private static final String CLASSNAME = StandardAttachmentsService.class
			.getName();
	private static ReferenceFactory refFactory = new ReferenceFactory();
	private static final Logger log;
	private static final String OBJECT_RESOURCE = "com.ptc.netmarkets.object.objectResource";
	private static String DEFAULT_ARCHIVE_FILE_NAME = "";
	protected static String PRIMARY = "primary";
	protected static String SECONDARY = "secondary";

	static {
		try {
			log = LogR.getLogger(StandardAttachmentsService.class.getName());
		} catch (Exception localException) {
			throw new ExceptionInInitializerError(localException);
		}
		try {
			WTProperties localWTProperties = WTProperties.getLocalProperties();

			DEFAULT_ARCHIVE_FILE_NAME = localWTProperties.getProperty(
					"wt.export.defaultDownloadFileName",
					"document_contents.zip");
		} catch (Throwable localThrowable) {
			log.error("Error reading wt.properties");
		}
	}

	/**
	 * @deprecated
	 */
	public String getConceptualClassname() {
		return CLASSNAME;
	}

	public static StandardAttachmentsService newStandardAttachmentsService()
			throws WTException {
		StandardAttachmentsService localStandardAttachmentsService = new StandardAttachmentsService();
		localStandardAttachmentsService.initialize();
		return localStandardAttachmentsService;
	}

	public void persistAttachments(ContentHolder paramContentHolder,
			List<ContentItem> paramList,
			HashMap<ContentItem, CachedContentDescriptor> paramHashMap)
			throws WTException, PropertyVetoException {
		doPersistence(paramContentHolder, paramList, paramHashMap, null);
	}

	public QueryResult getAttachments(Object paramObject,
			ContentRoleType paramContentRoleType) throws WTException {
		QueryResult localQueryResult = new QueryResult();
		if ((paramObject != null) && (paramContentRoleType != null)
				&& ((paramObject instanceof ContentHolder))) {
			localQueryResult = ContentHelper.service.getContentsByRole(
					(ContentHolder) paramObject, paramContentRoleType, true);
		}
		return localQueryResult;
	}

	public boolean isPrimaryContentRequired(
			FormatContentHolder paramFormatContentHolder) throws WTException {
		return false;
	}

	public boolean isPrimaryContentExisting(
			FormatContentHolder paramFormatContentHolder) throws WTException {
		return false;
	}

	public boolean isPrimaryContentSelected(
			FormatContentHolder paramFormatContentHolder) throws WTException {
		return false;
	}

	public boolean isPrimaryContentUploaded(
			FormatContentHolder paramFormatContentHolder) throws WTException {
		return false;
	}

	public void removeAttachments(ContentHolder paramContentHolder,
			List<NmOid> paramList) throws WTException, WTPropertyVetoException {
		if ((paramContentHolder != null) && (paramList != null)) {
			for (int i = 0; i < paramList.size(); i++) {
				log.debug("contentItemOids.get(" + i + "):  "
						+ paramList.get(i));
				ContentItem localContentItem = (ContentItem) ((NmOid) paramList
						.get(i)).getRef();
				log.debug("contentItem " + i + ":  " + localContentItem);
				if (localContentItem != null) {
					String str = localContentItem.toString();
					ContentServerHelper.service.deleteContent(
							paramContentHolder, localContentItem);

					log.debug("Deleted attachment " + str
							+ " on the content holder " + paramContentHolder);
				}
			}
		}
	}

	private HashMap<String, List<String>> parseSecurityLabels(String secLabels) {
		HashMap<String, List<String>> tags = new HashMap<String, List<String>>();
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		if (secLabels != null && secLabels.isEmpty() == false) {

			if (secLabels.contains(",") == true) {
				String labels[] = secLabels.split(",");
				for (int i = 0; i < labels.length; i++) {
					if (labels[i] != null && labels[i].contains("=")) {
						String nameVal[] = labels[i].split("=");
						if (nameVal.length == 2) {
							List<String> values = new ArrayList<String>();
							values.add(nameVal[1]);
							tags.put(nameVal[0], values);
							logger.info("nxtlbs em ctx localfile nameVal[0]"
									+ nameVal[0]);
							logger.info("nxtlbs em ctx localfile values"
									+ values);
							logger.info("nxtlbs em ctx localfile tags" + tags);

						}
					}
				}
			}
		}
		return tags;
	}

	public AttachmentsDownloadDirectionResponse downloadAttachmentDirections(
			AttachmentsDownloadDirectionRequest paramAttachmentsDownloadDirectionRequest)
			throws WTException {
		AttachmentsDownloadDirectionResponse localAttachmentsDownloadDirectionResponse = new AttachmentsDownloadDirectionResponse();
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		Object doc = null;
		HashMap<String, Object> result = new HashMap<String, Object>();
		logger.info("######################################################################################");
		logger.info("&&&&user name principal name &&&"
				+ SessionMgr.getPrincipal().getName());
		logger.info("&&&&user name principal getDn &&&"
				+ SessionMgr.getPrincipal().getDn());
		logger.info("&&&&user name principal getAdditionalAttributes &&&"
				+ SessionMgr.getPrincipal().getAdditionalAttributes());
		logger.info("&&&&user name principal Security labels &&&"
				+ SessionMgr.getPrincipal().getSecurityLabels());
		logger.info("&&&&user name principal getRequestId &&&"
				+ emCtx.getRequestId());
		AttachmentsDownloadDirectionRequestHolder localAttachmentsDownloadDirectionRequestHolder = paramAttachmentsDownloadDirectionRequest
				.getAttachmentsDownloadDirectionRequestHolder("unknown_holder_oid");

		logger.info("&&&&&&paramAttachmentsDownloadDirectionRequest oids &&&&"
				+ paramAttachmentsDownloadDirectionRequest.getOIDMap());
		Iterator<Object> iterator = paramAttachmentsDownloadDirectionRequest
				.getOIDMap().keySet().iterator();
		while (iterator.hasNext()) {
			Object keyObj = iterator.next();
			Object valueObj = paramAttachmentsDownloadDirectionRequest
					.getOIDMap().get(keyObj);
			if (keyObj instanceof String) {
				logger.info("&&&&&&paramAttachments DownloadDirectionRequest oids keyset&&&&"
						+ (String) keyObj);
				doc = WindchillObjectHelper.getObject((String) keyObj);
				result = doPolicyEvaluation(emCtx, SessionMgr.getPrincipal(),
						(String) keyObj);
				logger.info("&&&&&&Eval request result:&&&&" + result);
				if (doc instanceof WTDocument)
					logger.info("&&&&&&paramAttachments DownloadDirectionRequest security labels&&&&"
							+ ((WTDocument) doc).getSecurityLabels());
			}

		}
		Object localObject1;
		Object localObject2;
		Object localObject3;
		if ((localAttachmentsDownloadDirectionRequestHolder != null)
				&& (localAttachmentsDownloadDirectionRequestHolder
						.getContentItemOIDs() != null)
				&& (localAttachmentsDownloadDirectionRequestHolder
						.getContentItemOIDs().length == 1)) {
			logger.info("&&&&&&localAttachmentsDownloadDirectionRequestHolder oids &&&&"
					+ localAttachmentsDownloadDirectionRequestHolder
							.getContentItemOIDs()[0]);

			localObject1 = (ContentItem) getPersistableFromRefString(localAttachmentsDownloadDirectionRequestHolder
					.getContentItemOIDs()[0]);
			localObject2 = AttachmentsValidationHelper
					.getOwningContentHolder((ContentItem) localObject1);
			localObject3 = new ReferenceFactory();
			String str1 = ((ReferenceFactory) localObject3)
					.getReferenceString((Persistable) localObject2);
			paramAttachmentsDownloadDirectionRequest
					.setAttachmentsDownloadDirectionRequestHolder(str1,
							localAttachmentsDownloadDirectionRequestHolder);
			paramAttachmentsDownloadDirectionRequest
					.removeAttachmentsDownloadDirectionRequestHolder("unknown_holder_oid");
		}
		try {
			Object localObject4;
			Object localObject8;
			Object localObject9;
			Object localObject12;
			Object localObject13;
			Object localObject14;
			Object localObject5;
			Object localObject11;
			Object localObject7;
			if (paramAttachmentsDownloadDirectionRequest.getDownloadType() == 0) {
				logger.info("&&&&&&download type is 0&&&&");
				localObject1 = new Vector();
				localObject2 = null;
				localObject3 = null;
				int j = 0;

				String[] ids = paramAttachmentsDownloadDirectionRequest
						.getAllContentHolderOIDs();
				Object localObject6;
				for (int k = 0; k < ids.length; k++) {
					if ((ids[k] != null) && (ids[k].length() >= 1)) {
						localObject6 = paramAttachmentsDownloadDirectionRequest
								.getAttachmentsDownloadDirectionRequestHolder(ids[k]);
						localObject8 = (ContentHolder) getPersistableFromRefString(ids[k]);
						localObject8 = inflateContents((ContentHolder) localObject8);
						Object localObject10;
						if (((AttachmentsDownloadDirectionRequestHolder) localObject6)
								.getDownloadScope() == 0) {
							if ((localObject8 instanceof FormatContentHolder)) {
								localObject9 = (FormatContentHolder) localObject8;
								localObject10 = ((FormatContentHolder) localObject9)
										.getPrimary();
								if ((localObject10 != null)
										&& ((localObject10 instanceof ApplicationData))) {
									((Vector) localObject1).add(localObject10);
									localObject2 = localObject9;
									localObject3 = ids[k];
									j = 1;
								}
							}
						} else if (((AttachmentsDownloadDirectionRequestHolder) localObject6)
								.getDownloadScope() == 1) {
							if ((localObject8 instanceof FormatContentHolder)) {
								localObject9 = (FormatContentHolder) localObject8;
								localObject10 = ((FormatContentHolder) localObject9)
										.getPrimary();
								if ((localObject10 != null)
										&& ((localObject10 instanceof ApplicationData))) {
									((Vector) localObject1).add(localObject10);
									localObject2 = localObject9;
									localObject3 = ids[k];
									j = 1;
								}
							}
							localObject9 = ((ContentHolder) localObject8)
									.getContentVector();
							localObject10 = ((Vector) localObject9).iterator();
							while (((Iterator) localObject10).hasNext()) {
								localObject12 = (ContentItem) ((Iterator) localObject10)
										.next();
								if ((localObject12 != null)
										&& ((localObject12 instanceof ApplicationData))) {
									((Vector) localObject1).add(localObject12);
									localObject2 = localObject8;
									localObject3 = ids[k];
									j = 0;
								}
							}
						} else if (((AttachmentsDownloadDirectionRequestHolder) localObject6)
								.getDownloadScope() == 2) {
							String[] coids = ((AttachmentsDownloadDirectionRequestHolder) localObject6)
									.getContentItemOIDs();
							for (int n = 0; n < coids.length; n++) {
								if ((coids[n] != null)
										&& (coids[n].length() >= 1)) {
									localObject12 = (ContentItem) getPersistableFromRefString(coids[n]);
									if ((localObject12 != null)
											&& ((localObject12 instanceof ApplicationData))) {
										if (((ContentHolder) localObject8)
												.getContentVector().contains(
														localObject12)) {
											((Vector) localObject1)
													.add(localObject12);
											localObject2 = localObject8;
											localObject3 = ids[k];
										} else if ((localObject8 instanceof FormatContentHolder)) {
											localObject13 = (FormatContentHolder) localObject8;
											localObject14 = ((FormatContentHolder) localObject13)
													.getPrimary();
											if (((ContentItem) localObject12)
													.equals(localObject14)) {
												((Vector) localObject1)
														.add(localObject12);
												localObject2 = localObject8;
												localObject3 = ids[k];
												j = 1;
											}
										}
									}
								}
							}
						}
					}
				}
				if (((Vector) localObject1).size() >= 1) {
					localObject5 = null;
					try {
						localObject6 = StandardIXBService.getSaveFileOnServer();
						localObject5 = new IXBJarWriter((File) localObject6);
						localObject8 = new ArrayList();
						localObject9 = new HashMap();

						localObject11 = ((Vector) localObject1).iterator();
						while (((Iterator) localObject11).hasNext()) {
							localObject12 = (ApplicationData) ((Iterator) localObject11)
									.next();

							localObject13 = ((ApplicationData) localObject12)
									.getFileName();
							while (((List) localObject8)
									.contains(localObject13)) {
								localObject14 = (Integer) ((HashMap) localObject9)
										.get(localObject13);

								int i1 = ((String) localObject13)
										.lastIndexOf(".");
								String str2 = ((String) localObject13)
										.substring(i1);
								String str3 = ((String) localObject13)
										.substring(0, i1);
								if (localObject14 == null) {
									((HashMap) localObject9).put(localObject13,
											new Integer(1));
									localObject13 = str3 + "[" + 1 + "]" + str2;
								} else {
									((HashMap) localObject9).put(
											localObject13,
											new Integer(
													((Integer) localObject14)
															.intValue() + 1));
									localObject13 = str3
											+ "["
											+ (((Integer) localObject14)
													.intValue() + 1) + "]"
											+ str2;
								}
							}
							((List) localObject8).add(localObject13);

							localObject14 = (Streamed) ((ApplicationData) localObject12)
									.getStreamData().getObject();
							if ((localObject14 != null)
									&& (((Streamed) localObject14)
											.retrieveStream() != null)) {
								BufferedInputStream localBufferedInputStream = new BufferedInputStream(
										((Streamed) localObject14)
												.retrieveStream());
								boolean isEncryptallowed = false;
								if (result.get("DRM") != null) {
									isEncryptallowed = (Boolean) result
											.get("DRM");
								}
								if (isEncryptallowed) {
									boolean fromPrimary = false;
									if (result.get("secondary") != null) {
										String secondarytags = (String) result
												.get("secondary");
										if (secondarytags
												.equalsIgnoreCase("From Security Labels"))
											fromPrimary = true;
									}
									File encryptFile = encryptAttachements(
											localBufferedInputStream,
											localObject13, localObject12, doc,
											fromPrimary);
									if (encryptFile != null) {
										localBufferedInputStream = new BufferedInputStream(
												new FileInputStream(encryptFile));
										localObject13 = (String) localObject13
												+ ".nxl";
									}
								}
								((IXBJarWriter) localObject5).addEntry(
										localBufferedInputStream,
										(String) localObject13);

							}
						}
						((IXBJarWriter) localObject5).finalizeJar();
						localObject12 = getTempFileURLString(
								(File) localObject6, DEFAULT_ARCHIVE_FILE_NAME);

						localObject13 = new AttachmentsDownloadDirectionResponseHolder(
								DEFAULT_ARCHIVE_FILE_NAME);
						localObject14 = new AttachmentsDownloadDirectionResponseItem(
								DEFAULT_ARCHIVE_FILE_NAME);
						((AttachmentsDownloadDirectionResponseItem) localObject14)
								.setDownloadURLString((String) localObject12);
						((AttachmentsDownloadDirectionResponseItem) localObject14)
								.setFileName(DEFAULT_ARCHIVE_FILE_NAME);
						((AttachmentsDownloadDirectionResponseHolder) localObject13)
								.addSecondaryItem((AttachmentsDownloadDirectionResponseItem) localObject14);
						localAttachmentsDownloadDirectionResponse
								.addHolder((AttachmentsDownloadDirectionResponseHolder) localObject13);
					} catch (IOException localIOException2) {
						throw new WTException(localIOException2);
					}
				} else if (((Vector) localObject1).size() == 1) {
					logger.info("&&&&&&vector size=1&&&&");
					localObject5 = new AttachmentsDownloadDirectionResponseHolder(
							(String) localObject3);
					localObject7 = (ContentItem) ((Vector) localObject1)
							.iterator().next();
					localObject8 = ((ContentItem) localObject7)
							.getPersistInfo().getObjectIdentifier().toString();
					localObject9 = new AttachmentsDownloadDirectionResponseItem(
							(String) localObject8);
					localObject11 = (ApplicationData) localObject7;
					((AttachmentsDownloadDirectionResponseItem) localObject9)
							.setFileName(((ApplicationData) localObject11)
									.getFileName());
					((AttachmentsDownloadDirectionResponseItem) localObject9)
							.setLastModifiedTimestamp(((ApplicationData) localObject11)
									.getPersistInfo().getModifyStamp());
					logger.info("&&&&&&localObject9&&&&"
							+ localObject9.getClass());
					logger.info("&&&&&&localObject11&&&&"
							+ localObject11.getClass());
					logger.info("&&&&&&localObject5&&&&"
							+ localObject5.getClass());

					localObject12 = "";
					localObject13 = null;
					if ((localObject2 instanceof EPMDocument)) {
						logger.info("&&&&&&EPMDocument&&&&");
						localObject12 = EPMContentHelper.getContentName(
								(EPMDocument) localObject2,
								(ContentItem) localObject11);
						localObject13 = RedirectDownload.getPreferredURL(
								(ApplicationData) localObject11,
								(ContentHolder) localObject2,
								(String) localObject12);
					} else if ((localObject2 instanceof EPMSepFamilyTable)) {
						logger.info("&&&&&&EPMSepFamilyTable&&&&");
						localObject12 = EPMContentHelper.getContentDisplayName(
								(EPMSepFamilyTable) localObject2,
								(ContentItem) localObject11);
						localObject13 = RedirectDownload.getPreferredURL(
								(ApplicationData) localObject11,
								(ContentHolder) localObject2,
								(String) localObject12);
					} else {
						localObject13 = RedirectDownload.getPreferredURL(
								(ApplicationData) localObject11,
								(ContentHolder) localObject2);
						logger.info("&&&&&&localObject13&&&&"
								+ localObject13.getClass());
					}
					((AttachmentsDownloadDirectionResponseItem) localObject9)
							.setDownloadURLString(((URL) localObject13)
									.toString());
					logger.info("&&&&&&	((AttachmentsDownloadDirectionResponseItem) localObject9) url string&&&&"
							+ ((AttachmentsDownloadDirectionResponseItem) localObject9)
									.getDownloadURLString());
					if (j != 0) {
						logger.info("&&&&&&j is1&&&&");
						((AttachmentsDownloadDirectionResponseHolder) localObject5)
								.setPrimaryItem((AttachmentsDownloadDirectionResponseItem) localObject9);
					} else {
						logger.info("&&&&&&j is0&&&&");
						((AttachmentsDownloadDirectionResponseHolder) localObject5)
								.addSecondaryItem((AttachmentsDownloadDirectionResponseItem) localObject9);
					}
					localAttachmentsDownloadDirectionResponse
							.addHolder((AttachmentsDownloadDirectionResponseHolder) localObject5);
				} else {
					logger.info("&&&&&&else part of vector size is not 1&&&&");
					localAttachmentsDownloadDirectionResponse
							.addHolder(new AttachmentsDownloadDirectionResponseHolder(
									(String) localObject3));
				}
			} else {
				logger.info("&&&&&&download type is not 0&&&&");
				String[] coids = paramAttachmentsDownloadDirectionRequest
						.getAllContentHolderOIDs();
				for (int i = 0; i < coids.length; i++) {
					localObject3 = paramAttachmentsDownloadDirectionRequest
							.getAttachmentsDownloadDirectionRequestHolder(coids[i]);
					ContentHolder localContentHolder = (ContentHolder) getPersistableFromRefString(coids[i]);
					localContentHolder = inflateContents(localContentHolder);

					localObject4 = new AttachmentsDownloadDirectionResponseHolder(
							coids[i]);
					if (((AttachmentsDownloadDirectionRequestHolder) localObject3)
							.getDownloadScope() == 0) {
						logger.info("&&&&&&download scope is 0&&&&");
						if ((localContentHolder instanceof FormatContentHolder)) {
							localObject5 = (FormatContentHolder) localContentHolder;
							localObject7 = ((FormatContentHolder) localObject5)
									.getPrimary();
							if ((localObject7 != null)
									&& ((localObject7 instanceof ApplicationData))) {
								localObject8 = (ApplicationData) localObject7;
								localObject9 = ((ContentItem) localObject7)
										.getPersistInfo().getObjectIdentifier()
										.toString();
								localObject11 = new AttachmentsDownloadDirectionResponseItem(
										(String) localObject9);
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setPrimaryContentItem(true);
								localObject12 = null;
								if ((localContentHolder instanceof EPMDocument)) {
									localObject12 = EPMContentHelper
											.getContentName(
													(EPMDocument) localContentHolder,
													(ContentItem) localObject8);
								} else if ((localContentHolder instanceof EPMSepFamilyTable)) {
									localObject12 = EPMContentHelper
											.getContentDisplayName(
													(EPMSepFamilyTable) localContentHolder,
													(ContentItem) localObject8);
								} else {
									localObject12 = ((ApplicationData) localObject8)
											.getFileName();
								}
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setFileName((String) localObject12);
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setLastModifiedTimestamp(((ApplicationData) localObject8)
												.getPersistInfo()
												.getModifyStamp());
								localObject13 = RedirectDownload
										.getPreferredURL(
												(ApplicationData) localObject8,
												(ContentHolder) localObject5);
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setDownloadURLString(((URL) localObject13)
												.toString());
								((AttachmentsDownloadDirectionResponseHolder) localObject4)
										.setPrimaryItem((AttachmentsDownloadDirectionResponseItem) localObject11);
							}
						}
					} else if (((AttachmentsDownloadDirectionRequestHolder) localObject3)
							.getDownloadScope() == 1) {
						logger.info("&&&&&&download scope is 1&&&&");
						if ((localContentHolder instanceof FormatContentHolder)) {
							localObject5 = (FormatContentHolder) localContentHolder;
							localObject7 = ((FormatContentHolder) localObject5)
									.getPrimary();
							if ((localObject7 != null)
									&& ((localObject7 instanceof ApplicationData))) {
								localObject8 = (ApplicationData) localObject7;
								localObject9 = ((ContentItem) localObject7)
										.getPersistInfo().getObjectIdentifier()
										.toString();
								localObject11 = new AttachmentsDownloadDirectionResponseItem(
										(String) localObject9);
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setPrimaryContentItem(true);
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setFileName(((ApplicationData) localObject8)
												.getFileName());
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setLastModifiedTimestamp(((ApplicationData) localObject8)
												.getPersistInfo()
												.getModifyStamp());
								localObject12 = RedirectDownload
										.getPreferredURL(
												(ApplicationData) localObject8,
												(ContentHolder) localObject5);
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setDownloadURLString(((URL) localObject12)
												.toString());
								((AttachmentsDownloadDirectionResponseHolder) localObject4)
										.setPrimaryItem((AttachmentsDownloadDirectionResponseItem) localObject11);
							}
						}
						localObject5 = localContentHolder.getContentVector();
						localObject7 = ((Vector) localObject5).iterator();
						while (((Iterator) localObject7).hasNext()) {
							localObject8 = (ContentItem) ((Iterator) localObject7)
									.next();
							if ((localObject8 != null)
									&& ((localObject8 instanceof ApplicationData))) {
								localObject9 = ((ContentItem) localObject8)
										.getPersistInfo().getObjectIdentifier()
										.toString();
								localObject11 = new AttachmentsDownloadDirectionResponseItem(
										(String) localObject9);
								localObject12 = (ApplicationData) localObject8;
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setFileName(((ApplicationData) localObject12)
												.getFileName());
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setLastModifiedTimestamp(((ApplicationData) localObject12)
												.getPersistInfo()
												.getModifyStamp());
								localObject13 = RedirectDownload
										.getPreferredURL(
												(ApplicationData) localObject12,
												localContentHolder);
								((AttachmentsDownloadDirectionResponseItem) localObject11)
										.setDownloadURLString(((URL) localObject13)
												.toString());
								((AttachmentsDownloadDirectionResponseHolder) localObject4)
										.addSecondaryItem((AttachmentsDownloadDirectionResponseItem) localObject11);
							}
						}
					} else if (((AttachmentsDownloadDirectionRequestHolder) localObject3)
							.getDownloadScope() == 2) {
						logger.info("&&&&&&download scope is 2&&&&");
						String[] itemids = ((AttachmentsDownloadDirectionRequestHolder) localObject3)
								.getContentItemOIDs();
						for (int m = 0; m < itemids.length; m++) {
							localObject8 = (ContentItem) getPersistableFromRefString(itemids[m]);
							if ((localObject8 != null)
									&& ((localObject8 instanceof ApplicationData))) {
								localObject9 = (ApplicationData) localObject8;
								localObject11 = ((ContentItem) localObject8)
										.getPersistInfo().getObjectIdentifier()
										.toString();
								localObject12 = new AttachmentsDownloadDirectionResponseItem(
										(String) localObject11);
								((AttachmentsDownloadDirectionResponseItem) localObject12)
										.setFileName(((ApplicationData) localObject9)
												.getFileName());
								((AttachmentsDownloadDirectionResponseItem) localObject12)
										.setLastModifiedTimestamp(((ApplicationData) localObject9)
												.getPersistInfo()
												.getModifyStamp());
								localObject13 = RedirectDownload
										.getPreferredURL(
												(ApplicationData) localObject9,
												localContentHolder);
								((AttachmentsDownloadDirectionResponseItem) localObject12)
										.setDownloadURLString(((URL) localObject13)
												.toString());
								if ((localContentHolder instanceof FormatContentHolder)) {
									localObject14 = (FormatContentHolder) localContentHolder;
									if (((ApplicationData) localObject9)
											.equals(((FormatContentHolder) localObject14)
													.getPrimary())) {
										((AttachmentsDownloadDirectionResponseHolder) localObject4)
												.setPrimaryItem((AttachmentsDownloadDirectionResponseItem) localObject12);
									}
								}
								if (localContentHolder.getContentVector()
										.contains(localObject9)) {
									((AttachmentsDownloadDirectionResponseHolder) localObject4)
											.addSecondaryItem((AttachmentsDownloadDirectionResponseItem) localObject12);
								}
							}
						}
					}
					localAttachmentsDownloadDirectionResponse
							.addHolder((AttachmentsDownloadDirectionResponseHolder) localObject4);
				}
			}
		} catch (IOException localIOException1) {
			log.error(getClass().getName()
					+ ".downloadAttachmentDirections() IOException",
					localIOException1);
			throw new WTException(localIOException1);
		}
		return localAttachmentsDownloadDirectionResponse;
	}

	private HashMap<String, Object> doPolicyEvaluation(
			EntitlementManagerContext emCtx, WTPrincipal principal, String oid) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean isAllowed = true;
		HashMap<String, List<String>> tags = new HashMap<String, List<String>>();
		String policyName = null, policyMessage = null;

		long lStartTime = System.currentTimeMillis();
		logger.info("EntitlementManagerFilter doFilter start at " + lStartTime);

		Authenticator myAuth = new EMAuthenticator();
		Authenticator.setDefault(myAuth);
		QueryAgent_Service queryAgentService = new QueryAgent_Service();
		QueryAgent queryAgentPort = queryAgentService.getQueryAgentImplPort();
		List<String> params = new ArrayList<String>();

		params.add(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME);
		params.add(principal.getName());
		logger.info(com.nextlabs.em.windchill.Constants.PARANAME_USERNAME + "="
				+ principal.getName());

		params.add(com.nextlabs.em.windchill.Constants.PARANAME_OIDS);
		params.add(oid);

		params.add(com.nextlabs.em.windchill.Constants.PARANAME_POLICYACTION);
		params.add("DOWNLOAD");

		List<String> resultStrList = queryAgentPort.eval(emCtx.getRequestId(),
				params, "Entitlement Manager for Windchill");
		logger.debug("Web service call done resultStrList.length="
				+ resultStrList.size());
		logger.debug("Web service call done resultStrList.length="
				+ resultStrList);
		for (int i = 0; i < resultStrList.size(); i++) {
			if (i == resultStrList.size() - 1) {
				logger.debug("ret " + i + ":" + resultStrList.get(i)
						+ " is discarded");
			} else {
				logger.debug("ret " + i + ":" + resultStrList.get(i) + "="
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
				else if (resultStrList.get(i).equalsIgnoreCase(
						com.nextlabs.em.windchill.Constants.PARANAME_OBJNAME)) {
					String obligationName = resultStrList.get(++i);
					if (obligationName.equalsIgnoreCase("WDRM")) {
						resultMap.put("DRM", true);
					}
					logger.debug("SASERVICE Policy obligationName:"
							+ obligationName);
					if (resultStrList
							.get(++i)
							.equalsIgnoreCase(
									com.nextlabs.em.windchill.Constants.PARANAME_OBJPARAMNAME)) {
						String tagname = null, tagValue;
						for (int j = 0;; j++) {
							if (resultStrList
									.get(i)
									.equalsIgnoreCase(
											com.nextlabs.em.windchill.Constants.PARANAME_OBJPARAMVAlUE)) {
								break;
							}

							if (j % 2 == 0) {
								tagname = resultStrList.get(++i);
								logger.debug("SASERVICE Policy tagname:"
										+ tagname);
							} else {
								tagValue = resultStrList.get(++i);
								logger.debug("SASERVICE Policy tagValue:"
										+ tagValue);
								if (tagname != null
										&& tagname
												.equalsIgnoreCase("Tags for Attachments")) {
									resultMap.put("secondary", tagValue);
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
						logger.debug("SASERVICE Policy evaluated to allow:");
					} else
						isAllowed = false;
				}
			}
		}
		long lEndTime = System.currentTimeMillis();
		resultMap.put(
				com.nextlabs.em.windchill.Constants.PARANAME_POLICYEFFECT,
				isAllowed);
		resultMap.put("tags", tags);

		logger.info("EntitlementManagerFilter doFilter end at " + lStartTime
				+ "(" + (lEndTime - lStartTime) + ")");
		return resultMap;
	}

	private File encryptAttachements(
			BufferedInputStream localBufferedInputStream, Object localObject13,
			Object localObject12, Object doc, boolean fromPrimary) {
		String fileName = (String) localObject13;
		ApplicationData ad = (ApplicationData) localObject12;
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		File saveDirectory = StandardIXBService.getSaveDirectoryOnServer();
		logger.info("&&&&&&parent object exploration &&&&"
				+ ad.getPersistInfo().getObjectIdentifier().getStringValue());
		logger.info("&&&&&&Attachement file Name &&&&" + fileName);
		try {
			if (!fileName.endsWith(".nxl")) {
				File fileToEncrypt = new File(saveDirectory, fileName);
				logger.info("&&&&&&Attachement save directory &&&&"
						+ saveDirectory.getAbsolutePath());
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(fileToEncrypt));
				byte[] localObject5 = new byte[FvProperties.READ_BUFFER_SIZE];
				int n;
				while ((n = localBufferedInputStream
						.read((byte[]) localObject5)) > 0) {
					bos.write((byte[]) localObject5, 0, n);
				}
				localBufferedInputStream.close();
				bos.close();
				HashMap<String, List<String>> tags = new HashMap<String, List<String>>();
				if (null != doc && doc instanceof WTDocument) {
					SecurityLabels sl = ((WTDocument) doc).getSecurityLabels();
					if (sl != null) {
						if (!fromPrimary)
							tags = UtilityTools.parseSecurityLabels(
									sl.toString(), fileName);
						else
							tags = UtilityTools.parseSecurityLabels(
									sl.toString(), "primary");
					}
				}
				File encryptFile = encrypt(fileToEncrypt, tags);
				logger.info("&&&&&&File encrypted &&&&");
				return encryptFile;
			}
		} catch (Exception e) {
			logger.info("&&&&&&Exception while Encrypting&&&&" + e.getMessage());
			logger.error(e);
		}
		return null;
	}

	private File encrypt(File localFile3, HashMap<String, List<String>> tags) {
		File nxlFile = new File(localFile3.getAbsolutePath() + ".nxl");
		RightsManager rm = new RightsManagerHelper().getRightsManager();
		logger.info("nxtlbs em in encrypt method localfile tags" + tags);
		try {
			rm.encrypt(localFile3.getCanonicalPath(),
					nxlFile.getCanonicalPath(), null, null, tags);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return null;
		}
		return nxlFile;
	}

	public String redirectAttachmentURL(String paramString) throws WTException {
		Persistable localPersistable = getPersistableFromRefString(paramString);
		Object localObject;
		if ((localPersistable instanceof FormatContentHolder)) {
			localObject = (FormatContentHolder) localPersistable;
			if (!((FormatContentHolder) localObject).isHasContents()) {
				try {
					localObject = (FormatContentHolder) ContentHelper.service
							.getContents((ContentHolder) localObject);
				} catch (PropertyVetoException localPropertyVetoException) {
					throw new WTException(localPropertyVetoException);
				}
			}
			ContentItem localContentItem = ((FormatContentHolder) localObject)
					.getPrimary();
			if ((localContentItem != null)
					&& ((localContentItem instanceof URLData))) {
				URLData localURLData = (URLData) localContentItem;
				return localURLData.getUrlLocation();
			}
			if (localContentItem != null) {
				return localContentItem.getPersistInfo().getObjectIdentifier()
						.getStringValue();
			}
			return null;
		}
		if ((localPersistable instanceof URLData)) {
			localObject = (URLData) localPersistable;
			return ((URLData) localObject).getUrlLocation();
		}
		throw new WTException(getClass().getName() + " error: No URLData for "
				+ paramString);
	}

	public void rememberDownloadPath(
			RememberDownloadPathRequest paramRememberDownloadPathRequest)
			throws WTException {
		Persistable localPersistable = getPersistableFromRefString(paramRememberDownloadPathRequest
				.getHolderOID());
		if (((localPersistable instanceof FormatContentHolder))
				&& ((localPersistable instanceof Workable))) {
			if (WorkInProgressHelper.isCheckedOut((Workable) localPersistable,
					SessionMgr.getPrincipal())) {
				FormatContentHolder localFormatContentHolder = (FormatContentHolder) inflateContents((ContentHolder) localPersistable);
				ContentItem localContentItem = ContentHelper
						.getPrimary(localFormatContentHolder);
				if ((localContentItem instanceof ApplicationData)) {
					ApplicationData localApplicationData = (ApplicationData) localContentItem;
					try {
						localApplicationData
								.setUploadedFromPath(paramRememberDownloadPathRequest
										.getDownloadPath());
						ContentHelper.service.updateAppData(
								localFormatContentHolder, localApplicationData);
					} catch (WTPropertyVetoException localWTPropertyVetoException) {
						throw new WTException(localWTPropertyVetoException);
					} catch (PropertyVetoException localPropertyVetoException) {
						throw new WTException(localPropertyVetoException);
					}
				}
			}
		}
	}

	public void persistAttachments(ContentHolder paramContentHolder,
			List<ContentItem> paramList,
			HashMap<ContentItem, CachedContentDescriptor> paramHashMap,
			HashMap paramHashMap1) throws WTException, PropertyVetoException {
		doPersistence(paramContentHolder, paramList, paramHashMap,
				paramHashMap1);
	}

	private static FormatContentHolder removePrimary(
			FormatContentHolder paramFormatContentHolder) throws WTException,
			WTPropertyVetoException, PropertyVetoException {
		log.trace("AttachmentsHelper.removePrimary()");

		paramFormatContentHolder = (FormatContentHolder) ContentHelper.service
				.getContents(paramFormatContentHolder);
		ContentItem localContentItem = ContentHelper
				.getPrimary(paramFormatContentHolder);
		ContentServerHelper.service.deleteContent(paramFormatContentHolder,
				localContentItem);
		log.trace("primary deleted");
		return paramFormatContentHolder;
	}

	protected Persistable getPersistableFromRefString(String paramString)
			throws WTException {
		WTReference localWTReference = null;
		localWTReference = refFactory.getReference(paramString);
		localWTReference.refresh();
		Persistable localPersistable = localWTReference.getObject();
		return localPersistable;
	}

	protected ContentHolder inflateContents(ContentHolder paramContentHolder)
			throws WTException {
		try {
			if (!paramContentHolder.isHasContents()) {
			}
			return ContentHelper.service.getContents(paramContentHolder);
		} catch (PropertyVetoException localPropertyVetoException) {
			throw new WTException(localPropertyVetoException);
		}
	}

	protected boolean contentHolderHasAttachment(
			ContentHolder paramContentHolder, ContentItem paramContentItem)
			throws WTException {
		paramContentHolder = inflateContents(paramContentHolder);
		if (((paramContentHolder instanceof FormatContentHolder))
				&& (((FormatContentHolder) paramContentHolder).getPrimary()
						.equals(paramContentItem))) {
			return true;
		}
		if (paramContentHolder.getContentVector().contains(paramContentItem)) {
			return true;
		}
		return false;
	}

	protected String getTempFileURLString(File paramFile, String paramString)
			throws WTException {
		return NmObjectHelper.constructOutputURL(paramFile, paramString)
				.toString();
	}

	private void doPersistence(ContentHolder paramContentHolder,
			List<ContentItem> paramList,
			HashMap<ContentItem, CachedContentDescriptor> paramHashMap,
			HashMap paramHashMap1) throws WTException, PropertyVetoException {
		if ((paramContentHolder != null) && (paramList != null)) {
			for (int i = 0; i < paramList.size(); i++) {
				ContentItem localContentItem = (ContentItem) paramList.get(i);
				if (localContentItem != null) {
					Object localObject1;
					if ((InstalledProperties
							.isInstalled("Windchill.SoftwareConfigMgmtIntegration"))
							&& ((localContentItem instanceof ScmApplicationData))) {
						localObject1 = (ScmApplicationData) localContentItem;
						if (((((ScmApplicationData) localObject1).getCcPath() != null) && (!((ScmApplicationData) localObject1)
								.getCcPath().equals("")))
								|| (PersistenceHelper
										.isPersistent((Persistable) localObject1))) {
							if (PersistenceHelper
									.isPersistent((Persistable) localObject1)) {
								localObject1 = ScmFacade
										.getInstance()
										.updateScmApplicationData(
												paramContentHolder,
												(ScmApplicationData) localObject1);
							} else {
								ScmFacade.getInstance().createScmData(
										paramContentHolder,
										((ScmApplicationData) localObject1)
												.getCcPath(), paramHashMap1);
							}
						}
						log.debug("Added/Updated SCM attachment "
								+ ((ScmApplicationData) localObject1)
										.getFileName() + " - " + localObject1);
					} else if ((localContentItem instanceof ApplicationData)) {
						localObject1 = (ApplicationData) localContentItem;
						if ((paramHashMap != null)
								&& (paramHashMap.get(localObject1) != null)) {
							localObject1 = ContentServerHelper.service
									.updateContent(
											paramContentHolder,
											(ApplicationData) localObject1,
											(CachedContentDescriptor) paramHashMap
													.get(localObject1));

							log.debug("Added/Updated file attachment "
									+ ((ApplicationData) localObject1)
											.getFileName() + " - "
									+ localObject1);
						} else if (PersistenceHelper
								.isPersistent((Persistable) localObject1)) {
							ApplicationData localApplicationData = (ApplicationData) PersistenceHelper.manager
									.refresh((Persistable) localObject1);

							boolean bool2 = isContentItemUpdated(
									localApplicationData,
									(ContentItem) localObject1);
							log.debug("isAppDataUpdated = " + bool2);
							if (bool2) {
								localObject1 = ContentHelper.service
										.updateAppData(paramContentHolder,
												(ApplicationData) localObject1,
												bool2);
							}
						}
					} else {
						boolean bool1;
						Object localObject2;
						if ((localContentItem instanceof URLData)) {
							localObject1 = (URLData) localContentItem;
							bool1 = true;
							if (PersistenceHelper
									.isPersistent((Persistable) localObject1)) {
								localObject2 = (URLData) PersistenceHelper.manager
										.refresh((Persistable) localObject1);

								bool1 = isContentItemUpdated(
										(ContentItem) localObject2,
										(ContentItem) localObject1);
								log.debug("isURLDataUpdated = " + bool1);
							}
							if (bool1) {
								localObject1 = ContentServerHelper.service
										.updateContent(paramContentHolder,
												(URLData) localObject1);
								log.debug("Added/Updated URL attachment "
										+ ((URLData) localObject1)
												.getDisplayName() + " - "
										+ localObject1);
							}
						} else if ((localContentItem instanceof ExternalStoredData)) {
							localObject1 = (ExternalStoredData) localContentItem;
							bool1 = true;
							if (PersistenceHelper
									.isPersistent((Persistable) localObject1)) {
								localObject2 = (ExternalStoredData) PersistenceHelper.manager
										.refresh((Persistable) localObject1);

								bool1 = isContentItemUpdated(
										(ContentItem) localObject2,
										(ContentItem) localObject1);
								log.debug("isESDataUpdated = " + bool1);
							}
							if (bool1) {
								localObject1 = ContentServerHelper.service
										.updateContent(
												paramContentHolder,
												(ExternalStoredData) localObject1);
								log.debug("Added/Updated External Stored attachment "
										+ ((ExternalStoredData) localObject1)
												.getDisplayName()
										+ " - "
										+ localObject1);
							}
						}
					}
				}
			}
		}
	}

	private boolean isContentItemUpdated(ContentItem paramContentItem1,
			ContentItem paramContentItem2) {
		log.trace("In isContentItemUpdated");

		log.debug("Orig ContentItem attributes :-");
		logContentItemAttributes(paramContentItem1);
		log.debug("New ContentItem attributes :-");
		logContentItemAttributes(paramContentItem2);
		if (paramContentItem1.getLineNumber() > 0) {
			if (paramContentItem1.getLineNumber() != paramContentItem2
					.getLineNumber()) {
				return true;
			}
		} else if (paramContentItem2.getLineNumber() > 0) {
			return true;
		}
		if (paramContentItem1.getDescription() != null) {
			if (!paramContentItem1.getDescription().equals(
					paramContentItem2.getDescription())) {
				return true;
			}
		} else if ((paramContentItem2.getDescription() != null)
				&& (paramContentItem2.getDescription().length() > 0)) {
			return true;
		}
		if (paramContentItem1.getComments() != null) {
			if (!paramContentItem1.getComments().equals(
					paramContentItem2.getComments())) {
				return true;
			}
		} else if ((paramContentItem2.getComments() != null)
				&& (paramContentItem2.getComments().length() > 0)) {
			return true;
		}
		if (paramContentItem1.isDistributable() != paramContentItem2
				.isDistributable()) {
			return true;
		}
		if (paramContentItem1.getAuthoredBy() != null) {
			if (!paramContentItem1.getAuthoredBy().equals(
					paramContentItem2.getAuthoredBy())) {
				return true;
			}
		} else if ((paramContentItem2.getAuthoredBy() != null)
				&& (paramContentItem2.getAuthoredBy().length() > 0)) {
			return true;
		}
		if (paramContentItem1.getLastAuthored() != null) {
			if (!paramContentItem1.getLastAuthored().equals(
					paramContentItem2.getLastAuthored())) {
				return true;
			}
		} else if (paramContentItem2.getLastAuthored() != null) {
			return true;
		}
		Object localObject1;
		Object localObject2;
		if (((paramContentItem1 instanceof ApplicationData))
				&& ((paramContentItem2 instanceof ApplicationData))) {
			localObject1 = (ApplicationData) paramContentItem1;
			localObject2 = (ApplicationData) paramContentItem2;
			if (((ApplicationData) localObject1).getUploadedFromPath() != null) {
				if (!((ApplicationData) localObject1).getUploadedFromPath()
						.equals(((ApplicationData) localObject2)
								.getUploadedFromPath())) {
					return true;
				}
			} else if ((((ApplicationData) localObject2).getUploadedFromPath() != null)
					&& (((ApplicationData) localObject2).getUploadedFromPath()
							.length() > 0)) {
				return true;
			}
			if (((ApplicationData) localObject1).getFileVersion() != null) {
				if (!((ApplicationData) localObject1).getFileVersion().equals(
						((ApplicationData) localObject2).getFileVersion())) {
					return true;
				}
			} else if ((((ApplicationData) localObject2).getFileVersion() != null)
					&& (((ApplicationData) localObject2).getFileVersion()
							.length() > 0)) {
				return true;
			}
			if (((ApplicationData) localObject1).getToolName() != null) {
				if (!((ApplicationData) localObject1).getToolName().equals(
						((ApplicationData) localObject2).getToolName())) {
					return true;
				}
			} else if ((((ApplicationData) localObject2).getToolName() != null)
					&& (((ApplicationData) localObject2).getToolName().length() > 0)) {
				return true;
			}
			if (((ApplicationData) localObject1).getToolVersion() != null) {
				if (!((ApplicationData) localObject1).getToolVersion().equals(
						((ApplicationData) localObject2).getToolVersion())) {
					return true;
				}
			} else if ((((ApplicationData) localObject2).getToolVersion() != null)
					&& (((ApplicationData) localObject2).getToolVersion()
							.length() > 0)) {
				return true;
			}
		} else if (((paramContentItem1 instanceof URLData))
				&& ((paramContentItem2 instanceof URLData))) {
			localObject1 = (URLData) paramContentItem1;
			localObject2 = (URLData) paramContentItem2;
			if (((URLData) localObject1).getUrlLocation() != null) {
				if (!((URLData) localObject1).getUrlLocation().equals(
						((URLData) localObject2).getUrlLocation())) {
					return true;
				}
			} else if ((((URLData) localObject2).getUrlLocation() != null)
					&& (((URLData) localObject2).getUrlLocation().length() > 0)) {
				return true;
			}
			if (((URLData) localObject1).getDisplayName() != null) {
				if (!((URLData) localObject1).getDisplayName().equals(
						((URLData) localObject2).getDisplayName())) {
					return true;
				}
			} else if ((((URLData) localObject2).getDisplayName() != null)
					&& (((URLData) localObject2).getDisplayName().length() > 0)) {
				return true;
			}
		} else if (((paramContentItem1 instanceof ExternalStoredData))
				&& ((paramContentItem2 instanceof ExternalStoredData))) {
			localObject1 = (ExternalStoredData) paramContentItem1;
			localObject2 = (ExternalStoredData) paramContentItem2;
			if (((ExternalStoredData) localObject1).getExternalLocation() != null) {
				if (!((ExternalStoredData) localObject1).getExternalLocation()
						.equals(((ExternalStoredData) localObject2)
								.getExternalLocation())) {
					return true;
				}
			} else if ((((ExternalStoredData) localObject2)
					.getExternalLocation() != null)
					&& (((ExternalStoredData) localObject2)
							.getExternalLocation().length() > 0)) {
				return true;
			}
			if (((ExternalStoredData) localObject1).getDisplayName() != null) {
				if (!((ExternalStoredData) localObject1).getDisplayName()
						.equals(((ExternalStoredData) localObject2)
								.getDisplayName())) {
					return true;
				}
			} else if ((((ExternalStoredData) localObject2).getDisplayName() != null)
					&& (((ExternalStoredData) localObject2).getDisplayName()
							.length() > 0)) {
				return true;
			}
		}
		return false;
	}

	private void logContentItemAttributes(ContentItem paramContentItem) {
		Object localObject;
		if ((paramContentItem instanceof ApplicationData)) {
			localObject = (ApplicationData) paramContentItem;
			log.debug(" UploadedFromPath "
					+ ((ApplicationData) localObject).getUploadedFromPath());
			log.debug(" FileVersion "
					+ ((ApplicationData) localObject).getFileVersion());
			log.debug(" ToolName "
					+ ((ApplicationData) localObject).getToolName());
			log.debug(" ToolVersion "
					+ ((ApplicationData) localObject).getToolVersion());
		} else if ((paramContentItem instanceof URLData)) {
			localObject = (URLData) paramContentItem;
			log.debug(" URL Display Name "
					+ ((URLData) localObject).getDisplayName());
			log.debug(" URL Location "
					+ ((URLData) localObject).getUrlLocation());
		} else if ((paramContentItem instanceof ExternalStoredData)) {
			localObject = (ExternalStoredData) paramContentItem;
			log.debug(" ESD Display Name "
					+ ((ExternalStoredData) localObject).getDisplayName());
			log.debug(" External Location Location "
					+ ((ExternalStoredData) localObject).getExternalLocation());
		}
		log.debug(" Description " + paramContentItem.getDescription());
		log.debug(" Comments " + paramContentItem.getComments());
		log.debug(" Distributable " + paramContentItem.isDistributable());
		log.debug(" AuthoredBy " + paramContentItem.getAuthoredBy());
		log.debug(" LastAuthored " + paramContentItem.getLastAuthored());
	}
}
