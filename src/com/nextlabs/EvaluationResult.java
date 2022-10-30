package com.nextlabs;

//import com.nextlabs.destiny.sdk.CEEnforcement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nextlabs.IObligation.NextAction;

public class EvaluationResult {
	private Logger logger = Logger.getLogger(EvaluationResult.class);

	public class RuntimeObligation {
		private String name;
		private String policy;
		private HashMap<String, String> parameters;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setPolicy(String policy) {
			this.policy = policy;
		}

		public String getPolicy() {
			return policy;
		}

		public void setParameter(String name, String value) {
			if (parameters == null)
				parameters = new HashMap<String, String>();
			parameters.put(name, value);
		}

		public String getParameter(String name) {
			if (parameters == null)
				return null;
			return parameters.get(name);
		}

		public Map<String, String> getParameters() {
			return parameters;
		}
	}

	private boolean allowed;// true=allow, and false=deny
	private String policyName;
	private String policyMessage;
	private Map<String, List<RuntimeObligation>> obligations;

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public void setPolicyName(String name) {
		this.policyName = name;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyMessage(String message) {
		this.policyMessage = message;
	}

	public String getPolicyMessage() {
		return policyMessage;
	}

	public List<RuntimeObligation> getObligation(String obligationName) {
		if (obligations == null)
			return null;
		return obligations.get(obligationName);
	}

	public RuntimeObligation[] getObligations() {

		logger.info(" &&&&&Obligations :" + obligations);
		if (obligations != null) {
			int obligationsSize = 0;
			for (String key : obligations.keySet()) {
				List<RuntimeObligation> oblist = obligations.get(key);
				if (oblist != null) {
					obligationsSize = obligationsSize + oblist.size();
				}
			}
			logger.info(" &&&&&obligationsSize :" + obligationsSize);
			RuntimeObligation[] obs = new RuntimeObligation[obligationsSize];
			int idx = 0;

			for (String key : obligations.keySet()) {
				logger.info(" &&&&&Obligation Key:" + key);
				List<RuntimeObligation> oblist = obligations.get(key);
				for (int j = 0; j < oblist.size(); j++) {
					obs[idx] = oblist.get(j);
					idx++;
				}
			}
			return obs;
		}
		return null;
	}

	public void addObligation(RuntimeObligation ob) {
		if (obligations == null)
			obligations = new HashMap<String, List<RuntimeObligation>>();

		List<RuntimeObligation> oblist = obligations.get(ob.getName());
		if (oblist == null)
			oblist = new ArrayList<RuntimeObligation>();

		oblist.add(ob);
		obligations.put(ob.getName(), oblist);
	}

	public void print(EntitlementManagerContext ctx) {
		logger.info(" policy name:" + policyName);
		logger.info(" policy message:" + policyMessage);
		logger.info(" different obligations count:"
				+ (obligations == null ? 0 : obligations.size()));

		if (obligations != null) {
			for (java.util.Map.Entry<String, List<RuntimeObligation>> entry : obligations
					.entrySet()) {
				String obName = entry.getKey();
				logger.debug("    " + obName);
				List<RuntimeObligation> oblist = entry.getValue();
				if (oblist != null) {
					logger.debug("    obligation list size:" + oblist.size());
					for (int j = 0; j < oblist.size(); j++) {
						RuntimeObligation ob = oblist.get(j);
						logger.debug("    " + ob.getPolicy());
						for (java.util.Map.Entry<String, String> param : ob.parameters
								.entrySet()) {
							logger.debug("      " + param.getKey() + " => "
									+ param.getValue());
						}
					}
				}
			}
		}
	}

	public void ExecObligation(EntitlementManagerContext ctx, String[] oids,
			com.nextlabs.ObjectAttrCollection userAttrs,
			com.nextlabs.ObjectAttrCollection[] objAttrs) {
		com.nextlabs.configuration.Configuration obsConf = ConfigurationManager
				.getInstance().getObligationConfiguration();
		if (obsConf == null) {
			logger.info(" there is not any obligation configured!");
			return;
		}

		if (obligations != null) {
			for (java.util.Map.Entry<String, List<RuntimeObligation>> entry : obligations
					.entrySet()) {
				String obName = entry.getKey();
				List<RuntimeObligation> oblist = entry.getValue();
				if (oblist != null) {
					NextAction nextAction = NextAction.CONTINUE;
					com.nextlabs.configuration.Obligation obConf = obsConf
							.findObligation(obName);
					if (obConf != null
							&& (obConf.getDisabled() == null
									|| obConf.getDisabled().equalsIgnoreCase(
											"no") || obConf.getDisabled()
									.equalsIgnoreCase("false"))) {
						logger.info(" obligation " + obName
								+ " configured and returned from PC");
						try {
							@SuppressWarnings("unchecked")
							Class<com.nextlabs.IObligation> ownClass = (Class<com.nextlabs.IObligation>) Class
									.forName(obConf.getType());
							com.nextlabs.IObligation obligationClass = ownClass
									.newInstance();
							nextAction = obligationClass.process(
									ctx.getRequestId(), oids, oblist,
									userAttrs, objAttrs);

							logger.info(" obligation " + obName + " return "
									+ nextAction);
							if (nextAction == NextAction.STOP)
								break;
							if (nextAction == NextAction.DENY) {
								this.allowed = false;
								break;
							}
						} catch (Exception exp) {
							logger.error(exp);
						}
					} else {
						logger.info(" obligation " + obName
								+ " returned from PC but not configured");
					}

				}
			}
		}
	}

}
