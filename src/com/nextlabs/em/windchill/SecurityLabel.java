package com.nextlabs.em.windchill;

public class SecurityLabel {
	boolean isPrimaryContent;
	String securityLabelName;
	String securityLabelValue;



	public boolean isPrimaryContent() {
		return isPrimaryContent;
	}

	public void setPrimaryContent(boolean isPrimaryContent) {
		this.isPrimaryContent = isPrimaryContent;
	}

	public String getSecurityLabelName() {
		return securityLabelName;
	}

	public void setSecurityLabelName(String securityLabelName) {
		this.securityLabelName = securityLabelName;
	}

	public String getSecurityLabelValue() {
		return securityLabelValue;
	}

	public void setSecurityLabelValue(String securityLabelValue) {
		this.securityLabelValue = securityLabelValue;
	}
}
