package com.nextlabs.windchil.em.restapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.openaz.pepapi.Action;
import org.apache.openaz.pepapi.Environment;
import org.apache.openaz.pepapi.PepAgent;
import org.apache.openaz.pepapi.PepAgentFactory;
import org.apache.openaz.pepapi.PepResponse;
import org.apache.openaz.pepapi.Resource;
import org.apache.openaz.pepapi.Subject;
import org.apache.openaz.pepapi.std.StdPepAgentFactory;

import com.bluejungle.framework.crypt.IDecryptor;
import com.bluejungle.framework.crypt.ReversibleEncryptor;
import com.nextlabs.EntitlementManagerContext;
import com.nextlabs.LogLevel;
import com.nextlabs.Utilities;
import com.nextlabs.openaz.pepapi.Application;
import com.nextlabs.openaz.utils.Constants;

public class JPCRestCall {
	
	Logger logger = Logger.getLogger(JPCRestCall.class);
	
	// Singleton class - Single object for this class
	private static JPCRestCall jpcRestCall;

	// Variables needed for policy evaluation
	Properties jpcPropertiesFile;
	PepAgentFactory pepAgentFactory;
	PepAgent pepAgent;
	Subject user;
	Resource resource;
	Action action;

	/***
	 * 
	 * @return
	 */
	public Subject getUser() {
		return user;
	}

	/***
	 * 
	 * @param user
	 */
	public void setUser(Subject user) {
		this.user = user;
	}

	/***
	 * 
	 * @return
	 */
	public Resource getResource() {
		return resource;
	}

	/***
	 * 
	 * @param resource
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/***
	 * 
	 * @return
	 */
	public Action getAction() {
		return action;
	}

	/***
	 * 
	 * @param action
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/***
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	private JPCRestCall(String filePath)
			throws IOException {
		jpcPropertiesFile = new Properties();
		logger.debug("  ==========JPCRestCall:Open AZ Property file path:" +filePath);
		
		FileInputStream fis = new FileInputStream(filePath);
		jpcPropertiesFile.load(fis);
		IDecryptor decryptor = new ReversibleEncryptor();
		//Added to handle encrypted password
		if((jpcPropertiesFile.getProperty(Constants.PDP_REST_OAUTH2_CLIENT_SECRET)!=null)) {
			jpcPropertiesFile.setProperty(Constants.PDP_REST_OAUTH2_CLIENT_SECRET, decryptor.decrypt(jpcPropertiesFile.getProperty(Constants.PDP_REST_OAUTH2_CLIENT_SECRET)));
		}

		logger.debug("  ==========JPCRestCall:Property File Loaded");
		logger.debug( "JPCRestCall:Property File Loaded");
		pepAgentFactory = new StdPepAgentFactory(jpcPropertiesFile);
		pepAgent = pepAgentFactory.getPepAgent();
		logger.debug("  ==========JPCRestCall:RestpepAgent created");
		
	}

	/***
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static JPCRestCall getInstance(String filePath
			) throws IOException {
		if (jpcRestCall == null) {
			jpcRestCall = new JPCRestCall(filePath);
		}
		return jpcRestCall;
	}

	public static void main(String args[]) {
		String path = new File("").getAbsolutePath();
		path = path + "/openaz-pep-on-prem.properties";

		Subject user = Subject.newInstance("Tddy");
		user.addAttribute("location", "IN");
		user.addAttribute("department", "IT");

		/*
		 * Build an Action object with action short name, Action short name
		 * should match with the action from respective policy model action.
		 */
		Action action = Action.newInstance("VW");

		/*
		 * Build a Resource object with resource id(This will typically be the
		 * name of the resource e.g. ./foo.txt) and populate all the available
		 * resource attributes
		 */
		Resource resource = Resource.newInstance("Product Page");
		resource.addAttribute(
				Constants.ID_RESOURCE_RESOURCE_TYPE.stringValue(), "windchill");
		resource.addAttribute("page", "Product");

		/*
		 * try { JPCRestCall jpcRestCall = JPCRestCall.getInstance(path);
		 * jpcRestCall.setUser(user); jpcRestCall.setAction(action);
		 * jpcRestCall.setResource(resource); PepResponse
		 * pepResponse=jpcRestCall.evaluate(); Decision decision =
		 * pepResponse.getWrappedResult().getDecision(); String effectValue =
		 * (decision == Decision.PERMIT) ? "Allow" : decision.toString();
		 * System.out.println(effectValue); } catch (IOException e) {
		 * System.out.println(e.getMessage()); }
		 */

	}

	public PepResponse evaluate() {
		Environment env=Environment.newInstance();
		env.addAttribute("nocache", "yes");
		Application app=Application.newInstance("Windchill");
		PepResponse pepResponse = pepAgent.decide(user, action, resource,env,app);
		return pepResponse;
	}
}
