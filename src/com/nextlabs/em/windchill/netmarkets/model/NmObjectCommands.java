package com.nextlabs.em.windchill.netmarkets.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;

import wt.fc.Persistable;
import wt.iba.value.IBAHolder;
import wt.ixb.handlers.netmarkets.JSPFeedback;
import wt.ixb.handlers.netmarkets.NmFeedbackSpec;
import wt.log4j.LogR;
import wt.org.WTPrincipal;
import wt.services.applicationcontext.implementation.DefaultServiceProvider;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTMessage;
import wt.util.WTPropertyVetoException;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

import com.nextlabs.EntitlementManagerContext;
import com.ptc.core.HTMLtemplateutil.server.processors.AttributeKey;
import com.ptc.core.components.beans.TypeInstanceBean;
import com.ptc.core.foundation.type.common.impl.AttributeValueException;
import com.ptc.core.foundation.type.server.impl.SoftAttributesHelper;
import com.ptc.core.foundation.type.server.impl.TypeHelper;
import com.ptc.core.meta.common.AttributeIdentifier;
import com.ptc.core.meta.common.AttributeTypeIdentifier;
import com.ptc.core.meta.common.IdentifierFactory;
import com.ptc.core.meta.common.IllegalFormatException;
import com.ptc.core.meta.common.OperationIdentifier;
import com.ptc.core.meta.common.TypeInstanceIdentifier;
import com.ptc.core.meta.container.common.AttributeContainerSpec;
import com.ptc.core.meta.server.TypeIdentifierUtility;
import com.ptc.core.meta.type.common.TypeInstance;
import com.ptc.netmarkets.model.NmException;
import com.ptc.netmarkets.util.beans.NmCommandBean;

public class NmObjectCommands {

	private static final Logger logger = LogR.getLogger(NmObjectCommands.class.getName());
	private static final IdentifierFactory IDENTIFIER_FACTORY = (IdentifierFactory) DefaultServiceProvider
			.getService(IdentifierFactory.class, "logical");

	public static void downloadFolderContentFiles(NmCommandBean paramNmCommandBean) throws WTException {
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		logger.info("############################# Primary attachement download ####################################");
		Object localObject1 = null;
		NmFeedbackSpec localNmFeedbackSpec = null;
		URL localURL = null;

		HashMap localHashMap = paramNmCommandBean.getMap();
		localHashMap.put("jfb", localObject1);
		localHashMap.put("savefullpath", paramNmCommandBean.getTextParameter("savefullpath"));
		localHashMap.put("includesubfolders", paramNmCommandBean.getTextParameter("includesubfolders"));
		localHashMap.put("checkoutondownload", paramNmCommandBean.getTextParameter("checkoutondownload"));
		try {
			Object localObject2 = new HashMap();
			((HashMap) localObject2).put("refresh", "true");
			localObject1 = paramNmCommandBean.initializeFeedback("export1", paramNmCommandBean.getElementOid(), false,
					(HashMap) localObject2);
			localNmFeedbackSpec = ((JSPFeedback) localObject1).getSpec();
			localNmFeedbackSpec.setTitle(WTMessage.getLocalizedMessage("com.ptc.netmarkets.object.objectResource",
					"218", null, SessionHelper.getLocale()));

			localURL = NmObjectHelper.service.downloadFolderContentFiles(paramNmCommandBean);
			paramNmCommandBean.getSessionBean().getStorage().put("urlToDownload", localURL.toExternalForm());

			localObject2 = null;
			if (localURL != null) {
				localObject2 = localURL.toExternalForm();
			}
			paramNmCommandBean.finalizeFeedback((JSPFeedback) localObject1, (String) localObject2);
		} catch (NmException localNmException) {
			if (localNmFeedbackSpec != null) {
				localNmFeedbackSpec.setException(localNmException);
			}
			throw localNmException;
		} finally {
			String str = null;
			if (localURL != null) {
				str = localURL.toExternalForm();
			}
			paramNmCommandBean.finalizeFeedback((JSPFeedback) localObject1, str);
		}
	}

	public static Workable getWorkingCopy(Workable paramWorkable) throws WTException {
		Workable localWorkable = paramWorkable;
		WTPrincipal localWTPrincipal = SessionHelper.getPrincipal();
		boolean bool = WorkInProgressHelper.isCheckedOut(paramWorkable, localWTPrincipal);
		if ((bool) && (!WorkInProgressHelper.isWorkingCopy(paramWorkable))) {
			localWorkable = WorkInProgressHelper.service.workingCopyOf(paramWorkable);
		}
		return localWorkable;
	}

	public static void updateSoftAttributes(NmCommandBean paramNmCommandBean, String paramString,
			IBAHolder paramIBAHolder) throws WTException {
		updateSoftAttributes(paramNmCommandBean, paramString, paramIBAHolder, false);
	}

