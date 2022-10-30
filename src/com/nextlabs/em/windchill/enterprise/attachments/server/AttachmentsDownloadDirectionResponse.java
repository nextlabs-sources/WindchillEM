package com.nextlabs.em.windchill.enterprise.attachments.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class AttachmentsDownloadDirectionResponse implements Serializable {
	public static final String PREF_KEY_OPERATION = "/com/ptc/windchill/enterprise/attachments/downloadOperation";
	public static final String PREF_KEY_DEFAULT_DIR = "/com/ptc/windchill/enterprise/attachments/defaultLocalDirectory";
	private static final long serialVersionUID = 1L;
	private Map<String, AttachmentsDownloadDirectionResponseHolder> m_OIDmap = new HashMap();
	private Map<String, String> m_associates = new HashMap();

	public String[] getAllContentHolderOIDs() {
		String[] arrayOfString = new String[this.m_OIDmap.size()];
		arrayOfString = (String[]) this.m_OIDmap.keySet().toArray(arrayOfString);
		return arrayOfString;
	}

	public AttachmentsDownloadDirectionResponseHolder getHolder(String paramString) {
		return (AttachmentsDownloadDirectionResponseHolder) this.m_OIDmap.get(paramString);
	}

	public AttachmentsDownloadDirectionResponseItem getSingletonItem() {
		if (this.m_OIDmap.size() != 1) {
			return null;
		}
		AttachmentsDownloadDirectionResponseHolder localAttachmentsDownloadDirectionResponseHolder = (AttachmentsDownloadDirectionResponseHolder) this.m_OIDmap
				.values().iterator().next();
		if (localAttachmentsDownloadDirectionResponseHolder.getNumberOfItems() != 1) {
			return null;
		}
		return localAttachmentsDownloadDirectionResponseHolder.getAllItems()[0];
	}

	public String getAssociatedPreference(String paramString) {
		return (String) this.m_associates.get(paramString);
	}

	public void setAssociatedPreference(String paramString1, String paramString2) {
		this.m_associates.put(paramString1, paramString2);
	}

	void addHolder(AttachmentsDownloadDirectionResponseHolder paramAttachmentsDownloadDirectionResponseHolder) {
		this.m_OIDmap.put(paramAttachmentsDownloadDirectionResponseHolder.getContentHolderOID(),
				paramAttachmentsDownloadDirectionResponseHolder);
	}
}
