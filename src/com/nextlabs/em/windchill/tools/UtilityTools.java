package com.nextlabs.em.windchill.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.nextlabs.EntitlementManagerContext;

public class UtilityTools {
	private static Logger logger = Logger.getLogger(UtilityTools.class);
	EntitlementManagerContext emCtx = new EntitlementManagerContext();

	public static HashMap<String, List<String>> parseSecurityLabels(
			String secLabels, String tagForContent) {
		HashMap<String, List<String>> tags = new HashMap<String, List<String>>();
		HashMap<String, HashMap<String, List<String>>> secTags = new HashMap<String, HashMap<String, List<String>>>();
		EntitlementManagerContext emCtx = new EntitlementManagerContext();
		secLabels = secLabels.replace("LAUQE", "=");
		secLabels = secLabels.replace("CAMMOC", ",");

		if (secLabels != null && secLabels.isEmpty() == false) {

			if (secLabels.contains(",") == true) {
				String labels[] = secLabels.split(",");
				for (int i = 0; i < labels.length; i++) {
					if (labels[i] != null && labels[i].contains("=")) {
						String nameVal[] = labels[i].split("=");
						if (nameVal.length == 2) {
							if (!(nameVal[1].startsWith("["))) {
								List<String> values = new ArrayList<String>();

								values.add(nameVal[1]);
								tags.put(nameVal[0], values);

								logger.info("nxtlbs em ctx localfile nameVal[0]"
										+ nameVal[0]);
								logger.info("nxtlbs em ctx localfile values"
										+ values);
								logger.info("nxtlbs em ctx localfile tags"
										+ tags);
							} else {
								secTags.putAll(getValidTags(nameVal[1]));
							}

						}

					}
				}
			}
		}
		logger.info( "******Secondary Tags:" + tags);

		if (tagForContent.equalsIgnoreCase("primary"))
			return tags;
		else {
			if (secTags.get(tagForContent) != null) {
				HashMap<String, List<String>> fileTags = secTags
						.get(tagForContent);
				logger.info( "******Secondary Tags:" + secTags);
				/*
				 * for (String tagname : fileTags.keySet()) { tags.put(tagname,
				 * fileTags.get(tagname));
				 * 
				 * }
				 */
				return fileTags;
			} else {
				return tags;
			}
		}
	}

	private static HashMap<String, HashMap<String, List<String>>> getValidTags(
			String value) {
		HashMap<String, HashMap<String, List<String>>> resultmap = new HashMap<String, HashMap<String, List<String>>>();

		StringTokenizer filetags = new StringTokenizer(value, "[]");
		while (filetags.hasMoreTokens()) {
			String filetoken = filetags.nextToken();
			HashMap<String, List<String>> tagmap = new HashMap<String, List<String>>();
			String[] filetokens = filetoken.split(":");
			String filename = filetokens[0];
			if (filetokens.length == 2) {
				StringTokenizer tags = new StringTokenizer(filetokens[1], "|");

				while (tags.hasMoreTokens()) {
					String tag = tags.nextToken();
					String[] tagContent = tag.split(">>");
					String tagName = tagContent[0];
					String tagValues = tagContent[1];
					String[] tagvalue = tagValues.split(";");
					ArrayList<String> list = new ArrayList<String>();
					for (String val : tagvalue)
						list.add(val);
					tagmap.put(tagName, list);
				}
			}
			resultmap.put(filename, tagmap);
		}
		return resultmap;
	}

	public static void main(String args[]) {
		String value = "NEXTLABS_EM=ENFORCED,THIRD_PARTY_PROPRIETARY=[Powerpoint PPTX.pptx:][pr.docx:NEXTLABS_EM>>enforced|][Public_Document.docx:EXPORT_CONTROL>>LNE|],EXPORT_CONTROL=DNE";
		System.out.println(parseSecurityLabels(value, "pr.docx"));
	}
}
