package com.nextlabs.em.windchill;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import com.nextlabs.ObjectAttrCollection;

@WebService
public interface QueryAgent {
	/*@WebMethod
	public ObjectAttrCollection QueryOid(
			@WebParam(name = "oid")String oid,
			@WebParam(name = "requestId")String requestId,
			@WebParam(name = "reserved1")String reserved1,
			@WebParam(name = "reserved2")String reserved2) throws QueryAgentException;
	@WebMethod
	public ObjectAttrCollection QueryUser(
			@WebParam(name = "userName")String userName,
			@WebParam(name = "requestId")String requestId,
			@WebParam(name = "reserved1")String reserved1,
			@WebParam(name = "reserved2")String reserved2) throws QueryAgentException;
	@WebMethod
	public ObjectAttrCollection QueryTest(String para1,String para2,String para3, String para4) throws QueryAgentException;
	@WebMethod
	public List<String>			Eval(
			@WebParam(name = "requestId")String requestId,
			@WebParam(name = "paras")String[] paras,
			@WebParam(name = "reserved1")String reserved1) throws QueryAgentException;*/
	@WebMethod
	public ObjectAttrCollection QueryOid(
			String oid,
			String requestId,
			String reserved1,
			String reserved2) throws QueryAgentException;
	@WebMethod
	public ObjectAttrCollection QueryUser(
			String userName,
			String requestId,
			String reserved1,
			String reserved2) throws QueryAgentException;
	@WebMethod
	public ObjectAttrCollection QueryTest(String para1,String para2,String para3, String para4) throws QueryAgentException;
	@WebMethod
	@XmlElement(required=true)
	public List<String>			Eval(
			@WebParam(name = "requestId")String requestId,
			@WebParam(name = "paras")String[] paras,
			@WebParam(name = "reserved")String reserved) throws QueryAgentException;
}
