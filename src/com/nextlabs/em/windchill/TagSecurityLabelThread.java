package com.nextlabs.em.windchill;

import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import wt.access.AccessControlServerHelper;
import wt.access.SecurityLabels;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.FormatContentHolder;
import wt.content.Streamed;
import wt.doc.WTDocument;
import wt.fv.FvProperties;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

public class TagSecurityLabelThread extends Thread {
	private Logger logger = Logger.getLogger(TagSecurityLabelThread.class);

	public void run() {
		WTDocument doc;
		logger.info("  ========== 	Timer Task Called; ");
		doc = QueryAgentImpl.documents.poll();
		while (doc != null) {

			logger.info("  ==========wtdoc doc :" + doc);

			writeToTempDirectory(doc);
			tagsecuritylabel(doc);

			doc = QueryAgentImpl.documents.poll();
		}
	}

	private void writeToTempDirectory(WTDocument doc) {

		String path = WindchillObjectHelper.getWTTempPath() + "\\nxldrm\\";

		logger.info("  ==========wtdoc path :" + path);

		try {
			logger.info("  ==========wtdoc 	doc.getLocation(); :" + doc);

			File dir = new File(path);
			dir.mkdirs();
			FormatContentHolder localFormatContentHolder = (FormatContentHolder) ContentHelper.service
					.getContents((ContentHolder) doc);
			logger.info("  ==========wtdoc streamed object localFormatContentHolder is :"
					+ localFormatContentHolder.getClass());
			ContentItem localContentItem = ContentHelper
					.getPrimary(localFormatContentHolder);
			logger.info("  ==========wtdoc streamed object localContentItem is :"
					+ localContentItem);
			if ((localContentItem != null)
					&& ((localContentItem instanceof ApplicationData))) {
				ApplicationData localApplicationData = (ApplicationData) localContentItem;
				logger.info("  ==========wtdoc streamed object localApplicationData is :"
						+ localApplicationData);
				Object localObject3 = localApplicationData != null ? (Streamed) localApplicationData
						.getStreamData().getObject() : null;
				if (localObject3 != null) {
					File file = new File(path,
							localApplicationData.getFileName());
					if (((Streamed) localObject3).retrieveStream() != null) {
						BufferedInputStream localBufferedInputStream = new BufferedInputStream(
								((Streamed) localObject3).retrieveStream());
						Object localObject4 = new BufferedOutputStream(
								new FileOutputStream(file));
						Object localObject5 = new byte[FvProperties.READ_BUFFER_SIZE];
						int n;
						while ((n = localBufferedInputStream
								.read((byte[]) localObject5)) > 0) {
							((BufferedOutputStream) localObject4).write(
									(byte[]) localObject5, 0, n);
						}
						localBufferedInputStream.close();
						((BufferedOutputStream) localObject4).close();
					} else {

						logger.info("  ==========wtdoc streamed object retrievestream is null :");
					}
				} else {

					logger.info("  ==========wtdoc streamed object  is null :"
							+ localObject3);
				}

			}

		} catch (WTException e) {
			logger.error("  ==========wtdoc WTException :" + e.getMessage());
		} catch (IOException e) {
			logger.error("  ==========wtdoc IOException :" + e.getMessage());
		} catch (PropertyVetoException e) {
			logger.error("  ==========wtdoc PropertyVetoException :"
					+ e.getMessage());
		}

	}

	private void tagsecuritylabel(WTDocument doc) {
		SecurityLabels sl;

		try {
			logger.info("  ==========Security Labels:"
					+ doc.getSecurityLabels().toString());
			sl = SecurityLabels
					.newSecurityLabels("1,NEXTLABS_EM=ENFORCED,EXPORT_CONTROL=DNE");
			logger.info("  ==========Security Labels:" + sl.toString());

			AccessControlServerHelper.manager.setSecurityLabel(doc,
					"NEXTLABS_EM", "ENFORCED", true);

			// PersistenceServerHelper.manager.update(doc);

		} catch (WTPropertyVetoException e) {
			logger.error("  ==========Security Labels:WT Exception"
					+ e.getMessage());
			logger.error(e);
		} catch (Exception e) {
			logger.error("  ==========Security Labels:WT Exception"
					+ e.getMessage());
			logger.error(e);

		}
	}

}
