package io.prathyusha.depempmvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


@SuppressWarnings("deprecation")
@Configuration
public class Internationalization extends WebMvcConfigurerAdapter {
	
	@Autowired
	  private Interceptor interceptor;
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:message");
		return messageSource;
	}
	
	 @Bean
	   public LocaleResolver localeResolver() {
	     
		 return new CookieLocaleResolver();
	  }
	 
	   @Bean
	   public LocaleChangeInterceptor localeChangeInterceptor() {
	      LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	      localeChangeInterceptor.setParamName("siteLanguage");
	      return localeChangeInterceptor;
	   }
	   
	   @Override
	   public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(localeChangeInterceptor());
	      registry.addInterceptor(interceptor);
	   }

}


