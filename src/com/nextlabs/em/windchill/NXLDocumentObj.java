package com.nextlabs.em.windchill;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NXLDocumentObj {
	File primaryContentFile;
	List<NXLSecondaryContent> secContents;

	public NXLDocumentObj() {
		secContents = new ArrayList<NXLSecondaryContent>();
	}

	public File getPrimaryContentFile() {
		return primaryContentFile;
	}

	public void setPrimaryContentFile(File primaryContentFile) {
		this.primaryContentFile = primaryContentFile;
	}

	public List<NXLSecondaryContent> getSecContents() {
		return secContents;
	}

	public void setSecContents(List<NXLSecondaryContent> secContents) {
		this.secContents = secContents;
	}

}
