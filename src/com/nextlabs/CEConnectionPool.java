package com.nextlabs;

import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.nextlabs.destiny.sdk.CEApplication;
import com.nextlabs.destiny.sdk.CESdk;
import com.nextlabs.destiny.sdk.CESdkException;
import com.nextlabs.destiny.sdk.CEUser;

public class CEConnectionPool {
	private static  Logger logger = Logger
			.getLogger(CEConnectionPool.class);
	public static final int DEFAULT_CONNECTION_POOL_SIZE 	= 1;
	private static int poolSize=getPoolSize();
	private static long ceSdkConnections[]=new long[poolSize];
	static {
		CESdk 			ceSdk=new CESdk();
		CEApplication 	ceApp=new CEApplication("com.nextlabs.em.windchill", "N/A");
		CEUser 			ceUser=new CEUser("NextLabs", "N/A");
		int poolSize=getPoolSize();
		if(poolSize!=DEFAULT_CONNECTION_POOL_SIZE)
		{
			ceSdkConnections=new long[poolSize];
		}
		try
		{
			synchronized(ceSdkConnections)
			{
				String ipAddr=null;
				ipAddr=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, Property.PC_IP_ADDRESS);
				if(ipAddr==null||ipAddr.isEmpty()==true)
				{
					try
					{
						InetAddress addr = InetAddress.getLocalHost();
						ipAddr=addr.getHostAddress();
					}
					catch(Exception exp)
					{
						
					}
				}
				if(ipAddr==null||ipAddr.isEmpty()==true)
					ipAddr="127.0.0.1";
				logger.info(" initial pc ip address is "+ipAddr);
				try
				{
					for(int i=0;i<ceSdkConnections.length;i++)
					{
						if(ceSdkConnections[i]==0)
						{
							ceSdkConnections[i] = ceSdk.Initialize(ceApp, ceUser, ipAddr, 5000);
						}
					}
				}
				catch(CESdkException ceSdkExp)
				{
					
				}
			}
		}
		catch(Exception expout)
		{
			logger.info("Exception:"+expout.getMessage());
			expout.printStackTrace();
		}
		
	}
	private static int getPoolSize()
	{
		String strPoolSize=ConfigurationManager.getInstance().getProperty(com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME, Property.PC_CONNECTION_POOL_SIZE);
		int poolsize=0;
		if(strPoolSize!=null&&strPoolSize.isEmpty()==false)
		{
			try
			{
				poolsize=Integer.parseInt(strPoolSize);
			}
			catch(NumberFormatException nfExp)
			{
			}
		}
		if(poolsize<=0)
			poolsize=DEFAULT_CONNECTION_POOL_SIZE;
		return poolsize;
	}
	private static CEConnectionPool instance=null;
	private CEConnectionPool(){}
	public static synchronized CEConnectionPool getInstance() {
		if(instance == null)
		{
			instance =new CEConnectionPool();
		}
		
		return instance;
	}
	private static CESdk 			ceSdk=new CESdk();
	public long getCeSdkConnection(String ipAddr,EntitlementManagerContext ctx)
	{
		
		CEApplication 	ceApp=new CEApplication("com.nextlabs.em.windchill", "N/A");
		CEUser 			ceUser=new CEUser("NextLabs", "N/A");
		try 
		{
			try
			{
				synchronized(ceSdkConnections)
				{
					if(ceSdkConnections[0]==0)
					{
						//logger.info"PC IP Addr="+ipAddr+" in getCeSdkConnection()");
						ceSdkConnections[0] = ceSdk.Initialize(ceApp, ceUser, ipAddr, 5000);
					}
					
				}
			}
			catch (CESdkException ex)
			{
				ex.printStackTrace(System.out);			
				ceSdkConnections[0] = 0;
			}

		} catch (Exception e) {
			e.printStackTrace(System.out);
			ceSdkConnections[0] = 0;
		}
		return ceSdkConnections[0];
	}

	public void resetConnection(long conn)
	{
		synchronized(ceSdkConnections)
		{
			for(int i=0;i<ceSdkConnections.length;i++)
			{
				if(ceSdkConnections[i]==conn)
				{
					ceSdkConnections[i]=0;
				}
			}
			
		}
	}
}
