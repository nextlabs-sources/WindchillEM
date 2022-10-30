package com.nextlabs.em.windchill;

import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

import wt.access.SecurityLabeled;
import wt.access.SecurityLabels;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentServerHelper;
import wt.content.DataFormat;
import wt.content.DataFormatReference;
import wt.content.FormatContentHolder;
//import wt.content.ContentHelper;
//import wt.content.ContentHolder;
import wt.doc.WTDocument;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTReference;
import wt.fc._WTObject;
import wt.folder.SubFolderReference;
import wt.iba.definition.litedefinition.AttributeDefDefaultView;
import wt.iba.definition.service.StandardIBADefinitionService;
import wt.iba.value.DefaultAttributeContainer;
import wt.iba.value.IBAHolder;
import wt.iba.value.IBAValueUtility;
import wt.iba.value.litevalue.AbstractValueView;
import wt.iba.value.litevalue.AbstractValueViewCollationKeyFactory;
import wt.iba.value.service.IBAValueHelper;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerHelper;
//import wt.introspection.ClassInfo;
import wt.org.OrganizationServicesHelper;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.SortedEnumeration;
import wt.util.WTException;
import wt.util.WTProperties;
import wt.vc.wip.CheckoutInfo;
import wt.vc.wip.WorkInProgressException;
import wt.vc.wip.WorkInProgressHelper;

import com.nextlabs.CESdkConstants;
import com.nextlabs.EntitlementManagerContext;
import com.nextlabs.LogLevel;
import com.nextlabs.ObjectAttrCollection;
import com.nextlabs.nxl.crypt.RightsManager;

//import com.nextlabs.nxl.pojos.PolicyControllerDetails;
public class WindchillObjectHelper {
	private static Logger logger = Logger.getLogger(WindchillObjectHelper.class);
	public static String getIBA_WTDoc_Attr(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, WTDocument doc) {
		try {
			com.ptc.core.lwc.server.LWCNormalizedObject obj = new com.ptc.core.lwc.server.LWCNormalizedObject(
					doc, null, java.util.Locale.US,
					new com.ptc.core.meta.common.DisplayOperationIdentifier());

			// obj.getTypeDescriptor();
			/* Get value of IBAName soft attribute */
			obj.load("INTERNAL_CLASSIFICATION");
			java.lang.String string_value = (java.lang.String) obj
					.get("INTERNAL_CLASSIFICATION");
			logger.debug( "Soft attibute value : " + string_value);

			Locale locale = Locale.getDefault();

			AbstractValueView[] avv = null;
			// AbstractValueView aabstractvalueview[] = null;

			IBAHolder ibah = (IBAHolder) doc;
			IBAHolder ibaHolder = IBAValueHelper.service
					.refreshAttributeContainer((IBAHolder) ibah, null, null,
							null);
			DefaultAttributeContainer dac = (DefaultAttributeContainer) ibaHolder
					.getAttributeContainer();

			if (dac != null) {
				int j = 0;
				avv = dac.getAttributeValues();

				Vector<AbstractValueView> avvVec = new Vector<AbstractValueView>();
				for (int i = 0; i < avv.length; i++)
					avvVec.addElement(avv[i]);

				Enumeration<AbstractValueView> e = avvVec.elements();
				AbstractValueViewCollationKeyFactory avvckf = new AbstractValueViewCollationKeyFactory(
						locale);
				SortedEnumeration se = new SortedEnumeration(e, avvckf);
				
				logger.debug("===AbstractValueView size=" + avvVec.size());
				logger.debug(
						" ===SortedEnumeration size=" + se.size());
				avv = new AbstractValueView[se.size()];
				while (se.hasMoreElements()) {
					avv[j] = (AbstractValueView) se.nextElement();
					j++;
				}
			}

			String txtTot = "";
			logger.debug( " ===avv size=" + avv.length);
			for (int i = 0; i < avv.length; i++) {
				String val = IBAValueUtility.getLocalizedIBAValueDisplayString(
						avv[i], null);
				AttributeDefDefaultView attrDef = avv[i].getDefinition();
				String nestedAttrName = attrDef.getName();
				txtTot = txtTot + nestedAttrName + ":" + val + "\r\n";

				System.out.println(nestedAttrName + ":" + val);
			}
			return txtTot;
		} catch (WTException ew) {
			ew.printStackTrace();
		} catch (RemoteException er) {
			er.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException ea) {
			ea.printStackTrace();
		}
		return "None";
	}

	public static String getIBAAttr(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, IBAHolder iba, String attrStg) {

		try {
			IBAHolder ibaholder = IBAValueHelper.service
					.refreshAttributeContainer(iba, null, null, null);
			DefaultAttributeContainer ac = (DefaultAttributeContainer) ibaholder
					.getAttributeContainer();
			if (ac != null) {
				logger.debug( " ac is not null");
				AttributeDefDefaultView ads[] = ac.getAttributeDefinitions();
				if (ads != null) {
					logger.debug( " ads.length=" + ads.length);
					for (int idx = 0; idx < ads.length; idx++) {
						logger.debug(
								" ad " + idx + ":" + ads[idx].getName());
					}
				} else
					logger.debug( " ads is null");
			}
			logger.debug( " ac is null");
		} catch (RemoteException er) {
			er.printStackTrace();
		} catch (WTException wtExp) {

		}

		String attrVal = null;
		String attrStgUp = attrStg;// .toUpperCase();

		try {
			IBAHolder ibaHolder = IBAValueHelper.service
					.refreshAttributeContainer((IBAHolder) iba, null, null,
							null);
			StandardIBADefinitionService defService = new StandardIBADefinitionService();
			DefaultAttributeContainer attributeContainer = (DefaultAttributeContainer) ibaHolder
					.getAttributeContainer();

			AttributeDefDefaultView attributeDefinition = defService
					.getAttributeDefDefaultViewByPath(attrStgUp);
			if (attributeDefinition == null)
				logger.debug( "the attributeDefinition is null");
			if (!(attributeContainer.getAttributeValues().length > 0)) {
				logger.debug( "Error: " + attrStgUp
						+ " Attribute Not Found");
				return null;
			}
			AbstractValueView attValue = attributeContainer
					.getAttributeValues(attributeDefinition)[0];
			attrVal = attValue.getLocalizedDisplayString();
			logger.debug( "  getIBA_WTP_Attr :" + attrStg + "="
					+ attrVal);
			return attrVal;
		} catch (WTException ew) {
			ew.printStackTrace();
		} catch (RemoteException er) {
			er.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException ea) {
			ea.printStackTrace();
		}
		return null;
	}

