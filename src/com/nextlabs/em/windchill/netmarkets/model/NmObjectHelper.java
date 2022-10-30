package com.nextlabs.em.windchill.netmarkets.model;



import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import wt.access.AccessControlHelper;
import wt.access.AccessControlServerHelper;
import wt.access.AccessPermission;
import wt.access.NotAuthorizedException;
import wt.doc.WTDocument;
import wt.enterprise.EnterpriseHelper;
import wt.enterprise.ObjectPropertyValue;
import wt.enterprise.RevisionControlled;
import wt.fc.EnumeratedType;
import wt.fc.EvolvableHelper;
import wt.fc.ObjectIdentifier;
import wt.fc.Persistable;
import wt.folder.Cabinet;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderException;
import wt.folder.FolderHelper;
import wt.folder.FolderNotFoundException;
import wt.folder.Foldered;
import wt.folder.SubFolder;
import wt.httpgw.GatewayServletHelper;
import wt.httpgw.URLFactory;
import wt.identity.IdentityFactory;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.inf.team.ContainerTeamManaged;
import wt.inf.team.ContainerTeamServerHelper;
import wt.log4j.LogR;
import wt.method.RemoteAccess;
import wt.org.WTOrganization;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pds.PDSObjectInput;
import wt.prefs.PreferenceHelper;
import wt.prefs.WTPreferences;
import wt.services.ServiceFactory;
import wt.session.SessionContext;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;
import wt.util.WTMessage;
import wt.util.WTRuntimeException;
import wt.vc.Iterated;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

import com.ptc.netmarkets.model.NmException;
import com.ptc.netmarkets.model.NmNamedObject;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.model.NmPDMObject;
import com.ptc.netmarkets.util.NmUtilClassProxy;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.netmarkets.util.beans.NmSessionBean;

