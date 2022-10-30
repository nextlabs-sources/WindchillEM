package com.nextlabs.em.windchill;

import java.util.ArrayList;
import java.util.List;

public class SecurityLabelList {
	List<SecurityLabel> securityLabels;

	public SecurityLabelList() {
		this.securityLabels = new ArrayList<SecurityLabel>();
	}

	public List<SecurityLabel> getSecurityLabels() {
		return securityLabels;
	}

	public void setSecurityLabels(List<SecurityLabel> securityLabels) {
		this.securityLabels = securityLabels;
	}

}
