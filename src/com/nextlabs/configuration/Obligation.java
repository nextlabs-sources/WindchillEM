package com.nextlabs.configuration;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Obligation")
public class Obligation{
	String type;
	String disabled;
	String name;
	@XmlAttribute(name = "type")
	public String getType()
	{
		return this.type;
	}
	
	@XmlAttribute(name = "disabled")
	public String getDisabled()
	{
		return this.disabled;
	}
	
	@XmlAttribute(name = "name")
	public String getName()
	{
		return this.name;
	}

	
	public void setType(String type)
	{
		this.type=type;
	}
	public void setDisabled(String disabled)
	{
		this.disabled=disabled;
	}
	public void setName(String name)
	{
		this.name=name;
	}
}