public class NmObjectHelper
  implements Externalizable, RemoteAccess
{
  private static final String RESOURCE = "com.ptc.netmarkets.model.modelResource";
  private static final String MORE_RESOURCE = "com.ptc.netmarkets.object.objectResource";
  private static final String CLASSNAME = NmObjectHelper.class.getName();
  public static final NmObjectService service = (NmObjectService)ServiceFactory.getService(NmObjectService.class);
  static final long serialVersionUID = 1L;
  public static final long EXTERNALIZATION_VERSION_UID = 957977401221134810L;
  protected static final long OLD_FORMAT_VERSION_UID = -3161748870496491727L;
  private static final Logger logger = LogR.getLogger(NmObjectHelper.class.getName());
  public static final String MISC_RESOURCE = "com.ptc.netmarkets.util.misc.miscResource";
  public static final String PJLJ_IGNORE_CACHE = "PJLJ_IGNORE_CACHE";
  public static final String TYPE_ALL = "all";
  public static final String TYPE_PROJECT = "proj";
  public static final String TYPE_DOCUMENT = "doc";
  public static final String TYPE_PART = "part";
  public static final String TYPE_MILESTONE = "mile";
  public static final String TYPE_DELIVERABLE = "deli";
  public static final String TYPE_MEETING = "meet";
  public static final String TYPE_PROJ_ACTIVITY = "projActivity";
  public static final String SCOPE_PROJECT = "project";
  public static final String SCOPE_GLOBAL = "global";
  public static final String MODEL_DEFAULT = "default";
  public static final int SELECT_TYPE_NONE = 0;
  public static final int SELECT_TYPE_SINGLE = 1;
  public static final int SELECT_TYPE_MULTI = 2;
  public static final String QUERY_0 = "qs0";
  public static final String QUERY_1 = "qs1";
  public static final String QUERY_2 = "qs2";
  public static final String QUERY_3 = "qs3";
  public static final String CB_COPY = "copy";
  public static final String CB_LINK = "link";
  public static final String CB_SHARE = "share";
  public static final String CHOICE = "choice";
  public static final String PRODUCT_STRUCTURE = "spscopy";
  public static final String CGI_DATA = "cgi_data_key";
  public static final String HTTPSTATE_OBJECT = "httpState object key";
  public static final String SKIP_IF_NOT = "skip_if_not_created";
  public static final String LOCATION = "location";
  public static final String LOCATION_LEVEL_1 = "location_level_one";
  public static final String LOCATION_LEVEL_2 = "location_level_two";
  public static final String LOCATION_LEVEL_3 = "location_level_three";
  public static final String LOCATION_LEVEL_ELIPSIS = "location_level_elipsis";
  public static final String LOCATION_LEVEL_1_SECURED = "location_level_one_secured";
  public static final String LOCATION_LEVEL_2_SECURED = "location_level_two_secured";
  public static final String LOCATION_LEVEL_3_SECURED = "location_level_three_secured";
  public static final String LOCATION_LEVEL_ELIPSIS_SECURED = "location_level_elipsis_secured";
  public static final String LOCATION_LEVEL_1_DISPLAY = "location_level_one_display";
  public static final String LOCATION_LEVEL_2_DISPLAY = "location_level_two_display";
  public static final String LOCATION_LEVEL_3_DISPLAY = "location_level_three_display";
  public static final String LOCATION_GREATERTHAN_3 = "location_greaterthan_three";
  public static final String CONTAINER_OID = "container_oid";
  
  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeLong(957977401221134810L);
  }
  
  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    long l = paramObjectInput.readLong();
    readVersion(this, paramObjectInput, l, false, false);
  }
  
  protected boolean readVersion(NmObjectHelper paramNmObjectHelper, ObjectInput paramObjectInput, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException, ClassNotFoundException
  {
    boolean bool = true;
    if (paramLong == 957977401221134810L) {
      return readVersion957977401221134810L(paramObjectInput, paramLong, paramBoolean2);
    }
    bool = readOldVersion(paramObjectInput, paramLong, paramBoolean1, paramBoolean2);
    if ((paramObjectInput instanceof PDSObjectInput)) {
      EvolvableHelper.requestRewriteOfEvolvedBlobbedObject();
    }
    return bool;
  }
  
  private boolean readOldVersion(ObjectInput paramObjectInput, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException, ClassNotFoundException
  {
    boolean bool = true;
    if (paramLong != -3161748870496491727L) {
      throw new InvalidClassException(CLASSNAME, "Local class not compatible: stream classdesc externalizationVersionUID=" + paramLong + " local class externalizationVersionUID=" + 957977401221134810L);
    }
    return bool;
  }
  
  private boolean readVersion957977401221134810L(ObjectInput paramObjectInput, long paramLong, boolean paramBoolean)
    throws IOException, ClassNotFoundException
  {
    return true;
  }
  
  public static boolean isReadOnlyUser(WTContainer paramWTContainer)
  {
    try
    {
      WTContainerRef localWTContainerRef = WTContainerRef.newWTContainerRef(paramWTContainer);
      WTUser localWTUser = (WTUser)SessionHelper.getPrincipal();
      boolean bool = WTContainerHelper.service.isAdministrator(localWTContainerRef, localWTUser);
      if ((paramWTContainer instanceof ContainerTeamManaged)) {
        return (!ContainerTeamServerHelper.service.isMember((ContainerTeamManaged)paramWTContainer, localWTUser)) && (!bool);
      }
      return false;
    }
    catch (WTException localWTException) {}
    return false;
  }
  
  public static boolean isPasteCopy(NmCommandBean paramNmCommandBean)
  {
    return "copy".equals(paramNmCommandBean.getMap().get("choice"));
  }
  
  public static boolean isPasteShare(NmCommandBean paramNmCommandBean)
  {
    return "share".equals(paramNmCommandBean.getMap().get("choice"));
  }
  
  public static boolean isPasteLink(NmCommandBean paramNmCommandBean)
  {
    return "link".equals(paramNmCommandBean.getMap().get("choice"));
  }
  
  public static Integer getIntFromString(String paramString, int paramInt)
  {
    Integer localInteger = new Integer(paramInt);
    if (paramString != null) {
      try
      {
        localInteger = new Integer(paramString);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        localInteger = new Integer(paramInt);
      }
    }
    return localInteger;
  }
  
  public static String getVersionDisplay(Iterated paramIterated)
    throws WTException
  {
    Locale localLocale = SessionHelper.getLocale();
    
    Properties localProperties = new Properties();
    localProperties.put("version_info", "qualified");
    
    ObjectPropertyValue localObjectPropertyValue = new ObjectPropertyValue();
    String str = localObjectPropertyValue.objectPropertyValueString(paramIterated, "VersionInfo", localProperties, localLocale);
    
    return str;
  }
  
  @Deprecated
  public static HashMap addSandboxInfo(HashMap<Object, Object> paramHashMap, RevisionControlled paramRevisionControlled)
    throws WTException
  {
    return paramHashMap;
  }
  
  public static String getName(Object paramObject, Locale paramLocale)
  {
    if ((paramObject instanceof String)) {
      return (String)paramObject;
    }
    if ((paramObject instanceof WTDocument)) {
      return ((WTDocument)paramObject).getName();
    }
    if ((paramObject instanceof WTPart)) {
      return ((WTPart)paramObject).getName();
    }
    if ((paramObject instanceof Persistable)) {
      return IdentityFactory.getDisplayIdentifier(paramObject).getLocalizedMessage(paramLocale);
    }
    if (paramObject == null) {
      return "";
    }
    return paramObject.toString();
  }
  
  public static boolean canSeeAllDetails(NmCommandBean paramNmCommandBean)
    throws WTException
  {
    return canSeeAllDetails(paramNmCommandBean, null);
  }
  
  public static boolean canSeeAllDetails(NmCommandBean paramNmCommandBean, NmOid paramNmOid)
    throws WTException
  {
    try
    {
      if (paramNmOid == null) {
        paramNmOid = paramNmCommandBean.getPageOid();
      }
      if (paramNmOid == null) {
        paramNmOid = paramNmCommandBean.getPrimaryOid();
      }
      if (paramNmOid != null)
      {
        boolean bool = AccessControlHelper.manager.hasAccess(paramNmOid.getContainerObject(), AccessPermission.READ);
        if (!bool) {
          return hasModifyOnOid(paramNmOid);
        }
      }
    }
    catch (Exception localException)
    {
      return hasModifyOnOid(paramNmOid);
    }
    return true;
  }
  
  public static boolean hasModifyOnOid(NmOid paramNmOid)
    throws WTException
  {
    if (paramNmOid != null)
    {
      Persistable localPersistable = (Persistable)paramNmOid.getRef();
      if (localPersistable == null) {
        return false;
      }
      return AccessControlHelper.manager.hasAccess(localPersistable, AccessPermission.MODIFY);
    }
    return false;
  }
  
  public static Integer getSelectedForumView(NmOid paramNmOid, NmSessionBean paramNmSessionBean, Object paramObject)
    throws Exception
  {
    HttpServletRequest localHttpServletRequest = (HttpServletRequest)paramObject;
    Integer localInteger = new Integer(0);
    String str1 = "project$view_forum$" + paramNmOid;
    String str2 = (String)paramNmSessionBean.getPref(str1, "sv");
    if (str2 == null) {
      str2 = "0";
    }
    if (localHttpServletRequest.getParameter("sv") != null)
    {
      str2 = NmCommandBean.convert(localHttpServletRequest.getParameter("sv"));
      paramNmSessionBean.putPref(str1, "sv", str2, false);
    }
    try
    {
      localInteger = new Integer(str2);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      localInteger = new Integer(0);
    }
    return localInteger;
  }
  
  public static void addDiscussTable(NmCommandBean paramNmCommandBean, HashMap paramHashMap)
    throws WTException
  {}
  
  public static EnumeratedType[] filterEnumeratedType(EnumeratedType[] paramArrayOfEnumeratedType)
    throws WTException
  {
    Vector localVector = new Vector();
    for (int i = 0; i < paramArrayOfEnumeratedType.length; i++) {
      if (paramArrayOfEnumeratedType[i].isSelectable()) {
        localVector.add(paramArrayOfEnumeratedType[i]);
      }
    }
    return (EnumeratedType[])localVector.toArray(new EnumeratedType[localVector.size()]);
  }
  
  public static Timestamp getTimestamp(String paramString)
    throws WTException
  {
    Locale localLocale = SessionHelper.getLocale();
    if (paramString == null) {
      return null;
    }
    paramString = paramString.trim();
    if ("".equals(paramString)) {
      return null;
    }
    String str = WTMessage.getLocalizedMessage("com.ptc.netmarkets.util.misc.miscResource", "14", null, localLocale);
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(str);
    localSimpleDateFormat.setTimeZone(NmUtilClassProxy.getLocalTimeZoneForUser(SessionHelper.getPrincipal()));
    try
    {
      Date localDate = localSimpleDateFormat.parse(paramString);
      return new Timestamp(localDate.getTime());
    }
    catch (ParseException localParseException)
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = WTMessage.getLocalizedMessage("com.ptc.netmarkets.util.misc.miscResource", "14a", null, localLocale);
      throw new NmException(WTMessage.getLocalizedMessage("com.ptc.netmarkets.model.modelResource", "66", arrayOfString, localLocale));
    }
  }
  
  public static boolean getProjectLinkPref(String paramString, boolean paramBoolean)
    throws WTException
  {
    return getProjectLinkPref(paramString, WTContainerHelper.getExchangeRef(), paramBoolean);
  }
  
  public static boolean getProjectLinkPref(String paramString, WTContainerRef paramWTContainerRef, boolean paramBoolean)
    throws WTException
  {
    WTPreferences localWTPreferences = (WTPreferences)WTPreferences.root().node("/ProjectLink");
    
    localWTPreferences.setContextMask(PreferenceHelper.createContextMask(paramWTContainerRef, "", (WTUser)SessionHelper.getPrincipal()));
    
    return localWTPreferences.getBoolean(paramString, paramBoolean);
  }
  
  public static boolean isNetworkLink(Object paramObject)
  {
    return isInstanceOfClass(paramObject, "com.ptc.core.relcontext.server.NetworkLink");
  }
  
  private static boolean isInstanceOfClass(Object paramObject, String paramString)
  {
    boolean bool = false;
    try
    {
      if (paramObject != null) {
        bool = Class.forName(paramString).isAssignableFrom(paramObject.getClass());
      }
    }
    catch (ClassNotFoundException localClassNotFoundException) {}
    return bool;
  }
  
  public static String getLocationOfObject(Persistable paramPersistable, boolean paramBoolean)
    throws WTException
  {
    String str1 = "";
    if ((paramPersistable instanceof Foldered))
    {
      AccessControlServerHelper.disableNotAuthorizedAudit();
      try
      {
        str1 = ((Foldered)paramPersistable).getLocation();
        return str1;
      }
      catch (WTRuntimeException localWTRuntimeException)
      {
        Throwable localThrowable = localWTRuntimeException.getNestedThrowable();
        if (((localThrowable instanceof FolderException)) || ((localThrowable instanceof NotAuthorizedException))) {
          return "";
        }
        throw localWTRuntimeException;
      }
      finally
      {
        AccessControlServerHelper.reenableNotAuthorizedAudit();
      }
    }
    return str1;
  }
  
  public static void getLocationOfObject(Persistable paramPersistable, HashMap<Object, Object> paramHashMap)
    throws WTException
  {
    paramHashMap.put("location", getLocationOfObject(paramPersistable, true));
    
    boolean bool1 = false;
    boolean bool2;
    Object localObject1;
    if ((paramPersistable instanceof WTContained))
    {
      bool2 = SessionServerHelper.manager.setAccessEnforced(false);
      try
      {
        localObject1 = ((WTContained)paramPersistable).getContainerReference();
        bool1 = WTContainerHelper.isClassicRef((WTContainerRef)localObject1);
      }
      finally
      {
        SessionServerHelper.manager.setAccessEnforced(bool2);
      }
    }
    if ((paramPersistable instanceof Foldered))
    {
      if (((paramPersistable instanceof Workable)) && (WorkInProgressHelper.isCheckedOut((Workable)paramPersistable)) && (WorkInProgressHelper.isWorkingCopy((Workable)paramPersistable))) {
        paramPersistable = WorkInProgressHelper.service.originalCopyOf((Workable)paramPersistable);
      }
      bool2 = false;
      localObject1 = initializeNmFolderForLocation((FolderEntry)paramPersistable, paramHashMap, "location_level_one", "location_level_one_secured", bool1);
      if ((localObject1 instanceof SubFolder))
      {
        Folder localFolder1 = initializeNmFolderForLocation((FolderEntry)localObject1, paramHashMap, "location_level_two", "location_level_two_secured", bool1);
        if ((localFolder1 instanceof SubFolder))
        {
          Folder localFolder2 = initializeNmFolderForLocation((FolderEntry)localFolder1, paramHashMap, "location_level_three", "location_level_three_secured", bool1);
          if ((localFolder2 instanceof SubFolder))
          {
            bool2 = true;
            initializeNmFolderForLocation((FolderEntry)localFolder2, paramHashMap, "location_level_elipsis", "location_level_elipsis_secured", bool1);
          }
        }
      }
      paramHashMap.put("location_greaterthan_three", new Boolean(bool2));
    }
  }
  
  private static Folder initializeNmFolderForLocation(FolderEntry paramFolderEntry, HashMap<Object, Object> paramHashMap, String paramString1, String paramString2, boolean paramBoolean)
    throws WTException
  {
    Folder localFolder = null;
    AccessControlServerHelper.disableNotAuthorizedAudit();
    try
    {
      localFolder = FolderHelper.service.getFolder(paramFolderEntry);
      if (localFolder != null)
      {
        NmOid localNmOid = new NmOid(localFolder);
        
        Object localObject1 = null;
        if (paramBoolean) {
          localObject1 = new NmPDMObject();
        } else {
          localObject1 = new NmNamedObject();
        }
        if (((localFolder instanceof Cabinet)) && (!paramBoolean))
        {
          addContainerInfo((Cabinet)localFolder, (NmNamedObject)localObject1, paramHashMap);
          
          localNmOid = new NmOid((ObjectIdentifier)((Cabinet)localFolder).getContainerReference().getKey());
        }
        else
        {
          ((NmNamedObject)localObject1).setName(localFolder.getName());
        }
        ((NmNamedObject)localObject1).setOid(localNmOid);
        paramHashMap.put(paramString1, localObject1);
      }
      else
      {
        paramHashMap.put(paramString2, new Boolean(true));
      }
    }
    catch (NotAuthorizedException localNotAuthorizedException)
    {
      paramHashMap.put(paramString2, new Boolean(true));
    }
    catch (FolderNotFoundException localFolderNotFoundException)
    {
      paramHashMap.put(paramString2, new Boolean(true));
    }
    catch (WTRuntimeException localWTRuntimeException)
    {
      if ((localWTRuntimeException.getNestedThrowable() instanceof NotAuthorizedException)) {
        paramHashMap.put(paramString2, new Boolean(true));
      }
    }
    finally
    {
      AccessControlServerHelper.reenableNotAuthorizedAudit();
    }
    return localFolder;
  }
  
  private static void addContainerInfo(Cabinet paramCabinet, NmNamedObject paramNmNamedObject, HashMap<Object, Object> paramHashMap)
    throws WTException
  {
    WTContainer localWTContainer = paramCabinet.getContainer();
    paramNmNamedObject.setName(localWTContainer.getName());
    NmOid localNmOid = new NmOid();
    NmCommandBean.setTypes(localWTContainer, localNmOid);
    localNmOid.setOid((ObjectIdentifier)paramCabinet.getContainerReference().getKey());
    paramHashMap.put("container_oid", localNmOid);
  }
  
  public static boolean isNewNumRequired(WTOrganization paramWTOrganization, Class paramClass)
    throws WTException
  {
    boolean bool1 = true;
    boolean bool2 = EnterpriseHelper.isAutoNumber(paramClass, WTContainerHelper.service.getClassicRef());
    if (bool2) {
      bool1 = false;
    } else {
      bool1 = true;
    }
    return bool1;
  }
  
  public static URL constructOutputURL(File paramFile, String paramString)
    throws WTException
  {
    HashMap localHashMap = new HashMap();
    return constructOutputURL(paramFile, paramString, localHashMap);
  }
  
  public static URL constructOutputURL(File paramFile, String paramString, HashMap<String, String> paramHashMap)
    throws WTException
  {
    try
    {
      WTPrincipal localWTPrincipal = SessionHelper.manager.getPrincipal();
      String str1 = localWTPrincipal.getName();
      if (logger.isDebugEnabled()) {
        logger.debug(str1);
      }
      String str2 = paramFile.getCanonicalPath();
      SessionContext localSessionContext = new SessionContext();
      localSessionContext.register();
      String str3 = localSessionContext.getSessionId();
      paramHashMap.put("sessId", str3);
      paramHashMap.put("attach", "true");
      
      URLFactory localURLFactory = new URLFactory();
      localSessionContext.put("fname", str2);
      localSessionContext.put("userName", str1);
      URL localURL = GatewayServletHelper.buildAuthenticatedURL(localURLFactory, "com.ptc.netmarkets.model.StandardNmObjectService", "redirectRecipient", paramString, paramHashMap);
      if (logger.isDebugEnabled()) {
        logger.debug("   constructOutputURL - OUT: " + localURL.toExternalForm() + ", file = " + paramFile);
      }
      return localURL;
    }
    catch (MalformedURLException localMalformedURLException)
    {
      throw new WTException(localMalformedURLException, "com.ptc.netmarkets.object.objectResource", "225", null);
    }
    catch (IOException localIOException)
    {
      throw new WTException(localIOException);
    }
  }
  
  public static class RequestAttConstants
  {
    public static final String ITERATION_HISTORY = "req_att_1";
    public static final String DISCUSSIONS = "req_att_2";
    public static final String REFERENCES = "req_att_3";
    public static final String ROUTE_STATUS = "req_att_4";
    public static final String ACTIONMODEL = "req_att_5";
    public static final String LIST_REFERENCES = "RequestAttConstants.LIST_REFS";
    public static final String LIST_DESCRIBES = "RequestAttConstants.listDescribes";
    public static final String LIST_CONTENT = "RequestAttConstants.listContent";
    public static final String LIFECYCLE_STATES = "RequestAttConstants.lcstates";
    public static final String CURRENT_STATE = "RequestAttConstants.currState";
    public static final String ACT_TASKS = "RequestAttConstants.ACT_TASKS";
    public static final String ACT_DELIVERABLES = "RequestAttConstants.ACT_DELIVERABLES";
    public static final String PROJ_OBJECT = "RequestAttConstants.PROJ_OBJECT";
    public static final String PROJ_NMOBJECT = "RequestAttConstants.PROJ_NMOBJECT";
    public static final String TRACK_COST = "RequestAttConstants.TRACK_COST";
    public static final String ISLINKDELIVERABLES = "RequestAttConstants.ISLINKDELIVERABLES";
    public static final String ISHASDELIVERABLES = "RequestAttConstants.ISHASDELIVERABLES";
    public static final String ISCREATETASKSEXECOBJ = "RequestAttConstants.ISCREATETASKSEXECOBJ";
    public static final String OWNER = "RequestAttConstants.OWNER";
    public static final String SUMMARY_TASKS = "RequestAttConstants.SUMMARY_TASKS";
    public static final String SCM_ATTRS = "RequestAttConstants.SCM_ATTRS";
  }
}
