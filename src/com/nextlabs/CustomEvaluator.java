package com.nextlabs;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nextlabs.em.windchill.QueryAgentImpl;

import wt.access.SecurityLabeled;
import wt.access.UnrestrictedPrincipalEvaluator;
import wt.org.WTPrincipal;
import wt.util.WTException;

/**
 * This is an example custom evaluator for use in a custom security label configuration
 *
 */
public class CustomEvaluator extends UnrestrictedPrincipalEvaluator {
	private Logger logger = Logger.getLogger(CustomEvaluator.class);
   private static final String SECURITY_ADMINISTRATORS_GROUP = "SecurityAdministrators";

   private static final String LABEL_VALUE_DELIMITER = "\\+,;";

   private static final Map<String, String> labelValueToGroup = new HashMap<String, String>();
   EntitlementManagerContext emctx=new EntitlementManagerContext();

   // HashMap of possible label values and their associated Windchill group names.
   static {
      labelValueToGroup.put("CompanyA", "CompanyAGroup");
      labelValueToGroup.put("CompanyB", "CompanyBGroup");
      labelValueToGroup.put("CompanyC", "CompanyCGroup");
      labelValueToGroup.put("CompanyD", "CompanyDGroup");
   }

   /**
    * Method to determine if a participant is restricted (not authorized) by a security label value. In this example
    * implementation, the method is passed a delimited list of company names for the value. This allows an object to
    * have multiple companies associated with a single security-labeled object. A principal needs to be a member of all
    * specified company groups to be cleared for the security label value. The method splits the label_value on the
    * delimiter and calls a helper method <code>isMemberOfGroup</code>, which ensures that the principal is a member of
    * all of the Windchill groups associated with the companies passed in.
    *
    * If an unknown string is passed in, for example CompanyDE, only principals in the SecurityAdministrators
    * group are cleared. If the principal is not in the SecurityAdministrators group an exception is thrown.
    *
    * @param principal
    *           Participant whose authorization is to be evaluated.
    * @param label_name
    *           Name of the label being checked.
    * @param label_value
    *           Value of the label being checked. This is the internal representation of the value.
    * @return boolean true if the principal is restricted by the security label; false if the principal is not
    *         restricted.
    * @throws WTException
    *            If an unexpected value is passed in.
    */
   @Override
   public boolean isRestrictedBySecurityLabelValue(WTPrincipal principal, String label_name, String label_value)
         throws WTException {

      boolean isRestrictedBySecurityLabelValue = false;
     /* emctx.log(LogLevel.INFO,"isRestrictedBySecurityLabelValue Set Security Labels principal "+ principal.getName());
      emctx.log(LogLevel.INFO,"isRestrictedBySecurityLabelValue Set Security Labels label_name "+ label_name);
      emctx.log(LogLevel.INFO,"isRestrictedBySecurityLabelValue Set Security Labels label_value "+ label_value);
      */
   /*   boolean isMemberOfGroup = false;

      
       * The custom translator replaces all commas(,) with the plus character(+). Since this method is handed the
       * internal representation of the security label value, the string is split on the plus character(+) to get the
       * companies
       
      String[] companies = label_value.split(LABEL_VALUE_DELIMITER);

      // Iterating through the list of values passed in with the label_value
      for (String company : companies) {
         company = company.trim();

         if (!"".equals(company)) {
            // If a value is received that does not exist in the defined map, it is an unexpected value
            if (!labelValueToGroup.containsKey(company)) {
               
                * If a value is received that doesn't match any of the defined companies in the map, this method
                * restricts all participants except members of the "SecurityAdministrators" group. If the principal is
                * not a member of that group, an exception is thrown. To prevent the exception, security label values
                * should be validated by the UI before being persisted on an object.
                
               isMemberOfGroup = isMemberOfGroup(principal, SECURITY_ADMINISTRATORS_GROUP);

               if (!isMemberOfGroup) {
                  // The principal is not a member of the SecurityAdministrators group so an exception is thrown
                  throw new WTException(
                        "ATTENTION: Security labels are not valid. An object could not be accessed because its security labels are not valid. Report this to your administrator.");
               }
            } else {
               // Checks to see if the principal is a member of the associated Windchill group
               isMemberOfGroup = isMemberOfGroup(principal, labelValueToGroup.get(company));
            }

            if (!isMemberOfGroup) {
               
                * As soon as the principal is found to not be a member of one of the groups, the principal is restricted
                * by the label value
                
               isRestrictedBySecurityLabelValue = true;
               break;
            }
         }
      }*/

      return isRestrictedBySecurityLabelValue;
   }

