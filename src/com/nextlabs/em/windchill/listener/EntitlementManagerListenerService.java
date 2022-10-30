package com.nextlabs.em.windchill.listener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import wt.access.AccessControlServerHelper;
import wt.access.SecurityLabels;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.content.DataFormat;
import wt.content.DataFormatReference;
import wt.content.FormatContentHolder;
import wt.content.Streamed;
import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.events.KeyedEvent;
import wt.events.KeyedEventListener;
import wt.events.summary.ModifyContentSummaryEvent;
import wt.fc.ObjectIdentifier;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
//import wt.fc.Persistable;
import wt.fc.PersistenceManagerEvent;
import wt.fc.QueryResult;
import wt.fv.FvProperties;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.recent.RecentUpdate;
//import wt.fc.WTObject;
import wt.services.ManagerException;
import wt.services.ServiceEventListenerAdapter;
import wt.services.StandardManager;
import wt.session.SessionMgr;
import wt.util.WTException;
import wt.vc.VersionControlServiceEvent;

import com.nextlabs.ConfigurationManager;
import com.nextlabs.EntitlementManagerContext;
import com.nextlabs.RightsManagerHelper;
import com.nextlabs.em.windchill.EntitlementManagerFilter;
import com.nextlabs.em.windchill.NXLDocumentObj;
import com.nextlabs.em.windchill.NXLSecondaryContent;
import com.nextlabs.em.windchill.QueryAgentImpl;
import com.nextlabs.em.windchill.SecurityLabel;
import com.nextlabs.em.windchill.SecurityLabelHelper;
import com.nextlabs.em.windchill.SecurityLabelList;
import com.nextlabs.em.windchill.WindchillObjectHelper;
import com.nextlabs.jtagger.Tagger;
import com.nextlabs.jtagger.TaggerFactory;
import com.nextlabs.nxl.crypt.RightsManager;

/*
 wt.fc.PersistenceManagerEvent 
 wt.lifecycle.LifeCycleServiceEvent
 wt.vc.wip.WorkInProgressServiceEvent 
 wt.events.summary.ModifyContentSummaryEvent
 */
/**
 * @author snehru
 * 
 */
