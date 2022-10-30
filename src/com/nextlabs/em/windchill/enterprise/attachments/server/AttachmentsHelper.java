package com.nextlabs.em.windchill.enterprise.attachments.server;


import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.log4j.Logger;

import wt.access.AccessControlHelper;
import wt.access.AccessPermission;
import wt.access.NotAuthorizedException;
import wt.content.ContentHolder;
import wt.log4j.LogR;
import wt.services.ServiceFactory;
import wt.util.WTException;

import com.ptc.netmarkets.model.NmSimpleOid;

public class AttachmentsHelper
  implements Externalizable
{
  private static final String RESOURCE = "com.ptc.windchill.enterprise.attachments.server.serverResource";
  private static final String CLASSNAME = AttachmentsHelper.class.getName();
  public static final AttachmentsService service = (AttachmentsService)ServiceFactory.getService(AttachmentsService.class);
  static final long serialVersionUID = 1L;
  public static final long EXTERNALIZATION_VERSION_UID = 957977401221134810L;
  private static final Logger log = LogR.getLogger(AttachmentsHelper.class.getName());
  
  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeLong(957977401221134810L);
  }
  
  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    long l = paramObjectInput.readLong();
    if (l != 957977401221134810L) {
      throw new InvalidClassException(CLASSNAME, "Local class not compatible: stream classdesc externalizationVersionUID=" + l + " local class externalizationVersionUID=" + 957977401221134810L);
    }
  }
  
  public String escapeCharactersForDisplayString(String paramString)
  {
    return null;
  }
  
  private String replace(String[] paramArrayOfString1, String[] paramArrayOfString2, String paramString)
  {
    log.trace("AttachmentsHelper.replace()");
    log.debug("     chars = " + paramArrayOfString1);
    log.debug("     reps = " + paramArrayOfString2);
    log.debug("     input = " + paramString);
    if (paramString == null) {
      return "";
    }
    int i = paramString.length();
    for (int j = 0; j < paramArrayOfString1.length; j++)
    {
      int k = paramString.indexOf(paramArrayOfString1[j]);
      while (k >= 0)
      {
        int n = paramArrayOfString2[j].length();
        int m = paramArrayOfString1[j].length();
        paramString = paramString.substring(0, k) + paramArrayOfString2[j] + paramString.substring(k + m, i);
        i = paramString.length();
        k = paramString.indexOf(paramArrayOfString1[j], k + n);
      }
    }
    return paramString;
  }
  
  public String escapeCharactersForURL(String paramString)
  {
    log.trace("AttachmentsHelper.escapeCharactersForURL()");
    log.debug("     input=\"" + paramString + "\"");
    
    String[] arrayOfString1 = { "\"", "'" };
    String[] arrayOfString2 = { "%22", "%27" };
    return replace(arrayOfString1, arrayOfString2, paramString);
  }
  
  public String fixURLProtocol(String paramString)
  {
    log.trace("AttachmentsHelper.fixURLProtocol()");
    log.debug("     urlString=\"" + paramString + "\"");
    if ((paramString.length() == 0) || (paramString.lastIndexOf("//") != -1) || (paramString.lastIndexOf("\\\\") != -1)) {
      return paramString;
    }
    return "http://" + paramString;
  }
  
  public static NmSimpleOid createNmSimpleOid(String paramString)
  {
    NmSimpleOid localNmSimpleOid = null;
    if (paramString != null)
    {
      localNmSimpleOid = new NmSimpleOid();
      
      localNmSimpleOid.setInternalName(paramString + ":" + System.currentTimeMillis());
      localNmSimpleOid.setType(paramString);
    }
    return localNmSimpleOid;
  }
  
  public static boolean isContentDownloadAllowed(ContentHolder paramContentHolder)
  {
    return hasPermission(paramContentHolder, AccessPermission.DOWNLOAD);
  }
  
  public static boolean isModifyContentAllowed(ContentHolder paramContentHolder)
  {
    return hasPermission(paramContentHolder, AccessPermission.MODIFY_CONTENT);
  }
  
  public static boolean hasPermission(ContentHolder paramContentHolder, AccessPermission paramAccessPermission)
  {
    boolean bool = false;
    if ((paramContentHolder != null) && (paramAccessPermission != null)) {
      try
      {
        bool = AccessControlHelper.manager.checkAccess(paramContentHolder, paramAccessPermission);
      }
      catch (NotAuthorizedException localNotAuthorizedException)
      {
        bool = false;
      }
      catch (WTException localWTException)
      {
        bool = false;
      }
    }
    return bool;
  }
}