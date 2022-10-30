package com.nextlabs;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement  
@XmlAccessorType(XmlAccessType.FIELD)  
public class ObjectAttrCollection {
	@SuppressWarnings("unused")
	private String message;
	
	@XmlJavaTypeAdapter(value = MapAdapter.class)  
	private HashMap<String,String> attrs;

	public void add(String name,String value)
	{
		if(name==null||name.isEmpty()||value==null||value.isEmpty())
			return;
		if(attrs==null)
			attrs=new HashMap<String,String>();
		attrs.put(name, value);
	}
	
	public HashMap<String,String> getAttrs()
	{
		return attrs;
	}
	public int size()
	{
		if(attrs!=null)
			return attrs.size();
		return 0;
	}
	public String getAttr(String name)
	{
		String ret=null;
		try
		{
			if(attrs!=null)
			{
				ret=attrs.get(name);
			}
		}
		catch(Exception exp)
		{
			
		}
		return ret;
	}
}
