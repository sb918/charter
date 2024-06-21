package com.example.charter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


//////////exclude anntoation addded for Spring Security log in screen
// to make it active again just remove exclude. Some things can be broken
// because we just disabled all auto config

//to disable this, line should be as below
//@SpringBootApplication (exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication (exclude = { SecurityAutoConfiguration.class })

public class CharterApplication   {



	public static void main(String[] args) {
		SpringApplication.run(CharterApplication.class, args);
	}


	@Bean
	Validator validator(){
		return new LocalValidatorFactoryBean();
	}


}
