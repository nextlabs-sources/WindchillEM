package com.nextlabs.em.windchill.test;

import javax.xml.ws.Endpoint;

import com.nextlabs.em.windchill.QueryAgentImpl;

public class EndpointPublisher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("listen on port 8070");
		Endpoint.publish("http://localhost:8070/nextlabs/QueryAgent", new QueryAgentImpl()); 
		//System.exit(0);
	}

}
