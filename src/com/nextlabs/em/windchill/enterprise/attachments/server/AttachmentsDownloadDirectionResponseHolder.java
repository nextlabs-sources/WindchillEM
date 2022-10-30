package com.nextlabs.em.windchill.enterprise.attachments.server;

import java.io.Serializable;
import java.util.Vector;

public final class AttachmentsDownloadDirectionResponseHolder implements Serializable {
	private static final long serialVersionUID = 1L;
	private String m_oid = null;
	private AttachmentsDownloadDirectionResponseItem m_primary = null;
	private Vector<AttachmentsDownloadDirectionResponseItem> m_secondaries = new Vector();
	private String m_strName = null;

	AttachmentsDownloadDirectionResponseHolder(String paramString) {
		this.m_oid = paramString;
	}

	public String getContentHolderOID() {
		return this.m_oid;
	}

	public AttachmentsDownloadDirectionResponseItem[] getAllItems() {
		AttachmentsDownloadDirectionResponseItem[] arrayOfAttachmentsDownloadDirectionResponseItem = null;
		int i;
		if (this.m_primary != null) {
			arrayOfAttachmentsDownloadDirectionResponseItem = new AttachmentsDownloadDirectionResponseItem[this.m_secondaries
					.size() + 1];
			arrayOfAttachmentsDownloadDirectionResponseItem[0] = this.m_primary;
			for (i = 1; i < arrayOfAttachmentsDownloadDirectionResponseItem.length; i++) {
				arrayOfAttachmentsDownloadDirectionResponseItem[i] = ((AttachmentsDownloadDirectionResponseItem) this.m_secondaries
						.get(i - 1));
			}
		} else {
			arrayOfAttachmentsDownloadDirectionResponseItem = new AttachmentsDownloadDirectionResponseItem[this.m_secondaries
					.size()];
			for (i = 0; i < arrayOfAttachmentsDownloadDirectionResponseItem.length; i++) {
				arrayOfAttachmentsDownloadDirectionResponseItem[i] = ((AttachmentsDownloadDirectionResponseItem) this.m_secondaries
						.get(i));
			}
		}
		return arrayOfAttachmentsDownloadDirectionResponseItem;
	}

	public int getNumberOfItems() {
		if (this.m_primary == null) {
			return this.m_secondaries.size();
		}
		return this.m_secondaries.size() + 1;
	}

	public String getName() {
		return this.m_strName;
	}

	void setPrimaryItem(AttachmentsDownloadDirectionResponseItem paramAttachmentsDownloadDirectionResponseItem) {
		this.m_primary = paramAttachmentsDownloadDirectionResponseItem;
		this.m_primary.setPrimaryContentItem(true);
	}

	void addSecondaryItem(AttachmentsDownloadDirectionResponseItem paramAttachmentsDownloadDirectionResponseItem) {
		paramAttachmentsDownloadDirectionResponseItem.setPrimaryContentItem(false);
		this.m_secondaries.add(paramAttachmentsDownloadDirectionResponseItem);
	}

	void setName(String paramString) {
		this.m_strName = paramString;
	}
}
