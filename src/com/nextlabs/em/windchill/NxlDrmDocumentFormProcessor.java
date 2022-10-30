package com.nextlabs.em.windchill;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import wt.fc.Persistable;

import com.nextlabs.EntitlementManagerContext;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.beans.NmCommandBean;

public class NxlDrmDocumentFormProcessor {
	private static Logger logger = Logger
			.getLogger(NxlDrmDocumentFormProcessor.class);

	public static Object execute(NmCommandBean arg) {
		EntitlementManagerContext ctx = new EntitlementManagerContext();
		HttpServletRequest httpServletRequest = arg.getRequest();
		String requestId = (String) httpServletRequest
				.getAttribute("requestId");

		if (requestId != null && requestId.isEmpty() == false) {
			ctx.setRequestId(requestId);
		}
		logger.debug(" NxlDrmDocumentFormProcessor.execute begin...");
		try {
			ArrayList<String> oids = new ArrayList<String>();
			ArrayList<NmOid> nmOids = arg.getNmOidSelected();
			for (int idx = 0; idx < nmOids.size(); idx++) {
				NmOid nmoid = nmOids.get(idx);
				logger.debug(" oid[" + (idx + 1) + "]=" + nmoid.toString());
				oids.add(nmoid.toString());
			}

			String actionOidStr = arg.getActionOid().toString();
			logger.debug(" action oid:" + actionOidStr);
			String[] actioniOids = actionOidStr.split("~");
			for (int i = 0; i < actioniOids.length; i++) {
				logger.debug(" actionOid[" + (i + 1) + "]=" + actioniOids[i]);
				oids.add(actioniOids[i]);

			}
			for (int i = 0; i < oids.size(); i++) {
				String oid = oids.get(i);
				Persistable wcObj = WindchillObjectHelper.getObject(oid);
				if (wcObj instanceof wt.doc.WTDocument) {
					wt.doc.WTDocument doc = (wt.doc.WTDocument) wcObj;
					logger.debug(oid + " is a wt.doc.wtdocument");
					WindchillObjectHelper.nxlDrmDoc(ctx, doc, null);
				}
			}
			// Persistable wcObj=WindchillObjectHelper.getObject(actionOid);

		} catch (Exception exp) {
			logger.warn(" exception message:" + exp.getMessage());
		}
		return new Object();
	}
}
