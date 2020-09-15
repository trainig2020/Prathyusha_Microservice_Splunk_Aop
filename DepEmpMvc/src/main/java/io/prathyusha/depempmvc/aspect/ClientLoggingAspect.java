
  package io.prathyusha.depempmvc.aspect;
  
  import java.util.Arrays;
  
  import org.aspectj.lang.JoinPoint;
  import org.aspectj.lang.ProceedingJoinPoint; 
  import org.aspectj.lang.annotation.After; 
  import org.aspectj.lang.annotation.Aspect; 
  import org.aspectj.lang.annotation.Before; 
  import org.aspectj.lang.annotation.Pointcut; 
  import org.springframework.stereotype.Component;
  import com.splunk.Receiver; import com.splunk.Service;
  import io.prathyusha.depempmvc.controller.SplunkConnection;
  
  
  @Aspect
  @Component
  public class ClientLoggingAspect {
  
	  Service service;
	     
	  Receiver receive;
	 
	  
	  @Pointcut("within(@org.springframework.stereotype.Repository *)" +
	        " || within(@org.springframework.stereotype.Service *)" +
	        " || within(@org.springframework.web.bind.annotation.RestController *)")
	    public void springBeanPointcut() {
	       
	    }

	  
	    
	    @Pointcut("within(io.prathyusha.depempmvc..*)" +
		        " || within(io.prathyusha.depempmvc.service..*)" +
		        " || within(io.prathyusha.depempmvc.dao..*)" +
		        " || within(io.prathyusha.depempmvc.controller..*)")
		    public void applicationPackagePointcut() {
		      
		    } 
		  
	
	  @Before("applicationPackagePointcut() && springBeanPointcut()")
	    public void beforeAdviceMethods(JoinPoint joinPoint) {
		  service = SplunkConnection.getService();
		  receive= service.getReceiver();
	      String pacakage = "Before method:" + joinPoint.getSignature().getDeclaringTypeName();
	      String method= joinPoint.getSignature().getName();
	      String args=Arrays.toString(joinPoint.getArgs());
	       receive.log("main", pacakage+method+args);
	    }
	    
	  @After("applicationPackagePointcut() && springBeanPointcut()")
	  public void afterAdviceMethods(JoinPoint joinPoint) {
		  service = SplunkConnection.getService();
		  receive= service.getReceiver();
	      String pacakage = "After method:" + joinPoint.getSignature().getDeclaringTypeName();
	      String method=joinPoint.getSignature().getName();
	      receive.log("main", pacakage+method);
	  }
	  
  }
	


  
  
  
  
  
 