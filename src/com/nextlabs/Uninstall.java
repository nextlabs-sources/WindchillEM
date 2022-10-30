package com.nextlabs;

import com.nextlabs.em.windchill.install.ConfigureHelper;

public class Uninstall {


	public static void main(String[] args) 
	{
		ConfigureHelper configHelper=new ConfigureHelper();
		configHelper.uninstall(args);
	}
}
