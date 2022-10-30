package com.nextlabs.em.windchill.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatches {
	
	public static Pattern r_0;
	public static Matcher m_0;

	/*
	 public static void main( String args[] ){

	      // String to be scanned to find the pattern.
	      String line = "http://10.65.2.194/Windchill/app/#ptc1/tcomp/test/infoPage?oid=OR%3Awt.folder.SubFolder%3A45280&u8=1";
	      String pattern_0 = "tcomp/(.*)/infoPage";
	      String pattern_1 = "infoPage";
	      // Create a Pattern object
	      Pattern r_0 = Pattern.compile(pattern_0);
	      Pattern r_1 = Pattern.compile(pattern_1);

	      // Now create matcher object.
	      Matcher m_0 = r_0.matcher(line);
	      Matcher m_1 = r_1.matcher(line);
	      if (m_0.find( )) {
	         System.out.println("Found value: " + m_0.group(0) );
	         //System.out.println("Found value: " + m_0.group(1) );
	         //System.out.println("Found value: " + m_0.group(2) );
	      } else {
	         System.out.println("NO MATCH");
	      }
	      
	      if (m_1.find( )) {
		         System.out.println("Found value: " + m_1.group(0) );
		         //System.out.println("Found value: " + m_1.group(1) );
		         //System.out.println("Found value: " + m_1.group(2) );
		      } else {
		         System.out.println("NO MATCH");
		      }
	   }
	 */
	 public static boolean matcher(String url, Pattern pattern){
	      m_0 = pattern.matcher(url);
 	      return m_0.find( );
	 }

}
