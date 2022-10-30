package com.nextlabs.em.windchill.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import wt.util.WTProperties;

import com.nextlabs.TextProtector;

public class SetQueryAgentUser   {
	public static void main(String[] args)
	{
		try
		{
			WTProperties wtproperties = WTProperties.getLocalProperties();  
		    String wthome = wtproperties.getProperty("wt.home", "");
	
		    String name=null;
		    String password=null;
		    if(args.length==0)
		    {
		    	System.out.print("Please input Windchill Administrator name:");
		    	name=System.console().readLine();
		    	System.out.print("Please input your password:");
		    	char[] chPasswd1=System.console().readPassword();
		    	System.out.print("Please input your password again:");
		    	char[] chPasswd2=System.console().readPassword();
		    	password=String.valueOf(chPasswd1);
		    	String password2=String.valueOf(chPasswd2);
		    	while(password!=null&&password2!=null&&password.compareTo(password2)!=0)
		    	{
		    		System.out.println("You input two different password, try again");
		    		System.out.print("Please input your password:");
			    	chPasswd1=System.console().readPassword();
			    	System.out.print("Please input your password again:");
			    	chPasswd2=System.console().readPassword();
			    	password=String.valueOf(chPasswd1);
			    	password2=String.valueOf(chPasswd2);
		    		
		    		//return;
		    	}
		    }
		    if(args.length==1)
		    {
		    	name=args[0];
		    	System.out.print("Please input your password:");
		    	char[] chPasswd1=System.console().readPassword();
		    	System.out.print("Please input your password again:");
		    	char[] chPasswd2=System.console().readPassword();
		    	password=String.valueOf(chPasswd1);
		    	String password2=String.valueOf(chPasswd2);
		    	if(password!=null&&password2!=null&&password.compareTo(password2)!=0)
		    	{
		    		System.out.println("You input two different password, try again");
		    		return;
		    	}
		    }
		    if(args.length==2)
		    {
		    	name=args[0];
		    	password=args[1];
		    }

		    TextProtector textProtector=new TextProtector();
		    String encryptedPassword=textProtector.encryptText(password);
		    
		    String filePath=wthome+"\\codebase\\com\\nextlabs\\conf\\"+com.nextlabs.Property.PEP_PROPERTIES_FILE_NAME;
		    InputStream is=null;
			try
			{
				is = new FileInputStream(filePath);
			}
			catch(FileNotFoundException exp)
			{
			
			}
			if(is==null)
				return;
			
	        Properties prop = new Properties(); 
	        try
	        {
	        	prop.load(is); 
	        }
	        catch(IOException ioExp)
	        {
	        	ioExp.printStackTrace();
	        }
	        prop.setProperty(com.nextlabs.Property.PEP_WINDCHILL_QUERYAGENT_USER, name);
	        prop.setProperty(com.nextlabs.Property.PEP_WINDCHILL_QUERYAGENT_PASSWORD, encryptedPassword);
	        FileOutputStream fos = new FileOutputStream(filePath);
	        prop.store(fos, "changed via com.nextlabs.em.windchill.tools.SetQueryAgentUser");
	        fos.flush();
	        fos.close();
		}
		catch(IOException ioExp)
		{
		
		}
	}
	
}
