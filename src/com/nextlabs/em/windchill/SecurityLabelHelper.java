package com.nextlabs.em.windchill;

import wt.access.SecurityLabels;

public class SecurityLabelHelper extends SecurityLabels {

	public static boolean isLabelValueDefinedvalid(String securitylabel, String securitylabelvalue) {
		return isLabelValueDefined(securitylabel, securitylabelvalue);
	}

}