public class EntitlementManagerListenerService extends StandardManager
		implements ListenerService, Serializable {
	private Logger logger = Logger
			.getLogger(EntitlementManagerListenerService.class);
	EntitlementManagerContext emCtx = new EntitlementManagerContext();
	// com.ptc.windchill.annotations.
	/*
	 * private class EMAuthenticator extends Authenticator {
	 * 
	 * @Override protected PasswordAuthentication getPasswordAuthentication() {
	 * String user = ConfigurationManager.getInstance().getProperty(
	 * com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
	 * com.nextlabs.Property.PEP_WINDCHILL_QUERYAGENT_USER); String password =
	 * ConfigurationManager.getInstance() .getQueryAgentPassword(); return new
	 * PasswordAuthentication(user, password.toCharArray()); } }
	 */

	private static final long serialVersionUID = 1L;
	private static final String CLASSNAME = EntitlementManagerListenerService.class
			.getName();

	private KeyedEventListener listener;

	public String getConceptualClassname() {
		return CLASSNAME;
	}

	public static EntitlementManagerListenerService newEntitlementManagerListenerService()
			throws WTException {
		EntitlementManagerListenerService instance = new EntitlementManagerListenerService();
		instance.initialize();
		return instance;
	}

	protected void performStartupProcess() throws ManagerException {
		listener = new WCListenerEventListener(this.getConceptualClassname());

		getManagerService().addEventListener(
				listener,
				PersistenceManagerEvent
						.generateEventKey(PersistenceManagerEvent.POST_DELETE));
		getManagerService().addEventListener(
				listener,
				PersistenceManagerEvent
						.generateEventKey(PersistenceManagerEvent.POST_MODIFY));
		getManagerService()
				.addEventListener(
						listener,
						VersionControlServiceEvent
								.generateEventKey(VersionControlServiceEvent.NEW_VERSION));
		getManagerService()
				.addEventListener(
						listener,
						VersionControlServiceEvent
								.generateEventKey(VersionControlServiceEvent.NEW_ITERATION));
		getManagerService()
				.addEventListener(
						listener,
						VersionControlServiceEvent
								.generateEventKey(VersionControlServiceEvent.PRE_NEW_VERSION));
		getManagerService()
				.addEventListener(
						listener,
						VersionControlServiceEvent
								.generateEventKey(VersionControlServiceEvent.PRE_INSERT_ITERATION));
		getManagerService()
				.addEventListener(
						listener,
						VersionControlServiceEvent
								.generateEventKey(VersionControlServiceEvent.POST_INSERT_ITERATION));

		// getManagerService().addEventListener(listener,
		// VersionControlServiceEvent.generateEventKey(VersionControlServiceEvent.));
		// getManagerService().addEventListener(listener,PersistenceManagerEvent.generateEventKey(PersistenceManagerEvent.));
		// wt.services.StandardManagerServiceEvent.ALL_SERVICES_STARTED

		getManagerService().addEventListener(
				listener,
				ModifyContentSummaryEvent.generateEventKey(WTDocument.class,
						ModifyContentSummaryEvent.ADD_CONTENT_ITEM));
		getManagerService().addEventListener(listener,
				ModifyContentSummaryEvent.REMOVE_CONTENT_ITEM);
		// wt.events.summary.ModifyContentSummaryEvent.ADD_CONTENT_ITEM
	}

	class WCListenerEventListener extends ServiceEventListenerAdapter {
		private String post_delete = PersistenceManagerEvent.POST_DELETE;
		private String post_modify = PersistenceManagerEvent.POST_MODIFY;
		private String add_content_item = ModifyContentSummaryEvent.ADD_CONTENT_ITEM;
		private String remove_content_item = ModifyContentSummaryEvent.REMOVE_CONTENT_ITEM;
		private String new_version = VersionControlServiceEvent.NEW_VERSION;

		private String new_iteration = VersionControlServiceEvent.NEW_ITERATION;

		public WCListenerEventListener(String manager_name) {
			super(manager_name);
		}

		@SuppressWarnings("deprecation")
		public void notifyVetoableEvent(Object eve) throws Exception {
			if (!(eve instanceof KeyedEvent))
				return;
			KeyedEvent event = (KeyedEvent) eve;
			Object target = event.getEventTarget();
			String eventType = event.getEventType();
			logger.info("  ==========event type  happen:"
					+ event.getEventType());
			logger.info("  ==========target class:"
					+ event.getEventTarget().getClass());

			if (eventType.equals(post_delete)) {
				logger.info("  ==========post delete happen:"
						+ target.toString());
			} else if (eventType.equals(post_modify)) {
				if (target instanceof WTDocument) {
					WTDocument doc = (WTDocument) target;
					QueryAgentImpl.documents.add(doc);
				}
				if (target instanceof RecentUpdate
						|| target instanceof WTDocumentMaster) {
					runlabeltagging();
				}

				logger.info("  ==========post modify happen:"
						+ target.getClass());
			} else if (eventType.equals(add_content_item)) {

				logger.info("  ==========add content item happen:");
			} else if (eventType.equals(remove_content_item)) {

				logger.info("  ==========remove content item happen:");
			} else if (eventType.equals(new_version)) {

				logger.info("  ==========new version happen:"
						+ target.toString());
				if (target instanceof WTDocument) {
					WTDocument doc = (WTDocument) target;
					QueryAgentImpl.documents.add(doc);
				}

			} else if (eventType.equals(new_iteration)) {

				logger.info("  ==========new iteration happen:"
						+ target.toString());
				if (target instanceof WTDocument) {
					/*
					 * if (!QueryAgentImpl.documents.contains(target)) {
					 * WTDocument doc = (WTDocument) target;
					 * QueryAgentImpl.documents.add(doc); }
					 */
				}

			}
			if (target instanceof WTDocument && eventType.equals(post_delete)) {
				WTDocument document = (WTDocument) target;
				if ("Silverlight0.log".equals(document.getName())) {
					throw new WTException(
							" document Silverlight0.log can't be deleted because nextlabs policy");
				}
			}
		}
	}

	/**
	 * This method extracts tags from Nxl files/non-Nxl files and update the
	 * security labels in the windchill database
	 * 
	 * @param doc
	 */
	private void onUploadAddTagsToDocSecLabels(WTDocument doc) {
		String path = WindchillObjectHelper.getWTTempPath() + "\\nxldrm\\";
		HashMap<String, Object> result = new HashMap<String, Object>();
		logger.info("  ==========wtdoc path :" + path);
		NXLDocumentObj document = new NXLDocumentObj();
		File file = null;
		try {

			logger.info("  ==========wtdoc 	doc.getLocation(); :" + doc);
			File dir = new File(path);
			dir.mkdirs();
			FormatContentHolder localFormatContentHolder = (FormatContentHolder) ContentHelper.service
					.getContents((ContentHolder) doc);
			ContentItem localContentItem = ContentHelper
					.getPrimary(localFormatContentHolder);
			if ((localContentItem != null)
					&& ((localContentItem instanceof ApplicationData))) {
				ApplicationData localApplicationData = (ApplicationData) localContentItem;
				Object localObject3 = localApplicationData != null ? (Streamed) localApplicationData
						.getStreamData().getObject() : null;
				if (localObject3 != null) {
					file = new File(path, localApplicationData.getFileName());
					document.setPrimaryContentFile(file);
					if (((Streamed) localObject3).retrieveStream() != null) {
						writeCotentToDisk(((Streamed) localObject3), file);
					} else {

						logger.info("  ==========wtdoc streamed object retrievestream is null :");
					}
					EntitlementManagerContext emCtx = new EntitlementManagerContext();
					ObjectIdentifier obj = PersistenceHelper
							.getObjectIdentifier(doc);
					String keyObj = null;
					if (obj != null)
						keyObj = obj.getStringValue();

					logger.info("  ==========UPLOAD EVENT Policy evaluation start :"
							+ keyObj);
					if (keyObj != null) {

						result = new EntitlementManagerFilter()
								.doUploadPolicyEvaluation(emCtx,
										SessionMgr.getPrincipal(), keyObj);

						logger.info("  ==========UPLOAD EVENT Policy evaluation end Results:"
								+ result);
					} else {

						logger.info("  ==========UPLOAD EVENT Policy evaluation not happened :"
								+ result);
					}
					String objName = (String) result.get("obligationame");
					if (objName != null
							&& objName.equalsIgnoreCase("TAGUPLOAD")) {
						if (null != doc
								&& null != doc.getPersistInfo()
								&& null != doc.getPersistInfo()
										.getObjectIdentifier()
								&& null != doc.getPersistInfo()
										.getObjectIdentifier().getStringValue()) {
							Persistable wcObj = WindchillObjectHelper
									.getObject(doc.getPersistInfo()
											.getObjectIdentifier()
											.getStringValue());
							if (wcObj instanceof WTDocument) {
								HashMap<String, String> tagMapping = (HashMap<String, String>) result
										.get("tags");
								SecurityLabelList securityLabels = tagsecuritylabel(
										(WTDocument) wcObj, file, tagMapping);
								String securityLabelName = getSecurityLabel(result);
								HashMap<String, String> secTagMapping = (HashMap<String, String>) result
										.get("sectags");
								HashMap<String, Object> secContentsMap = getSecondaryContent(
										path, doc, path, securityLabelName,
										securityLabels, secTagMapping);
								/*
								 * if (null != secContentsMap
								 * .get("secSecurityLabels") &&
								 * secTagMapping.size() > 0) {
								 * 
								 * securityLabels = (SecurityLabelList)
								 * secContentsMap .get("secSecurityLabels"); }
								 */

								String updateType = getUpdateSecurityLabelTransactionType();

								logger.info("  ==========ONUPLOAD updateType :"
										+ updateType);
								if (updateType != null
										&& updateType.equalsIgnoreCase("yes")) {
									updateSecurityLabelsForDocument(
											(WTDocument) wcObj, securityLabels);
								} else {
									updateValidSecurityLabelsForDocument(
											(WTDocument) wcObj, securityLabels);
								}
								if (null != secContentsMap
										.get("secContentItem")) {
									List<NXLSecondaryContent> secContents = (List<NXLSecondaryContent>) secContentsMap
											.get("secContentItem");
									document.setSecContents(secContents);
									if (document != null) {

										logger.info("  ==========wtdoc tagging and decrypting file :");

										updatePrimaryContent(document,
												(WTDocument) wcObj);

									}
								}
							}

						}
					}
				} else {

					logger.info("  ==========wtdoc streamed object  is null :"
							+ localObject3);
				}

			}

		} catch (Exception e) {

			logger.info("  ==========wtdoc WTException :" + e.getMessage());
			logger.error(e);
		}

	}

	private String getSecurityLabel(HashMap<String, Object> result) {
		String secondarySecurityLabelName = ConfigurationManager.getInstance()
				.getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
						com.nextlabs.Property.SEC_SECURITYLABEL);

		return secondarySecurityLabelName;
	}

	private String getUpdateSecurityLabelTransactionType() {
		String securitylabelUpdateType = ConfigurationManager
				.getInstance()
				.getProperty(
						com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
						com.nextlabs.Property.PEP_ROLLBACK_TRANSACTION_SECURITYLABEL_FAILED);

		return securitylabelUpdateType;
	}

	private HashMap<String, Object> getSecondaryContent(String path,
			WTDocument doc, String path2, String securityLabelName,
			SecurityLabelList securityLabels,
			HashMap<String, String> secTagMapping) {
		HashMap<String, Object> secContentsMap = new HashMap<String, Object>();
		ContentHolder holder;
		List<NXLSecondaryContent> nxlSecContents = new ArrayList<NXLSecondaryContent>();
		try {
			holder = ContentHelper.service.getContents((ContentHolder) doc);
			StringBuilder label_value = new StringBuilder();
			Vector contents = ContentHelper.getContentList(holder);
			logger.info("UPLOAD scondary content .size():" + contents.size());
			for (int j = 0; j < contents.size(); j++) {
				ContentItem obj = (ContentItem) contents.elementAt(j);
				if (obj instanceof ApplicationData) {
					ApplicationData ap1 = (ApplicationData) obj;

					logger.info("UPLOAD scondary content ApplicationData: "
							+ ap1.getUploadedFromPath() + ", "
							+ ap1.getFileName());
					Streamed secStream = ap1 != null ? (Streamed) ap1
							.getStreamData().getObject() : null;
					if (secStream != null && secStream.retrieveStream() != null) {
						File file = new File(path, ap1.getFileName());
						writeCotentToDisk(secStream, file);
						if (file.getName().endsWith("nxl")) {
							NXLSecondaryContent secContent = new NXLSecondaryContent();
							secContent.setContent(obj);
							secContent.setContentFile(file);
							nxlSecContents.add(secContent);

						}
						HashMap<String, List<String>> tags = getTags(file);
						label_value.append(getLabelValue(
								getNonNxlName(ap1.getFileName()), tags,
								secTagMapping));

						logger.info("  ==========getting secondary content tags for file name"
								+ getNonNxlName(ap1.getFileName())
								+ " :"
								+ tags);
					} else {

						logger.info("  ==========getting secondary content tags "
								+ ap1.getFileName()
								+ " streamed object  is null :");

					}
				}

			}

			logger.info("  ==========getting secondary content securitylabelname: "
					+ securityLabelName);

			logger.info("  ==========getting secondary content securitylabelvalue: "
					+ label_value.toString());
			if (securityLabelName != null) {
				SecurityLabel sl = new SecurityLabel();
				sl.setPrimaryContent(false);
				sl.setSecurityLabelName(securityLabelName);
				sl.setSecurityLabelValue(label_value.toString());
				if (secTagMapping.size() > 0)
					securityLabels.getSecurityLabels().add(sl);

			}
		} catch (Exception e) {
			logger.info("UPLOAD scondary content Exception:" + e.getMessage());
			logger.error(e);
		}
		secContentsMap.put("secContentItem", nxlSecContents);
		secContentsMap.put("secSecurityLabels", securityLabels);
		return secContentsMap;
	}

	private String getNonNxlName(String fileName) {
		String nonnxlname = fileName;
		if (fileName.endsWith(".nxl")) {
			int index = fileName.indexOf(".nxl");
			nonnxlname = fileName.substring(0, index);
		}
		return nonnxlname;
	}

	/**
	 * This method u
	 * 
	 * @param fileName
	 *            This fileName is String object which stores the name of the
	 *            file where the tags are read
	 * 
	 * @param tags
	 *            The tags and its values are concatenated into single string
	 *            value
	 * @param secTagMapping
	 * 
	 * @return the label value to be updated as custom security label
	 */

	private String getLabelValue(String fileName,
			HashMap<String, List<String>> tags,
			HashMap<String, String> secTagMapping) {

		Set<String> allowedTags = secTagMapping.keySet();
		logger.info("*******Upload tagSet :" + allowedTags);
		StringBuilder label_value = new StringBuilder("[");
		label_value.append(fileName);
		label_value.append(":");
		for (String tagName : allowedTags) {
			if (tags.get(tagName) != null) {
				label_value.append(tagName);
				label_value.append(">>");
				boolean flag = false;
				for (String tagValue : tags.get(tagName)) {
					if (flag)
						label_value.append(";");
					label_value.append(tagValue);
					flag = true;
				}
				label_value.append("|");
			}
		}
		label_value.append("]");
		return label_value.toString();
	}

	/**
	 * This method updates the secondary attachment classification information
	 * in the security label values of the Document object
	 * 
	 * @param fileName
	 *            Secondary attachment filename
	 * @param doc
	 *            Business Document Object
	 * @param tags
	 *            Tags and values need to be updated for the Business Document
	 *            Object
	 * @param securityLabelName
	 *            Security label of document object which needs to be updated
	 */

	private void updateValidSecurityLabelsForDocument(WTDocument doc,
			SecurityLabelList slList) {
		Transaction trx = null;

		for (SecurityLabel sl : slList.getSecurityLabels()) {
			try {
				trx = new Transaction();
				trx.start();
				if (SecurityLabelHelper.isLabelValueDefinedvalid(
						sl.getSecurityLabelName(), sl.getSecurityLabelValue())) {

					logger.info("**************** Update Security Labels  slName: "
							+ sl.getSecurityLabelName());

					logger.info("**************** Update Security Labels  slValue: "
							+ sl.getSecurityLabelValue());
					String securitylabelValue = sl.getSecurityLabelValue();
					securitylabelValue = securitylabelValue.replace("=",
							"LAUQE");
					securitylabelValue = securitylabelValue.replace(",",
							"CAMMOC");
					sl.setSecurityLabelValue(securitylabelValue);

					logger.info("**************** Update Security Labels  slValue: "
							+ sl.getSecurityLabelValue());
					AccessControlServerHelper.manager.setSecurityLabel(doc,
							sl.getSecurityLabelName(),
							sl.getSecurityLabelValue(), true);
				}

				trx.commit();

				logger.info("**************** Update Security Labels commited");
				trx = null;
			} catch (Exception e) {

				logger.info("**************** Update Security Labels Exception:"
						+ e.getMessage());
				logger.error(e);
			} finally {
				if (trx != null) {

				}

			}
		}

	}

	private void updateSecurityLabelsForDocument(WTDocument doc,
			SecurityLabelList slList) {
		Transaction trx = new Transaction();
		try {
			trx.start();
			for (SecurityLabel sl : slList.getSecurityLabels()) {

				logger.info("**************** Update Security Labels  slName: "
						+ sl.getSecurityLabelName());

				logger.info("**************** Update Security Labels  slValue: "
						+ sl.getSecurityLabelValue());
				String securitylabelValue = sl.getSecurityLabelValue();
				securitylabelValue = securitylabelValue.replace("=", "LAUQE");
				securitylabelValue = securitylabelValue.replace(",", "CAMMOC");
				sl.setSecurityLabelValue(securitylabelValue);

				logger.info("**************** Update Security Labels  slValue after modification: "
						+ sl.getSecurityLabelValue());
				AccessControlServerHelper.manager.setSecurityLabel(doc,
						sl.getSecurityLabelName(), sl.getSecurityLabelValue(),
						true);
			}
			trx.commit();
			trx = null;
		} catch (Exception e) {

			logger.info("**************** Update Security Labels Exception:"
					+ e.getMessage());
			logger.error(e);
		} finally {
			if (trx != null) {

				logger.debug("   **************** Update Security Labels Exception trx is not null, start to rollback.");
				trx.rollback();
			}
		}

	}

	private void writeCotentToDisk(Streamed secStream, File file)
			throws WTException, IOException {
		BufferedInputStream localBufferedInputStream = new BufferedInputStream(
				((Streamed) secStream).retrieveStream());
		Object localObject4 = new BufferedOutputStream(new FileOutputStream(
				file));
		Object localObject5 = new byte[FvProperties.READ_BUFFER_SIZE];
		int n;
		while ((n = localBufferedInputStream.read((byte[]) localObject5)) > 0) {
			((BufferedOutputStream) localObject4).write((byte[]) localObject5,
					0, n);
		}
		localBufferedInputStream.close();
		((BufferedOutputStream) localObject4).close();

		logger.info("  ==========File Name :" + file.getName());

	}

	private String decryptfile(File document) {
		RightsManager rm = new RightsManagerHelper().getRightsManager();
		logger.info("UPLOAD DECRYPTION New File Path:"
				+ document.getAbsolutePath());
		String decryptFilePath = document.getAbsolutePath();
		if (decryptFilePath.endsWith(".nxl")) {
			int index = decryptFilePath.indexOf(".nxl");
			if (index > 0)
				decryptFilePath = decryptFilePath.substring(0, index);

			logger.info("UPLOAD DECRYPTION New File Path after removing nxl extenssion:"
					+ decryptFilePath);
			try {
				rm.decrypt(document, decryptFilePath);
				// updatePrimaryContent(decryptFilePath, doc);
			} catch (Exception e) {
				logger.info("UPLOAD DECRYPTION Exception:" + e.getMessage());
			}
		}
		return decryptFilePath;
	}

	private void updatePrimaryContent(NXLDocumentObj document, WTDocument doc) {
		Transaction trx = new Transaction();

		try {
			File updateFile = new File(
					decryptfile(document.getPrimaryContentFile()));
			InputStream nxlInStream = new FileInputStream(updateFile);
			trx.start();
			// 1. Delete existed Primary Content

			logger.info("Update Primary content :Deletting primary content");
			ContentHolder holder = (FormatContentHolder) ContentHelper.service
					.getContents((ContentHolder) doc);
			ContentItem ciUpload = ((FormatContentHolder) holder).getPrimary();

			ContentServerHelper.service.deleteContent((ContentHolder) doc,
					ciUpload);
			for (NXLSecondaryContent sc : document.getSecContents()) {
				if (sc.getContent() != null)
					ContentServerHelper.service.deleteContent(
							(ContentHolder) doc, sc.getContent());
			}
			// 2. Create new ApplicationDatain

			logger.info("Update Primary content :Creating new primary content for "
					+ doc);
			ApplicationData appData = ApplicationData
					.newApplicationData((FormatContentHolder) doc);

			appData.setFileName(updateFile.getName());
			appData.setFileSize(updateFile.length());
			appData.setUploadedFromPath(updateFile.getPath());

			// 3. Primary
			appData = ContentServerHelper.service.updatePrimary(
					(FormatContentHolder) doc, (ApplicationData) appData,
					(InputStream) nxlInStream);
			// 4. Secondary
			logger.info("****UPDATE Primary Content done");
			nxlInStream.close();
			// To update format for ApplicationData - Microsoft Word format
			DataFormatReference newdfr = getNewDataFormat(appData
					.getBusinessType());
			if (newdfr != null) {
				doc.setFormat(newdfr);
				// PersistenceServerHelper.manager.update(doc);
				FormatContentHolder fch = ContentServerHelper.service
						.updateHolderFormat((FormatContentHolder) doc);
			}

			logger.info("Update Primary content : primary content document format updated");

			for (NXLSecondaryContent sc : document.getSecContents()) {
				if (sc.getContentFile() != null) {
					File secondaryFile = new File(
							decryptfile(sc.getContentFile()));
					InputStream nxlSecInStream = new FileInputStream(
							secondaryFile);
					ApplicationData secAppData = ApplicationData
							.newApplicationData((FormatContentHolder) doc);
					secAppData.setFileName(secondaryFile.getName());
					secAppData.setFileSize(secondaryFile.length());
					secAppData.setUploadedFromPath(secondaryFile.getPath());
					secAppData.setRole(ContentRoleType.SECONDARY);
					secAppData.setCreatedBy(doc.getCreator());
					secAppData.setDescription("Updating Secondary Content");

					ContentServerHelper.service.updateContent(
							(FormatContentHolder) doc, secAppData,
							(InputStream) nxlSecInStream);
					logger.info("****UPDATE Secondary Content done:"
							+ secondaryFile.getName());
					nxlSecInStream.close();
				}
			}

			logger.info("Update Primary content : primary content updated");

			trx.commit();
			trx = null;
		} catch (Exception e) {
			logger.info("Update Primary content Exception:" + e.getClass());
			logger.info("Update Primary content Exception:" + e.getMessage());
			logger.error(e);
		} finally {
			if (trx != null) {

				logger.info("Update Primary content transaction is not null and rolling back:");
				trx.rollback();
			}
		}
	}

	private DataFormatReference getNewDataFormat(String name) {
		DataFormatReference dfr = null;

		logger.info("Update Primary content :Appdata businesstype:" + name);
		/*
		 * String extension = ""; int index = name.lastIndexOf("."); if (index >
		 * 0) { extension = name.substring(index + 1);
		 * logger.info("Update Primary content extension" + extension); }
		 */
		try {
			QuerySpec qs = new QuerySpec(DataFormat.class);
			qs.appendWhere(new SearchCondition(DataFormat.class,
					DataFormat.FORMAT_NAME, SearchCondition.EQUAL, name));
			QueryResult qr = PersistenceHelper.manager.find(qs);
			while (qr.hasMoreElements()) {
				DataFormat df = (DataFormat) qr.nextElement();
				logger.info("DataFormat" + df.getFormatName());
				//
				dfr = DataFormatReference.newDataFormatReference(df);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (dfr != null)
			logger.info("Update Primary content dfr" + dfr.getFormatName());
		return dfr;
	}

	private SecurityLabelList tagsecuritylabel(WTDocument doc, File file,
			HashMap<String, String> tagMapping) {
		SecurityLabels sl;
		SecurityLabelList secLabelList = new SecurityLabelList();
		logger.info("  ==========Object OID:"
				+ doc.getPersistInfo().getObjectIdentifier().getStringValue());
		try {
			HashMap<String, List<String>> tags = getTags(file);

			logger.info("  ==========Security Labels:of document object"
					+ doc.getSecurityLabels().toString());

			Iterator<String> iterator = tags.keySet().iterator();
			while (iterator.hasNext()) {
				String label_name = iterator.next();
				StringBuilder label_value = new StringBuilder("");
				for (String tag : tags.get(label_name)) {
					if (label_value.toString().equalsIgnoreCase("")) {
						label_value.append(tag);
					} else {
						label_value.append(";");
						label_value.append(tag);
					}
				}

				logger.info("  ==========Security Labels:label name"
						+ label_name);

				logger.info("  ==========Security Labels:label value"
						+ label_value);

				if (tagMapping != null) {
					String securityLabel = tagMapping.get(label_name);

					logger.info("  ==========Security Labels:securityLabel"
							+ securityLabel);
					if (securityLabel != null) {
						SecurityLabel slabel = new SecurityLabel();
						slabel.setPrimaryContent(true);
						slabel.setSecurityLabelName(securityLabel);
						slabel.setSecurityLabelValue(label_value.toString());
						secLabelList.getSecurityLabels().add(slabel);
					}

				}
			}

		} catch (Exception e) {

			logger.info("  ==========Security Labels:WT Exception"
					+ e.getMessage());
			logger.error(e);

		}
		return secLabelList;
	}

	private HashMap<String, List<String>> getTags(File file) {
		HashMap<String, List<String>> tags = new HashMap<String, List<String>>();
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		logger.info("======== file name" + file.getName());
		RightsManager rm = RightsManagerHelper.getRightsManager();
		if (file.getName().endsWith("nxl")) {
			logger.info("======== nxl file ");
			try {
				// tags = (HashMap<String, List<String>>) rm.readTags(file);
				logger.info("NXL File tags " + tags);
			} catch (Exception e) {
				logger.info("error occured " + e.getMessage());
				logger.error(e);
			}
		} else {
			logger.info("======== non nxl file ");
			try {
				tags = getNonNxlFiletags(file);
			} catch (Exception e) {
				logger.info("non nxl file error occured " + e.getMessage());
				logger.error(e);
			}
		}
		return tags;
	}

	private HashMap<String, List<String>> getNonNxlFiletags(File file) {

		HashMap<String, List<String>> tags = new HashMap<String, List<String>>();
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		logger.info("getNonNxlFiletags 1for file:" + file.exists());
		logger.info("getNonNxlFiletags 1for file path:"
				+ file.getAbsolutePath());
		// String delimiterEscape = "\\|";
		Tagger tagger = null;
		logger.info("====Tagger Created");
		try {
			logger.info("====inside try catch Created");
			tagger = TaggerFactory.getTagger(file.getAbsolutePath());
			logger.info("====tagger initialized Created");

			HashMap<String, Object> taggertags = tagger.getAllTags(false);
			logger.info("getNonNxlFiletags" + taggertags.toString());
			Iterator<String> iterator = taggertags.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				ArrayList<String> valList = new ArrayList<String>();
				Object obj = taggertags.get(key);
				if (obj != null) {
					valList.add(obj.toString());
				}
				tags.put(key, valList);
			}
		} catch (Exception e) {
			logger.info("error occured " + e.getMessage());
			logger.error(e);
		}

		return tags;
	}

	public void runlabeltagging() {
		WTDocument doc;

		logger.info("  ========== 	Timer Task Called; ");
		doc = QueryAgentImpl.documents.poll();
		while (doc != null) {
			logger.info("  ==========wtdoc doc :" + doc);
			onUploadAddTagsToDocSecLabels(doc);

			doc = QueryAgentImpl.documents.poll();
		}
	}

	public static void main(String args[]) {
		System.out.println(new EntitlementManagerListenerService()
				.getNonNxlName("ITAR DSP.docx"));
	}
}
