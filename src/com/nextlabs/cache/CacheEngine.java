package com.nextlabs.cache;

import java.util.HashMap;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class CacheEngine {
	private Logger logger = Logger.getLogger(CacheEngine.class);
	
	private HashMap<String, PolicyDecisionObject> pdpDecisionCache;
	private Stack<String> uidStack;
	private Timer resetTimer;
	private static CacheEngine cacheEngine;



	private CacheEngine() {
			 
		pdpDecisionCache = new HashMap<String, PolicyDecisionObject>();
		uidStack = new Stack<String>();
		resetTimer = new Timer();
		logger.info("WINDCHILL PEP: Cache Engine Intialized");
	}

	public static CacheEngine getInstance() {
		if (cacheEngine == null)
			cacheEngine = new CacheEngine();
		return cacheEngine;
	}

	public void insertIntoCache(String uid, PolicyDecisionObject pdpDecision) {
		pdpDecisionCache.put(uid, pdpDecision);
		uidStack.push(uid);
		logger.info("WINDCHILL PEP: Cache Engine Push Operation");
		logger.info("WINDCHILL PEP: Cache Engine Pushed UID:"+uid);
		logger.info("WINDCHILL PEP: Cache Engine Map:"+pdpDecisionCache);
		 TimerTask RemoveFromCacheTask = new TimerTask() {
			@Override
			public void run() {
				if (uidStack.size() > 0) {
					String removeId = uidStack.pop();
					if (pdpDecisionCache.get(removeId) != null) {
						pdpDecisionCache.remove(removeId);
					}
				}
				logger.info("WINDCHILL PEP: RemoveFromCacheTask completed succesfully ");
			}
		};
		resetTimer.schedule(RemoveFromCacheTask, 10000);
	}

	public PolicyDecisionObject getFromCache(String uid) {
		return pdpDecisionCache.get(uid);
	}

}
