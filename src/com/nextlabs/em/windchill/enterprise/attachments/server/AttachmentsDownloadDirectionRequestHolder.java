package com.nextlabs.em.windchill.enterprise.attachments.server;

import java.io.Serializable;

public final class AttachmentsDownloadDirectionRequestHolder implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int I_SCOPE_PRIMARY = 0;
	public static final int I_SCOPE_ALL = 1;
	public static final int I_SCOPE_SOME_ITEMS = 2;
	private int m_iScope = 0;
	private String[] m_itemOIDs = null;

	public AttachmentsDownloadDirectionRequestHolder(int paramInt, String[] paramArrayOfString) {
		switch (paramInt) {
		case 0:
		default:
			this.m_iScope = 0;
			this.m_itemOIDs = null;
			break;
		case 1:
			this.m_iScope = 1;
			this.m_itemOIDs = new String[0];
			break;
		case 2:
			this.m_iScope = 2;
			this.m_itemOIDs = paramArrayOfString;
		}
	}

	public AttachmentsDownloadDirectionRequestHolder(String[] paramArrayOfString) {
		if (paramArrayOfString == null) {
			this.m_iScope = 0;
			this.m_itemOIDs = paramArrayOfString;
		} else if (paramArrayOfString.length == 0) {
			this.m_iScope = 1;
			this.m_itemOIDs = paramArrayOfString;
		} else {
			this.m_iScope = 2;
			this.m_itemOIDs = paramArrayOfString;
		}
	}

	public int getDownloadScope() {
		return this.m_iScope;
	}

	public String[] getContentItemOIDs() {
		return this.m_itemOIDs;
	}
}
