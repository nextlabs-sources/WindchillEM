package com.nextlabs.em.windchill;

import java.io.File;

import wt.content.ContentItem;

public class NXLSecondaryContent {
	File contentFile;
	ContentItem content;
	public File getContentFile() {
		return contentFile;
	}
	public void setContentFile(File contentFile) {
		this.contentFile = contentFile;
	}
	public ContentItem getContent() {
		return content;
	}
	public void setContent(ContentItem content) {
		this.content = content;
	}
}