	public static void updateSoftAttributes(NmCommandBean paramNmCommandBean, String paramString,
			IBAHolder paramIBAHolder, boolean paramBoolean) throws WTException {
		if ((paramString != null) && (paramString.length() != 0)) {
			String str1 = (String) paramNmCommandBean.getMap().get("softattributeidentifier");
			Locale localLocale = SessionHelper.getLocale();

			int i = (str1 == null) || (str1.equals("null")) ? 1 : 0;
			String str2 = null;
			String str3 = null;
			int j = 0;

			HashMap localHashMap1 = new HashMap();
			HashMap localHashMap2 = new HashMap();
			String localObject1;
			Set<String> localObject2;
			Object localObject3;
			Object localObject4;
			String str4;
			if (i != 0) {
				String s_ = TypeHelper.getBaseType(TypeHelper.getTypeIdentifier(paramString));
				boolean k ;
				localObject2 = new HashSet();
				try {
					k = TypeHelper.hasCustomSchemaAttributes(paramString.toString(), s_, localObject2)[1];
				} catch (WTPropertyVetoException localWTPropertyVetoException) {
					throw new WTException(localWTPropertyVetoException);
				}
				if (k !=  false) {
					localObject3 = new StringBuffer();
					localObject4 = WTMessage.getLocalizedMessage("com.ptc.netmarkets.model.modelResource", "55", null,
							localLocale);
					for (Object localObject5 = ((Set) localObject2).iterator(); ((Iterator) localObject5).hasNext();) {
						str4 = (String) ((Iterator) localObject5).next();
						if (!((StringBuffer) localObject3).toString().equals("")) {
							((StringBuffer) localObject3).append((String) localObject4);
						}
						((StringBuffer) localObject3).append(str4);
					}
					Object localObject5 = WTMessage.getLocalizedMessage("com.ptc.netmarkets.model.modelResource", "57",
							new Object[] { localObject3 }, localLocale);

					throw new AttributeValueException((String) localObject5);
				}
			}
			if (i == 0) {
				TypeInstance t_ = getTypeInstanceWithSoftAttrs(paramIBAHolder);

				ArrayList localArrayList = parseEncodedField(str1, ";;;zzz");
				ArrayList a_ = parseEncodedField((String) paramNmCommandBean.getMap().get("softattributevalue"),";;;zzz");
				localObject3 = parseEncodedField((String) paramNmCommandBean.getMap().get("attributekey"), ";;;zzz");
				if (((ArrayList) a_).isEmpty()) {
					((ArrayList) a_).add("");
				}
				localObject4 = (IdentifierFactory) DefaultServiceProvider.getService(IdentifierFactory.class,
						"default");
				for (int m = 0; m < localArrayList.size(); m++) {
					str4 = (String) ((ArrayList) localObject3).get(m);

					AttributeKey localAttributeKey = null;
					localAttributeKey = new AttributeKey(str4);
					if (AttributeKey.isSoftAttributeKey(str4)) {
						j = 0;
					} else if (AttributeKey.isModeledAttributeKey(str4)) {
						j = 1;
					}
					if (localAttributeKey != null) {
						str3 = localAttributeKey.getDataType();
						str2 = localAttributeKey.getLabel();
					}
					AttributeTypeIdentifier localAttributeTypeIdentifier = (AttributeTypeIdentifier) ((IdentifierFactory) localObject4)
							.get((String) localArrayList.get(m));

					String str5 = (String) ((ArrayList) a_).get(m);

					Object localObject6 = null;
					if ((str5 != null) && (!str5.trim().equals(""))) {
						localObject6 = SoftAttributesHelper.convertStringToAttributeValue(str5, str3, str2,
								localLocale);
					}
					if (j != 0) {
						localHashMap2.put(localAttributeTypeIdentifier, localObject6);
					} else if (t_ != null) {
						AttributeIdentifier[] arrayOfAttributeIdentifier = ((TypeInstance) t_)
								.getAttributeIdentifiers(localAttributeTypeIdentifier);
						if ((arrayOfAttributeIdentifier != null) && (arrayOfAttributeIdentifier.length > 0)) {
							localHashMap1.put(arrayOfAttributeIdentifier[0], localObject6);
						} else {
							localHashMap1.put(localAttributeKey.getAttributeId(), localObject6);
						}
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Modeled Attributes hashMap" + localHashMap2);
				logger.debug("SoftAttributes hashMap" + localHashMap1);
			}
			TypeHelper.setCustomAttributes((Persistable) paramIBAHolder, localHashMap2, localHashMap1, localLocale,
					paramBoolean);
		}
	}

	private static TypeInstance getTypeInstanceWithSoftAttrs(IBAHolder paramIBAHolder)
			throws IllegalFormatException, WTException {
		TypeInstanceIdentifier localTypeInstanceIdentifier = TypeIdentifierUtility
				.getTypeInstanceIdentifier(paramIBAHolder);
		TypeInstanceBean localTypeInstanceBean = new TypeInstanceBean();
		localTypeInstanceBean.setRowData(Collections.singletonList(paramIBAHolder));
		AttributeContainerSpec localAttributeContainerSpec = new AttributeContainerSpec();
		AttributeTypeIdentifier localAttributeTypeIdentifier = (AttributeTypeIdentifier) IDENTIFIER_FACTORY
				.get("ALL_SOFT_ATTRIBUTES", localTypeInstanceIdentifier.getDefinitionIdentifier());

		localAttributeContainerSpec.putEntry(localAttributeTypeIdentifier, false, false);
		localAttributeContainerSpec
				.setNextOperation(OperationIdentifier.newOperationIdentifier("STDOP|com.ptc.windchill.update"));
		localTypeInstanceBean.setFilter(localAttributeContainerSpec);
		localTypeInstanceBean.setInflateTypeInstanceRows(true);
		List localList = localTypeInstanceBean.getTypeInstances();
		TypeInstance localTypeInstance = (TypeInstance) localList.get(0);
		return localTypeInstance;
	}

	protected static ArrayList parseEncodedField(String paramString1, String paramString2) {
		ArrayList localArrayList = new ArrayList();
		int i = -1;
		int j = 0;
		if ((paramString1 != null) && (paramString1.length() > 0)) {
			do {
				j = paramString1.indexOf(paramString2, i + 1);
				if (j == -1) {
					localArrayList.add(paramString1.substring(i + 1, paramString1.length()));
					i = paramString1.length();
				} else {
					localArrayList.add(paramString1.substring(i + 1, j));
					i += paramString1.substring(i + 1, j).length() + paramString2.length();
				}
			} while (i < paramString1.length());
		}
		return localArrayList;
	}

}
