package com.nextlabs.em.windchill.install;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSPConfigure {

	private String jspPath;
	BufferedReader buffReader = null;
	FileWriter fw = null;
	FileReader fr = null;
	String totalStr = "";
	String line = "";
	private boolean bWriteNeeded = false;

	public JSPConfigure(String work_docMgrActionXml) {
		this.jspPath = work_docMgrActionXml;
	}

	public void load() {
		try {
			fr = new FileReader(jspPath);
			buffReader = new BufferedReader(new FileReader(jspPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateJSPContent() {

		try {
			line = buffReader.readLine();
			while (line != null) {
				// System.out.println(line);
				if (line.contains("com.ptc.windchill.enterprise.servlets.AttachmentsDownloadDirectionServlet")) {
					line = line.replace("com.ptc.windchill.enterprise.servlets.AttachmentsDownloadDirectionServlet",
							"com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet");
					bWriteNeeded=true;
					System.out.println(ConfigurerUtils.formatString(" com.ptc.windchill.enterprise.servlets.AttachmentsDownloadDirectionServlet","found"));
				}else if(line.contains("com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse")){
					line = line.replace("com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse",
							"com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse");
					bWriteNeeded=true;
					System.out.println(ConfigurerUtils.formatString(" com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse","found"));
				}else if(line.contains("com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem")){
					line = line.replace("com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem",
							"com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem");
					bWriteNeeded=true;
					System.out.println(ConfigurerUtils.formatString(" com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem","found"));
				}
				totalStr = totalStr + line + System.getProperty("line.separator");
				line = buffReader.readLine();
			}
			fr.close();
			buffReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		if(!bWriteNeeded){
			System.out.println(ConfigurerUtils.formatString(" com.ptc.windchill.enterprise.servlets.AttachmentsDownloadDirectionServlet","not found"));
			System.out.println(ConfigurerUtils.formatString(" com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse","not found"));
			System.out.println(ConfigurerUtils.formatString(" com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem","not found"));
		}

	}
	
	public void updateJSPContentForCreatePage() {

		try {
			line = buffReader.readLine();
			while (line != null) {
				// System.out.println(line);
				if (line.contains("securityLabelStep")) {
					System.out.println(ConfigurerUtils.formatString(" securityLabelStep at "+jspPath,"found"));
					line = "";
					bWriteNeeded=true;

				}
				totalStr = totalStr + line + System.getProperty("line.separator");
				line = buffReader.readLine();
			}
			fr.close();
			buffReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		if(!bWriteNeeded){
			System.out.println(ConfigurerUtils.formatString(" securityLabelStep at "+jspPath,"not found"));
		}

	}

	public boolean unload() {
		if(!bWriteNeeded){
			return false;
		}
		
		FileWriter fw;
		try {
			fw = new FileWriter(jspPath);
			fw.write(totalStr);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public void undoJSPContentForCreatePage() {
		try {
			line = buffReader.readLine();
			while (line != null) {
				String tmp=null;
				if (line.contains("Set Security Label Step")) {
					tmp = line;
					line = buffReader.readLine();
					if(!line.contains("securityLabelStep")){
						System.out.println(ConfigurerUtils.formatString(" securityLabelStep at "+jspPath,"not found"));
						line="<jca:wizardStep action=\"securityLabelStep\" type=\"securityLabels\"/>";
					}

					bWriteNeeded=true;

				}
				if(null!=line && line.contains("securityLabelStep")){
					totalStr = totalStr + tmp + "\n" + line + System.getProperty("line.separator");
				}else{
					totalStr = totalStr+ line + System.getProperty("line.separator");
				}
				line = buffReader.readLine();
			}
			fr.close();
			buffReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		if(!bWriteNeeded){
			System.out.println(ConfigurerUtils.formatString(" securityLabelStep at "+jspPath,"not found"));
		}
		
	}

	public void undoJSPContent() {
		try {
			line = buffReader.readLine();
			while (line != null) {
				// System.out.println(line);
				if (line.contains("com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet")) {
					line = line.replace("com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet",
							"com.ptc.windchill.enterprise.servlets.AttachmentsDownloadDirectionServlet");
					bWriteNeeded=true;
					System.out.println(ConfigurerUtils.formatString(" com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet","found"));
				}else if(line.contains("com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse")){
					line = line.replace("com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse",
							"com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse");
					bWriteNeeded=true;
					System.out.println(ConfigurerUtils.formatString(" com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse","found"));
				}else if(line.contains("com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem")){
					line = line.replace("com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem",
							"com.ptc.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem");
					bWriteNeeded=true;
					System.out.println(ConfigurerUtils.formatString(" com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem","found"));
				}
				totalStr = totalStr + line + System.getProperty("line.separator");
				line = buffReader.readLine();
			}
			fr.close();
			buffReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		if(!bWriteNeeded){
			System.out.println(ConfigurerUtils.formatString(" com.nextlabs.em.windchill.servlet.AttachmentsDownloadDirectionServlet","not found"));
			System.out.println(ConfigurerUtils.formatString(" com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponse","not found"));
			System.out.println(ConfigurerUtils.formatString(" com.nextlabs.em.windchill.enterprise.attachments.server.AttachmentsDownloadDirectionResponseItem","not found"));
		}
		
	}

}
