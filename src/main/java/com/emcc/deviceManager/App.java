package com.emcc.deviceManager;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * Hello world!
 *
 */
@SpringBootApplication
//@EnableDiscoveryClient
@ServletComponentScan
@EnableFeignClients
@EnableAsync
//@EnableCaching
public class App 
{
	
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);    	
    }
}
