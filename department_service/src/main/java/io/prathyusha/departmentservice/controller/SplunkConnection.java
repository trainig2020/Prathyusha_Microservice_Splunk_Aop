package io.prathyusha.departmentservice.controller;

import com.splunk.Application;
import com.splunk.Args;
import com.splunk.HttpService;
import com.splunk.Receiver;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;
import com.splunk.ServiceArgs;

public class SplunkConnection {
	
	
	  public static Service getService() {
		  

		  HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
		  
		  ServiceArgs loginArgs= new ServiceArgs();
		  loginArgs.setUsername("prathyushasplunk");
		  loginArgs.setPassword("Splunk@0805"); 
		  loginArgs.setHost("DESKTOP-KLJ2H36");
		  loginArgs.setPort(8089);
		   
		  Service service= Service.connect(loginArgs);
		  		  
		   return service;
	  
	  }
	 
}
