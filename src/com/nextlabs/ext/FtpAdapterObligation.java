package com.nextlabs.ext;

import java.util.List;

import com.nextlabs.EvaluationResult.RuntimeObligation;
import com.nextlabs.IObligation;
import com.nextlabs.ObjectAttrCollection;

public class FtpAdapterObligation implements IObligation {

	@Override
	public NextAction process(String requestId, String[] oids,  List<RuntimeObligation> oblist,ObjectAttrCollection userAttrs, ObjectAttrCollection[] objAttrs) {
		// TODO Auto-generated method stub
		System.out.println("from FtpAdapter obligation");
		return null;
	}

}
