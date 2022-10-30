package com.nextlabs.em.windchill;

import org.apache.log4j.Logger;

import wt.access.AccessControlHelper;
import wt.access.AccessControlManager;
//import wt.access.AccessControlServerHelper;
import wt.access.SecurityLabeledDownloadAcknowledgment;
//import wt.access.StandardAccessControlManager;

public class NxlAccessControlManager implements AccessControlManager {
	private Logger logger = Logger.getLogger(NxlAccessControlManager.class);
	 // Method descriptor #4 (Ljava/lang/Object;Lwt/access/AccessPermission;)Z
	  public boolean checkAccess(java.lang.Object arg0, wt.access.AccessPermission arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////checkAccess 1");
		  return AccessControlHelper.manager.checkAccess(arg0, arg1);
	  }
	  
	  // Method descriptor #7 (Lwt/fc/collections/WTCollection;Lwt/access/AccessPermission;)V
	  public void checkAccess(wt.fc.collections.WTCollection arg0, wt.access.AccessPermission arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////checkAccess 2");
		  AccessControlHelper.manager.checkAccess(arg0, arg1);
	  }
	  
	  // Method descriptor #8 (Lwt/admin/AdminDomainRef;Ljava/lang/String;Lwt/access/AccessPermission;)V
	  public void checkAccess(wt.admin.AdminDomainRef arg0, java.lang.String arg1, wt.access.AccessPermission arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////checkAccess 3");
		  AccessControlHelper.manager.checkAccess(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #10 (Lwt/fc/collections/WTCollection;Lwt/access/AccessPermission;)Lwt/fc/collections/WTKeyedHashMap;
	  public wt.fc.collections.WTKeyedHashMap getAccess(wt.fc.collections.WTCollection arg0, wt.access.AccessPermission arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getAccess 1");
		  return AccessControlHelper.manager.getAccess(arg0, arg1);
	  }
	  
	  // Method descriptor #11 (Lwt/org/WTPrincipal;Lwt/fc/collections/WTCollection;Lwt/access/AccessPermission;)Lwt/fc/collections/WTKeyedHashMap;
	  public wt.fc.collections.WTKeyedHashMap getAccess(wt.org.WTPrincipal arg0, wt.fc.collections.WTCollection arg1, wt.access.AccessPermission arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getAccess 2");
		  return AccessControlHelper.manager.getAccess(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #4 (Ljava/lang/Object;Lwt/access/AccessPermission;)Z
	  public boolean hasAccess(java.lang.Object arg0, wt.access.AccessPermission arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////hasAccess 1");
		  return AccessControlHelper.manager.hasAccess(arg0, arg1);
	  }
	  
	  // Method descriptor #13 (Lwt/fc/collections/WTCollection;Lwt/access/AccessPermission;)Z
	  public boolean hasAccess(wt.fc.collections.WTCollection arg0, wt.access.AccessPermission arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////hasAccess 2");
		  return AccessControlHelper.manager.hasAccess(arg0, arg1);
	  }
	  
	  // Method descriptor #14 (Lwt/org/WTPrincipal;Ljava/lang/Object;Lwt/access/AccessPermission;)Z
	  public boolean hasAccess(wt.org.WTPrincipal arg0, java.lang.Object arg1, wt.access.AccessPermission arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////hasAccess 3");
		  return AccessControlHelper.manager.hasAccess(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #15 (Lwt/org/WTPrincipal;Lwt/fc/collections/WTCollection;Lwt/access/AccessPermission;)Z
	  public boolean hasAccess(wt.org.WTPrincipal arg0, wt.fc.collections.WTCollection arg1, wt.access.AccessPermission arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////hasAccess 4");
		  return AccessControlHelper.manager.hasAccess(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #16 (Lwt/org/WTPrincipal;Ljava/lang/String;Lwt/admin/AdminDomainRef;Lwt/lifecycle/State;Lwt/access/AccessPermission;)Z
	  public boolean hasAccess(wt.org.WTPrincipal arg0, java.lang.String arg1, wt.admin.AdminDomainRef arg2, wt.lifecycle.State arg3, wt.access.AccessPermission arg4) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////hasAccess 5");
		  return AccessControlHelper.manager.hasAccess(arg0, arg1, arg2, arg3, arg4);
	  }
	  
	  // Method descriptor #18 (Lwt/fc/ObjectVectorIfc;Lwt/access/AccessPermission;)Lwt/fc/ObjectVectorIfc;
	  public wt.fc.ObjectVectorIfc filterObjects(wt.fc.ObjectVectorIfc arg0, wt.access.AccessPermission arg1) throws wt.util.WTException
	  {
		  

		 logger.debug("/////////////////////////////filterObjects 1");
		  return AccessControlHelper.manager.filterObjects(arg0, arg1);
	  }
	  
	  // Method descriptor #19 (Lwt/fc/QueryResult;Lwt/access/AccessPermission;)Lwt/fc/QueryResult;
	  public wt.fc.QueryResult filterObjects(wt.fc.QueryResult arg0, wt.access.AccessPermission arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////filterObjects 2");
		  return AccessControlHelper.manager.filterObjects(arg0, arg1);
	  }
	  
	  // Method descriptor #20 (Lwt/org/WTPrincipal;Lwt/fc/collections/WTSet;Lwt/access/AccessPermissionSet;)Lwt/fc/collections/WTSet;
	  public wt.fc.collections.WTSet filterObjects(wt.org.WTPrincipal arg0, wt.fc.collections.WTSet arg1, wt.access.AccessPermissionSet arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////filterObjects 3");
		  return AccessControlHelper.manager.filterObjects(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #22 (Lwt/admin/AdminDomainRef;Ljava/lang/String;Lwt/lifecycle/State;Lwt/org/WTPrincipalReference;ZLwt/access/AccessPermissionSet;Lwt/access/AccessPermissionSet;Lwt/access/AccessPermissionSet;)V
	  public void createAccessControlRule(wt.admin.AdminDomainRef arg0, java.lang.String arg1, wt.lifecycle.State arg2, wt.org.WTPrincipalReference arg3, boolean arg4, wt.access.AccessPermissionSet arg5, wt.access.AccessPermissionSet arg6, wt.access.AccessPermissionSet arg7) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////createAccessControlRule 1");
		  AccessControlHelper.manager.createAccessControlRule(arg0, arg1, arg2, arg3, arg4, arg5,arg6,arg7);
	  }
	  
	  // Method descriptor #23 (Lwt/admin/AdminDomainRef;Ljava/lang/String;Lwt/lifecycle/State;Lwt/org/WTPrincipalReference;Lwt/access/AccessPermissionSet;Lwt/access/AccessPermissionSet;)V (deprecated)
	  @SuppressWarnings("deprecation")
	public void createAccessControlRule(wt.admin.AdminDomainRef arg0, java.lang.String arg1, wt.lifecycle.State arg2, wt.org.WTPrincipalReference arg3, wt.access.AccessPermissionSet arg4, wt.access.AccessPermissionSet arg5) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////createAccessControlRule 2");
		  AccessControlHelper.manager.createAccessControlRule(arg0, arg1, arg2, arg3, arg4, arg5);
	  }
	  
	  // Method descriptor #26 (Lwt/admin/AdminDomainRef;Ljava/lang/String;Lwt/lifecycle/State;Lwt/org/WTPrincipalReference;Z)V
	  public void deleteAccessControlRule(wt.admin.AdminDomainRef arg0, java.lang.String arg1, wt.lifecycle.State arg2, wt.org.WTPrincipalReference arg3, boolean arg4) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////deleteAccessControlRule 1");
		  AccessControlHelper.manager.deleteAccessControlRule(arg0, arg1, arg2, arg3, arg4);
	  }
	  
	  // Method descriptor #28 (Lwt/admin/AdminDomainRef;)V
	  public void deleteAccessControlRules(wt.admin.AdminDomainRef arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////deleteAccessControlRule 2");
		  AccessControlHelper.manager.deleteAccessControlRules(arg0);
	  }
	  
	  // Method descriptor #30 (Lwt/admin/AdminDomainRef;Ljava/lang/String;Lwt/lifecycle/State;Lwt/org/WTPrincipalReference;Z)Lwt/access/AccessControlRule;
	  public wt.access.AccessControlRule getAccessControlRule(wt.admin.AdminDomainRef arg0, java.lang.String arg1, wt.lifecycle.State arg2, wt.org.WTPrincipalReference arg3, boolean arg4) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getAccessControlRule 1");
		  return AccessControlHelper.manager.getAccessControlRule(arg0, arg1, arg2, arg3, arg4);
	  }
	  
	  // Method descriptor #31 (Lwt/admin/AdminDomainRef;Ljava/lang/String;Lwt/lifecycle/State;Lwt/org/WTPrincipalReference;)Lwt/access/AccessControlRule; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AccessControlRule getAccessControlRule(wt.admin.AdminDomainRef arg0, java.lang.String arg1, wt.lifecycle.State arg2, wt.org.WTPrincipalReference arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getAccessControlRule 2");
		  return AccessControlHelper.manager.getAccessControlRule(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #33 (Lwt/admin/AdminDomainRef;)Ljava/util/Collection;
	  // Signature: (Lwt/admin/AdminDomainRef;)Ljava/util/Collection<Lwt/access/AccessControlRule;>;
	  @SuppressWarnings({ "unchecked", "rawtypes" })
	public java.util.Collection getAccessControlRules(wt.admin.AdminDomainRef arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getAccessControlRules 1");
		  return AccessControlHelper.manager.getAccessControlRules(arg0);
	  }
	  
	  // Method descriptor #22 (Lwt/admin/AdminDomainRef;Ljava/lang/String;Lwt/lifecycle/State;Lwt/org/WTPrincipalReference;ZLwt/access/AccessPermissionSet;Lwt/access/AccessPermissionSet;Lwt/access/AccessPermissionSet;)V
	  public void updateAccessControlRule(wt.admin.AdminDomainRef arg0, java.lang.String arg1, wt.lifecycle.State arg2, wt.org.WTPrincipalReference arg3, boolean arg4, wt.access.AccessPermissionSet arg5, wt.access.AccessPermissionSet arg6, wt.access.AccessPermissionSet arg7) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////updateAccessControlRule 1");
		  AccessControlHelper.manager.updateAccessControlRule(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	  }
	  
	  // Method descriptor #38 (Lwt/access/AccessSelector;)Lwt/access/PolicyAcl;
	  public wt.access.PolicyAcl getPolicyAcl(wt.access.AccessSelector arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getPolicyAcl 1");
		  return AccessControlHelper.manager.getPolicyAcl(arg0);
	  }
	  
	  // Method descriptor #40 (Lwt/access/AccessSelector;)Lwt/access/AccessPolicyRule;
	  public wt.access.AccessPolicyRule getAccessPolicyRule(wt.access.AccessSelector arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getAccessPolicyRule 1");
		  return AccessControlHelper.manager.getAccessPolicyRule(arg0);
	  }
	  
	  // Method descriptor #42 (Lwt/admin/AdminDomainRef;)Ljava/util/Enumeration;
	  @SuppressWarnings("rawtypes")
	public java.util.Enumeration getAccessPolicyRules(wt.admin.AdminDomainRef arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getAccessPolicyRules 1");
		  return AccessControlHelper.manager.getAccessPolicyRules(arg0);
	  }
	  
	  // Method descriptor #44 (Ljava/lang/Class;)Ljava/util/Hashtable;
	  @SuppressWarnings("rawtypes")
	public java.util.Hashtable getSurrogateAttributes(java.lang.Class arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getSurrogateAttributes 1");
		  return AccessControlHelper.manager.getSurrogateAttributes(arg0);
	  }
	  
	  // Method descriptor #46 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Lwt/access/AccessPermission;Lwt/access/AdHocAccessKey;)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled addPermission(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, wt.access.AccessPermission arg2, wt.access.AdHocAccessKey arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////addPermission 1");
		  return AccessControlHelper.manager.addPermission(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #47 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Lwt/access/AccessPermission;Lwt/access/AdHocAccessKey;J)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled addPermission(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, wt.access.AccessPermission arg2, wt.access.AdHocAccessKey arg3, long arg4) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////addPermission 2");
		  return AccessControlHelper.manager.addPermission(arg0, arg1, arg2, arg3, arg4);
	  }
	  
	  // Method descriptor #49 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Ljava/util/Vector;Lwt/access/AdHocAccessKey;)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled addPermissions(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, @SuppressWarnings("rawtypes") java.util.Vector arg2, wt.access.AdHocAccessKey arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////addPermissions 1");
		  return AccessControlHelper.manager.addPermissions(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #50 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Ljava/util/Vector;Lwt/access/AdHocAccessKey;J)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled addPermissions(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, @SuppressWarnings("rawtypes") java.util.Vector arg2, wt.access.AdHocAccessKey arg3, long arg4) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////addPermissions 2");
		  return AccessControlHelper.manager.addPermissions(arg0, arg1, arg2, arg3, arg4);
	  }
	  
	  // Method descriptor #52 (Lwt/access/AdHocControlled;Lwt/access/AdHocControlled;Lwt/access/AdHocAccessKey;)Lwt/access/AdHocControlled;
	  public wt.access.AdHocControlled copyPermissions(wt.access.AdHocControlled arg0, wt.access.AdHocControlled arg1, wt.access.AdHocAccessKey arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////copyPermissions 1");
		  return AccessControlHelper.manager.copyPermissions(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #53 (Lwt/access/AdHocControlled;Lwt/access/AdHocControlled;Lwt/access/AdHocAccessKey;J)Lwt/access/AdHocControlled;
	  public wt.access.AdHocControlled copyPermissions(wt.access.AdHocControlled arg0, wt.access.AdHocControlled arg1, wt.access.AdHocAccessKey arg2, long arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////copyPermissions 2");
		  return AccessControlHelper.manager.copyPermissions(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #55 (Lwt/org/WTPrincipal;Lwt/fc/adminlock/AdministrativelyLockable;Z)Lwt/access/AccessPermissionSet;
	  public wt.access.AccessPermissionSet getAdminLockRestrictedPermissions(wt.org.WTPrincipal arg0, wt.fc.adminlock.AdministrativelyLockable arg1, boolean arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getAdminLockRestrictedPermissions 1");
		  return AccessControlHelper.manager.getAdminLockRestrictedPermissions(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #57 (Lwt/org/WTPrincipal;ZLwt/access/AccessControlled;)Lwt/access/AccessPermissionSet;
	  public wt.access.AccessPermissionSet getPermissions(wt.org.WTPrincipal arg0, boolean arg1, wt.access.AccessControlled arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getPermissions 1");
		  return AccessControlHelper.manager.getPermissions(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #58 (Lwt/org/WTPrincipal;Lwt/access/AccessControlled;)Lwt/access/AccessPermissionSet;
	  public wt.access.AccessPermissionSet getPermissions(wt.org.WTPrincipal arg0, wt.access.AccessControlled arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getPermissions 2");
		  return AccessControlHelper.manager.getPermissions(arg0, arg1);
	  }
	  
	  // Method descriptor #59 (Lwt/org/WTPrincipal;Lwt/fc/collections/WTCollection;)Lwt/fc/collections/WTKeyedHashMap;
	  public wt.fc.collections.WTKeyedHashMap getPermissions(wt.org.WTPrincipal arg0, wt.fc.collections.WTCollection arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getPermissions 3");
		  return AccessControlHelper.manager.getPermissions(arg0, arg1);
	  }
	  
	  // Method descriptor #60 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Lwt/access/AdHocAccessKey;)Lwt/util/EnumeratorVector;
	  public wt.util.EnumeratorVector getPermissions(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, wt.access.AdHocAccessKey arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getPermissions 4");
		  return AccessControlHelper.manager.getPermissions(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #61 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Lwt/access/AdHocAccessKey;J)Lwt/util/EnumeratorVector;
	  public wt.util.EnumeratorVector getPermissions(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, wt.access.AdHocAccessKey arg2, long arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getPermissions 5");
		  return AccessControlHelper.manager.getPermissions(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #46 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Lwt/access/AccessPermission;Lwt/access/AdHocAccessKey;)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled removePermission(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, wt.access.AccessPermission arg2, wt.access.AdHocAccessKey arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////removePermission 1");
		  return AccessControlHelper.manager.removePermission(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #47 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Lwt/access/AccessPermission;Lwt/access/AdHocAccessKey;J)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled removePermission(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, wt.access.AccessPermission arg2, wt.access.AdHocAccessKey arg3, long arg4) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////removePermission 2");
		  return AccessControlHelper.manager.removePermission(arg0, arg1, arg2, arg3, arg4);
	  }
	  
	  // Method descriptor #49 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Ljava/util/Vector;Lwt/access/AdHocAccessKey;)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled removePermissions(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, @SuppressWarnings("rawtypes") java.util.Vector arg2, wt.access.AdHocAccessKey arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////removePermissions 1");
		  return AccessControlHelper.manager.removePermissions(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #50 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Ljava/util/Vector;Lwt/access/AdHocAccessKey;J)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled removePermissions(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, @SuppressWarnings("rawtypes") java.util.Vector arg2, wt.access.AdHocAccessKey arg3, long arg4) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////removePermissions 2");
		  return AccessControlHelper.manager.removePermissions(arg0, arg1, arg2, arg3, arg4);
	  }
	  
	  // Method descriptor #64 (Lwt/access/AdHocControlled;Lwt/access/AdHocAccessKey;)Lwt/access/AdHocControlled;
	  public wt.access.AdHocControlled removePermissions(wt.access.AdHocControlled arg0, wt.access.AdHocAccessKey arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////removePermissions 3");
		  return AccessControlHelper.manager.removePermissions(arg0, arg1);
	  }
	  
	  // Method descriptor #65 (Lwt/access/AdHocControlled;Lwt/access/AdHocAccessKey;J)Lwt/access/AdHocControlled;
	  public wt.access.AdHocControlled removePermissions(wt.access.AdHocControlled arg0, wt.access.AdHocAccessKey arg1, long arg2) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////removePermissions 4");
		  return AccessControlHelper.manager.removePermissions(arg0, arg1, arg2);
	  }
	  
	  // Method descriptor #46 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Lwt/access/AccessPermission;Lwt/access/AdHocAccessKey;)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled setPermission(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, wt.access.AccessPermission arg2, wt.access.AdHocAccessKey arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////setPermission 1");
		  return AccessControlHelper.manager.setPermission(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #47 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Lwt/access/AccessPermission;Lwt/access/AdHocAccessKey;J)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled setPermission(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, wt.access.AccessPermission arg2, wt.access.AdHocAccessKey arg3, long arg4) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////setPermission 2");
		  return AccessControlHelper.manager.setPermission(arg0, arg1, arg2, arg3, arg4);
	  }
	  
	  // Method descriptor #49 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Ljava/util/Vector;Lwt/access/AdHocAccessKey;)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled setPermissions(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, @SuppressWarnings("rawtypes") java.util.Vector arg2, wt.access.AdHocAccessKey arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////setPermissions 1");
		  return AccessControlHelper.manager.setPermissions(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #50 (Lwt/access/AdHocControlled;Lwt/org/WTPrincipalReference;Ljava/util/Vector;Lwt/access/AdHocAccessKey;J)Lwt/access/AdHocControlled; (deprecated)
	  @SuppressWarnings("deprecation")
	public wt.access.AdHocControlled setPermissions(wt.access.AdHocControlled arg0, wt.org.WTPrincipalReference arg1, @SuppressWarnings("rawtypes") java.util.Vector arg2, wt.access.AdHocAccessKey arg3, long arg4) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////setPermissions 2");
		  return AccessControlHelper.manager.setPermissions(arg0, arg1, arg2, arg3, arg4);
	  }
	  
	  // Method descriptor #69 (Lwt/access/AdHocControlled;)Ljava/lang/String;
	  public java.lang.String showPermissions(wt.access.AdHocControlled arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////showPermissions 1");
		  return AccessControlHelper.manager.showPermissions(arg0);
	  }
	  
	  // Method descriptor #71 (Lwt/access/AccessControlList;)Ljava/util/Enumeration;
	  @SuppressWarnings("rawtypes")
	public java.util.Enumeration getEntries(wt.access.AccessControlList arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getEntries 1");
		  return AccessControlHelper.manager.getEntries(arg0);
	  }
	  
	  // Method descriptor #73 (Ljava/lang/String;Ljava/lang/Object;Lwt/access/AccessPermission;Lwt/util/WTMessage;)V
	  public void emitAccessEvent(java.lang.String arg0, java.lang.Object arg1, wt.access.AccessPermission arg2, wt.util.WTMessage arg3) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////emitAccessEvent 1");
		  AccessControlHelper.manager.emitAccessEvent(arg0, arg1, arg2, arg3);
	  }
	  
	  // Method descriptor #75 (Ljava/lang/String;)Ljava/util/Map;
	  // Signature: (Ljava/lang/String;)Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;
	  public java.util.Map<String,String> getDefinedSecurityLabels(java.lang.String arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getDefinesSecurityLabels 1");
		  return AccessControlHelper.manager.getDefinedSecurityLabels(arg0);
	  }
	  
	  // Method descriptor #78 (Lwt/access/SecurityLabeled;Ljava/lang/String;)Ljava/lang/String;
	  public  java.lang.String getSecurityLabel(wt.access.SecurityLabeled arg0, java.lang.String arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getSecurityLabel 1");
		  return AccessControlHelper.manager.getSecurityLabel(arg0, arg1);
	  }
	  
	  // Method descriptor #79 (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
	  public java.lang.String getSecurityLabel(java.lang.String arg0, java.lang.String arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getSecurityLabel 2");
		  return AccessControlHelper.manager.getSecurityLabel(arg0, arg1);
	  }
	  
	  // Method descriptor #81 (Lwt/access/SecurityLabeled;)Ljava/util/Map;
	  // Signature: (Lwt/access/SecurityLabeled;)Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;
	  public java.util.Map<String,String> getSecurityLabels(wt.access.SecurityLabeled arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getSecurityLabels 1");
		  return AccessControlHelper.manager.getSecurityLabels(arg0);
	  }
	  
	  // Method descriptor #83 (Lwt/fc/collections/WTCollection;)Lwt/fc/collections/WTKeyedMap;
	  public wt.fc.collections.WTKeyedMap getSecurityLabels(wt.fc.collections.WTCollection arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getSecurityLabels 2");
		  return AccessControlHelper.manager.getSecurityLabels(arg0);
	  }
	  
	  // Method descriptor #75 (Ljava/lang/String;)Ljava/util/Map;
	  // Signature: (Ljava/lang/String;)Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;
	  public java.util.Map<String,String> getSecurityLabels(java.lang.String arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getSecurityLabels 3");
		  return AccessControlHelper.manager.getSecurityLabels(arg0);
	  }
	  
	  // Method descriptor #85 (Lwt/access/SecurityLabeled;)Ljava/lang/String;
	  public java.lang.String getSecurityLabelsStringRepresentation(wt.access.SecurityLabeled arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getSecurityLabelsStringRepresentation 1");
		  return AccessControlHelper.manager.getSecurityLabelsStringRepresentation(arg0);
	  }
	  
	  // Method descriptor #87 (Lwt/access/SecurityLabeled;)Z
	  public boolean showSecurityLabelsGlyph(wt.access.SecurityLabeled arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////showSecurityLabelsGlyph 1");
		  return AccessControlHelper.manager.showSecurityLabelsGlyph(arg0);
	  }
	  
	  // Method descriptor #89 (Lwt/access/SecurityLabeled;Lwt/org/WTPrincipal;)Ljava/util/ArrayList;
	  // Signature: (Lwt/access/SecurityLabeled;Lwt/org/WTPrincipal;)Ljava/util/ArrayList<Lwt/access/SecurityLabeledDownloadAcknowledgment;>;
	  
	public java.util.ArrayList<SecurityLabeledDownloadAcknowledgment> getSecurityLabeledDownloadAcknowledgments(wt.access.SecurityLabeled arg0, wt.org.WTPrincipal arg1) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getSecurityLabeledDownloadAcknowledgments 1");
		  return AccessControlHelper.manager.getSecurityLabeledDownloadAcknowledgments(arg0,arg1);
	  }
	  
	  // Method descriptor #91 (Lwt/access/SecurityLabeled;)Ljava/util/ArrayList;
	  // Signature: (Lwt/access/SecurityLabeled;)Ljava/util/ArrayList<Lwt/access/SecurityLabeledDownloadAcknowledgment;>;
	 
	public java.util.ArrayList<SecurityLabeledDownloadAcknowledgment> getSecurityLabeledDownloadAcknowledgments(wt.access.SecurityLabeled arg0) throws wt.util.WTException
	  {

		 logger.debug("/////////////////////////////getSecurityLabeledDownloadAcknowledgments 2");
		  return AccessControlHelper.manager.getSecurityLabeledDownloadAcknowledgments(arg0);
	  }
	
}
