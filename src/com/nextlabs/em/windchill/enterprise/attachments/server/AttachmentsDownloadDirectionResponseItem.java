package com.nextlabs.em.windchill.enterprise.attachments.server;

import java.io.Serializable;
import java.sql.Timestamp;

public final class AttachmentsDownloadDirectionResponseItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private String m_oid = null;
	private String m_strFileName = null;
	private String m_strDownloadURL = null;
	private boolean m_bPrimary = false;
	private Timestamp m_lastModified = null;

	AttachmentsDownloadDirectionResponseItem(String paramString) {
		this.m_oid = paramString;
	}

	public String getContentItemOID() {
		return this.m_oid;
	}

	public String getFileName() {
		return this.m_strFileName;
	}

	public String getDownloadURLString() {
		return this.m_strDownloadURL;
	}

	public boolean isPrimaryConteintItem() {
		return this.m_bPrimary;
	}

	public Timestamp getLastModifiedTimestamp() {
		return this.m_lastModified;
	}

	void setFileName(String paramString) {
		this.m_strFileName = paramString;
	}

	void setDownloadURLString(String paramString) {
		this.m_strDownloadURL = paramString;
	}

	void setPrimaryContentItem(boolean paramBoolean) {
		this.m_bPrimary = paramBoolean;
	}

	void setLastModifiedTimestamp(Timestamp paramTimestamp) {
		this.m_lastModified = paramTimestamp;
	}
}
