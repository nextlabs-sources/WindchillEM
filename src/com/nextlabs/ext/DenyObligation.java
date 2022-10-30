package com.nextlabs.ext;

import java.util.List;

import com.nextlabs.EvaluationResult.RuntimeObligation;
import com.nextlabs.IObligation;
import com.nextlabs.ObjectAttrCollection;
public class DenyObligation implements IObligation {

	@Override
	public NextAction process(String requestId, String[] oids,
			List<RuntimeObligation> oblist, ObjectAttrCollection userAttrs,
			ObjectAttrCollection[] objAttrs) {
		return NextAction.DENY;
	}

}
