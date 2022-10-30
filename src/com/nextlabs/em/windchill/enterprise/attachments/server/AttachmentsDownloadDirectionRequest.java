package com.nextlabs.em.windchill.enterprise.attachments.server;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class AttachmentsDownloadDirectionRequest
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public static final int I_TYPE_BROWSER = 0;
  public static final int I_TYPE_DTI = 1;
  public static final int I_TYPE_APPLET = 2;
  public static final String UNKNOWN_HOLDER_OID = "unknown_holder_oid";
  private int m_iDownload = 0;
  private Map<String, String[]> m_OIDmap = new HashMap();
  
  public int getDownloadType()
  {
    return this.m_iDownload;
  }
  
  public String[] getAllContentHolderOIDs()
  {
    Set localSet = this.m_OIDmap.keySet();
    String[] arrayOfString = new String[localSet.size()];
    arrayOfString = (String[])localSet.toArray(arrayOfString);
    return arrayOfString;
  }
  
  public AttachmentsDownloadDirectionRequestHolder getAttachmentsDownloadDirectionRequestHolder(String paramString)
  {
    if (this.m_OIDmap.containsKey(paramString)) {
      return new AttachmentsDownloadDirectionRequestHolder(getContentItemOIDs(paramString));
    }
    return null;
  }
  
  public String[] getContentItemOIDs(String paramString)
  {
    String[] arrayOfString = (String[])this.m_OIDmap.get(paramString);
    return arrayOfString;
  }
  
  public void setAttachmentsDownloadDirectionRequestHolder(String paramString, AttachmentsDownloadDirectionRequestHolder paramAttachmentsDownloadDirectionRequestHolder)
  {
    if (paramString == null) {
      paramString = "unknown_holder_oid";
    }
    this.m_OIDmap.put(paramString, paramAttachmentsDownloadDirectionRequestHolder.getContentItemOIDs());
  }
  
  public void setDownloadType(int paramInt)
  {
    switch (paramInt)
    {
    case 0: 
    case 1: 
    case 2: 
      this.m_iDownload = paramInt;
    }
  }
  
  public Map getOIDMap()
  {
    return this.m_OIDmap;
  }
  
  void removeAttachmentsDownloadDirectionRequestHolder(String paramString)
  {
    this.m_OIDmap.remove(paramString);
  }
}
