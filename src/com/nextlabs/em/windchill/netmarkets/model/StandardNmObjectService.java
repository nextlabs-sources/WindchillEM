package com.nextlabs.em.windchill.netmarkets.model;

import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Logger;

import wt.access.AccessControlHelper;
import wt.access.AccessControlServerHelper;
import wt.access.AccessControlled;
import wt.access.AccessPermission;
import wt.access.NotAuthorizedException;
import wt.access.SecurityLabels;
import wt.admin.AdminDomainRef;
import wt.clients.util.IconCache;
import wt.clients.util.WTPrincipalUtil;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentHtmlUtils;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.content.FormatContentHolder;
import wt.content.Streamed;
import wt.content.URLData;
import wt.doc.WTDocument;
import wt.enterprise.IteratedFolderResident;
import wt.enterprise.RevisionControlled;
import wt.enterprise.TemplateInfo;
import wt.epm.EPMDocument;
import wt.events.summary.ImportSummaryEvent;
import wt.facade.persistedcollection.ManagedCollection;
import wt.facade.scm.ScmFacade;
import wt.fc.IconDelegate;
import wt.fc.IconDelegateFactory;
import wt.fc.ObjectIdentifier;
import wt.fc.ObjectNoLongerExistsException;
import wt.fc.ObjectReference;
import wt.fc.ObjectVector;
import wt.fc.ObjectVectorIfc;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryKey;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.fc.WTReference;
import wt.fc.collections.CollectionContainsDeletedException;
import wt.fc.collections.CollectionsHelper;
import wt.fc.collections.RefreshSpec;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTKeyedHashMap;
import wt.fc.collections.WTKeyedMap;
import wt.fc.collections.WTSet;
import wt.folder.Cabinet;
import wt.folder.CabinetBased;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.folder.SubFolder;
import wt.fv.FvProperties;
import wt.fv.master.RedirectDownload;
import wt.httpgw.HTTPRequest;
import wt.httpgw.HTTPResponse;
import wt.httpgw.URLFactory;
import wt.identity.IdentityFactory;
import wt.inf.container.ExchangeContainer;
import wt.inf.container.OrgContainer;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainedFilterable;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.inf.container.WTContainerTemplate;
import wt.inf.container.WTContainerTemplateRef;
import wt.inf.library.WTLibrary;
import wt.inf.sharing.DataSharingHelper;
import wt.inf.sharing.SharedContainerMap;
import wt.inf.team.ContainerTeam;
import wt.inf.template.ContainerTemplateHelper;
import wt.inf.template.DefaultWTContainerTemplate;
import wt.inf.template.ImportTemplateRequest;
import wt.inf.template.UploadTemplateRequest;
import wt.ixb.clientAccess.IXBJarWriter;
import wt.ixb.clientAccess.StandardIXBService;
import wt.ixb.handlers.netmarkets.JSPFeedback;
import wt.ixb.handlers.netmarkets.ProjectIXUtils;
import wt.ixb.handlers.netmarkets.TeamExportHolder;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.LifeCycleTemplate;
import wt.locks.LockHelper;
import wt.locks.Lockable;
import wt.log4j.LogR;
import wt.method.MethodContext;
import wt.notify.NotificationHelper;
import wt.notify.NotificationSubscription;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.ownership.Ownable;
import wt.ownership.OwnershipHelper;
import wt.part.WTPart;
import wt.part.WTPartAlternateLink;
import wt.pdmlink.PDMLinkProduct;
import wt.pds.InflateSpec;
import wt.pds.StatementSpec;
import wt.pom.PersistenceException;
import wt.pom.Transaction;
import wt.pom.TransactionListener;
import wt.project.Role;
import wt.projmgmt.admin.Project2;
import wt.projmgmt.admin.ProjectState;
import wt.projmgmt.execution.ExecutionObject;
import wt.projmgmt.execution.Milestone;
import wt.projmgmt.execution.ProjExecHelper;
import wt.projmgmt.execution.ProjectActivity;
import wt.projmgmt.execution.ProjectPlan;
import wt.projmgmt.execution.ProjectWorkItem;
import wt.projmgmt.resource.Deliverable;
import wt.projmgmt.resource.InformationResource;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.ColumnExpression;
import wt.query.CompoundQuerySpec;
import wt.query.ConstantExpression;
import wt.query.KeywordExpression;
import wt.query.QuerySpec;
import wt.query.SQLFunction;
import wt.query.SearchCondition;
import wt.query.SetOperator;
import wt.query.SubSelectExpression;
import wt.representation.RepresentationHelper;
import wt.sandbox.IOPState;
import wt.sandbox.SandboxHelper;
import wt.services.StandardManager;
import wt.session.SessionContext;
import wt.session.SessionHelper;
import wt.session.SessionMgr;
import wt.session.SessionServerHelper;
import wt.type.Typed;
import wt.type.TypedUtility;
import wt.util.InstalledProperties;
import wt.util.LocalizableMessage;
import wt.util.WTException;
import wt.util.WTMessage;
import wt.util.WTProperties;
import wt.util.WTPropertyVetoException;
import wt.util.WTRuntimeException;
import wt.vc.Iterated;
import wt.vc.Mastered;
import wt.vc.VersionControlHelper;
import wt.vc.VersionForeignKey;
import wt.vc.VersionReference;
import wt.vc.wip.CheckoutLink;
import wt.vc.wip.WorkInProgressException;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;
import wt.workflow.definer.WfDefinerHelper;
import wt.workflow.definer.WfProcessTemplate;
import wt.workflow.engine.WfEngineHelper;
import wt.workflow.engine.WfProcess;
import wt.workflow.engine.WfState;
import wt.workflow.forum.DiscussionPosting;
import wt.workflow.notebook.Bookmark;
import wt.workflow.notebook.FolderedBookmark;
import wt.workflow.notebook.ImportedBookmark;
import wt.workflow.notebook.NotebookFolder;
import wt.workflow.notebook.NotebookHelper;
import wt.workflow.notebook.NotebookUtil;
import wt.workflow.templates.TaskFormTemplate;
import wt.wvs.VisualizationHelperFactory;
import wt.wvs.VisualizationHelperIfc;

import com.nextlabs.ConfigurationManager;
import com.nextlabs.EntitlementManagerContext;
import com.nextlabs.RightsManagerHelper;
import com.nextlabs.em.windchill.WindchillObjectHelper;
import com.nextlabs.em.windchill.tools.UtilityTools;
import com.nextlabs.em.windchill.wsclient.QueryAgent;
import com.nextlabs.em.windchill.wsclient.QueryAgent_Service;
import com.nextlabs.nxl.crypt.RightsManager;
import com.ptc.core.HTMLtemplateutil.server.processors.TypeInstanceAttributesService;
import com.ptc.core.HTMLtemplateutil.server.processors.UtilProcessorService;
import com.ptc.core.HTMLtemplateutil.server.processors.WizardProcessor;
import com.ptc.core.components.forms.DynamicRefreshInfo;
import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.forms.FormResultAction;
import com.ptc.core.components.rendering.AbstractGuiComponent;
import com.ptc.core.components.rendering.guicomponents.IconComponent;
import com.ptc.core.components.rendering.guicomponents.TextDisplayComponent;
import com.ptc.core.components.util.RenderUtil;
import com.ptc.core.foundation.type.common.TypeIdentifierSelectionHelper;
import com.ptc.core.foundation.type.common.impl.AttributeData;
import com.ptc.core.foundation.type.server.impl.SoftAttributesHelper;
import com.ptc.core.foundation.type.server.impl.TypeHelper;
import com.ptc.core.htmlcomp.tableview.ConfigurableTableHelper;
import com.ptc.core.htmlcomp.tableview.TableViewCriterion;
import com.ptc.core.htmlcomp.tableview.TableViewDescriptor;
import com.ptc.core.htmlcomp.tableview.TableViewDescriptorHelper;
import com.ptc.core.meta.common.AttributeIdentifier;
import com.ptc.core.meta.common.AttributeTypeIdentifier;
import com.ptc.core.meta.common.TypeIdentifier;
import com.ptc.core.meta.container.common.AttributeTypeSummary;
import com.ptc.core.meta.container.common.impl.DefaultAttributeTypeSummary;
import com.ptc.core.meta.server.TypeIdentifierUtility;
import com.ptc.core.meta.type.admin.common.impl.TypeAdminHelper;
import com.ptc.core.meta.type.mgmt.common.TypeDefinitionDefaultView;
import com.ptc.core.meta.type.mgmt.common.TypeDefinitionNodeView;
import com.ptc.core.meta.type.mgmt.server.TypeDefinition;
import com.ptc.core.meta.type.mgmt.server.impl.TypeDefinitionObjectsFactory;
import com.ptc.core.meta.type.mgmt.server.impl.TypeDomainHelper;
import com.ptc.core.meta.type.mgmt.server.impl.WTTypeDefinition;
import com.ptc.core.ui.resources.ComponentType;
import com.ptc.core.ui.validation.UIComponentValidationHelper;
import com.ptc.core.ui.validation.UIValidationCriteria;
import com.ptc.core.ui.validation.UIValidationKey;
import com.ptc.core.ui.validation.UIValidationResult;
import com.ptc.core.ui.validation.UIValidationResultSet;
import com.ptc.core.ui.validation.UIValidationStatus;
import com.ptc.netmarkets.model.NmChangeModel;
import com.ptc.netmarkets.model.NmConsoleOpenException;
import com.ptc.netmarkets.model.NmException;
import com.ptc.netmarkets.model.NmJarReader;
import com.ptc.netmarkets.model.NmNamedObject;
import com.ptc.netmarkets.model.NmObject;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.model.NmPasteInfo;
import com.ptc.netmarkets.model.NmSearchHTMLTableModel;
import com.ptc.netmarkets.model.NmSimpleOid;
import com.ptc.netmarkets.model.NmSoftAttribute;
import com.ptc.netmarkets.project.NmProjectHelper;
import com.ptc.netmarkets.templates.StandardNmTemplatesService;
import com.ptc.netmarkets.util.NmUtilClassProxy;
import com.ptc.netmarkets.util.beans.NmClipboardBean;
import com.ptc.netmarkets.util.beans.NmClipboardItem;
import com.ptc.netmarkets.util.beans.NmClipboardItemInfo;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.netmarkets.util.beans.NmLinkBean;
import com.ptc.netmarkets.util.misc.FilePathFactory;
import com.ptc.netmarkets.util.misc.NmAction;
import com.ptc.netmarkets.util.misc.NmActionServiceHelper;
import com.ptc.netmarkets.util.misc.NmAnyURL;
import com.ptc.netmarkets.util.misc.NmContext;
import com.ptc.netmarkets.util.misc.NmContextItem;
import com.ptc.netmarkets.util.misc.NmDate;
import com.ptc.netmarkets.util.misc.NmElementAddress;
import com.ptc.netmarkets.util.misc.NmHTMLActionModel;
import com.ptc.netmarkets.util.misc.NmString;
import com.ptc.netmarkets.util.table.NmDefaultHTMLTable;
import com.ptc.netmarkets.util.table.NmHTMLTable;
import com.ptc.windchill.classproxy.ConsoleClassProxy;
import com.ptc.windchill.classproxy.WorkPackageClassProxy;
import com.ptc.windchill.enterprise.copy.server.CoreMetaUtility;

