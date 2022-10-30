package com.nextlabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.prefs.Preferences;

import org.apache.commons.io.FilenameUtils;

public class Utilities {


    public static String readToString(java.io.File file, String encoding)
    {
    	Long filelength = file.length();
    	byte[] filecontent = new byte[filelength.intValue()];
    	try {
    		java.io.FileInputStream in = new java.io.FileInputStream(file);
    		in.read(filecontent);
    		in.close();
    	}
    	catch (java.io.FileNotFoundException e)
    	{
    		e.printStackTrace();
    	}
    	catch (java.io.IOException e) 
    	{
    		e.printStackTrace();
    	}
    	
    	return new String(filecontent);
    	
    	/*catch (java.io.UnsupportedEncodingException e) 
    	{
    		System.err.println("The OS does not support " + encoding);
    		e.printStackTrace();
    		return null;
    	}*/
    }

    public static String getStackTraceString(Exception exp)
    {
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	exp.printStackTrace(pw);
    	return sw.toString();
    }
    public static String RandomString(int length) 
    {  
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789#@%&!";  
        Random random = new Random();  
        StringBuffer buf = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            int num = random.nextInt(str.length());  
            buf.append(str.charAt(num));  
        }  
        return buf.toString();  
    }
    
    public static long ipToInt(String addr) {
		String[] addrArray = addr.split("\\.");
	
		long num = 0;
		
		for (int i = 0; i < addrArray.length; i++) {
			int power = 3 - i;
	
			num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
		}
		
		return num;
	}

    public static String readRegString(int hkey, String key, String valueName)
    {
    	String ret=null;
    	try{
    		ret= WinRegistry.readString(hkey, key, valueName);
    	}
    	catch(Exception exp){
    		ret=null;
    	}
    	return ret;
    }
    
    public static String backup(String srcfilepath)
    {
    	return backup(srcfilepath,null);
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
    public static String backup(String srcfilepath,String id)
    {
    	String dotid=null;
    	if(id==null||id.isEmpty())
    		dotid="";
    	else
    		dotid="."+id;
    	String targetfilepath=	FilenameUtils.getFullPath(srcfilepath)
    							+FilenameUtils.getBaseName(srcfilepath)
    							+".bak."
    							+getDayTimeString("yyyyMMddHHmmssSSS")
    							+dotid
    							+"."+FilenameUtils.getExtension(srcfilepath);
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
    
    public static String getDayTimeString(String format)
    {//"yyyyMMddHHmmssSSS"
    	Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
    	return sdf.format(cal.getTime());
    }
    
    public static void extractFileFromJar(String jarFile,String destDir) throws IOException
    {
    	java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile);
    	
    	java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
    	while (entries.hasMoreElements()) {
    		java.util.jar.JarEntry file = (java.util.jar.JarEntry) entries.nextElement();
    		java.io.File f = new java.io.File(destDir + java.io.File.separator + file.getName());
    		if (file.isDirectory()) { // if its a directory, create it
    			f.mkdir();
    			continue;
    		}
    		java.io.InputStream is = jar.getInputStream(file); // get the input stream
    		java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
    		while (is.available() > 0) {  // write contents of 'is' to 'fos'
    			fos.write(is.read());
    		}
    		fos.close();
    		is.close();
    	}
    }
    public static class WinRegistry {
    	  public static final int HKEY_CURRENT_USER = 0x80000001;
    	  public static final int HKEY_LOCAL_MACHINE = 0x80000002;
    	  public static final int REG_SUCCESS = 0;
    	  public static final int REG_NOTFOUND = 2;
    	  public static final int REG_ACCESSDENIED = 5;

    	  private static final int KEY_ALL_ACCESS = 0xf003f;
    	  private static final int KEY_READ = 0x20019;
    	  private static Preferences userRoot = Preferences.userRoot();
    	  private static Preferences systemRoot = Preferences.systemRoot();
    	  private static Class<? extends Preferences> userClass = userRoot.getClass();
    	  private static Method regOpenKey = null;
    	  private static Method regCloseKey = null;
    	  private static Method regQueryValueEx = null;
    	  private static Method regEnumValue = null;
    	  private static Method regQueryInfoKey = null;
    	  private static Method regEnumKeyEx = null;
    	  private static Method regCreateKeyEx = null;
    	  private static Method regSetValueEx = null;
    	  private static Method regDeleteKey = null;
    	  private static Method regDeleteValue = null;

    	  static {
    	    try {
    	      regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey",
    	          new Class[] { int.class, byte[].class, int.class });
    	      regOpenKey.setAccessible(true);
    	      regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey",
    	          new Class[] { int.class });
    	      regCloseKey.setAccessible(true);
    	      regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx",
    	          new Class[] { int.class, byte[].class });
    	      regQueryValueEx.setAccessible(true);
    	      regEnumValue = userClass.getDeclaredMethod("WindowsRegEnumValue",
    	          new Class[] { int.class, int.class, int.class });
    	      regEnumValue.setAccessible(true);
    	      regQueryInfoKey = userClass.getDeclaredMethod("WindowsRegQueryInfoKey1",
    	          new Class[] { int.class });
    	      regQueryInfoKey.setAccessible(true);
    	      regEnumKeyEx = userClass.getDeclaredMethod(  
    	          "WindowsRegEnumKeyEx", new Class[] { int.class, int.class,  
    	              int.class });  
    	      regEnumKeyEx.setAccessible(true);
    	      regCreateKeyEx = userClass.getDeclaredMethod(  
    	          "WindowsRegCreateKeyEx", new Class[] { int.class,  
    	              byte[].class });  
    	      regCreateKeyEx.setAccessible(true);  
    	      regSetValueEx = userClass.getDeclaredMethod(  
    	          "WindowsRegSetValueEx", new Class[] { int.class,  
    	              byte[].class, byte[].class });  
    	      regSetValueEx.setAccessible(true); 
    	      regDeleteValue = userClass.getDeclaredMethod(  
    	          "WindowsRegDeleteValue", new Class[] { int.class,  
    	              byte[].class });  
    	      regDeleteValue.setAccessible(true); 
    	      regDeleteKey = userClass.getDeclaredMethod(  
    	          "WindowsRegDeleteKey", new Class[] { int.class,  
    	              byte[].class });  
    	      regDeleteKey.setAccessible(true); 
    	    }
    	    catch (Exception e) {
    	      e.printStackTrace();
    	    }
    	  }

    	  private WinRegistry() {  }

    	  /**
    	   * Read a value from key and value name
    	   * @param hkey   HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
    	   * @param key
    	   * @param valueName
    	   * @return the value
    	   * @throws IllegalArgumentException
    	   * @throws IllegalAccessException
    	   * @throws InvocationTargetException
    	   */
    	  public static String readString(int hkey, String key, String valueName) 
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    if (hkey == HKEY_LOCAL_MACHINE) {
    	      return readString(systemRoot, hkey, key, valueName);
    	    }
    	    else if (hkey == HKEY_CURRENT_USER) {
    	      return readString(userRoot, hkey, key, valueName);
    	    }
    	    else {
    	      throw new IllegalArgumentException("hkey=" + hkey);
    	    }
    	  }

    	  /**
    	   * Read value(s) and value name(s) form given key 
    	   * @param hkey  HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
    	   * @param key
    	   * @return the value name(s) plus the value(s)
    	   * @throws IllegalArgumentException
    	   * @throws IllegalAccessException
    	   * @throws InvocationTargetException
    	   */
    	  public static Map<String, String> readStringValues(int hkey, String key) 
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    if (hkey == HKEY_LOCAL_MACHINE) {
    	      return readStringValues(systemRoot, hkey, key);
    	    }
    	    else if (hkey == HKEY_CURRENT_USER) {
    	      return readStringValues(userRoot, hkey, key);
    	    }
    	    else {
    	      throw new IllegalArgumentException("hkey=" + hkey);
    	    }
    	  }

    	  /**
    	   * Read the value name(s) from a given key
    	   * @param hkey  HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
    	   * @param key
    	   * @return the value name(s)
    	   * @throws IllegalArgumentException
    	   * @throws IllegalAccessException
    	   * @throws InvocationTargetException
    	   */
    	  public static List<String> readStringSubKeys(int hkey, String key) 
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    if (hkey == HKEY_LOCAL_MACHINE) {
    	      return readStringSubKeys(systemRoot, hkey, key);
    	    }
    	    else if (hkey == HKEY_CURRENT_USER) {
    	      return readStringSubKeys(userRoot, hkey, key);
    	    }
    	    else {
    	      throw new IllegalArgumentException("hkey=" + hkey);
    	    }
    	  }

    	  /**
    	   * Create a key
    	   * @param hkey  HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
    	   * @param key
    	   * @throws IllegalArgumentException
    	   * @throws IllegalAccessException
    	   * @throws InvocationTargetException
    	   */
    	  public static void createKey(int hkey, String key) 
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    int [] ret;
    	    if (hkey == HKEY_LOCAL_MACHINE) {
    	      ret = createKey(systemRoot, hkey, key);
    	      regCloseKey.invoke(systemRoot, new Object[] { new Integer(ret[0]) });
    	    }
    	    else if (hkey == HKEY_CURRENT_USER) {
    	      ret = createKey(userRoot, hkey, key);
    	      regCloseKey.invoke(userRoot, new Object[] { new Integer(ret[0]) });
    	    }
    	    else {
    	      throw new IllegalArgumentException("hkey=" + hkey);
    	    }
    	    if (ret[1] != REG_SUCCESS) {
    	      throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
    	    }
    	  }

    	  /**
    	   * Write a value in a given key/value name
    	   * @param hkey
    	   * @param key
    	   * @param valueName
    	   * @param value
    	   * @throws IllegalArgumentException
    	   * @throws IllegalAccessException
    	   * @throws InvocationTargetException
    	   */
    	  public static void writeStringValue
    	    (int hkey, String key, String valueName, String value) 
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    if (hkey == HKEY_LOCAL_MACHINE) {
    	      writeStringValue(systemRoot, hkey, key, valueName, value);
    	    }
    	    else if (hkey == HKEY_CURRENT_USER) {
    	      writeStringValue(userRoot, hkey, key, valueName, value);
    	    }
    	    else {
    	      throw new IllegalArgumentException("hkey=" + hkey);
    	    }
    	  }

    	  /**
    	   * Delete a given key
    	   * @param hkey
    	   * @param key
    	   * @throws IllegalArgumentException
    	   * @throws IllegalAccessException
    	   * @throws InvocationTargetException
    	   */
    	  public static void deleteKey(int hkey, String key) 
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    int rc = -1;
    	    if (hkey == HKEY_LOCAL_MACHINE) {
    	      rc = deleteKey(systemRoot, hkey, key);
    	    }
    	    else if (hkey == HKEY_CURRENT_USER) {
    	      rc = deleteKey(userRoot, hkey, key);
    	    }
    	    if (rc != REG_SUCCESS) {
    	      throw new IllegalArgumentException("rc=" + rc + "  key=" + key);
    	    }
    	  }

    	  /**
    	   * delete a value from a given key/value name
    	   * @param hkey
    	   * @param key
    	   * @param value
    	   * @throws IllegalArgumentException
    	   * @throws IllegalAccessException
    	   * @throws InvocationTargetException
    	   */
    	  public static void deleteValue(int hkey, String key, String value) 
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    int rc = -1;
    	    if (hkey == HKEY_LOCAL_MACHINE) {
    	      rc = deleteValue(systemRoot, hkey, key, value);
    	    }
    	    else if (hkey == HKEY_CURRENT_USER) {
    	      rc = deleteValue(userRoot, hkey, key, value);
    	    }
    	    if (rc != REG_SUCCESS) {
    	      throw new IllegalArgumentException("rc=" + rc + "  key=" + key + "  value=" + value);
    	    }
    	  }

    	  // =====================

    	  private static int deleteValue
    	    (Preferences root, int hkey, String key, String value)
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
    	        new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS) });
    	    if (handles[1] != REG_SUCCESS) {
    	      return handles[1];  // can be REG_NOTFOUND, REG_ACCESSDENIED
    	    }
    	    int rc =((Integer) regDeleteValue.invoke(root,  
    	        new Object[] { 
    	          new Integer(handles[0]), toCstr(value) 
    	          })).intValue();
    	    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
    	    return rc;
    	  }

    	  private static int deleteKey(Preferences root, int hkey, String key) 
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    int rc =((Integer) regDeleteKey.invoke(root,  
    	        new Object[] { new Integer(hkey), toCstr(key) })).intValue();
    	    return rc;  // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
    	  }

    	  private static String readString(Preferences root, int hkey, String key, String value)
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
    	        new Integer(hkey), toCstr(key), new Integer(KEY_READ) });
    	    if (handles[1] != REG_SUCCESS) {
    	      return null; 
    	    }
    	    byte[] valb = (byte[]) regQueryValueEx.invoke(root, new Object[] {
    	        new Integer(handles[0]), toCstr(value) });
    	    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
    	    return (valb != null ? new String(valb).trim() : null);
    	  }

    	  private static Map<String,String> readStringValues
    	    (Preferences root, int hkey, String key)
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    HashMap<String, String> results = new HashMap<String,String>();
    	    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
    	        new Integer(hkey), toCstr(key), new Integer(KEY_READ) });
    	    if (handles[1] != REG_SUCCESS) {
    	      return null;
    	    }
    	    int[] info = (int[]) regQueryInfoKey.invoke(root,
    	        new Object[] { new Integer(handles[0]) });

    	    int count = info[0]; // count  
    	    int maxlen = info[3]; // value length max
    	    for(int index=0; index<count; index++)  {
    	      byte[] name = (byte[]) regEnumValue.invoke(root, new Object[] {
    	          new Integer
    	            (handles[0]), new Integer(index), new Integer(maxlen + 1)});
    	      String value = readString(hkey, key, new String(name));
    	      results.put(new String(name).trim(), value);
    	    }
    	    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
    	    return results;
    	  }

    	  private static List<String> readStringSubKeys
    	    (Preferences root, int hkey, String key)
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    List<String> results = new ArrayList<String>();
    	    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
    	        new Integer(hkey), toCstr(key), new Integer(KEY_READ) 
    	        });
    	    if (handles[1] != REG_SUCCESS) {
    	      return null;
    	    }
    	    int[] info = (int[]) regQueryInfoKey.invoke(root,
    	        new Object[] { new Integer(handles[0]) });

    	    int count  = info[0]; // Fix: info[2] was being used here with wrong results. Suggested by davenpcj, confirmed by Petrucio
    	    int maxlen = info[3]; // value length max
    	    for(int index=0; index<count; index++)  {
    	      byte[] name = (byte[]) regEnumKeyEx.invoke(root, new Object[] {
    	          new Integer
    	            (handles[0]), new Integer(index), new Integer(maxlen + 1)
    	          });
    	      results.add(new String(name).trim());
    	    }
    	    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
    	    return results;
    	  }

    	  private static int [] createKey(Preferences root, int hkey, String key)
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    return  (int[]) regCreateKeyEx.invoke(root,
    	        new Object[] { new Integer(hkey), toCstr(key) });
    	  }

    	  private static void writeStringValue 
    	    (Preferences root, int hkey, String key, String valueName, String value) 
    	    throws IllegalArgumentException, IllegalAccessException,
    	    InvocationTargetException 
    	  {
    	    int[] handles = (int[]) regOpenKey.invoke(root, new Object[] {
    	        new Integer(hkey), toCstr(key), new Integer(KEY_ALL_ACCESS) });

    	    regSetValueEx.invoke(root,  
    	        new Object[] { 
    	          new Integer(handles[0]), toCstr(valueName), toCstr(value) 
    	          }); 
    	    regCloseKey.invoke(root, new Object[] { new Integer(handles[0]) });
    	  }

    	  // utility
    	  private static byte[] toCstr(String str) {
    	    byte[] result = new byte[str.length() + 1];

    	    for (int i = 0; i < str.length(); i++) {
    	      result[i] = (byte) str.charAt(i);
    	    }
    	    result[str.length()] = 0;
    	    return result;
    	  }
    	}
}
