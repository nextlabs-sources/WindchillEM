package com.nextlabs.em.windchill.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

public class ConfigurerUtils {

	//private static int LINE_LENGTH=79;
	private static int LINE_LENGTH=159;
	public static String formatString(String leftPart, String rightPart)
	{
		int spaceLen=0;
		int totalLen=leftPart.length()+rightPart.length();
		String leftStrInternal=leftPart;
		int iFirstPartLen=35;
		if(totalLen>LINE_LENGTH)
		{
			leftStrInternal=leftPart.substring(0, iFirstPartLen)+"..."
					+leftPart.substring(iFirstPartLen+totalLen-LINE_LENGTH+(new String("...")).length()+1);
		}
		
		spaceLen=LINE_LENGTH-leftStrInternal.length();
		
		String formatString="%s%"+spaceLen+"s";
		//System.out.println(" format string:"+formatString+" for "+ leftPart);
		return String.format(formatString,leftStrInternal,rightPart);
	}
	public static void backupTo(String srcfilepath,String destfilepath)
    {
    	
    	InputStream inStream = null;
    	OutputStream outStream = null;
    	System.out.println("src="+srcfilepath);
    	System.out.println("dest="+destfilepath);
        try
        {
            File afile =new File(srcfilepath);
            File bfile =new File(destfilepath);
        	
            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);
           	
            byte[] buffer = new byte[1024];
        	
            int length;
            //copy the file content in bytes 
            while ((length = inStream.read(buffer)) > 0)
            {
            	outStream.write(buffer, 0, length);
            }
         
            inStream.close();
            outStream.close();
        }
        catch(IOException e)
        {
        	System.out.println("message:"+e.getMessage());
        	e.printStackTrace();
        }
    	return;
    }
	 public static String backup(String srcfilepath)
	    {
	    	return backup(srcfilepath,null);
	    }
	public static String getDayTimeString(String format)
    {//"yyyyMMddHHmmssSSS"
    	Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
    	return sdf.format(cal.getTime());
    }
    public static String backup(String srcfilepath,String id)
    {
    	String dotid=null;
    	if(id==null||id.isEmpty())
    		dotid="";
    	else
    		dotid="."+id;
			String targetfilepath="";
			if(srcfilepath.contains("Windchill-Auth")) {
				targetfilepath=	FilenameUtils.getFullPath(srcfilepath)
						+FilenameUtils.getBaseName(srcfilepath)
						+".bak."
						+getDayTimeString("yyyyMMddHHmmssSSS")
						+dotid;
						
			}else {
				targetfilepath=	FilenameUtils.getFullPath(srcfilepath)
									+FilenameUtils.getBaseName(srcfilepath)
									+".bak."
									+getDayTimeString("yyyyMMddHHmmssSSS")
									+dotid
									+"."+FilenameUtils.getExtension(srcfilepath);}
    	InputStream inStream = null;
    	OutputStream outStream = null;
    		
        try
        {
            File afile =new File(srcfilepath);
            File bfile =new File(targetfilepath);
        	
            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);
           	
            byte[] buffer = new byte[1024];
        	
            int length;
            //copy the file content in bytes 
            while ((length = inStream.read(buffer)) > 0)
            {
            	outStream.write(buffer, 0, length);
            }
         
            inStream.close();
            outStream.close();
        }
        catch(IOException e)
        {
        	e.printStackTrace();
        	targetfilepath=null;
        }
    	return targetfilepath;
    }
}
