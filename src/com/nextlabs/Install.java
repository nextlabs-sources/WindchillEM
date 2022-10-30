package com.nextlabs;

import com.nextlabs.em.windchill.install.ConfigureHelper;


public class Install {

	public static void main(String[] args) 
	{
		ConfigureHelper configHelper=new ConfigureHelper();
		configHelper.install(args);
	}
}
