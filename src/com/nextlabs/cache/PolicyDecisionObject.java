package com.nextlabs.cache;

import com.nextlabs.EvaluationResult;

public class PolicyDecisionObject {

	public String getResourceID() {
		return resourceID;
	}

	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public EvaluationResult getDecision() {
		return decision;
	}

	public void setDecision(EvaluationResult decision) {
		this.decision = decision;
	}

	String resourceID;
	String action;
	EvaluationResult decision;

	public PolicyDecisionObject(String resourceid, String action, EvaluationResult decision) {
		this.resourceID = resourceid;
		this.action = action;
		this.decision = decision;
	}

}
