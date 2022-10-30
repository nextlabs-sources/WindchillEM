package com.nextlabs.em.windchill.enterprise.attachments.server;


import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.List;

import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.FormatContentHolder;
import wt.fc.QueryResult;
import wt.fv.uploadtocache.CachedContentDescriptor;
import wt.method.RemoteInterface;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

import com.ptc.netmarkets.model.NmOid;

@RemoteInterface
public abstract interface AttachmentsService
{
  public abstract void persistAttachments(ContentHolder paramContentHolder, List<ContentItem> paramList, HashMap<ContentItem, CachedContentDescriptor> paramHashMap)
    throws WTException, PropertyVetoException;
  
  public abstract QueryResult getAttachments(Object paramObject, ContentRoleType paramContentRoleType)
    throws WTException;
  
  public abstract boolean isPrimaryContentRequired(FormatContentHolder paramFormatContentHolder)
    throws WTException;
  
  public abstract boolean isPrimaryContentExisting(FormatContentHolder paramFormatContentHolder)
    throws WTException;
  
  public abstract boolean isPrimaryContentSelected(FormatContentHolder paramFormatContentHolder)
    throws WTException;
  
  public abstract boolean isPrimaryContentUploaded(FormatContentHolder paramFormatContentHolder)
    throws WTException;
  
  public abstract void removeAttachments(ContentHolder paramContentHolder, List<NmOid> paramList)
    throws WTException, WTPropertyVetoException;
  
  public abstract AttachmentsDownloadDirectionResponse downloadAttachmentDirections(AttachmentsDownloadDirectionRequest paramAttachmentsDownloadDirectionRequest)
    throws WTException;
  
  public abstract String redirectAttachmentURL(String paramString)
    throws WTException;
  
  public abstract void rememberDownloadPath(RememberDownloadPathRequest paramRememberDownloadPathRequest)
    throws WTException;
  
  public abstract void persistAttachments(ContentHolder paramContentHolder, List<ContentItem> paramList, HashMap<ContentItem, CachedContentDescriptor> paramHashMap, HashMap paramHashMap1)
    throws WTException, PropertyVetoException;
}
