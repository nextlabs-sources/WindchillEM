package com.nextlabs.configuration;

import java.util.List;

//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="Configuration")
public class Configuration {

	List<Obligation> Obligations;
	@XmlElementWrapper(name="Obligations")
	@XmlElement(name="Obligation")
	public void setObligations(List<Obligation> obligations)
	{
		this.Obligations=obligations;
	}
	public List<Obligation> getObligations()
	{
		return this.Obligations;
	}
	
	public Obligation findObligation(String name)
	{
		for(int idx=0;idx<Obligations.size();idx++)
		{
			//com.nextlabs.Utilities.OutputDebugStringA(" configured obligation "+idx+":"+Obligations.get(idx).getName());
			if(Obligations.get(idx).getName().equalsIgnoreCase(name)==true)
				return Obligations.get(idx);
		}
		
		return null;
	}
}
