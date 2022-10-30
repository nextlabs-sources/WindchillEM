package com.nextlabs.em.windchill.netmarkets.model;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import wt.admin.AdminDomainRef;
import wt.content.ContentHolder;
import wt.content.ContentRoleType;
import wt.lifecycle.State;
import wt.method.RemoteInterface;
import wt.util.LocalizableMessage;
import wt.util.WTException;

import com.ptc.core.components.forms.FormResult;
import com.ptc.netmarkets.model.NmChangeModel;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.model.NmPasteInfo;
import com.ptc.netmarkets.model.NmSoftAttribute;
import com.ptc.netmarkets.util.beans.NmClipboardBean;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.netmarkets.util.beans.NmLinkBean;
import com.ptc.netmarkets.util.table.NmHTMLTable;

@RemoteInterface
public abstract interface NmObjectService
{
  public abstract FormResult delete(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract FormResult checkIn(NmCommandBean paramNmCommandBean, String paramString1, String paramString2, String paramString3, String paramString4)
    throws WTException;
  
  public abstract FormResult checkOut(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract FormResult undoCheckOut(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract NmChangeModel[] rollupIterations(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract FormResult list_delete(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract FormResult list__delete(NmCommandBean paramNmCommandBean, ArrayList paramArrayList)
    throws WTException;
  
  public abstract NmChangeModel[] list_undoCheckOut(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract String getDisplayName(NmOid paramNmOid)
    throws WTException;
  
  public abstract void uploadContent(ContentHolder paramContentHolder, String paramString1, String paramString2)
    throws WTException;
  
  public abstract void uploadContent(ContentHolder paramContentHolder, String paramString1, String paramString2, ContentRoleType paramContentRoleType)
    throws WTException;
  
  public abstract String getDisplayLocation(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract NmChangeModel[] importObjects(NmCommandBean paramNmCommandBean, String paramString1, String paramString2)
    throws WTException;
  
  public abstract String exportObjects(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract String getDefaultViewString(NmOid paramNmOid, boolean paramBoolean)
    throws WTException;
  
  public abstract String getLocation(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract Vector getLifeCycleStates(NmOid paramNmOid)
    throws WTException;
  
  public abstract State getCurrentState(NmOid paramNmOid)
    throws WTException;
  
  public abstract NmHTMLTable report(NmCommandBean paramNmCommandBean, String paramString1, String paramString2, String paramString3, String paramString4)
    throws WTException;
  
  public abstract NmHTMLTable getClipboardContents(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract NmPasteInfo getPasteInfo(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract ArrayList getPSPart(String paramString1, String paramString2, String paramString3, ArrayList paramArrayList)
    throws WTException;
  
  public abstract NmSoftAttribute getSoftAttribute(NmCommandBean paramNmCommandBean, NmLinkBean paramNmLinkBean)
    throws WTException;
  
  public abstract ArrayList getTypes(String paramString, NmOid paramNmOid1, NmOid paramNmOid2)
    throws WTException;
  
  public abstract NmChangeModel[] removeShare(NmCommandBean paramNmCommandBean, NmOid paramNmOid)
    throws WTException;
  
  public abstract ArrayList getObjectViewObjects(NmOid paramNmOid1, NmOid paramNmOid2)
    throws WTException;
  
  public abstract NmSoftAttribute getIBAsForObject(NmOid paramNmOid)
    throws WTException;
  
  public abstract ArrayList getTypes(String paramString, NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract void expandZipFileIntoFolder(NmCommandBean paramNmCommandBean, String paramString)
    throws WTException;
  
  public abstract URL downloadFolderContentFiles(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract void addSCMIAttachment(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract String getTypeSelector(String paramString1, NmOid paramNmOid, String paramString2, String paramString3, String paramString4, String paramString5)
    throws WTException;
  
  public abstract Boolean isShareUsed(NmOid paramNmOid)
    throws WTException;
  
  public abstract LocalizableMessage isRemoveShareValidOperation(NmOid paramNmOid)
    throws WTException;
  
  public abstract String getFilePath(Object paramObject1, Object paramObject2)
    throws WTException;
  
  public abstract List getInflatedClipboardItems(NmClipboardBean paramNmClipboardBean)
    throws WTException;
  
  public abstract FormResult setTemplateVisibility(NmCommandBean paramNmCommandBean, Boolean paramBoolean)
    throws WTException;
  
  public abstract FormResult setTemplateListVisibility(NmCommandBean paramNmCommandBean, Boolean paramBoolean)
    throws WTException;
  
  public abstract NmChangeModel[] importSavedQueries(NmCommandBean paramNmCommandBean, String paramString1, String paramString2)
    throws WTException;
  
  public abstract String exportSavedQueries(NmCommandBean paramNmCommandBean)
    throws WTException;
  
  public abstract String getTypeSelector(String paramString1, NmOid paramNmOid, String paramString2, String paramString3, String paramString4, String paramString5, AdminDomainRef paramAdminDomainRef)
    throws WTException;
}
