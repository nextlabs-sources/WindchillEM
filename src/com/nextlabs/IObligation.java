package com.nextlabs;

import java.util.List;

import com.nextlabs.EvaluationResult.RuntimeObligation;
public interface IObligation {
	public enum NextAction{STOP,CONTINUE,DENY};
	public NextAction process(String requestId, String[] oids,  List<RuntimeObligation> oblist,ObjectAttrCollection userAttrs, ObjectAttrCollection[] objAttrs);
}
