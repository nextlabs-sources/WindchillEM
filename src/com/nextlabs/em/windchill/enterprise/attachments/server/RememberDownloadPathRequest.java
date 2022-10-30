package com.nextlabs.em.windchill.enterprise.attachments.server;


import java.io.Serializable;

public final class RememberDownloadPathRequest
  implements Serializable
{
  private String m_holderOID = null;
  private String m_downloadPath = null;
  
  public String getHolderOID()
  {
    return this.m_holderOID;
  }
  
  public String getDownloadPath()
  {
    return this.m_downloadPath;
  }
  
  public void setHolderOID(String paramString)
  {
    this.m_holderOID = paramString;
  }
  
  public void setDownloadPath(String paramString)
  {
    this.m_downloadPath = paramString;
  }
}

