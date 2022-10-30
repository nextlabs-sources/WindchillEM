package com.nextlabs;

import org.apache.log4j.Logger;

import com.nextlabs.nxl.crypt.RightsManager;
import com.nextlabs.nxl.pojos.PolicyControllerDetails;

public class RightsManagerHelper {
	private static Logger logger = Logger.getLogger(RightsManagerHelper.class);
	private static RightsManager rm = null;
	static {
		try {
			String keyStoreName = ConfigurationManager.getInstance()
					.getProperty(
							com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
							com.nextlabs.Property.PC_KEY_STORE);
			String keyStorePassword = ConfigurationManager.getInstance()
					.getProperty(
							com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
							com.nextlabs.Property.PC_KEY_STORE_PASSWORD);
			String trustStoreName = ConfigurationManager.getInstance()
					.getProperty(
							com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
							com.nextlabs.Property.PC_TRUST_STORE);
			String trustStorePassword = ConfigurationManager.getInstance()
					.getProperty(
							com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
							com.nextlabs.Property.PC_TRUST_STORE_PASSWORD);
			String rmiPortNum = ConfigurationManager.getInstance().getProperty(
					com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PC_RMI_PORT);
			String pcIpaddr = ConfigurationManager.getInstance().getProperty(
					com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME,
					com.nextlabs.Property.PC_IP_ADDRESS);
			logger.debug("  >>>Key Store Name:" + keyStoreName);
			logger.debug("  >>>Key Store Password:" + keyStorePassword);
			logger.debug("  >>>Trust Store Name:" + trustStoreName);
			logger.debug("  >>>Trust Store password:" + trustStorePassword);
			logger.debug("  >>>RMI Port Num:" + rmiPortNum);
			logger.debug("  >>>PC Ip addr:" + pcIpaddr);
			int iPortNum = Integer.parseInt(rmiPortNum);
			// long lTime3=System.currentTimeMillis();
			PolicyControllerDetails policyControllerDetails = new PolicyControllerDetails();
			policyControllerDetails.setKeyStoreName(keyStoreName);
			policyControllerDetails.setKeyStorePassword(keyStorePassword);
			policyControllerDetails.setPcHostName(pcIpaddr);
			policyControllerDetails.setTrustStoreName(trustStoreName);
			policyControllerDetails.setRmiPortNum(iPortNum);
			policyControllerDetails.setTrustStorePasswd(trustStorePassword);
			rm = new RightsManager(policyControllerDetails);

		} catch (Exception exp) {

		}
	}

	public static RightsManager getRightsManager() {
		return rm;
	}
}