public class StandardNmObjectService extends StandardManager implements
		NmObjectService, Serializable {
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

	private static final long serialVersionUID = 1L;
	private static final String RESOURCE = "com.ptc.netmarkets.model.modelResource";
	private static final String PROJECT_PLAN_RESOURCE = "com.ptc.netmarkets.projectPlan.projectPlanResource";
	private static final String DEFAULT_ARCHIVE_FILE_NAME;
	private static final String MORE_RESOURCE = "com.ptc.netmarkets.object.objectResource";
	private static final String ACCESS_RESOURCE = "wt.access.accessResource";
	private static final String SHARE_RESOURCE = "wt.inf.sharing.sharingResource";
	private static final String COMPONENT_RESOURCE = "com.ptc.core.ui.componentRB";
	private static final boolean VERBOSE;
	private static final String HEADER_CONTENT_TYPE;
	private static boolean PdmSystemInstalled = false;
	private static TypeInstanceAttributesService typeInstanceAttributesService = new TypeInstanceAttributesService();
	private transient URLFactory url_factory;
	private VisualizationHelperIfc VIS_HELPER = VisualizationHelperFactory.HELPER;
	protected static int DEFAULT_STRING_INPUT_FIELD_LENGTH = 30;
	protected static int DEFAULT_INTEGER_INPUT_FIELD_LENGTH = 15;
	protected static int DEFAULT_FLOAT_INPUT_FIELD_LENGTH = 20;
	protected static int DEFAULT_TIMESTAMP_INPUT_FIELD_LENGTH = 30;
	public static String PDM_PASTE_OIDS = "pdm_paste_oids";
	private static final Class TaskResultClass;
	private static final String folderIconContextKey = "nmfolder.icon";
	private static File saveDirectory = null;
	private static final int FILE_NAME_MAX_LENGTH;
	private Logger deleteLogger = LogR.getLogger("com.ptc.delete");
	private Logger logger = LogR.getLogger(StandardNmObjectService.class
			.getName());
	private static final boolean IS_EMPTY_FILE_VALID;

	static {
		try {
			WTProperties localWTProperties = WTProperties.getLocalProperties();
			VERBOSE = localWTProperties.getProperty(
					"com.ptc.netmarkets.model.verbose", false);
			FILE_NAME_MAX_LENGTH = localWTProperties.getProperty(
					"com.ptc.netmarkets.object.fileNameMaxlength", 100);
			DEFAULT_ARCHIVE_FILE_NAME = localWTProperties.getProperty(
					"wt.export.defaultDownloadFileName",
					"document_contents.zip");

			HEADER_CONTENT_TYPE = localWTProperties.getProperty(
					"wt.export.headerContentType", "application/octet-stream");
			TaskResultClass = Class.forName("com.ptc.core.task.TaskResult");
			PdmSystemInstalled = InstalledProperties.isInstalled("pdmSystem");
			IS_EMPTY_FILE_VALID = localWTProperties.getProperty(
					"wt.content.validEmptyFile", false);
		} catch (Throwable localThrowable) {
			System.err.println("Error initializing "
					+ StandardNmObjectService.class.getName());
			localThrowable.printStackTrace(System.err);
			throw new ExceptionInInitializerError(localThrowable);
		}
	}

	public static StandardNmObjectService newStandardNmObjectService()
			throws WTException {
		StandardNmObjectService localStandardNmObjectService = new StandardNmObjectService();
		localStandardNmObjectService.initialize();
		return localStandardNmObjectService;
	}

	public FormResult delete(NmCommandBean paramNmCommandBean)
			throws WTException {
		ArrayList localArrayList = new ArrayList();
		localArrayList.add(paramNmCommandBean.constructContext());
		paramNmCommandBean.setSelected(localArrayList);
		return list__delete(paramNmCommandBean, null, true);
	}

	public FormResult checkIn(NmCommandBean paramNmCommandBean,
			String paramString1, String paramString2, String paramString3,
			String paramString4) throws WTException {
		NmOid localNmOid1 = paramNmCommandBean.getActionOid();
		Persistable localPersistable = ObjectReference.newObjectReference(
				localNmOid1.getOidObject()).getObject();
		Workable localWorkable = null;
		HashMap localHashMap = paramNmCommandBean.getMap();
		Object localObject1 = localHashMap.get("content_type");
		String str = (String) localHashMap.get("url");
		if (!WorkInProgressHelper.isWorkingCopy((Workable) localPersistable)) {
			localWorkable = WorkInProgressHelper.service
					.workingCopyOf((Workable) localPersistable);
		} else {
			localWorkable = (Workable) localPersistable;
		}
		Transaction localTransaction = null;
		try {
			localTransaction = new Transaction();
			localTransaction.start();
			PersistenceServerHelper.manager.lock(localWorkable);
			if (paramString3 != null) {
				Object localObject2;
				Object localObject3;
				if ((localWorkable instanceof WTDocument)) {
					localObject2 = (WTDocument) localWorkable;
					localObject3 = null;
					try {
						localObject3 = Class
								.forName("StandardNmDocumentService.ContentType.CLEARCASE");
					} catch (ClassNotFoundException localClassNotFoundException) {
					}
					if (localObject1.equals(localObject3)) {
						addSCMIPrimaryContent((ContentHolder) localObject2,
								paramString3);
					} else {
						uploadContent((ContentHolder) localObject2,
								paramString2, paramString3);
					}
				} else if ((localWorkable instanceof DefaultWTContainerTemplate)) {
					localObject2 = new File(paramString3);
					localObject3 = (DefaultWTContainerTemplate) localWorkable;
					Object localObject5;
					if (StandardNmTemplatesService
							.replaceBusinessXml((File) localObject2)) {
						localObject5 = ContainerTemplateHelper.service
								.getUploadRequest((File) localObject2);
						ContainerTemplateHelper.service
								.replaceBusinessXML(
										WTContainerTemplateRef
												.newWTContainerTemplateRef((WTContainerTemplate) localWorkable),
										(UploadTemplateRequest) localObject5);
					} else {
						localObject5 = ContainerTemplateHelper.service
								.getImportRequest((File) localObject2);
						ContainerTemplateHelper.service
								.createContainerTemplate(
										((DefaultWTContainerTemplate) localObject3)
												.getContainerReference(),
										(WTContainerTemplate) localObject3,
										(ImportTemplateRequest) localObject5,
										true);
					}
				} else if ((localWorkable instanceof TaskFormTemplate)) {
					localObject2 = (ContentHolder) localWorkable;
					try {
						localObject2 = ContentHelper.service
								.getContents((ContentHolder) localObject2);
					} catch (PropertyVetoException localPropertyVetoException2) {
						throw new WTException(localPropertyVetoException2);
					}
					Object localObject4 = ContentHelper
							.getApplicationData((ContentHolder) localObject2);
					if (((Vector) localObject4).size() == 1) {
						try {
							ContentServerHelper.service.deleteContent(
									(ContentHolder) localObject2,
									(ContentItem) ((Vector) localObject4)
											.get(0));
							uploadContent((ContentHolder) localObject2,
									paramString2, paramString3);
						} catch (WTPropertyVetoException localWTPropertyVetoException3) {
							throw new WTException(localWTPropertyVetoException3);
						}
					}
				}
			} else if ((str != null) && (str.length() > 0)
					&& ((localWorkable instanceof WTDocument))) {
				try {
					persistPrimaryURL((FormatContentHolder) localWorkable, str);
				} catch (WTPropertyVetoException localWTPropertyVetoException1) {
					throw new WTException(localWTPropertyVetoException1);
				} catch (PropertyVetoException localPropertyVetoException1) {
					throw new WTException(localPropertyVetoException1);
				}
			}
			if (WorkInProgressHelper.isWorkingCopy(localWorkable)) {
				try {
					localWorkable = WorkInProgressHelper.service.checkin(
							localWorkable, paramString1);
					if ((localWorkable instanceof WTPart)) {
						RepresentationHelper.service
								.emitReadyToPublishEvent(localWorkable);
					}
				} catch (WTPropertyVetoException localWTPropertyVetoException2) {
					throw new WTException(localWTPropertyVetoException2);
				} catch (WorkInProgressException localWorkInProgressException) {
					throw new WTException(localWorkInProgressException);
				} catch (PersistenceException localPersistenceException) {
					throw new WTException(localPersistenceException);
				} catch (WTException localWTException) {
					throw localWTException;
				}
			}
			localTransaction.commit();
			localTransaction = null;
		} finally {
			if (localTransaction != null) {
				localTransaction.rollback();
			}
		}
		localWorkable = (Workable) VersionControlHelper.getLatestIteration(
				localWorkable, false);
		NmOid localNmOid2 = NmOid.newNmOid(localWorkable.getPersistInfo()
				.getObjectIdentifier());

		Object localObject4 = new FormResult();
		if (ComponentType.INFO.name().equals(
				paramNmCommandBean.getTextParameter("componentType"))) {
			NmAction localNmAction = NmActionServiceHelper.service.getAction(
					"object", "view");
			localNmAction.setContextObject(localNmOid2);
			((FormResult) localObject4)
					.setNextAction(FormResultAction.LOAD_OPENER_URL);
			try {
				((FormResult) localObject4).setURL(localNmAction
						.getActionUrlExternal());
			} catch (Exception localException) {
				throw new WTException(localException);
			}
		} else {
			((FormResult) localObject4)
					.addDynamicRefreshInfo(new DynamicRefreshInfo(localNmOid2,
							localNmOid1, "U"));

			((FormResult) localObject4)
					.setNextAction(FormResultAction.REFRESH_OPENER);
		}
		return (FormResult) localObject4;
	}

	public FormResult checkOut(NmCommandBean paramNmCommandBean)
			throws WTException {
		NmOid localNmOid = paramNmCommandBean.getActionOid();
		return checkOut(localNmOid);
	}

	public FormResult undoCheckOut(NmCommandBean paramNmCommandBean)
			throws WTException {
		NmOid localNmOid = paramNmCommandBean.getActionOid();
		Persistable localPersistable = ObjectReference.newObjectReference(
				localNmOid.getOidObject()).getObject();
		FormResult localFormResult = new FormResult();
		localFormResult.setNextAction(FormResultAction.REFRESH_CURRENT_PAGE);
		localFormResult.setStatus(FormProcessingStatus.SUCCESS);
		try {
			WorkInProgressHelper.service
					.undoCheckout((Workable) localPersistable);
			if (VERBOSE) {
				System.out.println("undid check out");
			}
		} catch (WTPropertyVetoException localWTPropertyVetoException) {
			throw new WTException(localWTPropertyVetoException);
		} catch (WorkInProgressException localWorkInProgressException) {
			throw new WTException(localWorkInProgressException);
		} catch (PersistenceException localPersistenceException) {
			throw new WTException(localPersistenceException);
		} catch (WTException localWTException) {
			throw localWTException;
		}
		return localFormResult;
	}

	public NmChangeModel[] rollupIterations(NmCommandBean paramNmCommandBean)
			throws WTException {
		NmOid localNmOid = paramNmCommandBean.getActionOid();
		Persistable localPersistable = ObjectReference.newObjectReference(
				localNmOid.getOid()).getObject();
		try {
			QueryResult localQueryResult = VersionControlHelper.service
					.iterationsOf((Iterated) localPersistable);
			Workable localWorkable = null;
			while (localQueryResult.hasMoreElements()) {
				localWorkable = (Workable) localQueryResult.nextElement();
				if (VersionControlHelper.service
						.isFirstIteration(localWorkable)) {
					break;
				}
			}
			VersionControlHelper.service.rollup(localWorkable,
					(Iterated) localPersistable);
		} catch (WTPropertyVetoException localWTPropertyVetoException) {
			throw new WTException(localWTPropertyVetoException);
		} catch (WorkInProgressException localWorkInProgressException) {
			throw new WTException(localWorkInProgressException);
		} catch (PersistenceException localPersistenceException) {
			throw new WTException(localPersistenceException);
		} catch (WTException localWTException) {
			throw localWTException;
		}
		NmChangeModel[] arrayOfNmChangeModel = new NmChangeModel[1];
		arrayOfNmChangeModel[0] = new NmChangeModel();
		arrayOfNmChangeModel[0].setInvalidateCache(true);
		arrayOfNmChangeModel[0].setInvalidateAll(true);
		return arrayOfNmChangeModel;
	}

	public FormResult list_delete(NmCommandBean paramNmCommandBean)
			throws WTException {
		try {
			return list__delete(paramNmCommandBean, null, false);
		} catch (WTException localWTException1) {
			Object localObject = localWTException1;
			int i = 0;
			WTException localWTException2 = null;
			while (i == 0) {
				if ((localObject instanceof WTException)) {
					localObject = ((WTException) localObject)
							.getNestedThrowable();
					if ((localObject != null)
							&& ((localObject instanceof WTException))) {
						localWTException2 = (WTException) localObject;
					} else {
						i = 1;
					}
				} else {
					i = 1;
				}
			}
			throw localWTException2;
		}
	}

	public FormResult list__delete(NmCommandBean paramNmCommandBean,
			ArrayList paramArrayList) throws WTException {
		boolean bool = paramNmCommandBean.getMap().get("delete_background") != null;
		return list__delete(paramNmCommandBean, paramArrayList, bool);
	}

	public NmChangeModel[] list_undoCheckOut(NmCommandBean paramNmCommandBean)
			throws WTException {
		WTArrayList localWTArrayList = new WTArrayList();

		ArrayList localArrayList = paramNmCommandBean.getSelected();
		if (localArrayList == null) {
			return null;
		}
		int i = localArrayList.size();

		NmChangeModel[] arrayOfNmChangeModel = new NmChangeModel[i];

		int j = 0;
		for (int k = 0; k < i; k++) {
			Object localObject = localArrayList.get(k);
			if ((localObject instanceof NmContext)) {
				NmOid localNmOid = ((NmContext) localObject).getTargetOid();
				try {
					localObject = localNmOid.getRefObject();
				} catch (WTRuntimeException localWTRuntimeException) {
					Throwable localThrowable = localWTRuntimeException
							.getNestedThrowable();
					if ((localThrowable instanceof ObjectNoLongerExistsException)) {
						j = 1;
						continue;
					}
					throw new WTRuntimeException(localWTRuntimeException);
				}
			} else if ((localObject instanceof Persistable)) {
				localWTArrayList.add((Persistable) localObject);
				arrayOfNmChangeModel[k] = new NmChangeModel();
				arrayOfNmChangeModel[k].setInvalidateCache(true);
			}
		}
		doMultiObjUndoCheckout(localWTArrayList);
		if (j != 0) {
			throw new WTException(getUndoCheckoutFailureMsg());
		}
		return arrayOfNmChangeModel;
	}

	private void doMultiObjUndoCheckout(WTArrayList paramWTArrayList)
			throws WTException {
		if (paramWTArrayList.size() > 0) {
			try {
				WorkInProgressHelper.service.undoCheckouts(paramWTArrayList);
				this.logger
						.debug("multi object undo checkout finished successfully.");
			} catch (WTPropertyVetoException localWTPropertyVetoException) {
				throw new WTException(localWTPropertyVetoException,
						getUndoCheckoutFailureMsg());
			}
		} else {
			throw new WTException(getUndoCheckoutFailureMsg());
		}
	}

	private String getUndoCheckoutFailureMsg() {
		return WTMessage.getLocalizedMessage(
				"com.ptc.windchill.cadx.cancelcheckout.cancelCheckoutResource",
				"62", null);
	}

	public String getDisplayName(NmOid paramNmOid) throws WTException {
		String str = "";
		Object localObject;
		if ((paramNmOid.isA(WTPart.class))
				|| (paramNmOid.isA(WTDocument.class))) {
			localObject = (RevisionControlled) ObjectReference
					.newObjectReference(paramNmOid.getOid()).getObject();
			str = ((RevisionControlled) localObject).getName();
		} else if (paramNmOid.isA(SubFolder.class)) {
			localObject = (Folder) ObjectReference.newObjectReference(
					paramNmOid.getOid()).getObject();
			str = ((Folder) localObject).getName();
		} else if (paramNmOid.isA(DefaultWTContainerTemplate.class)) {
			localObject = (DefaultWTContainerTemplate) ObjectReference
					.newObjectReference(paramNmOid.getOid()).getObject();
			str = ((DefaultWTContainerTemplate) localObject).getName();
		}
		return str;
	}

	public void uploadContent(ContentHolder paramContentHolder,
			String paramString1, String paramString2) throws WTException {
		uploadContent(paramContentHolder, paramString1, paramString2,
				ContentRoleType.PRIMARY);
	}

	public void uploadContent(ContentHolder paramContentHolder,
			String paramString1, String paramString2,
			ContentRoleType paramContentRoleType) throws WTException {
		ApplicationData localApplicationData = ApplicationData
				.newApplicationData(paramContentHolder);
		FileInputStream localFileInputStream = null;
		try {
			localApplicationData.setFileName(ContentServerHelper
					.getFileName(paramString1));
			localApplicationData.setUploadedFromPath(paramString1);
			localApplicationData.setRole(paramContentRoleType);
			localFileInputStream = new FileInputStream(paramString2);
			if ((paramContentHolder != null)
					&& ((paramContentHolder instanceof FormatContentHolder))) {
				try {
					paramContentHolder = ContentServerHelper.service
							.updateHolderFormat((FormatContentHolder) paramContentHolder);
					ContentItem localContentItem = ContentHelper
							.getPrimary((FormatContentHolder) paramContentHolder);
					if (localContentItem != null) {
						ContentServerHelper.service.deleteContent(
								paramContentHolder, localContentItem);
					}
				} catch (PropertyVetoException localPropertyVetoException1) {
					throw new WTException(localPropertyVetoException1);
				}
			}
			localApplicationData = ContentServerHelper.service.updateContent(
					paramContentHolder, localApplicationData,
					localFileInputStream);
			try {
				if (localFileInputStream != null) {
					localFileInputStream.close();
				}
			} catch (Exception localException1) {
			}
			if (!(paramContentHolder instanceof FormatContentHolder)) {
				return;
			}
		} catch (WTPropertyVetoException localWTPropertyVetoException) {
			throw new WTException(localWTPropertyVetoException);
		} catch (Exception localException2) {
			if ((localException2 instanceof WTException)) {
				throw ((WTException) localException2);
			}
			throw new WTException(localException2);
		} finally {
			try {
				if (localFileInputStream != null) {
					localFileInputStream.close();
				}
			} catch (Exception localException3) {
			}
		}
		try {
			paramContentHolder = ContentServerHelper.service
					.updateHolderFormat((FormatContentHolder) paramContentHolder);
		} catch (PropertyVetoException localPropertyVetoException2) {
			throw new WTException(localPropertyVetoException2);
		}
	}

	public String getDisplayLocation(NmCommandBean paramNmCommandBean)
			throws WTException {
		boolean bool = SessionServerHelper.manager.setAccessEnforced(false);
		try {
			String str1 = getLocation(paramNmCommandBean);
			String str2;
			if (str1 == null) {
				return "";
			}
			if (str1.lastIndexOf('/') == 0) {
				str1 = str1.substring(0, 1);
			} else {
				str1 = str1.substring(str1.indexOf('/', 1));
			}
			return str1;
		} finally {
			SessionServerHelper.manager.setAccessEnforced(bool);
		}
	}

	public NmChangeModel[] importObjects(NmCommandBean paramNmCommandBean,
			String paramString1, String paramString2) throws WTException {
		if (VERBOSE) {
			System.out.println("=> importObjects: file = " + paramString1
					+ ", temp file = " + paramString2);
		}
		if (paramString2 == null) {
			throw new NmException("com.ptc.netmarkets.model.modelResource",
					"0", null);
		}
		if (paramNmCommandBean.getCompContext().indexOf("listTemplates") < 0) {
			NmOid localObject1 = paramNmCommandBean.getActionOid();
			if (VERBOSE) {
				System.out.println("   ref class = "
						+ ((NmOid) localObject1).getRef().getClass());
			}
			Transaction localObject2 = new Transaction();
			try {
				((Transaction) localObject2).start();
				WTContainerRef localObject3 = paramNmCommandBean
						.getContainerRef();
				final WTContainerRef localObject4 = localObject3;
				Transaction.addTransactionListener(new TransactionListener() {
					public void notifyCommit() {
					}

					public void notifyRollback() {
						TeamExportHolder localTeamExportHolder = (TeamExportHolder) MethodContext
								.getContext().get("TRANSACTION_CREATED_ROLES");
						if ((localTeamExportHolder != null)
								&& (Project2.class
										.isAssignableFrom(localObject4
												.getReferencedClass()))) {
							StandardNmObjectService.this.rollBackMemberList(
									(Project2) localObject4.getObject(),
									localTeamExportHolder);
						}
					}
				});
				ProjectIXUtils.importFromJarFile((WTContainerRef) localObject3,
						((NmOid) localObject1).getRef(), paramString2);

				((Transaction) localObject2).commit();
				localObject2 = null;
			} finally {
				if (localObject2 != null) {
					((Transaction) localObject2).rollback();
				}
			}
			NmChangeModel[] localObject3 = new NmChangeModel[1];
			localObject3[0] = new NmChangeModel();
			localObject3[0].setInvalidateAll(true);
			if (VERBOSE) {
				System.out.println("   importObjects - OUT");
			}
			return localObject3;
		}
		Object localObject1 = new File(paramString2);
		Object localObject2 = ContainerTemplateHelper.service
				.getImportRequest((File) localObject1);
		Object localObject3 = paramNmCommandBean.getContainerRef();
		ContainerTemplateHelper.service.importContainerTemplate(
				(ImportTemplateRequest) localObject2,
				(WTContainerRef) localObject3, null);
		return null;
	}

	public String exportObjects(NmCommandBean paramNmCommandBean)
			throws WTException {
		NmOid localNmOid = paramNmCommandBean.getPrimaryOid();
		Object localObject = (Persistable) localNmOid.getRef();
		if (((localObject instanceof Workable))
				&& (WorkInProgressHelper.isWorkingCopy((Workable) localObject))) {
			localObject = WorkInProgressHelper.service
					.originalCopyOf((Workable) localObject);
		}
		if (VERBOSE) {
			System.out.println("=> exportObjects: "
					+ PersistenceHelper
							.getObjectIdentifier((Persistable) localObject));
		}
		File localFile = StandardIXBService.getSaveFileOnServer();
		WTContainerRef localWTContainerRef = null;
		if (paramNmCommandBean.getSharedContextOid() != null) {
			localWTContainerRef = paramNmCommandBean.getSharedContextOid()
					.getContainerRef();
		} else if (localNmOid.getSharedContainer() != null) {
			localWTContainerRef = localNmOid.getSharedContainer()
					.getContainerRef();
		} else {
			localWTContainerRef = paramNmCommandBean.getContainerRef();
		}
		localFile = ProjectIXUtils.exportToJarFile(localWTContainerRef,
				(Persistable) localObject, localFile);
		if (localFile == null) {
			if (VERBOSE) {
				System.out.println("    exportObjects - OUT, returning null");
			}
			return null;
		}
		URL localURL = NmObjectHelper.constructOutputURL(localFile,
				DEFAULT_ARCHIVE_FILE_NAME);
		if (VERBOSE) {
			System.out.println("   exportObjects - OUT: " + localURL
					+ ", file = " + localFile);
		}
		return localURL.toString();
	}

	public String getDefaultViewString(NmOid paramNmOid, boolean paramBoolean)
			throws WTException {
		Object localObject1;
		Object localObject2;
		Object localObject3;
		Object localObject4;
		Object localObject6;
		Object localObject7;
		if (NmOid.isA(paramNmOid, WfProcess.class)) {
			localObject1 = (WfProcess) paramNmOid.getReferencedIteration();
			localObject2 = (WTUser) SessionHelper.manager.getPrincipal();
			localObject3 = ((WTUser) localObject2).getLocale();
			localObject4 = (ContentHolder) ((WfProcess) localObject1)
					.getTemplate().getObject();
			ApplicationData localApplicationData = WfEngineHelper.service
					.getProcessOverview((WfProcess) localObject1,
							(Locale) localObject3);
			if (localApplicationData == null) {
				localApplicationData = WfDefinerHelper.service
						.getProcessOverview((WfProcessTemplate) localObject4,
								"default");
			}
			if ((localApplicationData != null) && (localObject4 != null)) {
				URL url = localApplicationData
						.getViewContentURL((ContentHolder) localObject4);

				String str = ContentHtmlUtils.getFileDownloadLaunchURL(
						(ContentHolder) localObject4, localApplicationData,
						((URL) url).toString());

				return str;
			}
		} else if ((!NmOid.isA(paramNmOid, WTPart.class))
				&& (!NmOid.isA(paramNmOid, EPMDocument.class))) {
			localObject1 = paramNmOid.getReferencedIteration();
			localObject2 = (ContentHolder) localObject1;
			if (localObject2 == null) {
				throw new NotAuthorizedException(null,
						"com.ptc.netmarkets.model.modelResource", "69", null);
			}
			localObject3 = null;
			localObject4 = (ApplicationData) paramNmOid.getContent();
			try {
				if (localObject3 == null) {
					localObject3 = ContentHelper.service
							.getContents((ContentHolder) localObject2);
				}
			} catch (PropertyVetoException localPropertyVetoException) {
				throw new WTException(localPropertyVetoException);
			}
			Object localObject5;
			if (localObject4 == null) {
				localObject5 = null;
				if ((localObject1 instanceof WTDocument)) {
					localObject5 = ContentHelper
							.getPrimary((FormatContentHolder) localObject3);
				} else {
					localObject6 = ContentHelper
							.getApplicationData((ContentHolder) localObject3);
					if ((localObject6 != null)
							&& (((Vector) localObject6).size() > 0)) {
						localObject5 = (ContentItem) ((Vector) localObject6)
								.get(0);
					}
				}
				if ((localObject5 instanceof ApplicationData)) {
					localObject4 = (ApplicationData) localObject5;
					paramNmOid.setContent(localObject4);
				}
			}
			if ((localObject4 != null) && (localObject3 != null)) {
				localObject5 = ((ApplicationData) localObject4)
						.getViewContentURL((ContentHolder) localObject3);

				localObject6 = null;
				try {
					localObject7 = RedirectDownload.getPreferredURL(
							(ApplicationData) localObject4,
							(ContentHolder) localObject3);
					localObject6 = localObject7 != null ? ((URL) localObject7)
							.toString() : "";
				} catch (IOException localIOException) {
					localIOException.printStackTrace();
				}
				Object localObject8 = null;
				if ((((ApplicationData) localObject4).getFileName() != null)
						&& ((paramBoolean)
								|| (((ApplicationData) localObject4)
										.getFileName().toLowerCase()
										.indexOf(".ed") >= 0) || (((ApplicationData) localObject4)
								.getFileName().toLowerCase().indexOf(".ol") >= 0))) {
					ReferenceFactory localReferenceFactory = new ReferenceFactory();
					WTReference localWTReference = localReferenceFactory
							.getReference((Persistable) localObject1);
					String[] arrayOfString = this.VIS_HELPER
							.getDefaultVisualizationData(localWTReference,
									new Boolean(false),
									SessionHelper.getLocale());
					localObject8 = arrayOfString[17];
				} else {
					localObject8 = localObject6;
				}
				return (String) localObject8;
			}
		} else if ((paramNmOid.isA(WTPart.class))
				|| (paramNmOid.isA(EPMDocument.class))) {
			localObject1 = ObjectReference.newObjectReference(paramNmOid
					.getOid());
			localObject2 = this.VIS_HELPER
					.getPartStructurePVHTML(((ObjectReference) localObject1)
							.toString());
			return (String) localObject2;
		}
		return null;
	}

	public String getLocation(NmCommandBean paramNmCommandBean)
			throws WTException {
		boolean bool = SessionServerHelper.manager.isAccessEnforced();
		String str = null;

		NmOid localNmOid = getLocationOid(paramNmCommandBean);
		if (localNmOid != null) {
			ObjectIdentifier localObjectIdentifier = localNmOid.getOid();
			Persistable localPersistable = PersistenceHelper.manager
					.refresh(localObjectIdentifier);
			try {
				SessionServerHelper.manager.setAccessEnforced(false);
				if ((localPersistable instanceof Folder)) {
					str = FolderHelper.getFolderPath((Folder) localPersistable);
				} else if ((localPersistable instanceof CabinetBased)) {
					str = FolderHelper
							.getLocation((CabinetBased) localPersistable);
				} else {
					WTContainer localWTContainer = paramNmCommandBean
							.getContainer();
					if (localWTContainer != null) {
						str = localWTContainer.getDefaultCabinet()
								.getFolderPath();
					}
				}
			} finally {
				SessionServerHelper.manager.setAccessEnforced(bool);
			}
		}
		return str;
	}

	public Vector getLifeCycleStates(NmOid paramNmOid) throws WTException {
		WTObject localWTObject = null;
		boolean bool = false;
		try {
			bool = SessionServerHelper.manager.setAccessEnforced(false);
			localWTObject = (WTObject) ObjectReference.newObjectReference(
					paramNmOid.getOid()).getObject();
		} finally {
			SessionServerHelper.manager.setAccessEnforced(bool);
		}
		return getLifeCycleStates(localWTObject);
	}

	private Vector getLifeCycleStates(WTObject paramWTObject)
			throws WTException {
		if ((paramWTObject instanceof LifeCycleManaged)) {
			SessionContext localSessionContext = SessionContext.newContext();
			try {
				SessionHelper.manager.setAdministrator();
				LifeCycleTemplate localLifeCycleTemplate = (LifeCycleTemplate) ((LifeCycleManaged) paramWTObject)
						.getLifeCycleTemplate().getObject();
				return LifeCycleHelper.service
						.getPhaseTemplates(localLifeCycleTemplate);
			} finally {
				SessionContext.setContext(localSessionContext);
			}
		}
		return null;
	}

	public wt.lifecycle.State getCurrentState(NmOid paramNmOid)
			throws WTException {
		WTObject localWTObject = null;
		boolean bool = false;
		try {
			bool = SessionServerHelper.manager.setAccessEnforced(false);
			localWTObject = (WTObject) ObjectReference.newObjectReference(
					paramNmOid.getOid()).getObject();
		} finally {
			SessionServerHelper.manager.setAccessEnforced(bool);
		}
		return getCurrentState(localWTObject);
	}

	public wt.lifecycle.State getCurrentState(WTObject paramWTObject)
			throws WTException {
		if ((paramWTObject instanceof LifeCycleManaged)) {
			SessionContext localSessionContext = SessionContext.newContext();
			try {
				SessionHelper.manager.setAdministrator();
				return ((LifeCycleManaged) paramWTObject).getLifeCycleState();
			} finally {
				SessionContext.setContext(localSessionContext);
			}
		}
		return null;
	}

	public NmHTMLTable report(NmCommandBean paramNmCommandBean,
			String paramString1, String paramString2, String paramString3,
			String paramString4) throws WTException {
		if (VERBOSE) {
			System.out.println("\n StandardNmObjectService.report() : IN");
		}
		NmDefaultHTMLTable localNmDefaultHTMLTable = initializeReportTable();
		if (paramString4 == null) {
			return localNmDefaultHTMLTable;
		}
		WTUser localWTUser = (WTUser) SessionHelper.manager.getPrincipal();
		ObjectIdentifier localObjectIdentifier = PersistenceHelper
				.getObjectIdentifier(localWTUser);

		QueryResult localQueryResult = null;
		if (VERBOSE) {
			System.out
					.println("\n StandardNmObjectService.report() : objectType == "
							+ paramString1);
		}
		if (VERBOSE) {
			System.out
					.println("\n StandardNmObjectService.report() : status =="
							+ paramString2);
		}
		try {
			if (paramString2.equals("overdue")) {
				localQueryResult = getOverDueObjects(paramString1,
						localObjectIdentifier);
			} else if (paramString2.equals("completed")) {
				localQueryResult = getCompletedObjects(paramString1,
						localObjectIdentifier);
			} else if (paramString2.equals("uncompleted")) {
				localQueryResult = getInCompleteObjects(paramString1,
						localObjectIdentifier);
			}
		} catch (WTPropertyVetoException localWTPropertyVetoException) {
			localWTPropertyVetoException.printStackTrace();
		}
		populateReportsTable(localNmDefaultHTMLTable, localQueryResult,
				paramString3);
		return localNmDefaultHTMLTable;
	}

	private FormResult checkOut(NmOid paramNmOid) throws WTException {
		Persistable localPersistable = (Persistable) paramNmOid.getRefObject();
		FormResult localFormResult = new FormResult();
		localFormResult.setNextAction(FormResultAction.REFRESH_CURRENT_PAGE);
		localFormResult.setStatus(FormProcessingStatus.SUCCESS);
		Object localObject;
		if ((localPersistable instanceof Workable)) {
			localObject = WorkInProgressHelper.service.getCheckoutFolder();
			try {
				WorkInProgressHelper.service.checkout(
						(Workable) localPersistable, (Folder) localObject,
						null, true);
				localFormResult.addDynamicRefreshInfo(new DynamicRefreshInfo(
						paramNmOid, paramNmOid, "U"));
			} catch (WorkInProgressException localWorkInProgressException) {
				throw new NmException(localWorkInProgressException);
			} catch (PersistenceException localPersistenceException) {
				throw new NmException(localPersistenceException);
			} catch (WTException localWTException) {
				throw new NmException(localWTException);
			}
		}
		if (((localPersistable instanceof WTDocument))
				&& (((WTDocument) localPersistable).isTemplated())) {
			localObject = (WTDocument) WorkInProgressHelper.service
					.workingCopyOf((Workable) localPersistable);
			TemplateInfo localTemplateInfo = ((WTDocument) localObject)
					.getTemplate();
			if (localTemplateInfo != null) {
				localTemplateInfo.setEnabled(false);
				PersistenceServerHelper.manager
						.update((Persistable) localObject);
			}
		}
		return localFormResult;
	}

	public NmHTMLTable getClipboardContents(NmCommandBean paramNmCommandBean)
			throws WTException {
		Locale localLocale = SessionHelper.getLocale();
		NmHTMLActionModel localNmHTMLActionModel = NmActionServiceHelper.service
				.getActionModel("clipboard list", null, localLocale);
		NmDefaultHTMLTable localNmDefaultHTMLTable = new NmDefaultHTMLTable();

		localNmDefaultHTMLTable.addColumn(WTMessage.getLocalizedMessage(
				"com.ptc.netmarkets.model.modelResource", "6", null,
				localLocale));
		localNmDefaultHTMLTable.addColumn(WTMessage.getLocalizedMessage(
				"com.ptc.netmarkets.model.modelResource", "7", null,
				localLocale));

		ArrayList localArrayList = paramNmCommandBean.getClipped();
		int i = 0;
		if ((localArrayList != null) && (localArrayList.size() > 0)) {
			for (int j = 0; j < localArrayList.size(); j++) {
				Object localObject = localArrayList.get(j);
				NmOid localNmOid = NmCommandBean.getOidFromObject(localObject);

				NmNamedObject localNmNamedObject = getNmNamedObjectFromOid(
						localNmOid, localObject, localLocale);
				if (localNmNamedObject != null) {
					localNmOid = localNmNamedObject.getOid();
					String str = getTypeFromOid(localNmOid, localObject,
							localLocale);

					localNmDefaultHTMLTable.addObject(i, localNmNamedObject);
					localNmDefaultHTMLTable.addCellValue(i, 0,
							localNmNamedObject.getName());
					localNmDefaultHTMLTable.addCellValue(i, 1, str);

					i++;
				}
			}
		}
		localNmDefaultHTMLTable.setActionModel(localNmHTMLActionModel);
		localNmDefaultHTMLTable.setRowSelectable(true);
		localNmDefaultHTMLTable.setName(WTMessage.getLocalizedMessage(
				"com.ptc.netmarkets.model.modelResource", "19", null,
				localLocale));

		return localNmDefaultHTMLTable;
	}

	public NmPasteInfo getPasteInfo(NmCommandBean paramNmCommandBean)
			throws WTException {
		NmPasteInfo localNmPasteInfo = new NmPasteInfo();
		ArrayList localArrayList1 = paramNmCommandBean.getClipped();
		if ((localArrayList1 == null) || (localArrayList1.size() == 0)) {
			return localNmPasteInfo;
		}
		Locale localLocale = SessionHelper.getLocale();
		ArrayList localArrayList2 = new ArrayList();
		ArrayList localArrayList3 = new ArrayList();

		boolean bool1 = false;
		boolean bool2 = false;
		boolean bool3 = true;
		int i = 0;
		boolean bool4 = true;
		boolean bool5 = true;
		boolean bool6 = true;
		boolean bool7 = false;
		boolean bool8 = true;
		boolean bool9 = getTargetForGetPasteInfo(paramNmCommandBean,
				localNmPasteInfo, localArrayList3);
		Object localObject1 = null;
		int j = 0;
		boolean bool10 = false;
		for (Object localObject2 : localArrayList1) {
			NmOid localNmOid = null;
			try {
				localNmOid = getOidFromClippedObj(localObject2,
						localNmPasteInfo, localLocale);
				j = 1;
			} catch (NmException localNmException) {
				localObject1 = localNmException;
			}
			continue;
		}
		if (j == 0) {
			throw (NmException) localObject1;
		}
		if (bool5) {
			makeWorkpkgOnly(localNmPasteInfo);
			return localNmPasteInfo;
		}
		checkForBkmrkOnly(localNmPasteInfo, bool4);
		if (localArrayList2.size() > 1) {
			localNmPasteInfo.setMultipleSourceContainers(true);
		} else if (localArrayList2.size() > 0) {
			i = !((WTContainerRef) localArrayList2.get(0))
					.equals(localArrayList3.get(0)) ? 1 : 0;
		}
		if ((bool1) && (i == 0)) {
			localNmPasteInfo.setDoCopy(true);
		}
		if (!bool10) {
			checkForSandbox(localNmPasteInfo, localArrayList2, bool4, bool7);
		}
		if ((!localNmPasteInfo.isMultipleSourceContainers())
				&& (localNmPasteInfo.isProjectDestination())
				&& ((localArrayList2.size() == 0) || (((WTContainerRef) localArrayList2
						.get(0)).equals(localArrayList3.get(0))))) {
			localNmPasteInfo.setDoCopy(true);
		}
		if ((bool9) && (localArrayList3.size() <= 1)) {
			localNmPasteInfo.setDoCopy(true);
		}
		if ((!localNmPasteInfo.isSandbox())
				&& (paramNmCommandBean.isClippedToCut())) {
			localNmPasteInfo.setDoCut(true);
		}
		if ((paramNmCommandBean.isClippedToCut()) && (!bool10)) {
			checkClippedCut(localNmPasteInfo, localArrayList2, localArrayList3);
		}
		if ((!paramNmCommandBean.isClippedToCut())
				&& (!localNmPasteInfo.isSandbox())) {
			localNmPasteInfo.setAllowCopy(true);
		}
		if ((!localNmPasteInfo.isSandbox()) && (!bool2)) {
			localNmPasteInfo.setDoCopy(true);
		}
		if ((!localNmPasteInfo.isDoMove())
				&& (!localNmPasteInfo.isSandbox())
				&& (bool3)
				&& ((localNmPasteInfo.isMultipleSourceContainers()) || (i != 0))) {
			throw new NmException(WTMessage.getLocalizedMessage(
					"com.ptc.netmarkets.model.modelResource", "50", null,
					localLocale));
		}
		if (bool6) {
			throw new WTException("com.ptc.netmarkets.object.objectResource",
					"313", null);
		}
		if ((localNmPasteInfo.isDoCopy()) && (!localNmPasteInfo.isDoCut())
				&& (bool8)) {
			throw new WTException("com.ptc.netmarkets.object.objectResource",
					"313", null);
		}
		return localNmPasteInfo;
	}

	private void checkForSandbox(NmPasteInfo paramNmPasteInfo,
			List<WTContainerRef> paramList, boolean paramBoolean1,
			boolean paramBoolean2) {
		for (WTContainerRef localWTContainerRef : paramList) {
			if ((!Project2.class.isAssignableFrom(localWTContainerRef
					.getReferencedClass()))
					&& (!OrgContainer.class
							.isAssignableFrom(localWTContainerRef
									.getReferencedClass()))) {
				if ((paramNmPasteInfo.isProjectDestination())
						&& (!paramBoolean1) && (!paramBoolean2)) {
					paramNmPasteInfo.setSandbox(true);
				}
				if (WTContainerHelper.isClassicRef(localWTContainerRef)) {
					paramNmPasteInfo.setProjectDestination(false);
				}
			}
		}
	}

	private boolean getTargetForGetPasteInfo(NmCommandBean paramNmCommandBean,
			NmPasteInfo paramNmPasteInfo, List<WTContainerRef> paramList)
			throws WTException {
		List localList = getSelectedTargetOids(paramNmCommandBean);

		boolean bool = false;
		for (int i = 0; i < localList.size(); i++) {
			Object localObject = localList.get(i);
			NmOid localNmOid = NmCommandBean.getOidFromObject(localObject);
			if ((!(localNmOid instanceof NmSimpleOid)) && (localNmOid != null)) {
				if ((!WTContainer.class.isAssignableFrom(localNmOid
						.getReferencedClass()))
						&& (!Folder.class.isAssignableFrom(localNmOid
								.getReferencedClass()))) {
					throw new NmException(WTMessage.getLocalizedMessage(
							"com.ptc.netmarkets.model.modelResource", "68",
							null, SessionHelper.getLocale()));
				}
				WTContainerRef localWTContainerRef = localNmOid
						.getContainerRef();
				if (!paramList.contains(localWTContainerRef)) {
					if ((!Project2.class.isAssignableFrom(localWTContainerRef
							.getReferencedClass()))
							&& (!OrgContainer.class
									.isAssignableFrom(localWTContainerRef
											.getReferencedClass()))) {
						paramNmPasteInfo.setProjectDestination(false);
					}
					if ((OrgContainer.class
							.isAssignableFrom(localWTContainerRef
									.getReferencedClass()))
							|| (ExchangeContainer.class
									.isAssignableFrom(localWTContainerRef
											.getReferencedClass()))) {
						paramNmPasteInfo.setAllowShare(false);
						bool = true;
					}
					paramList.add(localWTContainerRef);
				}
			}
		}
		return bool;
	}

	private List getSelectedTargetOids(NmCommandBean paramNmCommandBean)
			throws WTException {
		ArrayList localArrayList = paramNmCommandBean.getSelected();
		if (localArrayList.size() <= 0) {
			NmOid localNmOid = paramNmCommandBean.getPageOid();
			if (localNmOid != null) {
				localArrayList.add(localNmOid);
			} else {
				throw new WTException("com.ptc.netmarkets.paste.pasteResource",
						"28", null);
			}
		}
		return localArrayList;
	}

	private NmOid getOidFromClippedObj(Object paramObject,
			NmPasteInfo paramNmPasteInfo, Locale paramLocale)
			throws WTException {
		Object localObject = null;
		if ((paramObject instanceof NmAnyURL)) {
			paramNmPasteInfo.setAllowShare(false);
			paramNmPasteInfo.setAllowCopy(false);
			paramNmPasteInfo.setDoCreateLinks(true);

			localObject = new NmSimpleOid();
			((NmSimpleOid) localObject)
					.setInternalName(((NmAnyURL) paramObject).getUrl());
		} else {
			localObject = NmCommandBean.getOidFromObject(paramObject);
			checkIfObjDeleted(paramLocale, (NmOid) localObject);
		}
		return (NmOid) localObject;
	}

	private void checkForBkmrkOnly(NmPasteInfo paramNmPasteInfo,
			boolean paramBoolean) {
		if (paramBoolean) {
			paramNmPasteInfo.setSandbox(false);
			paramNmPasteInfo.setDoCreateLinks(true);
		}
	}

	private boolean isManagedCollection(boolean paramBoolean,
			Persistable paramPersistable) throws WTException {
		if ((!(paramPersistable instanceof ManagedCollection))
				&& (paramBoolean)) {
			return false;
		}
		return paramBoolean;
	}

	private boolean isWorkPkg(boolean paramBoolean, Persistable paramPersistable)
			throws WTException {
		if ((paramBoolean)
				&& (!WorkPackageClassProxy.isWorkPackage(paramPersistable))) {
			paramBoolean = false;
		}
		if (WorkPackageClassProxy.isWorkPackageItem(paramPersistable)) {
			throw new WTException("com.ptc.netmarkets.object.objectResource",
					"313", null);
		}
		return paramBoolean;
	}

	private boolean isDeliveryRecord(boolean paramBoolean,
			Persistable paramPersistable) throws WTException {
		if ((paramBoolean)
				&& (!WorkPackageClassProxy.isDeliveryRecord(paramPersistable))) {
			paramBoolean = false;
		}
		return paramBoolean;
	}

	private boolean isBookMrk(boolean paramBoolean, Persistable paramPersistable) {
		if (((paramBoolean) && (!(paramPersistable instanceof Bookmark)))
				|| ((paramPersistable instanceof ImportedBookmark))) {
			paramBoolean = false;
		}
		return paramBoolean;
	}

	private boolean isObjectHidden(Persistable paramPersistable) {
		return SandboxHelper.isObjectHidden(paramPersistable);
	}

	private boolean isSharable(boolean paramBoolean,
			Persistable paramPersistable) throws WTException {
		if ((!paramBoolean)
				&& (DataSharingHelper.service.isShareable(paramPersistable))) {
			paramBoolean = true;
		}
		return paramBoolean;
	}

	private boolean isEPM(boolean paramBoolean, NmOid paramNmOid) {
		if ((paramBoolean) && (!NmOid.isA(paramNmOid, EPMDocument.class))) {
			paramBoolean = false;
		}
		return paramBoolean;
	}

	private boolean containsProjectObject(List<WTContainerRef> paramList,
			boolean paramBoolean, NmOid paramNmOid) throws WTException {
		WTContainerRef localWTContainerRef = getContainerRefForClippedObj(paramNmOid);
		if ((localWTContainerRef != null)
				&& (!paramList.contains(localWTContainerRef))) {
			paramList.add(localWTContainerRef);
		}
		if ((!paramBoolean)
				&& (localWTContainerRef != null)
				&& (Project2.class.isAssignableFrom(localWTContainerRef
						.getReferencedClass()))) {
			paramBoolean = true;
		}
		return paramBoolean;
	}

	private void checkClippedCut(NmPasteInfo paramNmPasteInfo,
			List<WTContainerRef> paramList1, List<WTContainerRef> paramList2)
			throws NmException, WTException {
		if (paramList1.size() == 0) {
			throw new NmException(WTMessage.getLocalizedMessage(
					"com.ptc.netmarkets.model.modelResource", "69", null,
					SessionHelper.getLocale()));
		}
		if ((paramNmPasteInfo.isMultipleSourceContainers())
				|| (!((WTContainerRef) paramList1.get(0)).equals(paramList2
						.get(0)))) {
			if ((isPDMLinkContainer(((WTContainerRef) paramList1.get(0))
					.getReferencedContainer()))
					&& (isPDMLinkContainer(((WTContainerRef) paramList2.get(0))
							.getReferencedContainer()))) {
				paramNmPasteInfo.setDoMove(true);
			} else {
				Object[] arrayOfObject = { IdentityFactory.getDisplayType(
						((WTContainerRef) paramList1.get(0)).getObject())
						.getLocalizedMessage(SessionHelper.getLocale()) };

				throw new NmException(WTMessage.getLocalizedMessage(
						"com.ptc.netmarkets.model.modelResource", "48",
						arrayOfObject, SessionHelper.getLocale()));
			}
		}
	}

	private void checkIfCopyShare(boolean paramBoolean,
			Persistable paramPersistable, List<WTContainerRef> paramList)
			throws WTException {
		if (paramBoolean) {
		}
	}

	private WTContainerRef getContainerRefForClippedObj(NmOid paramNmOid)
			throws WTException {
		WTContainerRef localWTContainerRef = null;
		if (paramNmOid.getSharedContainer() != null) {
			localWTContainerRef = paramNmOid.getSharedContainer()
					.getContainerRef();
		}
		if (localWTContainerRef == null) {
			localWTContainerRef = paramNmOid.getContainerRef();
		}
		return localWTContainerRef;
	}

	private void checkIfObjDeleted(Locale paramLocale, NmOid paramNmOid)
			throws WTException, NmException {
		Object localObject = paramNmOid.getRef();
		if (localObject == null) {
			throw new NmException(WTMessage.getLocalizedMessage(
					"com.ptc.netmarkets.paste.pasteResource", "93", null,
					paramLocale));
		}
	}

	private void makeWorkpkgOnly(NmPasteInfo paramNmPasteInfo) {
		paramNmPasteInfo.setDoCopy(true);
		paramNmPasteInfo.setSandbox(false);
	}

	public ArrayList getPSPart(String paramString1, String paramString2,
			String paramString3, ArrayList paramArrayList) throws WTException {
		return (ArrayList) UtilProcessorService.getPartUfild(paramString1,
				paramString2, paramString3, paramArrayList);
	}

	public NmSoftAttribute getSoftAttribute(NmCommandBean paramNmCommandBean,
			NmLinkBean paramNmLinkBean) throws WTException {
		HashMap localHashMap = paramNmCommandBean.getMap();

		String str1 = (String) localHashMap.get("identifier");
		String str2 = (String) localHashMap.get("create");
		String str3 = (String) localHashMap.get("update");
		String str4 = (String) localHashMap.get("exclude");
		String str5 = (String) localHashMap.get("template");
		Locale localLocale = SessionHelper.getLocale();

		boolean bool1 = (str2 != null) && (str2.equalsIgnoreCase("true"));
		int i = (str3 != null) && (str3.equalsIgnoreCase("true")) ? 1 : 0;
		boolean bool2 = (str4 != null) && (str4.equalsIgnoreCase("true"));

		ArrayList localArrayList1 = new ArrayList();
		ArrayList localArrayList2 = new ArrayList();
		ArrayList localArrayList3 = new ArrayList();
		StringBuffer localStringBuffer = new StringBuffer();
		ArrayList localArrayList4 = new ArrayList();
		ArrayList localArrayList5 = new ArrayList();
		ArrayList localArrayList6 = new ArrayList();

		ObjectIdentifier localObjectIdentifier = null;
		Object localObject1 = str1;
		TypeIdentifier localTypeIdentifier = TypeHelper.getTypeIdentifier(str1);
		if ((paramNmCommandBean.getPageOid() != null) && (i != 0)) {
			if (paramNmCommandBean.getActionOid() == null) {
				if ((str5 != null) && (str5.length() > 0)
						&& (!str5.equals("null"))) {
					localObjectIdentifier = new ObjectIdentifier(str5);
				} else {
					localObjectIdentifier = paramNmCommandBean.getPageOid()
							.getOid();
				}
			} else if ((str5 != null) && (str5.length() > 0)
					&& (!str5.equals("null"))) {
				localObjectIdentifier = new ObjectIdentifier(str5);
			} else {
				localObjectIdentifier = paramNmCommandBean.getActionOid()
						.getOid();
			}
			Object localObject2 = ObjectReference
					.newObjectReference(localObjectIdentifier);
			Object localObject3 = (Typed) ((ObjectReference) localObject2)
					.getObject();

			str1 = TypedUtility.getExternalTypeIdentifier((Typed) localObject3);
			localObject1 = ((ObjectReference) localObject2).getObject();
			localTypeIdentifier = TypeIdentifierUtility
					.getTypeIdentifier(localObject1);
		}
		Object localObject2 = TypeHelper.getBaseType(localTypeIdentifier);

		Object localObject3 = TypeHelper
				.getCustomAttributes(
						localObject1,
						(String) localObject2,
						i == 0,
						"ALL_SOFT_ATTRIBUTES,ALL_SOFT_SCHEMA_ATTRIBUTES_FOR_INPUT_TYPE",
						null, false, true, localStringBuffer, localLocale);
		if (localObject3 == null) {
			localObject3 = new ArrayList();
		}
		String str6 = (String) paramNmCommandBean.getMap().get(
				"softattributeidentifier");
		ArrayList localArrayList7 = null;
		ArrayList localArrayList8 = new ArrayList();
		if ((str6 != null) && (str6.length() > 0) && (!str6.equals("null"))) {
			localArrayList7 = parseEncodedField(str6, ";;;zzz");
			localArrayList8 = parseEncodedField((String) paramNmCommandBean
					.getMap().get("softattributevalue"), ";;;zzz");
			if (localArrayList8.isEmpty()) {
				localArrayList8.add("");
			}
		}
		for (int j = 0; j < ((ArrayList) localObject3).size(); j++) {
			AttributeData localAttributeData = (AttributeData) ((ArrayList) localObject3)
					.get(j);
			AttributeIdentifier localAttributeIdentifier = localAttributeData
					.getAttributeId();
			AttributeTypeSummary localAttributeTypeSummary = localAttributeData
					.getAttributeTypeSummary();
			com.ptc.core.meta.container.common.State localState = localAttributeData
					.getAttributeState();

			UIValidationStatus localUIValidationStatus = getAttributeVisibilityStatus(localAttributeIdentifier);
			if ((!localUIValidationStatus
					.equals(UIValidationStatus.ATTR_HIDDEN))
					&& (!localAttributeTypeSummary.isHidden())) {
				Object localObject4 = null;

				Object localObject5 = localAttributeData.getAttributeValue();
				String str7 = null;
				if ((str6 != null) && (!str6.equals("null"))) {
					int k = 0;
					String str9 = localAttributeIdentifier.toExternalForm();
					for (int m = 0; (m < localArrayList7.size()) && (k == 0); m++) {
						String str10 = (String) localArrayList7.get(m);
						int n = str9.indexOf(str10);
						k = (n != -1)
								&& (str9.substring(n + str10.length(), n
										+ str10.length() + 1).equals("~")) ? 1
								: 0;
						if (k != 0) {
							localObject4 = localArrayList8.get(m);
						}
					}
				}
				if ((localUIValidationStatus
						.equals(UIValidationStatus.ATTR_HIDDEN_VALUE))
						|| (localAttributeTypeSummary.isValueHidden())) {
					((DefaultAttributeTypeSummary) localAttributeTypeSummary)
							.constrainEditability();
					((DefaultAttributeTypeSummary) localAttributeTypeSummary)
							.constrainValueVisibility();
					str7 = WTMessage.getLocalizedMessage(
							"com.ptc.core.ui.componentRB",
							"HIDDEN_DISPLAY_STRING", null,
							SessionHelper.getLocale());
				} else {
					str7 = WizardProcessor.getCustomAttributeInputField(
							localAttributeIdentifier,
							localAttributeTypeSummary, localObject5,
							localObject4, localState, localLocale);
				}
				if (str7 != null) {
					String str8 = localAttributeTypeSummary.isRequired() ? "* "
							: "";
					localArrayList2.add(localAttributeTypeSummary.getLabel());
					localArrayList3
							.add(localAttributeTypeSummary.getDataType());
					localArrayList1.add(localAttributeIdentifier
							.toExternalForm());
					localArrayList4.add(str8
							+ localAttributeTypeSummary.getLabel());
					localArrayList6.add(str7);
					localArrayList5.add(localObject4);
				}
			}
		}
		NmSoftAttribute localNmSoftAttribute = new NmSoftAttribute();
		try {
			localNmSoftAttribute.setType(str1);
			localNmSoftAttribute.setCreate(bool1);
			localNmSoftAttribute.setExcludeImmutable(bool2);
			localNmSoftAttribute.setLocale(localLocale);
			localNmSoftAttribute.setLabels(localArrayList4);
			localNmSoftAttribute.setInputFields(localArrayList6);
			localNmSoftAttribute.setAttributeIdentifiers(localArrayList1);
			localNmSoftAttribute.setDataTypes(localArrayList3);
			localNmSoftAttribute.setLocalizedDisplayNames(localArrayList2);
			localNmSoftAttribute.setValues(localArrayList5);
		} catch (WTPropertyVetoException localWTPropertyVetoException) {
			localWTPropertyVetoException.printStackTrace();
		}
		return localNmSoftAttribute;
	}

	public ArrayList getTypes(String paramString, NmOid paramNmOid1,
			NmOid paramNmOid2) throws WTException {
		Locale localLocale = SessionHelper.getLocale();
		ArrayList localArrayList = new ArrayList();

		TypeIdentifier localTypeIdentifier = TypeHelper
				.getTypeIdentifier(paramString);

		boolean bool = SessionServerHelper.manager.isAccessEnforced();
		if (paramNmOid1.isA(Project2.class)) {
		}
		try {
			AccessControlServerHelper.disableNotAuthorizedAudit();
			if (paramNmOid1 == null) {
				return localArrayList;
			}
			Object localObject1 = null;
			WTContainer localWTContainer = null;
			int i = 0;
			try {
				localWTContainer = paramNmOid1.getContainer();
			} catch (Exception localException) {
				i = 1;
				bool = SessionServerHelper.manager.setAccessEnforced(false);
				try {
					localWTContainer = paramNmOid1.getContainer();
				} finally {
					bool = SessionServerHelper.manager.setAccessEnforced(true);
				}
			}
			if (i != 0) {
				System.out.println("Threw exception");
				bool = SessionServerHelper.manager.setAccessEnforced(false);
			}
			AdminDomainRef localAdminDomainRef = null;
			Object localObject3;
			if ((localWTContainer instanceof OrgContainer)) {
				localObject3 = (OrgContainer) localWTContainer;
				localAdminDomainRef = WTContainerHelper.service
						.getBasePublicDomain((OrgContainer) localObject3,
								Class.forName(paramString));
			}
			Persistable localPersistable;
			if (paramNmOid2 != null) {
				localObject3 = paramNmOid2.getOid();
				if (VERBOSE) {
					System.out.println("In a folder, getting the new domain.");
				}
				localPersistable = new ReferenceFactory().getReference(
						((ObjectIdentifier) localObject3).getStringValue())
						.getObject();
				if ((localPersistable instanceof SubFolder)) {
					localAdminDomainRef = ((SubFolder) localPersistable)
							.getDomainRef();
				}
				if ((localPersistable instanceof Cabinet)) {
					localAdminDomainRef = ((Cabinet) localPersistable)
							.getDomainRef();
				}
			}
			if (VERBOSE) {
				System.out.println("Getting creatable types for WTContainer.");
				System.out.println("thisCont: " + localWTContainer);
				System.out.println("baseType: " + localTypeIdentifier);
				System.out.println("domainRef: " + localAdminDomainRef);
			}
			TypeIdentifier[] type = TypeIdentifierSelectionHelper
					.sortTypesAlphabeticallyByName(paramString,
							localWTContainer, localAdminDomainRef, localLocale,
							null);

			SessionServerHelper.manager.setAccessEnforced(false);
			if (type == null) {
				return localArrayList;
			}
			for (int j = 0; j < type.length; j++) {
				localPersistable = (Persistable) type[j];
				String[] arrayOfString = new String[2];
				arrayOfString[0] = localPersistable.toString();
				arrayOfString[1] = CoreMetaUtility.getLocalizedTypeString(
						(TypeIdentifier) localPersistable, localLocale);
				localArrayList.add(arrayOfString);
			}
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new WTException(localClassNotFoundException);
		} finally {
			SessionServerHelper.manager.setAccessEnforced(bool);
			AccessControlServerHelper.reenableNotAuthorizedAudit();
		}
		return localArrayList;
	}

	public NmChangeModel[] removeShare(NmCommandBean paramNmCommandBean,
			NmOid paramNmOid) throws WTException {
		if (this.deleteLogger.isInfoEnabled()) {
			this.deleteLogger.info("=> removeShare: "
					+ paramNmCommandBean.getSharedContextOid());
		}
		NmOid localNmOid = paramNmCommandBean.getActionOid();
		Persistable localPersistable = (Persistable) localNmOid.getRef();
		WTContainerRef localWTContainerRef = getContainerRef(
				paramNmCommandBean, paramNmOid);
		if (this.deleteLogger.isInfoEnabled()) {
			this.deleteLogger.info("   cref = " + localWTContainerRef);
		}
		QueryResult localQueryResult = DataSharingHelper.service.getObjectMaps(
				localPersistable, localWTContainerRef, 0);
		SharedContainerMap localSharedContainerMap = null;
		if (localQueryResult.hasMoreElements()) {
			localSharedContainerMap = (SharedContainerMap) localQueryResult
					.nextElement();
			Object localObject = new ArrayList();
			((ArrayList) localObject).add(localSharedContainerMap);
			paramNmCommandBean.setSelected((ArrayList) localObject);

			checkValidShareMapForDelete(localSharedContainerMap);
			list__delete(paramNmCommandBean, null, true);
		}
		NmChangeModel[] localObject = new NmChangeModel[1];
		localObject[0] = new NmChangeModel();
		localObject[0].setType(2);
		localObject[0].put(localNmOid, null, null);
		if (this.deleteLogger.isInfoEnabled()) {
			this.deleteLogger.info("   removeShare - OUT");
		}
		return localObject;
	}

	public ArrayList getObjectViewObjects(NmOid paramNmOid1, NmOid paramNmOid2)
			throws WTException {
		ArrayList localArrayList = new ArrayList();
		boolean bool = SessionServerHelper.manager.setAccessEnforced(false);
		try {
			Persistable localPersistable = (Persistable) paramNmOid1.getRef();
			NmCommandBean.setTypes(localPersistable, paramNmOid1);
			WTContainerRef localWTContainerRef = paramNmOid1.getContainerRef();

			NmCommandBean.setTypes(localPersistable, paramNmOid1);
			NmAction localNmAction = new NmAction();
			localNmAction.setType(paramNmOid1.getType());
			localNmAction.setAction("view");
			localArrayList.add(localNmAction);
			localArrayList.add(paramNmOid1);
			localArrayList.add(localPersistable);
			localArrayList.add(localWTContainerRef);
		} finally {
			SessionServerHelper.manager.setAccessEnforced(bool);
		}
		return localArrayList;
	}

	public NmSoftAttribute getIBAsForObject(NmOid paramNmOid)
			throws WTException {
		Locale localLocale = SessionHelper.getLocale();

		Object localObject1 = paramNmOid.getReferencedIteration();

		StringBuffer localStringBuffer = new StringBuffer();

		TypeIdentifier localTypeIdentifier = TypeIdentifierUtility
				.getTypeIdentifier(localObject1);
		String str = TypeHelper.getBaseType(localTypeIdentifier);
		ArrayList localArrayList1 = TypeHelper
				.getCustomAttributes(
						localObject1,
						str,
						false,
						"ALL_SOFT_ATTRIBUTES,ALL_SOFT_SCHEMA_ATTRIBUTES_FOR_INPUT_TYPE",
						null, false, true, localStringBuffer, localLocale);
		if (localArrayList1 == null) {
			localArrayList1 = new ArrayList();
		}
		ArrayList localArrayList2 = new ArrayList();
		ArrayList localArrayList3 = new ArrayList();
		ArrayList localArrayList4 = new ArrayList();
		ArrayList localArrayList5 = new ArrayList();

		Iterator localIterator = localArrayList1.iterator();
		while (localIterator.hasNext()) {
			AttributeData localObject2 = (AttributeData) localIterator.next();
			AttributeIdentifier localAttributeIdentifier = ((AttributeData) localObject2)
					.getAttributeId();
			AttributeTypeSummary localAttributeTypeSummary = ((AttributeData) localObject2)
					.getAttributeTypeSummary();
			UIValidationStatus localUIValidationStatus = getAttributeVisibilityStatus(localAttributeIdentifier);
			if ((!localUIValidationStatus
					.equals(UIValidationStatus.ATTR_HIDDEN))
					&& (!localAttributeTypeSummary.isHidden())) {
				Object localObject3 = null;
				if ((localUIValidationStatus
						.equals(UIValidationStatus.ATTR_HIDDEN_VALUE))
						|| (localAttributeTypeSummary.isValueHidden())) {
					localObject3 = WTMessage.getLocalizedMessage(
							"com.ptc.core.ui.componentRB",
							"HIDDEN_DISPLAY_STRING", null,
							SessionHelper.getLocale());
				} else {
					localObject3 = ((AttributeData) localObject2)
							.getAttributeValue();
					if (((AttributeData) localObject2)
							.getAttributeState()
							.compareTo(
									com.ptc.core.meta.container.common.State.DEFAULT) == 0) {
						localObject3 = "";
					} else {
						localObject3 = SoftAttributesHelper
								.convertAttributeValueToString(localObject3,
										localLocale);
					}
				}
				localArrayList4.add(localAttributeIdentifier);
				localArrayList2.add(localAttributeTypeSummary.getLabel());
				localArrayList3.add(localObject3);
				localArrayList5.add(typeInstanceAttributesService
						.getLocalizedAttributeDisplayString(localObject3,
								localLocale, localAttributeTypeSummary));
			}
		}
		Object localObject2 = new NmSoftAttribute();
		try {
			((NmSoftAttribute) localObject2).setLabels(localArrayList2);
			((NmSoftAttribute) localObject2).setValues(localArrayList3);
			((NmSoftAttribute) localObject2)
					.setAttributeIdentifiers(localArrayList4);
			((NmSoftAttribute) localObject2).setLocale(localLocale);
			((NmSoftAttribute) localObject2)
					.setLocalizedDisplayNames(localArrayList5);
		} catch (WTPropertyVetoException localWTPropertyVetoException) {
			localWTPropertyVetoException.printStackTrace();
		}
		return (NmSoftAttribute) localObject2;
	}

	public ArrayList getTypes(String paramString,
			NmCommandBean paramNmCommandBean) throws WTException {
		WTContainer localWTContainer = paramNmCommandBean.getContainer();
		if (localWTContainer != null) {
			OrgContainer localOrgContainer = WTContainerHelper.service
					.getOrgContainer(localWTContainer);
			NmOid localNmOid = new NmOid(localOrgContainer);
			return getTypes(paramString, localNmOid, null);
		}
		return null;
	}

	public void expandZipFileIntoFolder(NmCommandBean paramNmCommandBean,
			String paramString) throws WTException {
		Transaction localTransaction = null;
		localTransaction = new Transaction();
		localTransaction.start();
		try {
			HashMap localHashMap1 = paramNmCommandBean.getMap();
			String str1 = (String) localHashMap1.get("type");
			String str2 = (String) localHashMap1.get("comment");
			String str3 = (String) localHashMap1.get("keepcheckedout");
			String str4 = (String) localHashMap1.get("location");
			String str5 = (String) localHashMap1.get("description");
			String str6 = (String) localHashMap1.get("usefoldernames");
			String str7 = (String) localHashMap1.get("conflictresolution");
			boolean bool1 = str7.equals("iterateconflicts");

			NmOid localNmOid1 = new NmOid(str4);
			boolean bool2 = new Boolean(str3).booleanValue();
			boolean bool3 = new Boolean(str6).booleanValue();
			int i = 0;
			StringBuffer localStringBuffer1 = new StringBuffer();
			int j = 0;
			StringBuffer localStringBuffer2 = new StringBuffer();
			try {
				Object localObject1 = new File(paramString);
				NmJarReader localObject2 = new NmJarReader((File) localObject1,
						(Locale) localHashMap1.get("reallocale"));
				String[] localObject3 = ((NmJarReader) localObject2)
						.getFileNames();
				int k = 1;
				int m = localObject3.length;

				ArrayList localArrayList1 = new ArrayList(localObject3.length);

				ArrayList localArrayList2 = new ArrayList(localObject3.length);

				ArrayList localArrayList3 = new ArrayList(localObject3.length);
				Object localObject6;
				Object localObject8;
				for (int n = 0; n < localObject3.length; n++) {
					Object localObject5 = localObject3[n];
					int i1 = ((String) localObject5).lastIndexOf('/');

					String str8 = i1 > 0 ? ((String) localObject5).substring(0,
							i1 + 1) : "/";
					localObject6 = i1 > 0 ? ((String) localObject5)
							.substring(i1 + 1) : localObject5;
					int i3 = ((String) localObject6).lastIndexOf(".");
					localObject8 = localObject6;
					if (i3 > 0) {
						localObject8 = ((String) localObject6).substring(0, i3);
					}
					if (((String) localObject8).length() > FILE_NAME_MAX_LENGTH) {
						j = 1;
						localStringBuffer2.append((String) localObject6 + "\n");
					}
					if (!localArrayList3.contains(str8)) {
						localArrayList3.add(str8);
					}
					localArrayList2.add(str8);
					localArrayList1.add(localObject6);
				}
				if (j != 0) {
					Object localObject4 = ResourceBundle.getBundle(
							"com.ptc.netmarkets.object.objectResource",
							SessionHelper.getLocale());
					Object localObject5 = ((ResourceBundle) localObject4)
							.getString("234")
							+ "\n\n"
							+ localStringBuffer2.toString();
					throw new NmException((String) localObject5);
				}
				Object localObject4 = new HashMap();
				Object localObject5 = new HashMap();
				HashMap localHashMap2 = new HashMap();
				Object localObject7;
				Object localObject9;
				Object localObject10;
				Object localObject11;
				for (int i2 = 0; i2 < localArrayList3.size(); i2++) {
					localObject6 = (String) localArrayList3.get(i2);
					this.logger.debug("uniqueFolderName = "
							+ (String) localObject6);
					localObject7 = bool3 ? createFolders(paramNmCommandBean,
							localNmOid1, (String) localObject6) : localNmOid1;
					((HashMap) localObject4).put(localObject6, localObject7);
					if (bool1) {
						localObject8 = (Folder) ((NmOid) localObject7).getRef();
						localObject9 = findFolderDocuments(
								(Folder) localObject8, false, null);
						QueryResult localQueryResult = new QueryResult(
								(ObjectVectorIfc) localObject9);
						ArrayList localArrayList4 = new ArrayList(
								localQueryResult.size());
						localObject10 = new ArrayList(localQueryResult.size());
						while (localQueryResult.hasMoreElements()) {
							WTDocument localWTDocument1 = (WTDocument) localQueryResult
									.nextElement();
							localWTDocument1 = (WTDocument) ContentHelper.service
									.getContents(localWTDocument1);
							ContentItem localContentItem = ContentHelper
									.getPrimary(localWTDocument1);
							if (localContentItem != null) {
								if ((localContentItem instanceof ApplicationData)) {
									localObject11 = ((ApplicationData) localContentItem)
											.getFileName();
									localArrayList4.add(localWTDocument1);
									((ArrayList) localObject10)
											.add(localObject11);
								}
							}
						}
						((HashMap) localObject5).put(localObject6,
								localArrayList4);
						localHashMap2.put(localObject6, localObject10);
					}
				}
				for (int i2 = 0; i2 < localArrayList2.size(); i2++) {
					localObject6 = (String) localArrayList2.get(i2);
					localObject7 = (String) localArrayList1.get(i2);
					if ((localObject7 != null)
							&& (!((String) localObject7).equals(""))) {
						localObject8 = "";
						localObject9 = "";
						int i4 = 0;
						do {
							localObject9 = localObject3[(i2 + i4)];
							int i5 = ((String) localObject9).lastIndexOf('/');
							if (i5 != ((String) localObject9).length() - 1) {
								localObject8 = i5 > 0 ? ((String) localObject9)
										.substring(i5 + 1) : localObject9;
							}
							i4++;
						} while (!((String) localObject8).equals(localObject7));
						this.logger.debug("currentDirAndFileName="
								+ (String) localObject9);
						InputStream localInputStream = ((NmJarReader) localObject2)
								.getStreamByName((String) localObject9);
						if (localInputStream == null) {
							this.logger.debug("InputStream is null");

							localObject10 = new NmJarReader(
									(File) localObject1, true,
									SessionHelper.getLocale(), "Cp437");

							((NmJarReader) localObject10).getFileNames();
							localObject3 = ((NmJarReader) localObject10)
									.getFileNames();

							localObject9 = localObject3[(i2 + i4 - 1)];
							int i6 = ((String) localObject9).lastIndexOf('/');
							if (i6 != ((String) localObject9).length() - 1) {
								localObject8 = i6 > 0 ? ((String) localObject9)
										.substring(i6 + 1) : localObject9;
							}
							int i7 = ((String) localObject8).lastIndexOf('/');
							if (i7 != ((String) localObject8).length() - 1) {
								localObject7 = i7 > 0 ? ((String) localObject8)
										.substring(i7 + 1) : localObject8;
								localInputStream = ((NmJarReader) localObject10)
										.getStreamByName((String) localObject9);
							}
						}
						localObject10 = new BufferedInputStream(
								localInputStream);

						Object[] arrayOfObject = { new Integer(k),
								new Integer(m) };
						WTMessage localWTMessage = new WTMessage(
								"com.ptc.netmarkets.object.objectResource",
								"232", arrayOfObject);
						JSPFeedback.sendFeedback(localWTMessage);
						k++;

						this.logger
								.debug("Start Create/Update documents==========");

						localObject11 = (ArrayList) localHashMap2
								.get(localObject6);
						Object localObject12;
						Object localObject13;
						Object localObject15;
						Object localObject16;
						if ((localObject11 != null)
								&& (((ArrayList) localObject11)
										.contains(localObject7))) {
							if (bool1) {
								this.logger
										.debug("---------Create document iterateconflicts ----");

								localObject12 = null;
								try {
									int i8 = ((ArrayList) localObject11)
											.indexOf(localObject7);
									localObject13 = (ArrayList) ((HashMap) localObject5)
											.get(localObject6);
									WTDocument localWTDocument2 = (WTDocument) ((ArrayList) localObject13)
											.get(i8);
									boolean bool4 = WorkInProgressHelper
											.isCheckedOut(localWTDocument2);
									boolean bool5 = WorkInProgressHelper
											.isCheckedOut(localWTDocument2,
													SessionHelper.manager
															.getPrincipal());
									boolean bool6 = AccessControlHelper.manager
											.hasAccess(localWTDocument2,
													AccessPermission.MODIFY);
									if (((!bool4) && (bool6))
											|| ((bool4) && (bool5))) {
										localObject12 = new Transaction();
										((Transaction) localObject12).start();

										this.logger
												.debug("Iterating existing document: "
														+ localWTDocument2
																.getName());
										if (!bool4) {
											localObject15 = WorkInProgressHelper.service
													.checkout(
															localWTDocument2,
															WorkInProgressHelper.service
																	.getCheckoutFolder(),
															"");
											localWTDocument2 = (WTDocument) ((CheckoutLink) localObject15)
													.getWorkingCopy();
										} else if (!WorkInProgressHelper
												.isWorkingCopy(localWTDocument2)) {
											localWTDocument2 = (WTDocument) WorkInProgressHelper.service
													.workingCopyOf(localWTDocument2);
										}
										localWTDocument2 = (WTDocument) ContentHelper.service
												.getContents(localWTDocument2);
										localObject15 = ContentHelper
												.getPrimary(localWTDocument2);
										ContentServerHelper.service
												.deleteContent(
														localWTDocument2,
														(ContentItem) localObject15);
										PersistenceHelper.manager
												.refresh(localWTDocument2);
										localObject16 = ApplicationData
												.newApplicationData(localWTDocument2);
										((ApplicationData) localObject16)
												.setFileName((String) localObject7);
										((ApplicationData) localObject16)
												.setRole(ContentRoleType.PRIMARY);
										ContentServerHelper.service
												.updateContent(
														localWTDocument2,
														(ApplicationData) localObject16,
														(InputStream) localObject10);

										NmObjectCommands.updateSoftAttributes(
												paramNmCommandBean, str1,
												localWTDocument2);
										localWTDocument2 = (WTDocument) PersistenceHelper.manager
												.save(localWTDocument2);

										localWTDocument2 = (WTDocument) WorkInProgressHelper.service
												.checkin(localWTDocument2, str2);
										if ((bool2) && (bool4)) {
											NmOid localNmOid4 = new NmOid(
													localWTDocument2
															.getPersistInfo()
															.getObjectIdentifier());
											checkOut(localNmOid4);
										}
										ImportSummaryEvent.getSummaryEvent()
												.contribute(localWTDocument2);

										((Transaction) localObject12).commit();
										localObject12 = null;
									} else {
										i = 1;
										localStringBuffer1
												.append((String) localObject7
														+ "\n");
									}
								} finally {
									if (localObject12 != null) {
										((Transaction) localObject12)
												.rollback();
									}
								}
							} else {
								this.logger
										.debug("Ignoring existing document: "
												+ (String) localObject7);
							}
						} else {
							localObject12 = StandardIXBService
									.getSaveFileOnServer();
							FileOutputStream localFileOutputStream = new FileOutputStream(
									(File) localObject12);
							ProjectIXUtils.copyStream(
									(InputStream) localObject10,
									localFileOutputStream);
							localFileOutputStream.close();
							localObject13 = ((File) localObject12)
									.getCanonicalPath();

							int i9 = ((String) localObject7).lastIndexOf(".");
							Object localObject14 = localObject7;
							if (i9 > 0) {
								localObject14 = ((String) localObject7)
										.substring(0, i9);
							}
							this.logger.debug("Creating new document: "
									+ (String) localObject14);

							NmOid localNmOid2 = (NmOid) ((HashMap) localObject4)
									.get(localObject6);
							NmOid localNmOid3 = NmUtilClassProxy.create(
									paramNmCommandBean, (String) localObject13,
									(String) localObject14, str1, str5,
									localNmOid2.toString(),
									(String) localObject7);
							NmUtilClassProxy.setAccessForObject(
									paramNmCommandBean, localNmOid3,
									Boolean.TRUE);
							localObject15 = (WTDocument) localNmOid3.getRef();
							localObject15 = (WTDocument) PersistenceHelper.manager
									.refresh((Persistable) localObject15);
							VersionControlHelper.setNote(
									(Iterated) localObject15, str2);
							PersistenceServerHelper.manager
									.update((Persistable) localObject15);
							((File) localObject12).delete();
							if (bool2) {
								this.logger
										.debug("'Keep Checked Out' check box is selected, checking out the document...");
								localObject16 = new NmOid(
										((WTDocument) localObject15)
												.getPersistInfo()
												.getObjectIdentifier());
								checkOut((NmOid) localObject16);
							}
						}
						((BufferedInputStream) localObject10).close();
					}
				}
				JSPFeedback.sendFeedback("@@@@");
				if (i != 0) {
					localObject1 = ResourceBundle.getBundle(
							"com.ptc.netmarkets.object.objectResource",
							SessionHelper.getLocale());
					Object stringmessage = ((ResourceBundle) localObject1)
							.getString("235")
							+ "\n\n"
							+ localStringBuffer1.toString();

					localTransaction.commit();
					localTransaction = null;
					throw new NmException((String) stringmessage);
				}
			} catch (PropertyVetoException localPropertyVetoException) {
				throw new NmException(localPropertyVetoException);
			} catch (IOException localIOException) {
				throw new NmException(localIOException);
			} catch (WTException localWTException) {
				Object localObject3;
				Object localObject2 = localWTException.getCause();
				if ((localObject2 != null)
						&& ((localObject2 instanceof InvocationTargetException))) {
					localObject3 = (InvocationTargetException) localObject2;
					if (((InvocationTargetException) localObject3)
							.getTargetException() != null) {
						if (((InvocationTargetException) localObject3)
								.getTargetException().getCause() != null) {
							throw new NmException(
									((InvocationTargetException) localObject3)
											.getTargetException().getCause());
						}
						throw new NmException(
								((InvocationTargetException) localObject3)
										.getTargetException());
					}
					throw new NmException((Throwable) localObject3);
				}
				throw new NmException(localWTException);
			} finally {
				JSPFeedback.sendFeedback("@@@@");
				if (i != 0) {
					ResourceBundle localResourceBundle = ResourceBundle
							.getBundle(
									"com.ptc.netmarkets.object.objectResource",
									SessionHelper.getLocale());
					String str9 = localResourceBundle.getString("235") + "\n\n"
							+ localStringBuffer1.toString();

					localTransaction.commit();
					localTransaction = null;
					throw new NmException(str9);
				}
			}
			localTransaction.commit();
			localTransaction = null;
		} finally {
			if (localTransaction != null) {
				localTransaction.rollback();
			}
		}
	}

	public URL downloadFolderContentFiles(NmCommandBean paramNmCommandBean)
			throws WTException {
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		logger.info("##############################################################################################");

		HashMap localHashMap = paramNmCommandBean.getMap();
		String username = paramNmCommandBean.getRequest().getRemoteUser();
		logger.info("nxtlbs em ctx username :" + username);

		/*
		 * HashMap cgikey=(HashMap) localHashMap.get("cgi_data_key"); String
		 * username; if(null!=cgikey&& null!=cgikey.get("cgi.remote_user")) {
		 * username=(String) cgikey.get("cgi.remote_user");
		 * logger.info("nxtlbs em ctx username :"+username); }
		 */
		JSPFeedback localJSPFeedback = (JSPFeedback) localHashMap.get("jfb");

		String str1 = (String) localHashMap.get("savefullpath");
		String str2 = (String) localHashMap.get("includesubfolders");
		String str3 = (String) localHashMap.get("checkoutondownload");
		WTContainerRef localWTContainerRef = null;
		if (paramNmCommandBean.getSharedContextOid() != null) {
			localWTContainerRef = paramNmCommandBean.getSharedContextOid()
					.getContainerRef();
		} else {
			localWTContainerRef = paramNmCommandBean.getContainerRef();
		}
		boolean bool1 = new Boolean(str1).booleanValue();
		boolean bool2 = new Boolean(str2).booleanValue();
		boolean bool3 = new Boolean(str3).booleanValue();

		ArrayList localArrayList = paramNmCommandBean.getSelectedInOpener();
		Object localObject = null;
		if (paramNmCommandBean.getActionOid() != null) {
			localObject = paramNmCommandBean.getActionOid().getRef();
		}
		if ((localObject == null)
				&& (paramNmCommandBean.getPrimaryOid() != null)) {
			localObject = paramNmCommandBean.getPrimaryOid().getRef();
		}
		ObjectVectorIfc localObjectVectorIfc = processSelectedObjectsForDocuments(
				localObject, localArrayList, bool2);

		QueryResult localQueryResult = new QueryResult(localObjectVectorIfc);
		if (localQueryResult.size() == 0) {
			throw new NmException("com.ptc.netmarkets.object.objectResource",
					"229", null);
		}
		File localFile = exportContentFiles(localQueryResult, localJSPFeedback,
				bool1, bool3, localWTContainerRef);

		return NmObjectHelper.constructOutputURL(localFile,
				DEFAULT_ARCHIVE_FILE_NAME);
	}

	public void addSCMIAttachment(NmCommandBean paramNmCommandBean)
			throws WTException {
		String[] arrayOfString = (String[]) paramNmCommandBean.getMap().get(
				"scmi_picker_result");
		if ((arrayOfString == null) || (arrayOfString.length == 0)) {
			throw new WTException("com.ptc.netmarkets.model.modelResource",
					"58", null);
		}
		HashMap localHashMap = paramNmCommandBean.getMap();

		String str = (String) localHashMap.get("new_object_oid");
		NmOid localNmOid;
		if (str != null) {
			localNmOid = NmOid.newNmOid(str);
		} else {
			localNmOid = paramNmCommandBean.getPrimaryOid();
		}
		Workable localWorkable = (Workable) localNmOid.getRef();
		ContentHolder localContentHolder = (ContentHolder) NmObjectCommands
				.getWorkingCopy(localWorkable);
		for (int i = 0; i < arrayOfString.length; i++) {
			ScmFacade.getInstance().createScmData(localContentHolder,
					arrayOfString[i], null);
		}
	}

	private NmOid getLocationOid(NmCommandBean paramNmCommandBean)
			throws WTException {
		NmOid localNmOid = null;
		Object localObject1;
		if (paramNmCommandBean.getOpenerElemAddress() != null) {
			localObject1 = NmElementAddress.fromString(paramNmCommandBean
					.getOpenerElemAddress());
			if (!((NmElementAddress) localObject1).isEmpty()) {
				localNmOid = (NmOid) ((NmElementAddress) localObject1)
						.getOids().peek();
			}
		}
		if (localNmOid == null) {
			localObject1 = paramNmCommandBean.getSelectedInOpener();
			if (((ArrayList) localObject1).size() == 0) {
				localNmOid = paramNmCommandBean.getCurrentNodeInOpener();
				if (localNmOid == null) {
					localNmOid = paramNmCommandBean.getPageOid();
				}
			} else {
				Object localObject2 = ((ArrayList) localObject1).get(0);
				if ((localObject2 instanceof NmContext)) {
					NmContext localNmContext = (NmContext) localObject2;
					NmContextItem localNmContextItem = (NmContextItem) localNmContext
							.getContextItems().peek();
					Stack localStack = localNmContextItem.getElemAddress()
							.getOids();
					localNmOid = (NmOid) localStack.peek();
				} else if ((localObject2 instanceof NmOid)) {
					localNmOid = (NmOid) localObject2;
				}
			}
		}
		if ((localNmOid instanceof NmSimpleOid)) {
			localNmOid = NmOid.newNmOid(localNmOid.toString());
		}
		return localNmOid;
	}

	public String getTypeSelector(String paramString1, NmOid paramNmOid,
			String paramString2, String paramString3, String paramString4,
			String paramString5) throws WTException {
		Locale localLocale = SessionHelper.getLocale();
		boolean bool = SessionServerHelper.manager.isAccessEnforced();
		try {
			WTContainer localWTContainer = null;
			int i = 0;
			try {
				localWTContainer = paramNmOid.getContainer();
			} catch (Exception localException) {
				i = 1;
				bool = SessionServerHelper.manager.setAccessEnforced(false);
				try {
					localWTContainer = paramNmOid.getContainer();
				} finally {
				}
			}
			if (i != 0) {
				SessionServerHelper.manager.setAccessEnforced(false);
			}
			AdminDomainRef localAdminDomainRef = TypeIdentifierSelectionHelper
					.getAdminDomainRef(localWTContainer);

			return TypeIdentifierSelectionHelper.getTypeHTMLSelector(
					paramString2, paramString1, localWTContainer,
					localAdminDomainRef, paramString3, paramString4,
					paramString5, localLocale);
		} finally {
			SessionServerHelper.manager.setAccessEnforced(bool);
		}
	}

	public Boolean isShareUsed(NmOid paramNmOid) throws WTException {
		NmOid localNmOid = paramNmOid.getSharedContainer();
		if (localNmOid != null) {
			Object localObject1 = localNmOid.getRef();
			if ((localObject1 instanceof Project2)) {
				WTContainerRef localWTContainerRef = WTContainerRef
						.newWTContainerRef(localNmOid.getOid());
				long l = DataSharingHelper.service.getShareKey(
						(Persistable) paramNmOid.getRef(), localWTContainerRef);

				QueryResult localQueryResult1 = DataSharingHelper.service
						.getKeyMaps(l, localWTContainerRef, 0);
				if ((localQueryResult1 == null)
						|| (localQueryResult1.size() == 0)) {
					return Boolean.FALSE;
				}
				long[] arrayOfLong = new long[localQueryResult1.size()];
				int i = 0;
				while (localQueryResult1.hasMoreElements()) {
					Object localObject2 = (SharedContainerMap) localQueryResult1
							.nextElement();
					Object localObject3 = ((SharedContainerMap) localObject2)
							.getShared();
					if (localObject3 != null) {
						Object localObject4 = ((Persistable) localObject3)
								.getPersistInfo().getObjectIdentifier();
						arrayOfLong[(i++)] = ((ObjectIdentifier) localObject4)
								.getId();
					}
				}
				Object localObject2 = new QuerySpec();
				((QuerySpec) localObject2).appendClassList(
						InformationResource.class, false);
				Object localObject3 = new ClassAttribute(
						InformationResource.class,
						"thePersistInfo.theObjectIdentifier.id");
				((QuerySpec) localObject2).appendSelect(
						(ColumnExpression) localObject3, false);
				((QuerySpec) localObject2).appendWhere(new SearchCondition(
						new ClassAttribute(InformationResource.class,
								"infoObjectRef.key.id"), "IN",
						new ArrayExpression(arrayOfLong)));

				Object localObject4 = new QuerySpec();
				((QuerySpec) localObject4).setAdvancedQueryEnabled(true);
				int j = ((QuerySpec) localObject4).appendClassList(
						Deliverable.class, false);
				SQLFunction localSQLFunction = new SQLFunction("COUNT",
						KeywordExpression.COLUMN_WILDCARD);
				((QuerySpec) localObject4).appendSelect(localSQLFunction,
						new int[] { j }, false);
				((QuerySpec) localObject4).appendWhere(new SearchCondition(
						Deliverable.class, "containerReference.key", "=",
						localNmOid.getOid()));

				((QuerySpec) localObject4).appendAnd();
				((QuerySpec) localObject4).appendWhere(new SearchCondition(
						new ClassAttribute(Deliverable.class,
								"resourceRef.key.id"), "IN",
						new SubSelectExpression((StatementSpec) localObject2)));

				((QuerySpec) localObject4).toString();
				QueryResult localQueryResult2 = PersistenceServerHelper.manager
						.query((QuerySpec) localObject4);
				if ((localQueryResult2 != null)
						&& (localQueryResult2.hasMoreElements())) {
					Object localObject5 = ((Object[]) (Object[]) localQueryResult2
							.nextElement())[0];
					if (((BigDecimal) localObject5).shortValue() > 0) {
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}

	public LocalizableMessage isRemoveShareValidOperation(NmOid paramNmOid)
			throws WTException {
		try {
			NmOid localNmOid = paramNmOid.getSharedContainer();
			WTContainerRef localWTContainerRef = WTContainerRef
					.newWTContainerRef(localNmOid.getOid());
			QueryResult localQueryResult = DataSharingHelper.service
					.getObjectMaps(paramNmOid.getRef(), localWTContainerRef, 0);

			SharedContainerMap localSharedContainerMap = (SharedContainerMap) localQueryResult
					.nextElement();
			return DataSharingHelper.service
					.isRemoveValidClientOp(localSharedContainerMap);
		} catch (Exception localException) {
			throw new WTException(localException);
		}
	}

	public String getFilePath(Object paramObject1, Object paramObject2)
			throws WTException {
		if ((paramObject1 instanceof NmOid)) {
			paramObject1 = ((NmOid) paramObject1).getRef();
		}
		String str = new FilePathFactory().getFilePath(paramObject1,
				paramObject2.toString());
		return str;
	}

	public List getInflatedClipboardItems(NmClipboardBean paramNmClipboardBean)
			throws WTException {
		ArrayList localArrayList = new ArrayList();

		int i = paramNmClipboardBean.getClipboardItems().size();
		WTHashSet localWTHashSet1 = new WTHashSet(i);
		WTHashSet localWTHashSet2 = new WTHashSet(i, 2);
		for (Object localObject1 = paramNmClipboardBean.getClipboardItems()
				.iterator(); ((Iterator) localObject1).hasNext();) {
			NmClipboardItem localNmClipboardItem = (NmClipboardItem) ((Iterator) localObject1)
					.next();

			Object localObject2 = localNmClipboardItem.getObject();
			if ((localObject2 instanceof NmAnyURL)) {
				this.logger.debug("Skip a URL");
			} else {
				Object localObject3 = NmCommandBean
						.getOidFromObject(localObject2);
				Object localObject4 = ((NmOid) localObject3).getWtRef();
				if (localObject4 == null) {
					this.logger.error("Encountered an unexpected oid: "
							+ localObject3);
				} else if ((localObject4 instanceof ObjectReference)) {
					localWTHashSet1.add((WTReference) localObject4);
				} else {
					localWTHashSet2.add((WTReference) localObject4);
				}
			}
		}
		Object localObject2;
		Object localObject3;
		Object localObject4;
		Object localObject1 = new RefreshSpec();
		try {
			((RefreshSpec) localObject1).setDeleteAction(1);
		} catch (WTPropertyVetoException localWTPropertyVetoException) {
			throw new WTException(localWTPropertyVetoException);
		}
		CollectionsHelper.manager.refresh(localWTHashSet1,
				(RefreshSpec) localObject1);
		CollectionsHelper.manager.refresh(localWTHashSet2,
				(RefreshSpec) localObject1);
		for (Iterator localIterator = paramNmClipboardBean.getClipboardItems()
				.iterator(); localIterator.hasNext();) {
			localObject2 = (NmClipboardItem) localIterator.next();
			localObject3 = new NmClipboardItemInfo();
			((NmClipboardItemInfo) localObject3)
					.setClipboardItem((NmClipboardItem) localObject2);
			if (((NmClipboardItem) localObject2).getActionId() == paramNmClipboardBean
					.getLastestActionId()) {
				this.logger.debug("Setting most recent on " + localObject2);
				((NmClipboardItemInfo) localObject3).setMostRecent(true);
			}
			localArrayList.add(0, localObject3);
			localObject4 = ((NmClipboardItem) localObject2).getObject();
			if (!(localObject4 instanceof NmAnyURL)) {
				NmOid localNmOid = NmCommandBean.getOidFromObject(localObject4);
				WTReference localWTReference = localNmOid.getWtRef();
				if (localWTReference != null) {
					((NmClipboardItemInfo) localObject3)
							.setPersistable(localWTReference.getObject());
				}
			}
		}
		return localArrayList;
	}

	public FormResult setTemplateVisibility(NmCommandBean paramNmCommandBean,
			Boolean paramBoolean) throws WTException {
		FormResult localFormResult = new FormResult();
		localFormResult.setNextAction(FormResultAction.NONE);
		localFormResult.setStatus(FormProcessingStatus.SUCCESS);
		NmOid localNmOid = paramNmCommandBean.getActionOid();
		Transaction localTransaction = new Transaction();
		try {
			localTransaction.start();
			setTemplatesFiltered(localNmOid,
					getContainerForTemplateTable(paramNmCommandBean),
					paramBoolean);
			localFormResult.addDynamicRefreshInfo(new DynamicRefreshInfo(
					localNmOid, localNmOid, "U"));

			localTransaction.commit();
			localTransaction = null;
		} finally {
			if (localTransaction != null) {
				localTransaction.rollback();
			}
		}
		return localFormResult;
	}

	protected WTContainer getContainerForTemplateTable(
			NmCommandBean paramNmCommandBean) throws WTException {
		NmOid localNmOid = paramNmCommandBean.getPrimaryOid();
		WTContainer localWTContainer = null;
		if (null == localNmOid) {
			localWTContainer = paramNmCommandBean.getContainer();
		} else {
			localWTContainer = localNmOid.getContainerObject();
		}
		if (null == localWTContainer) {
			localWTContainer = paramNmCommandBean.getContainer();
		}
		return localWTContainer;
	}

	public FormResult setTemplateListVisibility(
			NmCommandBean paramNmCommandBean, Boolean paramBoolean)
			throws WTException {
		FormResult localFormResult = new FormResult();
		localFormResult.setNextAction(FormResultAction.NONE);
		localFormResult.setStatus(FormProcessingStatus.SUCCESS);

		ArrayList localArrayList = paramNmCommandBean.getSelected();
		if ((localArrayList == null) || (localArrayList.size() <= 0)) {
			return null;
		}
		Transaction localTransaction = new Transaction();
		try {
			localTransaction.start();
			for (int i = 0; i < localArrayList.size(); i++) {
				NmOid localNmOid = null;
				if ((localArrayList.get(i) instanceof NmContext)) {
					localNmOid = ((NmContext) localArrayList.get(i))
							.getTargetOid();
				} else if ((localArrayList.get(i) instanceof NmOid)) {
					localNmOid = (NmOid) localArrayList.get(i);
				} else {
					localNmOid = new NmOid((Persistable) localArrayList.get(i));
				}
				setTemplatesFiltered(localNmOid,
						paramNmCommandBean.getContainer(), paramBoolean);
				localFormResult.addDynamicRefreshInfo(new DynamicRefreshInfo(
						localNmOid, localNmOid, "U"));
			}
			localTransaction.commit();
			localTransaction = null;
		} finally {
			if (localTransaction != null) {
				localTransaction.rollback();
			}
		}
		return localFormResult;
	}

	public NmChangeModel[] importSavedQueries(NmCommandBean paramNmCommandBean,
			String paramString1, String paramString2) throws WTException {
		if (VERBOSE) {
			System.out.println("=> importObjects: file = " + paramString1
					+ ", temp file = " + paramString2);
		}
		if (paramString2 == null) {
			throw new NmException("com.ptc.netmarkets.model.modelResource",
					"0", null);
		}
		String str1 = paramNmCommandBean.getTextParameter("isGlobalSearch");
		WTContainerRef localWTContainerRef = null;
		Hashtable localHashtable = new Hashtable();
		if (VERBOSE) {
			System.out.println("GlobalSearch: " + str1);
		}
		if ("on".equalsIgnoreCase(str1)) {
			String str2 = paramNmCommandBean.getTextParameter("containerScope");
			Object localObject1 = new ObjectIdentifier(str2);
			localWTContainerRef = WTContainerRef
					.newWTContainerRef((ObjectIdentifier) localObject1);
			if (VERBOSE) {
				System.out.println("scope: " + localWTContainerRef.getName());
			}
			localHashtable.put("containerScope", localWTContainerRef);
		}
		String str2 = paramNmCommandBean.getTextParameter("overwriteExisting");
		if (VERBOSE) {
			System.out.println("overwriteExisting: " + str2);
		}
		if ("on".equalsIgnoreCase(str2)) {
			localHashtable.put("overwriteExisting", new Boolean(true));
		}
		Object localObject1 = new Transaction();
		try {
			((Transaction) localObject1).start();
			Object localObject2 = paramNmCommandBean.getContainerRef();
			ProjectIXUtils.importSavedQuery((WTContainerRef) localObject2,
					paramString2, localHashtable);

			((Transaction) localObject1).commit();
			localObject1 = null;
		} finally {
			if (localObject1 != null) {
				((Transaction) localObject1).rollback();
			}
		}
		NmChangeModel[] localObject2 = new NmChangeModel[1];
		localObject2[0] = new NmChangeModel();
		localObject2[0].setInvalidateAll(true);
		if (VERBOSE) {
			System.out.println("   importSavedQueries - OUT");
		}
		return localObject2;
	}

	public String exportSavedQueries(NmCommandBean paramNmCommandBean)
			throws WTException {
		ArrayList localArrayList1 = paramNmCommandBean.getSelected();
		if (VERBOSE) {
			System.out.println("querySelected: " + localArrayList1.size());
		}
		ArrayList localArrayList2 = new ArrayList();
		for (Object localObject1 = localArrayList1.iterator(); ((Iterator) localObject1)
				.hasNext();) {
			Object localObject2 = ((Iterator) localObject1).next();
			Object localObject3 = NmCommandBean.getOidFromObject(localObject2);
			localArrayList2.add(((NmOid) localObject3).getRef());
		}
		Object localObject1 = StandardIXBService.getSaveFileOnServer();
		Object localObject2 = paramNmCommandBean.getContainerRef();
		if (localObject2 == null) {
			localObject2 = getDefaultContainerRef();
		}
		localObject1 = ProjectIXUtils.exportSavedQueryToJarFile(
				(WTContainerRef) localObject2, localArrayList2,
				(File) localObject1);
		if (localObject1 == null) {
			if (VERBOSE) {
				System.out.println("    exportObjects - OUT, returning null");
			}
			return null;
		}
		Object localObject3 = NmObjectHelper.constructOutputURL(
				(File) localObject1, DEFAULT_ARCHIVE_FILE_NAME);
		if (VERBOSE) {
			System.out.println("   exportObjects - OUT: " + localObject3
					+ ", file = " + localObject1);
		}
		return ((URL) localObject3).toString();
	}

	public String getTypeSelector(String paramString1, NmOid paramNmOid,
			String paramString2, String paramString3, String paramString4,
			String paramString5, AdminDomainRef paramAdminDomainRef)
			throws WTException {
		Locale localLocale = SessionHelper.getLocale();
		WTContainer localWTContainer = null;
		try {
			localWTContainer = paramNmOid.getContainer();
		} catch (Exception localException) {
			boolean bool = SessionServerHelper.manager.setAccessEnforced(false);
			try {
				localWTContainer = paramNmOid.getContainer();
			} finally {
				SessionServerHelper.manager.setAccessEnforced(bool);
			}
		}
		return TypeIdentifierSelectionHelper.getTypeHTMLSelector(paramString2,
				paramString1, localWTContainer, paramAdminDomainRef,
				paramString3, paramString4, paramString5, localLocale);
	}

	private final File exportContentFiles(QueryResult paramQueryResult,
			JSPFeedback paramJSPFeedback, boolean paramBoolean1,
			boolean paramBoolean2, WTContainerRef paramWTContainerRef)
			throws WTException {
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		HashMap<String, Object> result = new HashMap<String, Object>();
		ArrayList localArrayList1 = new ArrayList();
		label1196: try {
			File localFile1 = StandardIXBService.getSaveFileOnServer();
			IXBJarWriter localIXBJarWriter = new IXBJarWriter(localFile1);
			File localFile2 = getSaveDirectory();
			ArrayList localArrayList2 = new ArrayList();
			ArrayList localArrayList3 = new ArrayList();
			int i = 1;
			int j = paramQueryResult.size();
			ArrayList localArrayList4 = new ArrayList();
			HashMap localHashMap = new HashMap();
			Object localObject1;
			Object localObject2;
			while (paramQueryResult.hasMoreElements()) {
				localObject1 = new Object[] { new Integer(i), new Integer(j) };
				WTMessage localWTMessage = new WTMessage(
						"com.ptc.netmarkets.object.objectResource", "231",
						(Object[]) localObject1);
				JSPFeedback.sendFeedback(localWTMessage);
				i++;

				localObject2 = (WTDocument) paramQueryResult.nextElement();
				String keyObj = PersistenceHelper.getObjectIdentifier(
						(WTDocument) localObject2).getStringValue();
				result = doPolicyEvaluation(emCtx, SessionMgr.getPrincipal(),
						keyObj);
				if (!AccessControlHelper.manager.hasAccess(localObject2,
						AccessPermission.DOWNLOAD)) {
					this.logger.debug("No Download Access to "
							+ ((WTDocument) localObject2).getIdentity());
					AccessControlServerHelper.manager.logNotAuthorized(
							(AccessControlled) localObject2,
							SessionMgr.getPrincipal(),
							AccessPermission.DOWNLOAD);

					throw new NmException(new NotAuthorizedException(null,
							"wt.content.contentResource", "72",
							new String[] { ((WTDocument) localObject2)
									.getIdentity() }));
				}
				if (!SandboxHelper.isObjectHidden((Persistable) localObject2)) {
					if ((!WorkInProgressHelper
							.isCheckedOut((Workable) localObject2))
							&& (LockHelper.isLocked((Lockable) localObject2))) {
						this.logger
								.debug("Skipping doc since it is not the checked-in copy");
					} else {
						FormatContentHolder localFormatContentHolder = (FormatContentHolder) ContentHelper.service
								.getContents((ContentHolder) localObject2);
						ContentItem localContentItem = ContentHelper
								.getPrimary(localFormatContentHolder);
						if ((localContentItem != null)
								&& ((localContentItem instanceof ApplicationData))) {
							ApplicationData localApplicationData = (ApplicationData) localContentItem;
							Object localObject3 = localApplicationData != null ? (Streamed) localApplicationData
									.getStreamData().getObject() : null;
							if (localObject3 != null) {
								String str1 = localApplicationData
										.getFileName();
								Object localObject6;
								File localFile3;
								Object localObject5;
								int n;
								if (((Streamed) localObject3).retrieveStream() != null) {
									BufferedInputStream localBufferedInputStream = new BufferedInputStream(
											((Streamed) localObject3)
													.retrieveStream());
									while (localArrayList4.contains(str1)) {
										Object localObject4 = (Integer) localHashMap
												.get(str1);

										int m = str1.lastIndexOf(".");
										String str2 = str1.substring(m);
										localObject6 = str1.substring(0, m);
										if (localObject4 == null) {
											localHashMap.put(str1, new Integer(
													1));
											str1 = (String) localObject6 + "["
													+ 1 + "]" + str2;
										} else {
											localHashMap
													.put(str1,
															new Integer(
																	((Integer) localObject4)
																			.intValue() + 1));
											str1 = (String) localObject6
													+ "["
													+ (((Integer) localObject4)
															.intValue() + 1)
													+ "]" + str2;
										}
									}
									localArrayList4.add(str1);

									localFile3 = new File(localFile2, str1);
									localArrayList1.add(localFile3
											.getCanonicalPath());
									Object localObject4 = new BufferedOutputStream(
											new FileOutputStream(localFile3));
									localObject5 = new byte[FvProperties.READ_BUFFER_SIZE];
									while ((n = localBufferedInputStream
											.read((byte[]) localObject5)) > 0) {
										((BufferedOutputStream) localObject4)
												.write((byte[]) localObject5,
														0, n);
									}
									localBufferedInputStream.close();
									((BufferedOutputStream) localObject4)
											.close();
								} else {
									long l = localApplicationData.getFileSize();
									if ((!IS_EMPTY_FILE_VALID) || (l != 0L)) {
										continue;
									}
									localFile3 = new File(localFile2, str1);
									localObject5 = new BufferedOutputStream(
											new FileOutputStream(localFile3));
									((BufferedOutputStream) localObject5)
											.close();
								}

								boolean isEncryptallowed = false;
								if (result.get("DRM") != null) {
									isEncryptallowed = (Boolean) result
											.get("DRM");
								}
								if (isEncryptallowed) {

									logger.info("nxtlbs em ctx localfile path"
											+ localFile3);
									logger.info("nxtlbs em ctx localfile name"
											+ localFile3.getName());
									logger.info("nxtlbs em ctx localfile name"
											+ localFile3.getAbsolutePath());
									String path = WindchillObjectHelper
											.getWTTempPath()
											+ "\\nxldrm\\"
											+ localFile3.getName();
									;

									BufferedOutputStream fout = new BufferedOutputStream(
											new FileOutputStream(new File(path)));
									BufferedInputStream fin = new BufferedInputStream(
											new FileInputStream(localFile3));

									byte[] byteBuff = new byte[4096];
									int bytesRead = 0;
									while ((bytesRead = fin.read(byteBuff)) != -1) {
										fout.write(byteBuff, 0, bytesRead);
									}
									fout.close();
									SecurityLabels sl = ((WTDocument) localObject2)
											.getSecurityLabels();
									HashMap<String, List<String>> tags = new HashMap<String, List<String>>();
									if (sl != null && sl.toString() != null) {
										logger.info("nxtlbs em ctx localfile tags"
												+ tags.toString());
										tags = UtilityTools
												.parseSecurityLabels(
														sl.toString(),
														"primary");
										logger.info("nxtlbs em ctx localfile tags after parse call"
												+ tags.toString());
										logger.info("nxtlbs em ctx localfile tags"
												+ tags);
									}
									logger.info("nxtlbs security labels" + sl);
									logger.info("nxtlbs security label tags"
											+ sl.toString());
									if (!str1.endsWith(".nxl")) {
										File File3 = encrypt(localFile3, tags);
										logger.info("nxtlbs em ctx str1" + str1);
										if (File3 != null) {
											localFile3 = File3;
											str1 = str1 + ".nxl";
										}
									}
								}
								FileInputStream localFileInputStream = new FileInputStream(
										localFile3);
								logger.info("nxtlbs after em ctx str1" + str1);
								Object localObject4 = str1;
								if (paramBoolean1) {
									localObject5 = getJarPath(
											(WTDocument) localObject2,
											paramWTContainerRef);
									localObject4 = (String) localObject5 + str1;
								}
								boolean bool1 = SessionServerHelper.manager
										.setAccessEnforced(false);
								try {
									if (localArrayList3.contains(localObject4)) {
										if (!paramBoolean1) {
											n = localArrayList3
													.indexOf(localObject4);
											localObject6 = (NmOid) localArrayList2
													.get(n);
											WTDocument localWTDocument = (WTDocument) ((NmOid) localObject6)
													.getRef();
											if (localWTDocument
													.getName()
													.equals(((WTDocument) localObject2)
															.getName())) {
												boolean bool4 = getJarPath(
														(WTDocument) localObject2,
														paramWTContainerRef)
														.equals(getJarPath(
																localWTDocument,
																paramWTContainerRef));
												if (!bool4) {
													throw new NmException(
															"com.ptc.netmarkets.object.objectResource",
															"230", null);
												}
											}
										}
									} else {
										logger.info("nxtlbs after em ctx localobject4"
												+ (String) localObject4);
										localIXBJarWriter.addEntry(
												localFileInputStream,
												(String) localObject4);
									}
								} finally {
									SessionServerHelper.manager
											.setAccessEnforced(bool1);
								}
								NmOid localNmOid = new NmOid(
										(Persistable) localObject2);
								boolean bool2 = WorkInProgressHelper
										.isCheckedOut((Workable) localObject2);
								boolean bool3 = AccessControlHelper.manager
										.hasAccess(localObject2,
												AccessPermission.MODIFY);
								if ((paramBoolean2) && (!bool2) && (bool3)) {
									checkOut(localNmOid);
								}
								localArrayList2.add(localNmOid);
								localArrayList3.add(localObject4);
							}
						}
					}
				}
			}
			if (localArrayList3.isEmpty()) {
				throw new NmException(
						"com.ptc.netmarkets.object.objectResource", "229", null);
			}
			localIXBJarWriter.finalizeJar();
			int k;

			return localFile1;
		} catch (PropertyVetoException localPropertyVetoException) {
			throw new WTException(localPropertyVetoException);
		} catch (IOException localIOException) {
			throw new WTException(localIOException);
		} finally {
			/*
			 * try { for (int i1 = 0; i1 < localArrayList1.size(); i1++) { File
			 * localFile4 = new File((String)localArrayList1.get(i1));
			 * localFile4.delete(); } break label1196; } finally {}
			 */
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

	private File encrypt(File localFile3, HashMap<String, List<String>> tags) {
		File nxlFile = new File(localFile3.getAbsolutePath() + ".nxl");
		RightsManager rm = new RightsManagerHelper().getRightsManager();
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
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

	private NmOid createFolders(NmCommandBean paramNmCommandBean,
			NmOid paramNmOid, String paramString) throws WTException {
		int i = paramString.indexOf('/');
		NmOid localNmOid = null;
		if (i > 0) {
			String str = paramString.substring(0, i);
			Folder localFolder1 = (Folder) paramNmOid.getRef();
			QueryResult localQueryResult = FolderHelper.service.findSubFolders(
					localFolder1, AccessPermission.READ);
			Object localObject;
			while (localQueryResult.hasMoreElements()) {
				localObject = localQueryResult.nextElement();
				if ((localObject instanceof Folder)) {
					Folder localFolder2 = (Folder) localObject;
					if (localFolder2.getName().equals(str)) {
						if (VERBOSE) {
							System.out.println("Using existing folder: " + str);
						}
						localNmOid = new NmOid(localFolder2);
					}
				}
			}
			if (localNmOid == null) {
				if (VERBOSE) {
					System.out.println("Creating new folder: " + str);
				}
				paramNmCommandBean.getMap().put("name", str);
				NmChangeModel[] nmchange = NmUtilClassProxy.folder_create(
						paramNmCommandBean, paramNmOid.toString(), null,
						new Boolean(true));
				localNmOid = nmchange[0].getKey(0);
				NmUtilClassProxy.setAccessForObject(paramNmCommandBean,
						localNmOid, Boolean.valueOf(true));
			}
			if (i != paramString.length() - 1) {
				paramString = paramString.substring(i + 1);
				localNmOid = createFolders(paramNmCommandBean, localNmOid,
						paramString);
			}
		} else {
			localNmOid = paramNmOid;
		}
		return localNmOid;
	}

	private ObjectVectorIfc findFolderDocuments(Folder paramFolder,
			boolean paramBoolean, ObjectVectorIfc paramObjectVectorIfc)
			throws WTException {
		ArrayList localArrayList = new ArrayList();
		if (paramBoolean) {
			localArrayList = recurseFolders(paramFolder);
		} else {
			localArrayList.add(paramFolder);
		}
		if (paramObjectVectorIfc == null) {
			paramObjectVectorIfc = new ObjectVector();
		}
		for (int i = 0; i < localArrayList.size(); i++) {
			Folder localFolder = (Folder) localArrayList.get(i);
			QueryResult localQueryResult = getSharedFolderContents(localFolder,
					WTDocument.class, true);
			while (localQueryResult.hasMoreElements()) {
				paramObjectVectorIfc.addElement(localQueryResult.nextElement());
			}
		}
		if (VERBOSE) {
			System.out.println("Found " + paramObjectVectorIfc.size()
					+ " documents.");
		}
		return paramObjectVectorIfc;
	}

	private ArrayList recurseFolders(Folder paramFolder) throws WTException {
		ArrayList localArrayList = new ArrayList();
		localArrayList.add(paramFolder);

		QueryResult localQueryResult = getSharedFolderContents(paramFolder,
				SubFolder.class, true);
		while (localQueryResult.hasMoreElements()) {
			localArrayList.addAll(recurseFolders((Folder) localQueryResult
					.nextElement()));
		}
		return localArrayList;
	}

	private QueryResult getSharedFolderContents(Folder paramFolder,
			Class paramClass, boolean paramBoolean) throws WTException {
		WTHashSet localWTHashSet = new WTHashSet(2);
		localWTHashSet.add(paramFolder);
		WTKeyedMap localWTKeyedMap = FolderHelper.service
				.getFolderToContentsMap(localWTHashSet, paramClass,
						paramBoolean);
		WTSet localWTSet = (WTSet) localWTKeyedMap.get(paramFolder);
		if (localWTSet != null) {
			return new QueryResult(new ObjectVector(new Vector(
					localWTSet.persistableCollection())));
		}
		return new QueryResult();
	}

	private SharedContainerMap getShareMap(Persistable paramPersistable,
			WTContainerRef paramWTContainerRef) throws WTException {
		QueryResult localQueryResult = DataSharingHelper.service.getObjectMaps(
				paramPersistable, paramWTContainerRef, 1);
		if (localQueryResult.hasMoreElements()) {
			return (SharedContainerMap) localQueryResult.nextElement();
		}
		return null;
	}

	private String getJarPath(WTDocument paramWTDocument,
			WTContainerRef paramWTContainerRef) throws WTException {
		String str1 = "";
		if (!paramWTDocument.getContainerReference()
				.equals(paramWTContainerRef)) {
			SharedContainerMap localSharedContainerMap1 = getShareMap(
					paramWTDocument, paramWTContainerRef);
			Folder localFolder1 = null;
			if ((localSharedContainerMap1 != null)
					&& (localSharedContainerMap1.getTargetFolderRef() != null)
					&& (localSharedContainerMap1.getTargetFolderRef().getKey() != null)) {
				localFolder1 = (Folder) localSharedContainerMap1
						.getTargetFolderRef().getObject();
			}
			int i = (localFolder1 != null)
					&& (!localFolder1.getContainerReference().equals(
							paramWTContainerRef)) ? 1 : 0;
			if ((i == 0) && (localFolder1 != null)) {
				str1 = localFolder1.getFolderPath();
				str1 = str1.substring(str1.indexOf("/", 1) + 1) + "/";
			} else if (localFolder1 != null) {
				String str2 = localFolder1.getName() + "/";
				SharedContainerMap localSharedContainerMap2 = getShareMap(
						localFolder1, paramWTContainerRef);
				Folder localFolder2 = (Folder) localSharedContainerMap2
						.getTargetFolderRef().getObject();
				int j = !localFolder2.getContainerReference().equals(
						paramWTContainerRef) ? 1 : 0;
				while (j != 0) {
					str2 = localFolder2.getName() + "/" + str2;
					localSharedContainerMap2 = getShareMap(localFolder2,
							paramWTContainerRef);
					localFolder2 = (Folder) localSharedContainerMap2
							.getTargetFolderRef().getObject();
					j = !localFolder2.getContainerReference().equals(
							paramWTContainerRef) ? 1 : 0;
				}
				str1 = localFolder2.getFolderPath();
				str1 = str1.substring(str1.indexOf("/", 1) + 1) + "/" + str2;
			} else {
				try {
					str1 = paramWTDocument.getFolderPath();
					str1 = str1.substring(str1.indexOf("/", 1) + 1,
							str1.lastIndexOf("/") + 1);
				} catch (Exception localException2) {
					str1 = "";
				}
			}
		} else {
			try {
				str1 = paramWTDocument.getFolderPath();
				str1 = str1.substring(str1.indexOf("/", 1) + 1,
						str1.lastIndexOf("/") + 1);
			} catch (Exception localException1) {
				str1 = "";
			}
		}
		return str1;
	}

	private boolean isPDMLinkContainer(WTContainer paramWTContainer) {
		return ((paramWTContainer instanceof WTLibrary))
				|| ((paramWTContainer instanceof PDMLinkProduct));
	}

	public static void redirectRecipient(HTTPRequest paramHTTPRequest,
			HTTPResponse paramHTTPResponse) throws WTException {
		File localFile = null;
		try {
			Properties localProperties = HTTPRequest
					.splitQueryString(paramHTTPRequest.getQueryString());
			String str1 = localProperties.getProperty("sessId");
			if (VERBOSE) {
				System.out.println("session id = " + str1);
			}
			SessionContext localSessionContext = SessionContext
					.getContext(str1);
			if (localSessionContext == null) {
				throw new WTException("com.ptc.netmarkets.model.modelResource",
						"3", null);
			}
			String str2 = (String) localSessionContext.get("userName");
			if ((str2 == null) || (str2.equals(""))) {
				throw new NmException("com.ptc.netmarkets.model.modelResource",
						"4", null);
			}
			WTPrincipal localWTPrincipal = SessionHelper.manager.getPrincipal();
			String str3 = localWTPrincipal.getName();
			if (!str3.trim().equals(str2.trim())) {
				throw new NmException("com.ptc.netmarkets.model.modelResource",
						"5", null);
			}
			String str4 = (String) localSessionContext.get("fname");
			if (str4 == null) {
				throw new NmException("com.ptc.netmarkets.model.modelResource",
						"69", null);
			}
			localSessionContext.remove(str4);
			SessionContext.destroy("", str1);
			if (localProperties.getProperty("content-type") != null) {
				paramHTTPResponse.setHeader("content-type",
						localProperties.getProperty("content-type"));
			} else {
				paramHTTPResponse
						.setHeader("content-type", HEADER_CONTENT_TYPE);
			}
			if (localProperties.getProperty("attach") != null) {
				paramHTTPResponse.setHeader("content-disposition",
						"attachment;");
			}
			OutputStream localOutputStream = paramHTTPResponse
					.getOutputStream();

			localFile = new File(str4);
			if (localFile != null) {
				FileInputStream localFileInputStream = new FileInputStream(
						localFile);
				ProjectIXUtils.copyStream(localFileInputStream,
						localOutputStream);
				localFileInputStream.close();
			}
		} catch (FileNotFoundException localFileNotFoundException) {
			throw new NmException(localFileNotFoundException);
		} catch (IOException localIOException) {
			throw new NmException(localIOException);
		} finally {
			if (localFile != null) {
				localFile.delete();
			}
		}
	}

	private QueryResult getOverDueObjects(String paramString,
			ObjectIdentifier paramObjectIdentifier) throws WTException,
			WTPropertyVetoException {
		Object localObject = null;
		if (paramString.equals("deli")) {
			localObject = getQSForOverdueObjects(Deliverable.class,
					paramObjectIdentifier);
		} else if (paramString.equals("mile")) {
			localObject = getQSForOverdueObjects(Milestone.class,
					paramObjectIdentifier);
		} else if (paramString.equals("projActivity")) {
			localObject = getQSForOverdueObjects(ProjectActivity.class,
					paramObjectIdentifier);
		} else if (paramString.equals("all")) {
			QuerySpec localQuerySpec1 = getQSForOverdueObjects(
					Deliverable.class, paramObjectIdentifier);
			QuerySpec localQuerySpec2 = getQSForOverdueObjects(Milestone.class,
					paramObjectIdentifier);
			QuerySpec localQuerySpec3 = getQSForOverdueObjects(
					ProjectActivity.class, paramObjectIdentifier);
			CompoundQuerySpec localCompoundQuerySpec = new CompoundQuerySpec();
			localCompoundQuerySpec.setSetOperator(SetOperator.UNION);
			localCompoundQuerySpec.addComponent(localQuerySpec1);
			localCompoundQuerySpec.addComponent(localQuerySpec2);
			localCompoundQuerySpec.addComponent(localQuerySpec3);
			localObject = localCompoundQuerySpec;
		}
		return inflateQR(PersistenceHelper.manager
				.find((StatementSpec) localObject));
	}

	private QuerySpec getQSForOverdueObjects(Class paramClass,
			ObjectIdentifier paramObjectIdentifier) throws WTException,
			WTPropertyVetoException {
		if (VERBOSE) {
			System.out
					.println(" StandardNmObjectService.getQuerySpecForOverdueObjects:  IN");
		}
		QuerySpec localQuerySpec = getQSOwnables(paramClass,
				paramObjectIdentifier);
		Timestamp localTimestamp = new Timestamp(System.currentTimeMillis());
		localQuerySpec.appendAnd();
		localQuerySpec.appendWhere(new SearchCondition(paramClass, "deadline",
				"<", localTimestamp), new int[] { 0 });

		return localQuerySpec;
	}

	private void populateReportsTable(
			NmDefaultHTMLTable paramNmDefaultHTMLTable,
			QueryResult paramQueryResult, String paramString)
			throws WTException {
		if (VERBOSE) {
			System.out
					.println(" StandardNmObjectService.populateReportsTable(): IN");
		}
		int i = paramNmDefaultHTMLTable.getRowCount();
		Locale localLocale = SessionHelper.getLocale();
		while (paramQueryResult.hasMoreElements()) {
			Persistable localPersistable = (Persistable) paramQueryResult
					.nextElement();
			String str1 = "";
			String str2 = paramString;
			String str3 = "&nbsp;";
			Timestamp localTimestamp = null;
			Integer localInteger = null;
			ProjectState localProjectState = null;
			String str4 = null;
			NmString localNmString = null;
			Object localObject;
			if ((localPersistable instanceof ExecutionObject)) {
				localObject = (ExecutionObject) localPersistable;
				if (VERBOSE) {
					System.out
							.println(" StandardNmObjectService.populateReportsTable(): exexObj == "
									+ localObject);
				}
				str1 = ((ExecutionObject) localObject).getName();
				if ((localObject instanceof Deliverable)) {
					if (str2.equals("default")) {
						str2 = "deliverable row actions";
					}
					localProjectState = ((Deliverable) localObject)
							.getExecutionState();
				} else if ((localObject instanceof Milestone)) {
					if (str2.equals("default")) {
						str2 = "milestone row actions";
					}
					localProjectState = ProjExecHelper
							.getProjectState(((Milestone) localObject)
									.getState());
				} else if ((localObject instanceof ProjectActivity)) {
					if (str2.equals("default")) {
						str2 = "activity row actions";
					}
					localProjectState = ProjExecHelper
							.getProjectState(((ProjectActivity) localObject)
									.getState());
				} else {
					if ((localObject instanceof ProjectWorkItem)) {
						continue;
					}
				}
				str4 = localProjectState.getDisplay(localLocale);
				localNmString = NmUtilClassProxy
						.getStatus((ExecutionObject) localObject);
				localTimestamp = ((ExecutionObject) localObject).getDeadline();
				localInteger = new Integer(
						((ExecutionObject) localObject).getPercentComplete());

				str3 = WTPrincipalUtil.getOwnerFullName((Ownable) localObject);
			} else {
				localObject = new NmNamedObject();
				NmOid localNmOid = new NmOid(localPersistable);
				((NmNamedObject) localObject).setOid(localNmOid);
				((NmNamedObject) localObject).setName(str1);
				paramNmDefaultHTMLTable.addObject(i, (NmObject) localObject);

				String str5 = "";
				if ((localPersistable instanceof WTObject)) {
					str5 = ((WTObject) localPersistable).getDisplayType()
							.getLocalizedMessage(localLocale);
				}
				NmDate localNmDate = new NmDate();
				localNmDate.setDate(localTimestamp);
				localNmDate.setDisplayType(9);

				WTContainerRef localWTContainerRef = ((WTContained) localPersistable)
						.getContainerReference();
				if (((localWTContainerRef != null) && (Project2.class
						.isAssignableFrom(localWTContainerRef
								.getReferencedClass())))
						|| ((localPersistable instanceof Project2))) {
					NmHTMLActionModel localNmHTMLActionModel = NmActionServiceHelper.service
							.getActionModel(str2, localObject);
					paramNmDefaultHTMLTable.addCellValue(i, 0, localObject);
					paramNmDefaultHTMLTable.addCellValue(i, 1, str5);
					paramNmDefaultHTMLTable.addCellValue(i, 2,
							localNmHTMLActionModel);
					paramNmDefaultHTMLTable.addCellValue(i, 3, localNmDate);
					paramNmDefaultHTMLTable.addCellValue(i, 4, localInteger);
					paramNmDefaultHTMLTable.addCellValue(i, 5, str3);
					paramNmDefaultHTMLTable.addCellValue(i, 6, str4);
					paramNmDefaultHTMLTable.addCellValue(i, 7, localNmString);
					i++;
				}
			}
		}
	}

	private QueryResult getCompletedObjects(String paramString,
			ObjectIdentifier paramObjectIdentifier) throws WTException,
			WTPropertyVetoException {
		Object localObject = null;
		if (paramString.equals("deli")) {
			localObject = getQSForCompletedDelivs(Deliverable.class,
					paramObjectIdentifier);
		} else if (paramString.equals("mile")) {
			localObject = getQSForCompletedWfExecObjects(Milestone.class,
					paramObjectIdentifier);
		} else if (paramString.equals("projActivity")) {
			localObject = getQSForCompletedWfExecObjects(ProjectActivity.class,
					paramObjectIdentifier);
		} else if (paramString.equals("all")) {
			QuerySpec localQuerySpec1 = getQSForCompletedDelivs(
					Deliverable.class, paramObjectIdentifier);
			QuerySpec localQuerySpec2 = getQSForCompletedWfExecObjects(
					Milestone.class, paramObjectIdentifier);
			QuerySpec localQuerySpec3 = getQSForCompletedWfExecObjects(
					ProjectActivity.class, paramObjectIdentifier);
			CompoundQuerySpec localCompoundQuerySpec = new CompoundQuerySpec();
			localCompoundQuerySpec.setSetOperator(SetOperator.UNION);
			localCompoundQuerySpec.addComponent(localQuerySpec1);
			localCompoundQuerySpec.addComponent(localQuerySpec2);
			localCompoundQuerySpec.addComponent(localQuerySpec3);
			localObject = localCompoundQuerySpec;
		}
		return inflateQR(PersistenceHelper.manager
				.find((StatementSpec) localObject));
	}

	private QuerySpec getQSForCompletedDelivs(Class paramClass,
			ObjectIdentifier paramObjectIdentifier) throws WTException,
			WTPropertyVetoException {
		if (VERBOSE) {
			System.out
					.println(" StandardNmObjectService.getQSForCompletedDelivs:  IN");
		}
		QuerySpec localQuerySpec = getQSOwnables(paramClass,
				paramObjectIdentifier);
		localQuerySpec.appendAnd();
		localQuerySpec
				.appendWhere(new SearchCondition(paramClass, "executionState",
						"=", ProjectState.COMPLETED), new int[] { 0 });

		return localQuerySpec;
	}

	private QuerySpec getQSForCompletedWfExecObjects(Class paramClass,
			ObjectIdentifier paramObjectIdentifier) throws WTException,
			WTPropertyVetoException {
		if (VERBOSE) {
			System.out
					.println(" StandardNmObjectService.getQSForCompletedwfExecObjects:  IN");
		}
		QuerySpec localQuerySpec = getQSOwnables(paramClass,
				paramObjectIdentifier);
		localQuerySpec.appendAnd();
		localQuerySpec.appendOpenParen();
		localQuerySpec.appendWhere(new SearchCondition(paramClass, "state",
				"=", WfState.CLOSED_COMPLETED_EXECUTED), new int[] { 0 });

		localQuerySpec.appendOr();
		localQuerySpec.appendWhere(new SearchCondition(paramClass, "state",
				"=", WfState.CLOSED_COMPLETED_NOT_EXECUTED), new int[] { 0 });

		localQuerySpec.appendCloseParen();

		return localQuerySpec;
	}

	private QueryResult getInCompleteObjects(String paramString,
			ObjectIdentifier paramObjectIdentifier) throws WTException,
			WTPropertyVetoException {
		Object localObject = null;
		if (paramString.equals("deli")) {
			localObject = getQSForInCompleteDelivs(Deliverable.class,
					paramObjectIdentifier);
		} else if (paramString.equals("mile")) {
			localObject = getQSForInCompleteWfExecObjects(Milestone.class,
					paramObjectIdentifier);
		} else if (paramString.equals("projActivity")) {
			localObject = getQSForInCompleteWfExecObjects(
					ProjectActivity.class, paramObjectIdentifier);
		} else if (paramString.equals("all")) {
			QuerySpec localQuerySpec1 = getQSForInCompleteDelivs(
					Deliverable.class, paramObjectIdentifier);
			QuerySpec localQuerySpec2 = getQSForInCompleteWfExecObjects(
					Milestone.class, paramObjectIdentifier);
			QuerySpec localQuerySpec3 = getQSForInCompleteWfExecObjects(
					ProjectActivity.class, paramObjectIdentifier);
			CompoundQuerySpec localCompoundQuerySpec = new CompoundQuerySpec();
			localCompoundQuerySpec.setSetOperator(SetOperator.UNION);
			localCompoundQuerySpec.addComponent(localQuerySpec1);
			localCompoundQuerySpec.addComponent(localQuerySpec2);
			localCompoundQuerySpec.addComponent(localQuerySpec3);
			localObject = localCompoundQuerySpec;
		}
		return inflateQR(PersistenceHelper.manager
				.find((StatementSpec) localObject));
	}

	private QuerySpec getQSForInCompleteDelivs(Class paramClass,
			ObjectIdentifier paramObjectIdentifier) throws WTException,
			WTPropertyVetoException {
		if (VERBOSE) {
			System.out
					.println(" StandardNmObjectService.getQSForInCompleteDelivs:  IN");
		}
		QuerySpec localQuerySpec = getQSOwnables(paramClass,
				paramObjectIdentifier);
		localQuerySpec.appendAnd();
		localQuerySpec.appendWhere(new SearchCondition(paramClass,
				"executionState", "=", ProjectState.RUNNING), new int[] { 0 });

		return localQuerySpec;
	}

	private QuerySpec getQSForInCompleteWfExecObjects(Class paramClass,
			ObjectIdentifier paramObjectIdentifier) throws WTException,
			WTPropertyVetoException {
		if (VERBOSE) {
			System.out
					.println(" StandardNmObjectService.getQSForInCompleteWfExecObjects:  IN");
		}
		QuerySpec localQuerySpec = getQSOwnables(paramClass,
				paramObjectIdentifier);
		localQuerySpec.appendAnd();
		localQuerySpec.appendWhere(new SearchCondition(paramClass, "state",
				"=", WfState.OPEN_RUNNING), new int[] { 0 });

		return localQuerySpec;
	}

	private QuerySpec getQSOwnables(Class paramClass,
			ObjectIdentifier paramObjectIdentifier) throws WTException,
			WTPropertyVetoException {
		QuerySpec localQuerySpec = new QuerySpec();
		String str = "thePersistInfo.theObjectIdentifier.id";
		int i = localQuerySpec.appendClassList(paramClass, false);
		ConstantExpression localConstantExpression = new ConstantExpression(
				paramClass.getName());
		localConstantExpression.setColumnAlias("classname");
		localQuerySpec.appendSelect(localConstantExpression, false);
		localQuerySpec.appendSelectAttribute(str, i, false);

		WTUser localWTUser = (WTUser) PersistenceHelper.manager
				.refresh(paramObjectIdentifier);
		localQuerySpec.appendWhere(OwnershipHelper.getSearchCondition(
				paramClass, localWTUser, true), new int[] { 0 });

		return localQuerySpec;
	}

	private QueryResult inflateQR(QueryResult paramQueryResult)
			throws WTPropertyVetoException, WTException {
		if ((paramQueryResult != null) && (paramQueryResult.hasMoreElements())) {
			Object[] arrayOfObject = new Object[paramQueryResult.size()];
			int i = 0;
			while (paramQueryResult.hasMoreElements()) {
				arrayOfObject[i] = paramQueryResult.nextElement();
				i++;
			}
			paramQueryResult = PersistenceHelper.manager.find(new InflateSpec(
					arrayOfObject));
		}
		return paramQueryResult;
	}

	private NmDefaultHTMLTable initializeReportTable() throws WTException {
		Locale localLocale = SessionHelper.getLocale();

		NmDefaultHTMLTable localNmDefaultHTMLTable = new NmDefaultHTMLTable();
		localNmDefaultHTMLTable.setTableModel(new NmSearchHTMLTableModel());
		localNmDefaultHTMLTable.addColumn(WTMessage.getLocalizedMessage(
				"com.ptc.netmarkets.model.modelResource", "6", null,
				localLocale));
		localNmDefaultHTMLTable.addColumn(WTMessage.getLocalizedMessage(
				"com.ptc.netmarkets.model.modelResource", "7", null,
				localLocale));
		localNmDefaultHTMLTable.addColumn(WTMessage.getLocalizedMessage(
				"com.ptc.netmarkets.model.modelResource", "11", null,
				localLocale));
		localNmDefaultHTMLTable.addColumn("Deadline");
		localNmDefaultHTMLTable.addColumn("% Done");
		localNmDefaultHTMLTable.addColumn(" Owner");
		localNmDefaultHTMLTable.addColumn("State");
		localNmDefaultHTMLTable.addColumn("Status");
		return localNmDefaultHTMLTable;
	}

	private void rollBackMemberList(Project2 paramProject2,
			TeamExportHolder paramTeamExportHolder) {
		WTPrincipal localWTPrincipal1 = null;
		boolean bool = false;
		try {
			localWTPrincipal1 = SessionHelper.getPrincipal();
			bool = SessionServerHelper.manager.setAccessEnforced(false);
			WTPrincipal localWTPrincipal2 = SessionHelper.manager
					.getAdministrator();
			SessionHelper.manager.setPrincipal(localWTPrincipal2.getName());
			Role[] arrayOfRole = paramTeamExportHolder.getRoles();
			Iterator localIterator;
			for (int i = 0; i < arrayOfRole.length; i++) {
				List localList = paramTeamExportHolder
						.getMembers(arrayOfRole[i]);
				for (localIterator = localList.iterator(); localIterator
						.hasNext();) {
					WTPrincipalReference localWTPrincipalReference = (WTPrincipalReference) localIterator
							.next();
					WTUser localWTUser = (WTUser) localWTPrincipalReference
							.getPrincipal();
					((ContainerTeam) paramProject2.getContainerTeamReference()
							.getObject()).deletePrincipalTarget(arrayOfRole[i],
							localWTUser);
				}
			}
		} catch (WTException localWTException2) {
			localWTException2.printStackTrace();
		} finally {
			if (localWTPrincipal1 != null) {
				try {
					SessionHelper.manager.setPrincipal(localWTPrincipal1
							.getName());
				} catch (WTException localWTException4) {
				}
			}
			SessionServerHelper.manager.setAccessEnforced(bool);
		}
	}

	private NmNamedObject getNmNamedObjectFromOid(NmOid paramNmOid,
			Object paramObject, Locale paramLocale) throws WTException {
		NmNamedObject localNmNamedObject = new NmNamedObject();
		Object localObject;
		if ((paramObject instanceof NmAnyURL)) {
			localNmNamedObject.setName(((NmAnyURL) paramObject).getLabel());
			localObject = new NmSimpleOid();
			((NmSimpleOid) localObject)
					.setInternalName(((NmAnyURL) paramObject).getUrl());
			paramNmOid = (NmOid) localObject;
		} else if ((paramNmOid != null)
				&& (!(paramNmOid instanceof NmSimpleOid))) {
			localObject = (WTObject) paramNmOid.getRef();
			localNmNamedObject.setName(NmObjectHelper.getName(localObject,
					paramLocale));
		} else {
			localObject = NmObjectHelper.getName(paramObject, paramLocale);
			if (localObject != null) {
				localNmNamedObject.setName((String) localObject);
			} else {
				return null;
			}
		}
		localNmNamedObject.setOid(paramNmOid);
		return localNmNamedObject;
	}

	private String getTypeFromOid(NmOid paramNmOid, Object paramObject,
			Locale paramLocale) throws WTException {
		String str = null;
		if ((paramNmOid instanceof NmSimpleOid)) {
			str = WTMessage.getLocalizedMessage(
					"com.ptc.netmarkets.model.modelResource", "18", null,
					paramLocale);
		} else {
			WTObject localWTObject = (WTObject) paramNmOid.getRef();
			if (localWTObject != null) {
				str = localWTObject.getDisplayType().getLocalizedMessage(
						paramLocale);
			}
		}
		return str;
	}

	protected static ArrayList parseEncodedField(String paramString1,
			String paramString2) {
		ArrayList localArrayList = new ArrayList();
		int i = -1;
		int j = 0;
		if ((paramString1 != null) && (paramString1.length() > 0)) {
			do {
				j = paramString1.indexOf(paramString2, i + 1);
				if (j == -1) {
					localArrayList.add(paramString1.substring(i + 1,
							paramString1.length()));
					i = paramString1.length();
				} else {
					localArrayList.add(paramString1.substring(i + 1, j));
					i += paramString1.substring(i + 1, j).length()
							+ paramString2.length();
				}
			} while (i < paramString1.length());
		}
		return localArrayList;
	}

	private void checkValidShareMapForDelete(
			SharedContainerMap paramSharedContainerMap) throws WTException {
		if ((paramSharedContainerMap.getShareKey() != paramSharedContainerMap
				.getPersistInfo().getObjectIdentifier().getId())
				&& (paramSharedContainerMap.getShareType() == 3)) {
			Locale localLocale = SessionHelper.getLocale();
			String str = WTMessage.getLocalizedMessage(
					"wt.inf.sharing.sharingResource", "5", null, localLocale);
			Object[] arrayOfObject = {
					((WTObject) paramSharedContainerMap.getShared())
							.getDisplayIdentifier(), str };
			throw new NmException(WTMessage.getLocalizedMessage(
					"wt.inf.sharing.sharingResource", "2", arrayOfObject,
					localLocale));
		}
	}

	private FormResult list__delete(NmCommandBean paramNmCommandBean,
			ArrayList paramArrayList, boolean paramBoolean) throws WTException {
		if (this.deleteLogger.isInfoEnabled()) {
			this.deleteLogger.info("=> list__delete: " + paramArrayList
					+ ", do = " + paramBoolean + ", oid = "
					+ paramNmCommandBean.getPrimaryOid());
		}
		FormResult localFormResult = new FormResult();
		localFormResult.setNextAction(FormResultAction.NONE);
		ArrayList localArrayList1 = paramNmCommandBean.getSelected();
		if (this.deleteLogger.isInfoEnabled()) {
			this.deleteLogger.info("   # selected objects = "
					+ localArrayList1.size());
		}
		if (localArrayList1 == null) {
			if (this.deleteLogger.isInfoEnabled()) {
				this.deleteLogger
						.info("   list__delete - OUT: nothing selected");
			}
			return localFormResult;
		}
		WTContainerRef localWTContainerRef = getContainerRef(
				paramNmCommandBean, null);
		int i = 0;
		Transaction localTransaction = new Transaction();
		NmChangeModel[] arrayOfNmChangeModel = new NmChangeModel[1];
		arrayOfNmChangeModel[0] = new NmChangeModel();
		arrayOfNmChangeModel[0].setType(2);
		try {
			if (!paramBoolean) {
				localTransaction.start();
				i = 1;
			}
			ArrayList localArrayList2 = new ArrayList();

			WTHashSet localWTHashSet1 = getSelectedObjects(localArrayList1,
					arrayOfNmChangeModel, localWTContainerRef);

			WTHashSet localWTHashSet2 = new WTHashSet(localWTHashSet1.size());
			ArrayList localArrayList3 = new ArrayList(localWTHashSet1.size());
			for (Iterator localIterator = localWTHashSet1.persistableIterator(); localIterator
					.hasNext();) {
				Persistable localPersistable1 = (Persistable) localIterator
						.next();
				if (localPersistable1 != null) {
					if (this.deleteLogger.isInfoEnabled()) {
						this.deleteLogger.info("DELETE object "
								+ localPersistable1.getPersistInfo()
										.getObjectIdentifier());
					}
					if ((localPersistable1 instanceof Project2)) {
						paramNmCommandBean.setPrimaryOid(new NmOid(
								localPersistable1));
						NmProjectHelper.service.delete(paramNmCommandBean);
					} else {
						if ((localPersistable1 instanceof ProjectPlan)) {
							throw new WTException(
									"com.ptc.netmarkets.projectPlan.projectPlanResource",
									"51", null);
						}
						Object localObject1;
						if ((localPersistable1 instanceof NotebookFolder)) {
							if (i == 0) {
								localTransaction.start();
								i = 1;
								paramBoolean = false;
							}
							localObject1 = (NotebookFolder) localPersistable1;
							if (((NotebookFolder) localObject1).isHotlist()) {
								throw new NmException(
										"com.ptc.netmarkets.model.modelResource",
										"53", null);
							}
							NotebookHelper.service
									.deleteFolder((NotebookFolder) localObject1);
						} else {
							Object localObject2;
							boolean bool3;
							Object localObject3;
							Object localObject4;
							Object localObject5;
							if ((localPersistable1 instanceof FolderedBookmark)) {
								localObject1 = (FolderedBookmark) localPersistable1;
								localObject2 = FolderHelper.service
										.getFolder((FolderEntry) localObject1);
								boolean bool1 = AccessControlHelper.manager
										.hasAccess(localObject2,
												AccessPermission.MODIFY);

								bool3 = false;

								WTPrincipalReference localWTPrincipalReference1 = ((Bookmark) localObject1)
										.getCreator();
								localObject3 = null;
								if (localWTPrincipalReference1 != null) {
									localObject3 = localWTPrincipalReference1
											.getIdentity();
								}
								localObject4 = SessionHelper.manager
										.getPrincipal();
								localObject5 = null;
								if (localObject4 != null) {
									localObject5 = ((WTPrincipal) localObject4)
											.getIdentity();
								}
								if ((localObject3 != null)
										&& (localObject5 != null)) {
									bool3 = ((String) localObject3)
											.equals(localObject5);
								}
								if ((bool1) || (bool3)) {
									addTaskList(localArrayList3,
											localPersistable1);
								} else {
									throw new NotAuthorizedException(
											null,
											"com.ptc.netmarkets.model.modelResource",
											"69", null);
								}
							} else {
								Object localObject6;
								if ((localPersistable1 instanceof ImportedBookmark)) {
									localObject1 = (ImportedBookmark) localPersistable1;
									localObject2 = null;
									Persistable localPersistable2 = null;
									if (localObject1 != null) {
										localObject2 = NotebookUtil
												.getSubject(
														((ImportedBookmark) localObject1)
																.getParent(),
														false);
										if (localObject2 != null) {
											localPersistable2 = ((WTReference) localObject2)
													.getObject();
										}
									}
									bool3 = AccessControlHelper.manager
											.hasAccess(localPersistable2,
													AccessPermission.DELETE);

									boolean bool4 = false;

									localObject3 = ((ImportedBookmark) localObject1)
											.getCreator();
									localObject4 = null;
									if (localObject3 != null) {
										localObject4 = ((WTPrincipalReference) localObject3)
												.getIdentity();
									}
									localObject5 = SessionHelper.manager
											.getPrincipal();
									localObject6 = null;
									if (localObject5 != null) {
										localObject6 = ((WTPrincipal) localObject5)
												.getIdentity();
									}
									if ((localObject4 != null)
											&& (localObject6 != null)) {
										bool4 = ((String) localObject4)
												.equals(localObject6);
									}
									if ((bool3) || (bool4)) {
										addTaskList(localArrayList3,
												localPersistable1);
									} else {
										NmConsoleOpenException localNmConsoleOpenException = new NmConsoleOpenException(
												"");
										throw localNmConsoleOpenException;
									}
								} else if ((localPersistable1 instanceof DiscussionPosting)) {
									localObject1 = (DiscussionPosting) localPersistable1;
									localObject2 = null;
									localObject2 = ((DiscussionPosting) localObject1)
											.getSubjectObjectReference()
											.getObject();
									boolean bool2 = AccessControlHelper.manager
											.hasAccess(localObject2,
													AccessPermission.DELETE);
									bool3 = false;
									WTPrincipalReference localWTPrincipalReference2 = ((DiscussionPosting) localObject1)
											.getOwnership().getOwner();
									localObject3 = null;
									if (localWTPrincipalReference2 != null) {
										localObject3 = localWTPrincipalReference2
												.getIdentity();
									}
									localObject4 = SessionHelper.manager
											.getPrincipal();
									localObject5 = null;
									if (localObject4 != null) {
										localObject5 = ((WTPrincipal) localObject4)
												.getIdentity();
									}
									if ((localObject3 != null)
											&& (localObject5 != null)) {
										bool3 = ((String) localObject3)
												.equals(localObject5);
									}
									if ((bool2) || (bool3)) {
										addTaskList(localArrayList3,
												localPersistable1);
									} else {
										localObject6 = new NmConsoleOpenException(
												"");
										// throw ((Throwable)localObject6);
									}
								} else if ((localPersistable1 instanceof WTTypeDefinition)) {
									if (i == 0) {
										localTransaction.start();
										i = 1;
										paramBoolean = false;
									}
									try {
										localObject1 = (WTTypeDefinition) localPersistable1;
										localObject2 = TypeDefinitionObjectsFactory
												.newTypeDefinitionDefaultView((TypeDefinition) localObject1);
										TypeDefinitionNodeView localTypeDefinitionNodeView = ((TypeDefinitionDefaultView) localObject2)
												.toTypeDefinitionNodeView();
										String str = TypeDomainHelper
												.getDomain(localTypeDefinitionNodeView
														.getName());
										localTypeDefinitionNodeView = TypeAdminHelper.service
												.checkoutTypeNode(
														str,
														localTypeDefinitionNodeView,
														SessionHelper
																.getLocale());
										TypeAdminHelper.service
												.deleteTypeNode(localTypeDefinitionNodeView);
									} catch (RemoteException localRemoteException) {
									}
								} else if ((localPersistable1 instanceof NotificationSubscription)) {
									NotificationHelper.manager
											.deleteSubscription((NotificationSubscription) localPersistable1);
								} else if ((localPersistable1 instanceof RevisionControlled)) {
									localFormResult = getPageRefreshActionForDelete(paramNmCommandBean);
									addToAppropriateDeleteSet(paramBoolean,
											localArrayList3, localWTHashSet2,
											localPersistable1);
								} else if ((localArrayList1.size() == 1)
										&& ((localPersistable1 instanceof SharedContainerMap))) {
									localFormResult = getPageRefreshActionForDeletingSharedFolder(
											localFormResult,
											(SharedContainerMap) localPersistable1);
									addToAppropriateDeleteSet(paramBoolean,
											localArrayList3, localWTHashSet2,
											localPersistable1);
								} else {
									addToAppropriateDeleteSet(paramBoolean,
											localArrayList3, localWTHashSet2,
											localPersistable1);
									if ((localPersistable1 instanceof WTPartAlternateLink)) {
										localArrayList2
												.add((WTPartAlternateLink) localPersistable1);
									}
								}
							}
						}
					}
				}
			}
			if (this.deleteLogger.isInfoEnabled()) {
				this.deleteLogger.info("deleteTaskList = " + localArrayList3);
			}
			if ((paramBoolean) && (localArrayList3.size() > 0)) {
				removeObjectsBackground(localArrayList3);
			} else {
				SandboxHelper.service.removeObjects(localWTHashSet2);
			}
			if (i != 0) {
				localTransaction.commit();
				localTransaction = null;
			}
		} catch (WTPropertyVetoException localWTPropertyVetoException) {
			throw new WTException(localWTPropertyVetoException);
		} finally {
			if ((i != 0) && (localTransaction != null)) {
				localTransaction.rollback();
			}
		}
		if (this.deleteLogger.isInfoEnabled()) {
			this.deleteLogger.info("   list__delete - OUT: "
					+ arrayOfNmChangeModel[0].getKeys());
		}
		return localFormResult;
	}

	private void addToAppropriateDeleteSet(boolean paramBoolean,
			ArrayList paramArrayList, WTHashSet paramWTHashSet,
			Persistable paramPersistable) throws WTException {
		if (paramBoolean) {
			addTaskList(paramArrayList, paramPersistable);
		} else {
			paramWTHashSet.add(paramPersistable);
		}
	}

	private FormResult getPageRefreshActionForDeletingSharedFolder(
			FormResult paramFormResult,
			SharedContainerMap paramSharedContainerMap) throws WTException {
		ObjectIdentifier localObjectIdentifier = (ObjectIdentifier) paramSharedContainerMap
				.getSharedObjectRef().getKey();
		try {
			if ((localObjectIdentifier != null)
					&& (SubFolder.class.isAssignableFrom(Class
							.forName(localObjectIdentifier.getClassname())))) {
				NmOid localNmOid = new NmOid();
				localNmOid.setWtRef(paramSharedContainerMap
						.getTargetFolderRef());
				NmAction localNmAction = NmActionServiceHelper.service
						.getAction("object", "view");
				localNmAction.setContextObject(localNmOid);
				paramFormResult.setURL(localNmAction.getActionUrlExternal());
				paramFormResult.setNextAction(FormResultAction.LOAD_OPENER_URL);
			}
		} catch (ClassNotFoundException localClassNotFoundException) {
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return paramFormResult;
	}

	private FormResult getPageRefreshActionForDelete(
			NmCommandBean paramNmCommandBean) throws WTException {
		FormResult localFormResult = new FormResult();
		localFormResult.setStatus(FormProcessingStatus.SUCCESS);

		TableViewDescriptor localTableViewDescriptor = getTableViewDescriptor(paramNmCommandBean);
		if ((localTableViewDescriptor != null)
				&& (viewShowsSingleRevision(localTableViewDescriptor))) {
			localFormResult.addExtraData("refreshTableToExposeNewLatest",
					Boolean.valueOf(true));
		}
		return localFormResult;
	}

	protected TableViewDescriptor getTableViewDescriptor(
			NmCommandBean paramNmCommandBean) throws WTException {
		String str = paramNmCommandBean.getTextParameter("tableId");
		if (str != null) {
			str = RenderUtil.getOriginalTableId(str);
			if (isTableConfigurable(str)) {
				return getActiveView(str);
			}
		}
		return null;
	}

	protected boolean isTableConfigurable(String paramString) {
		return ConfigurableTableHelper.isTableConfigurable(paramString);
	}

	protected TableViewDescriptor getActiveView(String paramString)
			throws WTException {
		return TableViewDescriptorHelper.getCurrentActiveView(paramString,
				Locale.getDefault());
	}

	private boolean viewShowsSingleRevision(
			TableViewDescriptor paramTableViewDescriptor) {
		boolean bool = false;
		Vector localVector = paramTableViewDescriptor.getTableViewCriterion();
		Iterator localIterator;
		if (localVector != null) {
			for (localIterator = localVector.iterator(); localIterator
					.hasNext();) {
				TableViewCriterion localTableViewCriterion = (TableViewCriterion) localIterator
						.next();
				if (("objVersion".equals(localTableViewCriterion
						.getColumnName()))
						&& ("EQUALTO".equals(localTableViewCriterion
								.getOperator()))
						&& ("latest"
								.equals(localTableViewCriterion.getValue1()))) {
					bool = true;
					break;
				}
			}
		}
		return bool;
	}

	private WTContainerRef getContainerRef(NmCommandBean paramNmCommandBean,
			NmOid paramNmOid) throws WTException {
		WTContainerRef localWTContainerRef = null;
		WTContainer localWTContainer = paramNmCommandBean.getViewingContainer();
		if (localWTContainer != null) {
			localWTContainerRef = WTContainerRef
					.newWTContainerRef(localWTContainer);
		}
		if ((localWTContainerRef == null) && (paramNmOid != null)) {
			localWTContainerRef = paramNmOid.getContainerRef();
		}
		return localWTContainerRef;
	}

	private WTHashSet getSelectedObjects(List paramList,
			NmChangeModel[] paramArrayOfNmChangeModel,
			WTContainerRef paramWTContainerRef) throws WTException {
		WTHashSet localWTHashSet = new WTHashSet(paramList.size());
		WTKeyedHashMap localWTKeyedHashMap = new WTKeyedHashMap();
		for (Object localObject1 : paramList) {
			if (VERBOSE) {
				System.out.println("=> getSelectedObject: " + localObject1
						+ ", class = " + localObject1.getClass().getName()
						+ ", container = " + paramWTContainerRef);
			}
			if ((localObject1 instanceof Persistable)) {
				localWTHashSet.add((Persistable) localObject1);
			} else {
				NmOid localNmOid = null;
				if ((localObject1 instanceof NmOid)) {
					localNmOid = (NmOid) localObject1;
				} else if ((localObject1 instanceof NmContext)) {
					localNmOid = ((NmContext) localObject1).getTargetOid();
					if (localNmOid == null) {
						localNmOid = ((NmContext) localObject1).getPrimaryOid();
					}
				} else {
					throw new WTRuntimeException("Class not handled: "
							+ localObject1.getClass().getName());
				}
				Persistable localPersistable = (Persistable) localNmOid
						.getRefObject();
				if (localPersistable == null) {
					if (this.deleteLogger.isInfoEnabled()) {
						this.deleteLogger.info("BYPASSED DELETE of object "
								+ localObject1);
					}
				} else {
					if ((localPersistable instanceof SharedContainerMap)) {
						localWTHashSet.add(localPersistable);
					} else {
						WTContainerRef localWTContainerRef = null;
						if (localNmOid.getSharedContainer() != null) {
							localWTContainerRef = localNmOid
									.getSharedContainer()
									.getContainerReference();
						}
						if (localWTContainerRef == null) {
							localWTContainerRef = paramWTContainerRef;
						}
						if (localWTContainerRef != null) {
							Object localObject2 = (WTSet) localWTKeyedHashMap
									.get(localWTContainerRef);
							if (localObject2 == null) {
								localObject2 = new WTHashSet();
								localWTKeyedHashMap.put(localWTContainerRef,
										localObject2);
							}
							((WTSet) localObject2).add(localPersistable);
						} else {
							localWTHashSet.add(localPersistable);
						}
					}
					paramArrayOfNmChangeModel[0].put(localNmOid, null, null);
					if (VERBOSE) {
						System.out.println("   getSelectedObject - OUT: "
								+ localPersistable);
					}
				}
			}
		}
		if (!localWTKeyedHashMap.isEmpty()) {
			processSharedMaps(localWTHashSet, localWTKeyedHashMap);
		}
		return localWTHashSet;
	}

	private void processSharedMaps(WTHashSet paramWTHashSet,
			WTKeyedMap paramWTKeyedMap) throws WTException {
		WTHashSet localWTHashSet = new WTHashSet();
		for (Object localObject1 = paramWTKeyedMap.entrySet().iterator(); ((Iterator) localObject1)
				.hasNext();) {
			Object localObject2 = ((Iterator) localObject1).next();
			Object localObject3 = (WTKeyedMap.WTEntry) localObject2;
			Object localObject4 = (WTContainerRef) ((WTKeyedMap.WTEntry) localObject3)
					.getKeyAsReference();
			WTSet localWTSet = (WTSet) ((WTKeyedMap.WTEntry) localObject3)
					.getValue();

			WTCollection localWTCollection = DataSharingHelper.service
					.getAllShareMaps(localWTSet, (WTContainerRef) localObject4,
							1);
			Iterator localIterator;
			Object localObject5;
			if ((localWTCollection != null) && (!localWTCollection.isEmpty())) {
				localIterator = localWTCollection.persistableIterator();
				while (localIterator.hasNext()) {
					localObject5 = localIterator.next();
					if ((localObject5 instanceof SharedContainerMap)) {
						SharedContainerMap localSharedContainerMap = (SharedContainerMap) localObject5;
						checkValidShareMapForDelete(localSharedContainerMap);
						paramWTHashSet.add(localSharedContainerMap);

						removeObject(localWTSet, localSharedContainerMap);
					}
				}
			}
			if (!localWTSet.isEmpty()) {
				localIterator = localWTSet.persistableIterator();
				while (localIterator.hasNext()) {
					localObject5 = (Persistable) localIterator.next();
					if (((localObject5 instanceof WTContainer))
							|| ((localObject4 != null)
									&& ((localObject5 instanceof Iterated))
									&& ((localObject5 instanceof WTContained))
									&& (!((WTContained) localObject5)
											.getContainerReference().equals(
													localObject4)) && (Project2.class
										.isAssignableFrom(((WTContainerRef) localObject4)
												.getReferencedClass())))) {
						localWTHashSet.add((Persistable) localObject5);
					} else {
						paramWTHashSet.add((Persistable) localObject5);
					}
				}
			}
		}
		Object localObject2;
		Object localObject3;
		Object localObject4;
		WTSet localWTSet;
		if (!localWTHashSet.isEmpty()) {
			HashMap localObject1 = new HashMap(IOPState.getIOPStateSet().length);

			SandboxHelper.sortObjectsByIOPStates(localWTHashSet, null,
					(Map) localObject1);

			localObject2 = new WTHashSet();
			localObject3 = (WTSet) ((Map) localObject1)
					.get(IOPState.IOP_DEPRECATED);
			localObject4 = (WTSet) ((Map) localObject1)
					.get(IOPState.IOP_ABANDONED);
			localWTSet = (WTSet) ((Map) localObject1)
					.get(IOPState.IOP_TERMINAL);
			if ((localObject3 != null) && (!((WTSet) localObject3).isEmpty())) {
				((WTSet) localObject2).addAll((Collection) localObject3);
			}
			if ((localObject4 != null) && (!((WTSet) localObject4).isEmpty())) {
				((WTSet) localObject2).addAll((Collection) localObject4);
			}
			if ((localWTSet != null) && (!localWTSet.isEmpty())) {
				((WTSet) localObject2).addAll(localWTSet);
			}
			if (!((WTSet) localObject2).isEmpty()) {
				paramWTHashSet.addAll(((WTSet) localObject2)
						.persistableCollection());
				localWTHashSet.removeAll((Collection) localObject2);
			}
		}
		if (!localWTHashSet.isEmpty()) {
			Iterator localObject1 = localWTHashSet.persistableIterator();
			while (((Iterator) localObject1).hasNext()) {
				localObject2 = (Persistable) ((Iterator) localObject1).next();
				this.deleteLogger
						.warn("Skip delete of object because it appears to be a shared object recently unshared on a stale page."
								+ localObject2);
			}
		}
	}

	private void removeObject(WTSet paramWTSet,
			SharedContainerMap paramSharedContainerMap) throws WTException {
		Object localObject1;
		QueryKey localQueryKey;
		Object localObject3;
		Object localObject4;
		if (paramSharedContainerMap.isRevisionShare()) {
			localObject1 = paramSharedContainerMap.getSharedMasterRef();
			localQueryKey = ((ObjectReference) localObject1).getKey();
			Iterator localIterator = paramWTSet.persistableIterator();
			while (localIterator.hasNext()) {
				Object localObject2 = localIterator.next();
				if ((localObject2 instanceof Iterated)) {
					localObject3 = (Iterated) localObject2;
					localObject4 = ((Iterated) localObject3).getMaster()
							.getPersistInfo().getObjectIdentifier();
					if ((localObject4 != null)
							&& (((ObjectIdentifier) localObject4)
									.equals(localQueryKey))) {
						localIterator.remove();
					}
				}
			}
		} else if (paramSharedContainerMap.isVersionShare()) {
			localObject1 = paramSharedContainerMap.getSharedVersionRef();
			localQueryKey = ((VersionReference) localObject1).getKey();
			long l = -1L;
			if ((localQueryKey instanceof VersionForeignKey)) {
				l = ((VersionForeignKey) localQueryKey).getBranchId();
			}
			if (l != -1L) {
				localObject3 = paramWTSet.persistableIterator();
				while (((Iterator) localObject3).hasNext()) {
					localObject4 = ((Iterator) localObject3).next();
					if ((localObject4 instanceof Iterated)) {
						Iterated localIterated = (Iterated) localObject4;
						if (localIterated.getBranchIdentifier() == l) {
							((Iterator) localObject3).remove();
						}
					}
				}
			}
		} else {
			paramWTSet.remove(paramSharedContainerMap.getSharedObjectRef());
		}
	}

	private void removeObjectsBackground(ArrayList paramArrayList)
			throws WTException {
		if (this.deleteLogger.isInfoEnabled()) {
			this.deleteLogger.info("=> removeObjectsBackground: "
					+ paramArrayList);
		}
		try {
			Object localObject1 = ConsoleClassProxy.createDeleteTask();
			Object localObject2 = ConsoleClassProxy.createTaskData();
			ConsoleClassProxy.setTaskObjects(localObject2, paramArrayList);
			ConsoleClassProxy.setTaskData(localObject1, localObject2);
			SessionHelper.getLocale();
			Object localObject3 = ConsoleClassProxy.runTask(localObject1);
			Method localMethod = TaskResultClass.getMethod("isSuccess",
					new Class[0]);
			Object localObject4 = localMethod.invoke(localObject3,
					new Object[0]);
			if (((localObject4 instanceof Boolean))
					&& (!((Boolean) localObject4).booleanValue())) {
				NmConsoleOpenException localNmConsoleOpenException = new NmConsoleOpenException(
						"");
				throw localNmConsoleOpenException;
			}
		} catch (CollectionContainsDeletedException localCollectionContainsDeletedException) {
			throw new WTException(localCollectionContainsDeletedException);
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new WTException(localClassNotFoundException);
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new WTException(localNoSuchMethodException);
		} catch (InstantiationException localInstantiationException) {
			throw new WTException(localInstantiationException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new WTException(localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new WTException(localInvocationTargetException);
		}
		if (VERBOSE) {
			System.out.println("   removeObjectsBackground - OUT");
		}
	}

	private void addTaskList(ArrayList paramArrayList,
			Persistable paramPersistable) throws WTException {
		try {
			Object localObject = ConsoleClassProxy
					.createTaskObject(paramPersistable);
			ConsoleClassProxy.setObjectOnTaskDataObject(localObject,
					paramPersistable);
			paramArrayList.add(localObject);
		} catch (NoSuchMethodException localNoSuchMethodException) {
			throw new WTException(localNoSuchMethodException);
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new WTException(localClassNotFoundException);
		} catch (InstantiationException localInstantiationException) {
			throw new WTException(localInstantiationException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new WTException(localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			throw new WTException(localInvocationTargetException);
		}
	}

	private static void persistPrimaryURL(
			FormatContentHolder paramFormatContentHolder, String paramString)
			throws WTException, WTPropertyVetoException, PropertyVetoException {
		if ((paramString.indexOf("//") == -1)
				&& (paramString.indexOf("\\\\") == -1)) {
			paramString = "http://" + paramString;
		}
		if (VERBOSE) {
			System.out.println("\npersistPrimaryURL:  " + paramString);
		}
		Object localObject1 = paramFormatContentHolder;
		localObject1 = ContentHelper.service
				.getContents((ContentHolder) localObject1);
		ContentItem localContentItem = ContentHelper
				.getPrimary((FormatContentHolder) localObject1);
		URLData localURLData = null;
		Transaction localTransaction = new Transaction();
		try {
			localTransaction.start();
			if (localContentItem == null) {
				localURLData = URLData.newURLData((ContentHolder) localObject1);
			} else if ((localContentItem instanceof URLData)) {
				localURLData = (URLData) localContentItem;
				String str = localURLData.getUrlLocation();
				if (paramString.equals(str)) {
					return;
				}
			} else if ((localContentItem instanceof ApplicationData)) {
				ContentServerHelper.service.deleteContent(
						(ContentHolder) localObject1, localContentItem);

				localURLData = URLData.newURLData((ContentHolder) localObject1);
			}
			localURLData.setUrlLocation(paramString);
			localURLData.setRole(ContentRoleType.toContentRoleType("PRIMARY"));
			localURLData = ContentServerHelper.service.updateContent(
					(ContentHolder) localObject1, localURLData);
			localObject1 = ContentServerHelper.service
					.updateHolderFormat((FormatContentHolder) localObject1);
			localTransaction.commit();
			localTransaction = null;
		} finally {
			if (localTransaction != null) {
				localTransaction.rollback();
			}
		}
	}

	private void addSCMIPrimaryContent(ContentHolder paramContentHolder,
			String paramString) throws WTException {
		if (paramString.trim().length() > 0) {
			ScmFacade.getInstance().createScmData(paramContentHolder,
					paramString, null);
		}
	}

	private void setTemplatesFiltered(NmOid paramNmOid,
			WTContainer paramWTContainer, Boolean paramBoolean)
			throws WTException {
		Persistable localPersistable = (Persistable) paramNmOid.getRefObject();

		WTContainedFilterable localWTContainedFilterable = null;
		if (WTContainedFilterable.class.isAssignableFrom(localPersistable
				.getClass())) {
			localWTContainedFilterable = (WTContainedFilterable) localPersistable;
		} else {
			Mastered localMastered = null;
			if (IteratedFolderResident.class.isAssignableFrom(localPersistable
					.getClass())) {
				localMastered = ((IteratedFolderResident) localPersistable)
						.getMaster();
			} else if ((localPersistable instanceof TaskFormTemplate)) {
				localMastered = ((TaskFormTemplate) localPersistable)
						.getMaster();
			} else if ((localPersistable instanceof EPMDocument)) {
				localMastered = ((EPMDocument) localPersistable).getMaster();
			} else if ((localPersistable instanceof WfProcessTemplate)) {
				localMastered = ((WfProcessTemplate) localPersistable)
						.getMaster();
			} else if ((localPersistable instanceof DefaultWTContainerTemplate)) {
				localMastered = ((DefaultWTContainerTemplate) localPersistable)
						.getMaster();
			}
			if (WTContainedFilterable.class.isAssignableFrom(localMastered
					.getClass())) {
				localWTContainedFilterable = (WTContainedFilterable) localMastered;
			}
		}
		if (localWTContainedFilterable != null) {
			try {
				WTContainerHelper.service.setFiltered(
						localWTContainedFilterable, paramWTContainer,
						paramBoolean.booleanValue());
			} catch (WTPropertyVetoException localWTPropertyVetoException) {
				throw new WTException(
						localWTPropertyVetoException.getLocalizedMessage());
			}
		}
	}

	private static WTContainerRef getDefaultContainerRef() throws WTException {
		return WTContainerHelper.service.getExchangeRef();
	}

	public static final File getSaveDirectory() throws WTException {
		saveDirectory = StandardIXBService.getSaveDirectoryOnServer();

		return saveDirectory;
	}

	protected UIValidationStatus getAttributeVisibilityStatus(
			AttributeIdentifier paramAttributeIdentifier) throws WTException {
		AttributeTypeIdentifier localAttributeTypeIdentifier = (AttributeTypeIdentifier) paramAttributeIdentifier
				.getDefinitionIdentifier();
		String str = getAttributeKeyUsedForVisibility(localAttributeTypeIdentifier);
		if (str == null) {
			return UIValidationStatus.ENABLED;
		}
		ArrayList localArrayList = new ArrayList(1);
		localArrayList.add(new UIValidationKey(str, null));

		UIValidationResultSet localUIValidationResultSet = UIComponentValidationHelper.service
				.performLimitedPreValidation(localArrayList,
						new UIValidationCriteria(), SessionHelper.getLocale());
		return ((UIValidationResult) localUIValidationResultSet.getAllResults()
				.get(0)).getStatus();
	}

	protected String getAttributeKeyUsedForVisibility(
			AttributeTypeIdentifier paramAttributeTypeIdentifier)
			throws WTException {
		if (paramAttributeTypeIdentifier != null) {
			return "ATTRIBUTE:"
					+ paramAttributeTypeIdentifier.getAttributeName();
		}
		return null;
	}

	protected ObjectVectorIfc processSelectedObjectsForDocuments(
			Object paramObject, ArrayList paramArrayList, boolean paramBoolean)
			throws WTException {
		Object localObject = new ObjectVector();
		if (isRootSelected(paramObject)) {
			localObject = processSelectedObjectsForDocuments(paramArrayList,
					paramBoolean);
		} else if (paramArrayList.size() == 0) {
			Folder localFolder = (Folder) paramObject;
			if (localFolder != null) {
				localObject = findFolderDocuments(localFolder, paramBoolean,
						(ObjectVectorIfc) localObject);
			}
		} else {
			localObject = processSelectedObjectsForDocuments(paramArrayList,
					paramBoolean);
		}
		return (ObjectVectorIfc) localObject;
	}

	protected boolean isRootSelected(Object paramObject) {
		if (((paramObject instanceof Project2))
				|| ((paramObject instanceof Cabinet))) {
			return true;
		}
		return false;
	}

	protected ObjectVectorIfc processSelectedObjectsForDocuments(
			ArrayList paramArrayList, boolean paramBoolean) throws WTException {
		Object localObject1 = new ObjectVector();
		for (int i = 0; i < paramArrayList.size(); i++) {
			Object localObject2 = paramArrayList.get(i);
			Object localObject3;
			if ((localObject2 instanceof NmContext)) {
				localObject3 = (NmContext) localObject2;
				localObject2 = ((NmContext) localObject3).getTargetOid();
			} else if ((localObject2 instanceof NmObject)) {
				localObject3 = (NmObject) localObject2;
				localObject2 = ((NmObject) localObject3).getOid();
			}
			if ((localObject2 instanceof NmOid)) {
				localObject3 = (NmOid) localObject2;
				localObject2 = ((NmOid) localObject3).getRef();
			}
			if ((localObject2 instanceof SubFolder)) {
				localObject3 = (Folder) localObject2;
				localObject1 = findFolderDocuments((Folder) localObject3,
						paramBoolean, (ObjectVectorIfc) localObject1);
			} else if ((localObject2 instanceof WTDocument)) {
				((ObjectVectorIfc) localObject1).addElement(localObject2);
			}
		}
		return (ObjectVectorIfc) localObject1;
	}

	private static boolean isAccessingAgreementCabinet(NmOid paramNmOid) {
		boolean bool = false;
		try {
			Class localClass = Class
					.forName("com.ptc.core.agreements.commands.AgreementsCommands");
			Method localMethod = localClass.getMethod(
					"isAccessingAgreementCabinet", new Class[] { NmOid.class });
			Boolean localBoolean = (Boolean) localMethod.invoke(null,
					new Object[] { paramNmOid });
			bool = localBoolean.booleanValue();
			if (VERBOSE) {
				System.out.println("isAccessingAgreementCabinet= " + bool);
			}
		} catch (Exception localException) {
			if (VERBOSE) {
				System.out
						.println("Error while calling AgreementsCommands.isAccessingAgreementCabinet");
				localException.printStackTrace();
			}
			bool = false;
		}
		return bool;
	}

	protected AbstractGuiComponent getContainerIcon(WTContainer paramWTContainer)
			throws WTException {
		IconComponent localIconComponent = null;
		IconDelegate localIconDelegate = null;
		try {
			localIconDelegate = IconDelegateFactory
					.getIconDelegate(paramWTContainer);
			if (localIconDelegate != null) {
				String str = IconCache.getIconResource(localIconDelegate,
						Boolean.valueOf(false));
				if (str != null) {
					localIconComponent = new IconComponent(str);
					if (localIconComponent != null) {
						return localIconComponent;
					}
				}
			}
			if (localIconComponent == null) {
				return TextDisplayComponent.NBSP;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return TextDisplayComponent.NBSP;
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
						for (int j = 0;; j++) {
							if (resultStrList
									.get(i)
									.equalsIgnoreCase(
											com.nextlabs.em.windchill.Constants.PARANAME_OBJPARAMVAlUE)) {
								break;
							}
							if (j % 2 == 0) {
								String tagname = resultStrList.get(++i);
								logger.debug("SASERVICE Policy tagname:"
										+ tagname);
							} else {
								String tagValue = resultStrList.get(++i);
								logger.debug("SASERVICE Policy tagValue:"
										+ tagValue);
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
}