   /**
    * Method to determine whether a principal is allowed to modify a security label value. This example implementation
    * first ensures that the principal is not restricted by the security label value. If the principal is not restricted
    * by the value, then the method checks the object's life cycle state. If it is in the In Work state, the method
    * calls Windchill's default implementation. If the object is in any other state, only principals in the
    * SecurityAdministrators group are allowed to modify the security label value.
    *
    * If the object is not <code>LifeCycleManaged</code>, the method will call Windchill's default implementation.
    *
    * The default method will check to see what UFID is specified in the UnrestrictedPrincipal section of the security
    * labels configuration file for the label value. The principal is allowed to modify the label value if the
    * authorized participant identified by the configured UFID is the same principal, or if the authorized participant
    * is a group or organization and the specified principal is a member of that group or organization. If there is no
    * UFID configured, all principals are allowed to modify the label.
    *
    * @param principal
    *           Participant whose authorization is to be evaluated.
    * @param object
    *           <code>SecurityLabeled</code> object for which the security label would be modified.
    * @param label_name
    *           Name for the label being checked.
    * @param label_value
    *           Value for the label being checked.
    * @return boolean true if the principal is allowed to modify the security label value; otherwise false.
    * @throws WTException
    *            Returns and exception if an unexpected error occurs.
    */
   @Override
   public boolean isAllowedToModifySecurityLabelValue(WTPrincipal principal, SecurityLabeled object, String label_name,
         String label_value) throws WTException {
      /*
       * Check that the principal is not restricted by the security label value. If they are restricted by the value, they do
       * not have rights to modify the security label value
       */
	   
	     logger.info("isAllowedToModifySecurityLabelValue Set Security Labels principal "+ principal.getName());
	     logger.info("isAllowedToModifySecurityLabelValue Set Security Labels label_name "+ label_name);
	     logger.info("isAllowedToModifySecurityLabelValue Set Security Labels label_value "+ label_value);
      boolean allowedToModifySecurityLabels = true;

    /*  if (allowedToModifySecurityLabels) {

         // Getting the life cycle state of the object
         if (object instanceof LifeCycleManaged) {

            
             * Although the LifeCycleManaged interface and getState method are not listed as supported APIs prior to
             * Windchill 10.1, PTC does support their usage
             
            LifeCycleState state = ((LifeCycleManaged) object).getState();
            State objectState = state.getState();

            if (State.INWORK.equals(objectState))
               
                * If the object is in the In Work state, call the default method to evaluate if the principal can modify the
                * security label value
                
               allowedToModifySecurityLabels = super.isAllowedToModifySecurityLabelValue(principal, object,
                        label_name, label_value);
            else
               
                * If the object is not in the In Work state, check to see if the principal is in the
                * SecurityAdministrators group. This is to cover rare cases, such as if a invalid label value is
                * persisted on an object, and only a member of the SecurityAdministrators group has access to the object
                * to correct the label value
                
               allowedToModifySecurityLabels = isMemberOfGroup(principal, SECURITY_ADMINISTRATORS_GROUP);
         } else {
            // The object is not LifeCycleStateManaged
            allowedToModifySecurityLabels = super.isAllowedToModifySecurityLabelValue(principal, object, label_name,
                  label_value);
         }
      }*/

      return allowedToModifySecurityLabels;
   }

   /**
    * Helper method to determine if a principal is a member of a group. This method assumes that it is a site level
    * group for which the membership is checked.
    *
    * @param principal
    *           Participant whose group membership is to be evaluated.
    * @param groupName
    *           Name of group for which the membership is checked.
    * @return boolean true if the principal is a member of the group; false if they are not.
    * @throws WTException
    *            Return an exception if an unexpected error occurs.
    */
/*   private static boolean isMemberOfGroup(WTPrincipal principal, String groupName) throws WTException {
      boolean isMember = false;

      
       * Gets the node in LDAP for the container where the groups are created. In our example, this is the Site
       * container. This allows the correct group to be returned. Although the getExchangeContainer and
       * getContextProvider methods are not listed as supported APIs prior to Windchill 10.1, PTC does support their
       * usage
       
      DirectoryContextProvider dcp = WTContainerHelper.service.getExchangeContainer().getContextProvider();

      WTGroup group = OrganizationServicesHelper.manager.getGroup(groupName, dcp);
      if (group != null)
         isMember = OrganizationServicesHelper.manager.isMember(group, principal);

      return isMember;
   }*/
}