	public static String getWTTempPath() {
		String loc = null;
		try {
			WTProperties wtproperties = WTProperties.getLocalProperties();
			loc = wtproperties.getProperty("wt.temp", "");
			if (loc == null || loc.isEmpty() == true)
				loc = "C:\\Temp\\windchill";
		} catch (Exception exp) {
			if (loc == null || loc.isEmpty() == true)
				loc = "C:\\Temp\\windchill";
		}
		return loc;
	}

	/*
	 * return the working path
	 */
	public static String nxlDrmDoc(EntitlementManagerContext ctx,
			wt.doc.WTDocument doc, HashMap<String, List<String>> tags) {
		String workingpath = null;
		logger.debug( "nxlDrmDoc start for " + doc.getName());

		Transaction trx = new Transaction();
		try {
			workingpath = getWTTempPath();
			workingpath += "\\nxldrm\\" + ctx.getRequestId();
			logger.debug( " temp path:" + workingpath);
			java.io.File dir = new java.io.File(workingpath);

			wt.doc.WTDocument anotherdoc = null;
			anotherdoc = (wt.doc.WTDocument) wt.content.ContentHelper.service
					.getContents(doc);
			wt.content.ContentItem ci = anotherdoc.getPrimary();

			if (ci != null) {
				String fileName = ((wt.content.ApplicationData) ci)
						.getFileName();

				if (fileName.endsWith(".nxl") == false) {
					dir.mkdirs();
					java.io.File fOrigFile = new java.io.File(dir, fileName);
					java.io.File nxlFile = new java.io.File(dir, fileName
							+ ".nxl");
					// long lTime0=System.currentTimeMillis();
					wt.content.ContentServerHelper.service.writeContentStream(
							(wt.content.ApplicationData) ci,
							fOrigFile.getCanonicalPath());

					RightsManager manager = com.nextlabs.RightsManagerHelper
							.getRightsManager();

					long lTime1 = System.currentTimeMillis();
					manager.encrypt(fOrigFile.getCanonicalPath(),
							nxlFile.getCanonicalPath(), null, null, tags);
					long lTime2 = System.currentTimeMillis();
				logger.info(
							" it take " + (lTime2 - lTime1)
									+ " to encrypt the document "
									+ fOrigFile.getCanonicalPath());
					// logger.debug(" "+lTime0+" "+(lTime1-lTime0)+","+(lTime2-lTime1)+","+(lTime3-lTime2)+","+(lTime4-lTime3)+","+(lTime5-lTime4));
					// manager.cleanup();

					InputStream nxlInStream = new FileInputStream(nxlFile);
					nxlInStream.close();

					try {
						trx.start();
						// ================================================
						// 1. Delete existed Primary Content
						ContentHolder holder = (FormatContentHolder) ContentHelper.service
								.getContents((ContentHolder) doc);
						ContentItem ciUpload = ((FormatContentHolder) holder)
								.getPrimary();
	
						ContentServerHelper.service.deleteContent(
								(ContentHolder) doc, ciUpload);

						// 2. Create new ApplicationData
						ApplicationData appData = ApplicationData
								.newApplicationData((FormatContentHolder) doc);
						appData.setFileName(nxlFile.getName());
						appData.setFileSize(nxlFile.length());
						appData.setUploadedFromPath(nxlFile.getPath());

						// 3. Primary
						// Don't need set.

						// 3.1 SECONDARY
						// appData.setRole(ContentRoleType.SECONDARY);

						// appData.setCreatedBy(principalReference);
						appData.setDescription("Encrypt the content data by NextLabs DRM");
						// 4. Primary
						appData = ContentServerHelper.service.updatePrimary(
								(FormatContentHolder) doc,
								(ApplicationData) appData,
								(InputStream) nxlInStream);

						// 4.1 SECONDARY
						// appData =
						// ContentServerHelper.service.updateContent((ContentHolder)epmdoc,
						// (ApplicationData)appData, (InputStream)fileStream);

						// 5. Update DataFormat
						DataFormatReference newdfr = null;
						QuerySpec qs = new QuerySpec(DataFormat.class);
						// qs.appendWhere(new SearchCondition(DataFormat.class,
						// DataFormat.FORMAT_NAME, SearchCondition.EQUAL,
						// appData.getBusinessType()));
						qs.appendWhere(new SearchCondition(DataFormat.class,
								DataFormat.FORMAT_NAME, SearchCondition.EQUAL,
								appData.getBusinessType()), new int[] { 0 });
						@SuppressWarnings("deprecation")
						QueryResult qr = PersistenceHelper.manager.find(qs);
						while (qr.hasMoreElements()) {
							DataFormat df = (DataFormat) qr.nextElement();
							newdfr = DataFormatReference
									.newDataFormatReference(df);
						}
						doc.setFormat(newdfr);
						PersistenceServerHelper.manager.update(doc);

						trx.commit();
						trx = null;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (trx != null) {
							logger.debug(
									"   **************** trx is not null, start to rollback.");
							trx.rollback();
						}
					}

				} else
					logger.debug(
							"   ************ file end with .nxl already");
			} else
				logger.debug(
						"   ************ ContentItem is null for "
								+ workingpath);
		} catch (Exception e) {
			logger.debug(
					"   ********************Exception:" + e.getMessage());
			logger.debug(
					"   ********************  exception stacktrace:"
							+ com.nextlabs.Utilities.getStackTraceString(e));
		}
		return workingpath;

	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx,
			wt.inf.container.OrgContainer orgContainer) {
		attrCol.add("container name", orgContainer.getContainerName());
		attrCol.add("description", orgContainer.getDescription());
		attrCol.add("name", orgContainer.getName());
		attrCol.add("organization coding system",
				orgContainer.getOrganizationCodingSystem());
		attrCol.add("organization name", orgContainer.getOrganizationName());
		attrCol.add("organization unique identifier",
				orgContainer.getOrganizationUniqueIdentifier());
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.part.WTPart part) {
		SubFolderReference parent = part.getParentFolder();
		//Persistable wcObj = WindchillObjectHelper.getObject(parent
		//		.getObjectId().getStringValue());
		logger.warn(
				" exception getContainerpath:" + parent.getName());
		//logger.warn( " exception getContainerpath:" + wcObj.getType());

		attrCol.add("branch identifier",
				String.valueOf(part.getBranchIdentifier()));
		attrCol.add("container name", part.getContainerName());
		attrCol.add("contract number", part.getContractNumber());
		if (part.getCreator() != null)
			attrCol.add("created by", part.getCreator().getName());
		attrCol.add("flex type id path", part.getFlexTypeIdPath());
		attrCol.add("has contents", String.valueOf(part.isHasContents()));
		attrCol.add("job authorization nubmer",
				part.getJobAuthorizationNumber());
		if (part.getLifeCycleTemplate() != null)
			attrCol.add("life cycle template", part.getLifeCycleTemplate()
					.getName());

		attrCol.add("name", part.getName());
		attrCol.add("number", part.getNumber());
		attrCol.add("organization coding system",
				part.getOrganizationCodingSystem());
		attrCol.add("organization name", part.getOrganizationName());
		attrCol.add("organization unique identifier",
				part.getOrganizationUniqueIdentifier());
		attrCol.add("part type", String.valueOf(part.getPartType()));
		attrCol.add("phase", part.getPhase());
		attrCol.add("state", String.valueOf(part.getState()));
		attrCol.add("view name", part.getViewName());
		/*
		logger.warn("***************************************************");
		logger.warn("***************************************************");
		logger.warn(" Folder path :" + part.getFolderPath());//// /Site/Demo Organization/Drive System/wt.folder.SubFolder:304420<sub folder access check>/
		
		SubFolderReference sfr=part.getParentFolder();
		Persistable ro=sfr.getObject();
		wt.folder.SubFolder roa = null;
		if(ro instanceof wt.folder.SubFolder)
		{
			roa = (wt.folder.SubFolder)ro;
			logger.warn(" parent folder :" + roa.getName());//// Default/NextLabs-Demo/sub folder access check
			logger.warn(" URL attribute of the subfolder : " + constructURL(attrCol.getAttr("url"),getParent(roa)+"/"+roa.getName()+"/"+part.getName()) );// Folder
			attrCol.add("url", constructURL(attrCol.getAttr("url"),getParent(roa)+"/"+roa.getName()+"/"+part.getName()));

		}*/
		try {
			if(WorkInProgressHelper.isCheckedOut(part)){
				try {
					WTPart originalPart =  (WTPart)WorkInProgressHelper.service.originalCopyOf(part);
					
					//logger.warn(" folder path of the working copy :" + );
					logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,originalPart.getParentFolder(),ctx)+"/"+originalPart.getName() );// Folder
					attrCol.add("url", setAttrsInternal(attrCol,originalPart.getParentFolder(),ctx)+"/"+originalPart.getName());
				} catch (WorkInProgressException e) {
					attrCol.add("url", setAttrsInternal(attrCol,part.getParentFolder(),ctx)+"/"+part.getName());
					logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,part.getParentFolder(),ctx)+"/"+part.getName());// Folder
					e.printStackTrace();
				} catch (WTException e) {
					attrCol.add("url", setAttrsInternal(attrCol,part.getParentFolder(),ctx)+"/"+part.getName());
					logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,part.getParentFolder(),ctx)+"/"+part.getName());// Folder
					e.printStackTrace();
				}
				
			}else{
				attrCol.add("url", setAttrsInternal(attrCol,part.getParentFolder(),ctx)+"/"+part.getName());
				logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,part.getParentFolder(),ctx)+"/"+part.getName());// Folder
			}
		} catch (WTException e) {
			attrCol.add("url", setAttrsInternal(attrCol,part.getParentFolder(),ctx)+"/"+part.getName());
			logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,part.getParentFolder(),ctx)+"/"+part.getName());// Folder
			e.printStackTrace();
		}
		
		
		
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.org.WTUser user) {
		try {
			attrCol.add("authentication name", user.getAuthenticationName());
			attrCol.add("business type", user.getBusinessType());
			attrCol.add("dn", user.getDn());
		logger.info("user.getEmail"+user.getEMail());
			attrCol.add("email", user.getEMail());
		logger.info("user.getDn"+user.getDn());
			attrCol.add("fax number", user.getFaxNumber());
			attrCol.add("first", user.getFirst());
			attrCol.add("format name", user.getFormatName());
			attrCol.add("last", user.getLast());
			attrCol.add("middle", user.getMiddle());
			attrCol.add("mobile phone number", user.getMobilePhoneNumber());
			attrCol.add("wt.org.WTUser.name", user.getName());
			attrCol.add("organization name", user.getOrganizationName());
			attrCol.add("postal address", user.getPostalAddress());
			attrCol.add("prefix", user.getPrefix());
			attrCol.add("principal display identifier",
					user.getPrincipalDisplayIdentifier());
			attrCol.add("principal identifier", user.getPrincipalIdentifier());
			attrCol.add("repository", user.getRepository());
			attrCol.add("suffix", user.getSuffix());
			attrCol.add("telephone number", user.getTelephoneNumber());

			wt.org.AttributeHolder attrHolder = user.getAttributes();

			@SuppressWarnings("unchecked")
			Enumeration<String> attrNames = (Enumeration<String>) attrHolder
					.getAttrNames();
			while (attrNames.hasMoreElements()) {
				String name = attrNames.nextElement();
				@SuppressWarnings("unchecked")
				Enumeration<String> values = (Enumeration<String>) attrHolder
						.getValues(name);
				String value = null;
				while (values.hasMoreElements()) {
					value += values.nextElement() + ";";

				}
				if (name.equalsIgnoreCase("name"))
					name = "wt.org.WTUser.name";
				if (value.equalsIgnoreCase("null") == false)
					attrCol.add(name, value);
			}
		} catch (WTException ex) {

		}
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.doc.WTDocument doc) {
		attrCol.add("container name", doc.getContainerName());
		attrCol.add("description", doc.getDescription());
		attrCol.add("flex type id path", doc.getFlexTypeIdPath());
		attrCol.add("format name", doc.getFormatName());
		attrCol.add("name", doc.getName());
		attrCol.add("number", doc.getNumber());
		attrCol.add("organization coding system",
				doc.getOrganizationCodingSystem());
		attrCol.add("organization name", doc.getOrganizationName());
		attrCol.add("organization unique identifier",
				doc.getOrganizationUniqueIdentifier());
		attrCol.add("title", doc.getTitle());
		//String.valueOf(part.getState())
		attrCol.add("state", String.valueOf(doc.getState()));
		logger.warn("***************************************************");
		logger.warn("***************************************************");
		logger.warn(" Folder path :" + doc.getFolderPath());//// /Site/Demo Organization/Drive System/wt.folder.SubFolder:304420<sub folder access check>/
		logger.warn(" doc name :" + doc.getName());//// Default/NextLabs-Demo/sub folder access check
		logger.warn(" getCheckoutInfo state :" + ((CheckoutInfo) doc.getCheckoutInfo()));
		
		logger.warn(" CHECKOUT_INFO :" + doc.CHECKOUT_INFO);//checkoutInfo
		//logger.warn(" CHECKOUT_INFO :" + doc.get);
		logger.warn(" static folder path :" + doc.FOLDER_PATH);
		logger.warn(" static foldering info :" + doc.FOLDERING_INFO);
		logger.warn(" business type :" + doc.getBusinessType());//WTDocument
		logger.warn(" FlexTypeIdPath :" + doc.getFlexTypeIdPath());
		logger.warn(" FormatName :" + doc.getFormatName());
		logger.warn(" Identity :" + doc.getIdentity());
		logger.warn(" IterationNote :" + doc.getIterationNote());
		logger.warn(" LifeCycleName :" + doc.getLifeCycleName());
		logger.warn(" Location :" + doc.getLocation());
		logger.warn(" State :" + doc.getState().toString());
		logger.warn(" State :" + doc.getState().toString());
		//logger.warn(" State :" + doc.get);
		//logger.warn(" State :" + doc.);

		try {
			
			if(WorkInProgressHelper.isCheckedOut(doc)){
				
				logger.warn("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%inside checked out%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
				try {
					WTDocument originalDoc =  (WTDocument)WorkInProgressHelper.service.originalCopyOf(doc);
					
					//logger.warn(" folder path of the working copy :" + );
					logger.warn(" State :" + originalDoc.getParentFolder());
					logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,originalDoc.getParentFolder(),ctx)+"/"+originalDoc.getName() );// Folder
					attrCol.add("url", setAttrsInternal(attrCol,originalDoc.getParentFolder(),ctx)+"/"+originalDoc.getName());
				} catch (WorkInProgressException e) {
					// TODO Auto-generated catch block
					logger.warn(" State :" + doc.getParentFolder());
					attrCol.add("url", setAttrsInternal(attrCol,doc.getParentFolder(),ctx)+"/"+doc.getName());
					logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,doc.getParentFolder(),ctx)+"/"+doc.getName());// Folder
					//e.printStackTrace();
					
					e.printStackTrace();
				} catch (WTException e) {
					logger.warn(" State :" + doc.getParentFolder());
					attrCol.add("url", setAttrsInternal(attrCol,doc.getParentFolder(),ctx)+"/"+doc.getName());
					logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,doc.getParentFolder(),ctx)+"/"+doc.getName());// Folder
					//e.printStackTrace();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				
				logger.warn(" State :" + doc.getParentFolder());
				attrCol.add("url", setAttrsInternal(attrCol,doc.getParentFolder(),ctx)+"/"+doc.getName());
				logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,doc.getParentFolder(),ctx)+"/"+doc.getName());// Folder
			}
		} catch (WTException e) {
			logger.warn(" State :" + doc.getParentFolder());
			attrCol.add("url", setAttrsInternal(attrCol,doc.getParentFolder(),ctx)+"/"+doc.getName());
			logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,doc.getParentFolder(),ctx)+"/"+doc.getName());// Folder
			e.printStackTrace();
		}
				

	
		
		
		
		
		return true;

	}
	
	private static String setAttrsInternal(ObjectAttrCollection attrCol,SubFolderReference reference,EntitlementManagerContext ctx){
		Object obj=null;
		obj = reference.getObject();
		if(null!=obj){
			
				Persistable ro=reference.getObject();
				wt.folder.SubFolder roa = null;
				if(ro instanceof wt.folder.SubFolder)
				{
					roa = (wt.folder.SubFolder)ro;
					logger.warn(" parent folder :" + roa.getName());//// Default/NextLabs-Demo/sub folder access check
					logger.warn(" URL attribute of the subfolder : " + constructURL(attrCol.getAttr("url"),getParent(roa)+"/"+roa.getName()) );// Folder
					return constructURL(attrCol.getAttr("url"),getParent(roa)+"/"+roa.getName());

				}
				else
				{
					return "";
				}
		
			
		}else{
			logger.warn(" URL attribute of the subfolder : " + constructURL(attrCol.getAttr("url"),"") );
			return constructURL(attrCol.getAttr("url"),"");
		}

	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.content.ApplicationData appData) {
		attrCol.add("authored by", appData.getAuthoredBy());
		attrCol.add("business type", appData.getBusinessType());
		attrCol.add("category", appData.getCategory());
		attrCol.add("comments", appData.getComments());
		attrCol.add("content identity", appData.getContentIdentity());
		attrCol.add("description", appData.getDescription());
		attrCol.add("file name", appData.getFileName());
		attrCol.add("file version", appData.getFileVersion());
		attrCol.add("format name", appData.getFormatName());
		attrCol.add("identity", appData.getIdentity());
		attrCol.add("tool name", appData.getToolName());
		attrCol.add("tool version", appData.getToolVersion());
		attrCol.add("file size", String.valueOf(appData.getFileSize()));
		attrCol.add("uploaded from path", appData.getUploadedFromPath());
		if (appData.getCreatedBy() != null)
			attrCol.add("created by", appData.getCreatedBy().getName());
		if (appData.getModifiedBy() != null)
			attrCol.add("modified by", appData.getModifiedBy().getName());
		if (appData.getLastAuthored() != null)
			attrCol.add("last authored", appData.getLastAuthored().toString());
		// ObjectReference objRef=appData.getStreamData();

		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.pdmlink.PDMLinkProduct prod) {
		attrCol.add("container name", prod.getContainerName());
		attrCol.add("description", prod.getDescription());
		attrCol.add("flex type id path", prod.getFlexTypeIdPath());
		attrCol.add("invitation msg", prod.getInvitationMsg());
		attrCol.add("name", prod.getName());
		attrCol.add("organization coding system",
				prod.getOrganizationCodingSystem());
		attrCol.add("organization name", prod.getOrganizationName());
		attrCol.add("organization unique identifier",
				prod.getOrganizationUniqueIdentifier());
		attrCol.add("product name", prod.getProductName());
		attrCol.add("product number", prod.getProductNumber());
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.folder.SubFolder subFolder) {
		attrCol.add("container display name",
				subFolder.getContainerDisplayName());
		attrCol.add("container name", subFolder.getContainerName());
		attrCol.add("description", subFolder.getDescription());
		attrCol.add("flex type id path", subFolder.getFlexTypeIdPath());
		attrCol.add("name", subFolder.getName());
		
		logger.warn("***************************************************");
		logger.warn("***************************************************");
		logger.warn(" Folder path :" + subFolder.getFolderPath());//// /Site/Demo Organization/Drive System/wt.folder.SubFolder:304420<sub folder access check>/
		logger.warn(" Folder path :" + subFolder.getFolderPath());//// Default/NextLabs-Demo/sub folder access check
		logger.warn(" URL attribute of the subfolder : " + constructURL(attrCol.getAttr("url"),getParent(subFolder)) );// Folder
		attrCol.add("url", constructURL(attrCol.getAttr("url"),getParent(subFolder))+"/"+subFolder.getName());
		return true;
	}
	
	private static String constructURL(String containerPath, String folderPath){
		
		 //System.out.println(containerPath.lastIndexOf( '/' ));
	    return (containerPath.substring(0, containerPath.lastIndexOf( '/' )))+folderPath;
		
	}
	
	private static String getParent(wt.folder.SubFolder subFolder){
		if(null == subFolder || null == subFolder.getParentFolder()){
			return "";
		}else{
			String folder_name = subFolder.getParentFolder().getName();

			SubFolderReference sfr=subFolder.getParentFolder();
			Persistable ro=sfr.getObject();
			wt.folder.SubFolder roa = null;
			if(ro instanceof wt.folder.SubFolder)
			{
				roa = (wt.folder.SubFolder) ro;
			}
			if(null == folder_name){
				return getParent(roa);
			}else{
				return getParent(roa)+"/"+folder_name;
			}
			
		}
		
		
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, com.ptc.wpcfg.doc.VariantSpec varSpec) {
		attrCol.add("container name", varSpec.getContainerName());
		attrCol.add("description", varSpec.getDescription());
		attrCol.add("flex type id path", varSpec.getFlexTypeIdPath());
		attrCol.add("format name", varSpec.getFormatName());
		attrCol.add("organization coding system",
				varSpec.getOrganizationCodingSystem());
		attrCol.add("organization name", varSpec.getOrganizationName());
		attrCol.add("organization unique identifier",
				varSpec.getOrganizationUniqueIdentifier());
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.epm.EPMDocument epmDoc) {
		attrCol.add("CAD name", epmDoc.getCADName());
		attrCol.add("description", epmDoc.getDescription());
		attrCol.add("flex type id path", epmDoc.getFlexTypeIdPath());
		attrCol.add("global id", epmDoc.getGlobalID());
		attrCol.add("format name", epmDoc.getFormatName());
		attrCol.add("number", epmDoc.getNumber());
		if (epmDoc.getAuthoringApplication() != null)
			attrCol.add("authoring application", epmDoc
					.getAuthoringApplication().getDisplay());
		if (epmDoc.getDocSubType() != null)
			attrCol.add("doc sub type", epmDoc.getDocSubType().getDisplay());
		if (epmDoc.getDocType() != null)
			attrCol.add("doc type", epmDoc.getDocType().getDisplay());
		/*
		logger.warn("***************************************************");
		logger.warn("***************************************************");
		logger.warn(" Folder path :" + epmDoc.getFolderPath());//// /Site/Demo Organization/Drive System/wt.folder.SubFolder:304420<sub folder access check>/

		SubFolderReference sfr=epmDoc.getParentFolder();
		Persistable ro=sfr.getObject();
		wt.folder.SubFolder roa = null;
		if(ro instanceof wt.folder.SubFolder)
		{
			roa = (wt.folder.SubFolder)ro;
			logger.warn(" parent folder :" + roa.getName());//// Default/NextLabs-Demo/sub folder access check
			logger.warn(" URL attribute of the subfolder : " + constructURL(attrCol.getAttr("url"),getParent(roa)+"/"+roa.getName()+"/"+epmDoc.getName()) );// Folder
			attrCol.add("url", constructURL(attrCol.getAttr("url"),getParent(roa)+"/"+roa.getName()+"/"+epmDoc.getName()));

		}*/
		try {
			if(WorkInProgressHelper.isCheckedOut(epmDoc)){
				try {
					wt.epm.EPMDocument originalepmDoc =  (wt.epm.EPMDocument)WorkInProgressHelper.service.originalCopyOf(epmDoc);
					
					//logger.warn(" folder path of the working copy :" + );
					logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,originalepmDoc.getParentFolder(),ctx)+"/"+originalepmDoc.getName() );// Folder
					attrCol.add("url", setAttrsInternal(attrCol,originalepmDoc.getParentFolder(),ctx)+"/"+originalepmDoc.getName());
				} catch (WorkInProgressException e) {
					attrCol.add("url", setAttrsInternal(attrCol,epmDoc.getParentFolder(),ctx)+"/"+epmDoc.getName());
					logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,epmDoc.getParentFolder(),ctx)+"/"+epmDoc.getName());// Folder
					e.printStackTrace();
				} catch (WTException e) {
					attrCol.add("url", setAttrsInternal(attrCol,epmDoc.getParentFolder(),ctx)+"/"+epmDoc.getName());
					logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,epmDoc.getParentFolder(),ctx)+"/"+epmDoc.getName());// Folder
					e.printStackTrace();
				}
				
			}else{
				attrCol.add("url", setAttrsInternal(attrCol,epmDoc.getParentFolder(),ctx)+"/"+epmDoc.getName());
				logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,epmDoc.getParentFolder(),ctx)+"/"+epmDoc.getName());// Folder
			}
		} catch (WTException e) {
			attrCol.add("url", setAttrsInternal(attrCol,epmDoc.getParentFolder(),ctx)+"/"+epmDoc.getName());
			logger.warn(" URL attribute of the subfolder : " + setAttrsInternal(attrCol,epmDoc.getParentFolder(),ctx)+"/"+epmDoc.getName());// Folder
			e.printStackTrace();
		}
		
		
		//attrCol.add("url", setAttrsInternal(attrCol,epmDoc.getParentFolder(),ctx)+"/"+epmDoc.getName());
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, com.ptc.windchill.wp.WorkPackage wp) {
		attrCol.add("context", wp.getContext());
		// attrCol.add("accepted of",wp.getAcceptedOf());
		attrCol.add("default recipient instructions",
				wp.getDefaultRecipientInstructions());
		attrCol.add("description", wp.getDescription());
		attrCol.add("excluded member content roles",
				wp.getExcludedMemberContentRoles());
		attrCol.add("filter spec", wp.getFilterSpec());
		attrCol.add("flex type id path", wp.getFlexTypeIdPath());
		attrCol.add("format name", wp.getFormatName());
		attrCol.add("included package content roles",
				wp.getIncludedPackageContentRoles());
		attrCol.add("name", wp.getName());
		attrCol.add("number", wp.getNumber());
		attrCol.add("package number", wp.getPackageNumber());
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.projmgmt.admin.Project2 proj) {
		attrCol.add("business location", proj.getBusinessLocation());
		attrCol.add("business unit", proj.getBusinessUnit());
		attrCol.add("description", proj.getDescription());
		attrCol.add("flex type id path", proj.getFlexTypeIdPath());
		attrCol.add("identity", proj.getIdentity());
		// attrCol.add("internet domain", proj.getInternetDomain());
		attrCol.add("invitation msg", proj.getInvitationMsg());
		attrCol.add("name", proj.getName());
		attrCol.add("project number", proj.getProjectNumber());
		attrCol.add("risk description", proj.getRiskDescription());
		if (proj.getRiskValue() != null)
			attrCol.add("risk value", proj.getRiskValue().getDisplay());
		attrCol.add("scope", proj.getScope());
		attrCol.add("status description", proj.getStatusDescription());
		if (proj.getActualEnd() != null)
			attrCol.add("actual end", proj.getActualEnd().toString());
		if (proj.getActualStart() != null)
			attrCol.add("actual start", proj.getActualStart().toString());
		if (proj.getBudget() != null)
			attrCol.add("budget", proj.getBudget().toString());
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.folder.Cabinet cab) {
		attrCol.add("container display name", cab.getContainerDisplayName());
		attrCol.add("container name", cab.getContainerName());
		attrCol.add("description", cab.getDescription());
		attrCol.add("location", cab.getLocation());
		attrCol.add("name", cab.getName());
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx, wt.inf.library.WTLibrary lib) {
		attrCol.add("flex type id path", lib.getFlexTypeIdPath());
		try {
			attrCol.add("internet domain", lib.getInternetDomain());
		} catch (WTException ex) {

		}
		attrCol.add("invitation msg", lib.getInvitationMsg());
		attrCol.add("name", lib.getName());
		attrCol.add("organization coding system",
				lib.getOrganizationCodingSystem());
		attrCol.add("organization name", lib.getOrganizationName());
		attrCol.add("organizatin unique identifier",
				lib.getOrganizationUniqueIdentifier());
		return true;
	}

	private static boolean getAttrsInternal(ObjectAttrCollection attrCol,
			EntitlementManagerContext ctx,
			wt.enterprise.RevisionControlled revCtrl) {
		attrCol.add("cabinet name", revCtrl.getCabinetName());
		attrCol.add("creator email", revCtrl.getCreatorEMail());
		attrCol.add("creator full name", revCtrl.getCreatorFullName());
		attrCol.add("creator name", revCtrl.getCreatorName());
		attrCol.add("folder path", revCtrl.getFolderPath());
		attrCol.add("identity", revCtrl.getIdentity());
		attrCol.add("iteration note", revCtrl.getIterationNote());
		attrCol.add("life cycle name", revCtrl.getLifeCycleName());
		attrCol.add("location", revCtrl.getLocation());
		attrCol.add("locker email", revCtrl.getLockerEMail());
		attrCol.add("locker full name", revCtrl.getLockerFullName());
		attrCol.add("locker name", revCtrl.getLockerName());
		attrCol.add("lock note", revCtrl.getLockNote());
		attrCol.add("modifier email", revCtrl.getModifierEMail());
		attrCol.add("modifier full name", revCtrl.getModifierFullName());
		attrCol.add("modifier name", revCtrl.getModifierName());
		attrCol.add("name", revCtrl.getName());
		attrCol.add("team identity", revCtrl.getTeamIdentity());
		attrCol.add("team name", revCtrl.getTeamName());
		attrCol.add("team template identity", revCtrl.getTeamTemplateIdentity());
		attrCol.add("team template name", revCtrl.getTeamTemplateName());
		
		if (revCtrl.getLifeCycleState() != null)
			attrCol.add("life cycle state", revCtrl.getLifeCycleState()
					.getDisplay());
		return true;
	}

	private static String getContainerPath(WTContained contained,
			EntitlementManagerContext ctx) {
		String containerStr = null;
		containerStr = "/" + contained.toString();
		try {
			WTContainer preContainer = null;
			WTContainer curContainer = WTContainerHelper
					.getContainer(contained);
			int i = 0;
			while (curContainer != null) {
				containerStr = "/" + curContainer.getName() + containerStr;
				preContainer = curContainer;
				curContainer = preContainer.getContainer();
				if (curContainer == null)
					break;

				if (preContainer.equals(curContainer) == true)
					break;
				i++;// in case dead loop. In theory, it should break out due to
					// above two "if" statements.
				if (i > 10)
					break;
			}
		} catch (WTException wtExp) {
			logger.warn(
					" exception getContainerpath:" + wtExp.getMessage());
		}
		return containerStr;
	}

	private static void parseSecurityLabels(ObjectAttrCollection attrCol,
			String secLabels) {
		if (secLabels != null && secLabels.isEmpty() == false) {
			attrCol.add("security labels", secLabels);
			if (secLabels.contains(",") == true) {
				String labels[] = secLabels.split(",");
				for (int i = 0; i < labels.length; i++) {
					if (labels[i] != null && labels[i].contains("=")) {
						String nameVal[] = labels[i].split("=");
						if (nameVal.length == 2)
							attrCol.add(nameVal[0], nameVal[1]);

					}
				}
			}
		}
	}

	private static void readSoftAttribute(ObjectAttrCollection attrCol,
			Persistable psObj, EntitlementManagerContext ctx) {
		String attrs = com.nextlabs.ConfigurationManager.getInstance()
				.getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
						com.nextlabs.Property.PEP_WINDCHILL_SOFT_ATTRIBUTES);
		//logger.debug( " soft attribute(s):" + attrs);
		if (attrs != null && attrs.isEmpty() == false) {
			String[] attrArray = attrs.split(";");
			if (attrArray != null) {
				for (int i = 0; i < attrArray.length; i++) {
					if (attrArray[i] != null && attrArray[i].isEmpty() == false) {
						try {
							logger.debug( "    start to read:"
									+ attrArray[i]);
							com.ptc.core.lwc.server.LWCNormalizedObject obj = new com.ptc.core.lwc.server.LWCNormalizedObject(
									psObj,
									null,
									java.util.Locale.US,
									new com.ptc.core.meta.common.DisplayOperationIdentifier());
							logger.debug(
									"    obj to class:" + obj.toString());

							String value = null;
							if (obj.toString().contains(attrArray[i])) {
								obj.load(attrArray[i]);
								value = (java.lang.String) obj
										.get(attrArray[i]);
							}

							attrCol.add(attrArray[i], value);
							logger.debug( "    soft attribute value:"
									+ attrArray[i] + "=" + value);
						} catch (WTException wtExp) {
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static WTUser getUser(String userName) {
		WTUser wtUser = null;
		try {
			wtUser = OrganizationServicesHelper.manager.getUser(userName);
		} catch (WTException wtExp) {
		}
		return wtUser;

	}

	public static Persistable getObject(String oid) {
		Persistable obj = null;
		try {
			ReferenceFactory referencefactory = new ReferenceFactory();
			WTReference wtreference = referencefactory.getReference(oid);
			obj = (Persistable) wtreference.getObject();
		} catch (WTException e) {
		}
		return obj;
	}

	/*
	 * return: true means success and the attrCol is usable.
	 */
	public static boolean getAttrs(ObjectAttrCollection attrCol,
			Persistable psObj, EntitlementManagerContext ctx) {
		boolean bSuccess = true;

		if (attrCol == null)
			return false;

		if (psObj == null)
			return true;

		if (psObj instanceof WTContained) {
			WTContained contained = (WTContained) psObj;
			String containerStr = getContainerPath(contained, ctx);
			if (containerStr != null) {
						
				attrCol.add(CESdkConstants.RESOURCE_ATTR_KEY, containerStr);
				attrCol.add("url", containerStr);// resource.portal.url
			}
		}

		readSoftAttribute(attrCol, psObj, ctx);

		// common attributes
		if (psObj instanceof _WTObject) {
			_WTObject _wtObj = (_WTObject) psObj;
			attrCol.add("type", _wtObj.getType());
			attrCol.add("conceptual class name",
					_wtObj.getConceptualClassname());
			if (_wtObj.getCreateTimestamp() != null)
				//attrCol.add("created", _wtObj.getCreateTimestamp().toString());
				attrCol.add("created",  String.valueOf(_wtObj.getCreateTimestamp().getTime()));
			if (_wtObj.getModifyTimestamp() != null)
				attrCol.add("modified", String.valueOf(_wtObj.getModifyTimestamp().getTime()));
			if (_wtObj.getDisplayIdentifier() != null)
				attrCol.add("display identifier", _wtObj.getDisplayIdentifier()
						.toString());
			if (_wtObj.getDisplayIdentity() != null)
				attrCol.add("display identity", _wtObj.getDisplayIdentity()
						.toString());
		}

		// RevisionControlled attributes
		if (psObj instanceof wt.enterprise.RevisionControlled) {
		
			wt.enterprise.RevisionControlled revCtrl = (wt.enterprise.RevisionControlled) psObj;
			getAttrsInternal(attrCol, ctx, revCtrl);
		}
		// Security Labels
		if (psObj instanceof SecurityLabeled) {
			
			
			
			SecurityLabeled slObj = (SecurityLabeled) psObj;
			SecurityLabels sLs = slObj.getSecurityLabels();
			if (sLs != null) {
				String secLabels = sLs.toString();
				parseSecurityLabels(attrCol, secLabels);
			}
		}

		// call attribute collector for different object type
		if (psObj instanceof wt.inf.container.OrgContainer) {// Organization
																// Container
			wt.inf.container.OrgContainer orgContainer = (wt.inf.container.OrgContainer) psObj;
			return getAttrsInternal(attrCol, ctx, orgContainer);
		} else if (psObj instanceof WTPart) {// Part
			WTPart part = (WTPart) psObj;
			return getAttrsInternal(attrCol, ctx, part);
		} else if (psObj instanceof WTUser) {// User
			WTUser user = (WTUser) psObj;
			return getAttrsInternal(attrCol, ctx, user);
		} else if (psObj instanceof wt.doc.WTDocument) {// Document
			wt.doc.WTDocument doc = (wt.doc.WTDocument) psObj;
			return getAttrsInternal(attrCol, ctx, doc);
		} else if (psObj instanceof wt.epm.EPMDocument) {
			wt.epm.EPMDocument epmDoc = (wt.epm.EPMDocument) psObj;
			return getAttrsInternal(attrCol, ctx, epmDoc);
		} else if (psObj instanceof wt.content.ApplicationData) {
			wt.content.ApplicationData appData = (wt.content.ApplicationData) psObj;
			return getAttrsInternal(attrCol, ctx, appData);
		} else if (psObj instanceof wt.pdmlink.PDMLinkProduct) {// Product
			wt.pdmlink.PDMLinkProduct pdmLinkProd = (wt.pdmlink.PDMLinkProduct) psObj;
			return getAttrsInternal(attrCol, ctx, pdmLinkProd);
		} else if (psObj instanceof wt.folder.SubFolder) {// Product
			wt.folder.SubFolder subFolder = (wt.folder.SubFolder) psObj;
			return getAttrsInternal(attrCol, ctx, subFolder);
		} else if (psObj instanceof wt.inf.library.WTLibrary) {
			wt.inf.library.WTLibrary lib = (wt.inf.library.WTLibrary) psObj;
			return getAttrsInternal(attrCol, ctx, lib);
		} else if (psObj instanceof com.ptc.windchill.wp.WorkPackage) {
			com.ptc.windchill.wp.WorkPackage wp = (com.ptc.windchill.wp.WorkPackage) psObj;
			return getAttrsInternal(attrCol, ctx, wp);
		} else if (psObj instanceof wt.projmgmt.admin.Project2) {
			wt.projmgmt.admin.Project2 proj = (wt.projmgmt.admin.Project2) psObj;
			return getAttrsInternal(attrCol, ctx, proj);
		} else if (psObj instanceof com.ptc.wpcfg.doc.VariantSpec) {
			com.ptc.wpcfg.doc.VariantSpec varSpec = (com.ptc.wpcfg.doc.VariantSpec) psObj;
			return getAttrsInternal(attrCol, ctx, varSpec);
		} else if (psObj instanceof wt.folder.Cabinet) {
			wt.folder.Cabinet cab = (wt.folder.Cabinet) psObj;
			return getAttrsInternal(attrCol, ctx, cab);
		} else {
			// bSuccess=true;
			attrCol.add("type", psObj.getClass().getName());
		}
		return bSuccess;
	}
}