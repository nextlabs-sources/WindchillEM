package com.nextlabs.em.windchill.install;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ApacheConfigurer {
	private String confPath=null;
	private List<String> lines=null;
	private boolean bWriteNeeded=false;
	private int lineNumberWhereInsert=-1;
	private List<String> linesNeedInsert=null;
	//private List<List<String>> parsedLines=null;
	public ApacheConfigurer(String path)
	{
		confPath=path;
		lines=new ArrayList<String>();
		linesNeedInsert=new ArrayList<String>();
	}
	
	public void load()
	{
		BufferedReader br=null;
		try
		{
			FileInputStream fstream = new FileInputStream(confPath);
			br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   
			{
				lines.add(strLine);
			}
			br.close();
		}
		catch(Exception exp)
		{
			lines=null;
		}
	}
	public boolean unload()
	{
		if(this.bWriteNeeded==false)
			return false;
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter(confPath));
			for (int i = 0; i < lines.size(); i++) 
			{
				if(i==lineNumberWhereInsert)
				{
					for(int j=0;j<linesNeedInsert.size();j++)
					{
						pw.println(linesNeedInsert.get(j));
					}
				}
				if(lines.get(i)!=null)
					pw.println(lines.get(i));
			}
			if(lineNumberWhereInsert>=lines.size())
			{
				for(int j=0;j<linesNeedInsert.size();j++)
				{
					pw.println(linesNeedInsert.get(j));
				}
			}
			pw.close();
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
		return true;
	}
	public void print()
	{
		if(lines==null)
			return;
		for(int i=0;i<lines.size();i++)
			System.out.println(" "+i+":"+lines.get(i));
	}
	private List<String> createAuthLines(String appName,List<String> authProviders,String authName)
	{
		/* C:\ptc\Windchill_10.1\Apache\conf\extra\app-Windchill-Auth.conf
		<LocationMatch ^/+Windchill/+com/+nextlabs(;.*)?>
		  AuthzLDAPAuthoritative off
		  AuthName "Windchill"
		  AuthType Basic
		  AuthBasicProvider Windchill-AdministrativeLdap Windchill-EnterpriseLdap 
		  require valid-user
		</LocationMatch>
		*/
		String provider="";
		for(int i=0;i<authProviders.size();i++)
		{
			provider+=" "+authProviders.get(i);
		}
		String[] srcLines={
				"<LocationMatch ^/+"+appName+"/+com/+nextlabs(;.*)?>",
				"  AuthzLDAPAuthoritative off",
				"  AuthName "+authName,
				"  AuthType Basic",
				"  AuthBasicProvider"+provider,
				"  require valid-user",
				"</LocationMatch>"
		};
		linesNeedInsert.clear();
		for(int i=0;i<srcLines.length;i++)
		{
			//System.out.println(srcLines[i]);
			linesNeedInsert.add(srcLines[i]);
		}
		return linesNeedInsert;
	}
	public void addAuth(String appName)
	{
		boolean bFound=false;
		List<String> authProviders=new ArrayList<String>();
		String authName=null;
		for(int i=0;i<lines.size();i++)
		{
			String line=lines.get(i);
			String location="^/+"+appName+"/+com/+nextlabs(;.*)?"; //^/+Windchill/+com/+nextlabs(;.*)?
			if(line.contains(location)&&line.contains("<LocationMatch"))
			{
				bFound=true;
			}
			if(line.contains("</LocationMatch>"))
			{
				this.lineNumberWhereInsert=i+1;
			}
			if(line.contains("AuthName"))
			{
				String[] authsWords=line.split(" ");
				boolean bAuthNameFound=false;
				for(int j=0;j<authsWords.length;j++)
				{
					String word=authsWords[j];
					if(bAuthNameFound==true&&word!=null&&word.isEmpty()==false)
					{
						if(word.charAt(word.length()-1)=='>')
						{
							//System.out.println(ConfigurerUtils.formatString("AuthName1 "+word.substring(0, word.length()-2),"found"));
							authName=word.substring(0, word.length()-2);
						}
						else
						{
							//System.out.println(ConfigurerUtils.formatString("AuthName2 "+word,"found"));
							authName=word;
						}
					}
					if(word.toLowerCase().equalsIgnoreCase("authname"))
						bAuthNameFound=true;
				}
			}
			if(line.contains("AuthnProviderAlias ldap"))
			{
				String[] providerWords=line.split(" ");
				boolean bLdapFound=false;
				for(int j=0;j<providerWords.length;j++)
				{
					String word=providerWords[j];
					if(bLdapFound==true&&word!=null&&word.isEmpty()==false)
					{
						if(word.charAt(word.length()-1)=='>')
						{
							System.out.println(ConfigurerUtils.formatString(" AuthnProvider "+word.substring(0, word.length()-1),"found"));
							authProviders.add(word.substring(0, word.length()-1));
						}
						else
						{
							System.out.println(ConfigurerUtils.formatString("AuthnProvider "+word,"found"));
							authProviders.add(word);
						}
					}
					if(word.toLowerCase().equalsIgnoreCase("ldap"))
						bLdapFound=true;
				}
			}
		}
		if(bFound==true)
		{
			System.out.println(ConfigurerUtils.formatString(" Authenticated resource for /"+appName+"/com/nextlabs/*","found"));
			return;
		}
		else
		{
			System.out.println(ConfigurerUtils.formatString(" Authenticated resource for /"+appName+"/com/nextlabs/*","not found"));
			createAuthLines(appName,authProviders,authName);
			bWriteNeeded=true;
		}
	}
	
	public void removeAuth(String appName)
	{
		boolean bStartTagFound=false, bEndTagFound=false;
		for(int i=0;i<lines.size();i++)
		{
			String line=lines.get(i);
			String location="^/+"+appName+"/+com/+nextlabs(;.*)?"; //^/+Windchill/+com/+nextlabs(;.*)?
			if(line.contains(location)&&line.contains("<LocationMatch"))
			{
				lines.set(i,null);
				bStartTagFound=true;
			}
			if(line.contains("</LocationMatch>")&&bStartTagFound==true)
			{
				lines.set(i,null);
				bEndTagFound=true;
			}
			if(bStartTagFound==true&&bEndTagFound==false)
				lines.set(i,null);
			if(bStartTagFound==true&&bEndTagFound==true)
			{
				bWriteNeeded=true;
				break;
			}
		}
	}
	private String createAJPLine(String ajpWorkerName)
	{
		/*
		 * JkMount /Windchill/com/nextlabs/* ajpWorker
		 */
		linesNeedInsert.clear();
		String line="  JkMount /Windchill/com/nextlabs/* "+ajpWorkerName;
		linesNeedInsert.add(line);
		return line;
	}
	public void addAjp(String ajpWorkerName)
	{
		/*
		C:\ptc\Windchill_10.1\Apache\conf\extra\app-Windchill-AJP.conf
		*/
		boolean bFound=false;
		for(int i=0;i<lines.size();i++)
		{
			String line=lines.get(i);
			if(line.contains("/Windchill/com/nextlabs/*")&&line.contains("JkMount"))
			{
				bFound=true;
				break;
			}
			if(line.contains("</IfModule>"))
			{
				this.lineNumberWhereInsert=i;
				break;
			}
		}
		if(bFound==true)
		{
			System.out.println(ConfigurerUtils.formatString(" AJP Worker for \"/Windchill/com/nextlabs/*\"","found"));
			return;
		}
		else
		{
			System.out.println(ConfigurerUtils.formatString(" AJP Worker for \"/Windchill/com/nextlabs/*\"","not found"));
			createAJPLine(ajpWorkerName);
			bWriteNeeded=true;
		}
	}

	public void removeAjp(String ajpWorker)
	{
		int i=0;
		for( i=0;i<lines.size();i++)
		{
			String line=lines.get(i);
			if(line.contains("/Windchill/com/nextlabs/*")&&line.contains("JkMount"))
			{
				//lines.remove(i);
				System.out.println(ConfigurerUtils.formatString(" AJP Worker for \"/Windchill/com/nextlabs/*\"","found"));
				lines.set(i, null);
				bWriteNeeded=true;
				break;
			}
		}
		if(i==lines.size())
			System.out.println(ConfigurerUtils.formatString(" AJP Worker for \"/Windchill/com/nextlabs/*\"","not found"));
	}

}
