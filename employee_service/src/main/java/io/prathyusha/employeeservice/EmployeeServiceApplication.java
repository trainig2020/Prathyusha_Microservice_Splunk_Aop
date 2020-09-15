package io.prathyusha.employeeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

import io.prathyusha.aopLogging.aspects.EmployeeLoggingAspect;

@SpringBootApplication
@EnableEurekaClient
@RefreshScope
@Import(EmployeeLoggingAspect.class)
public class EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

}
