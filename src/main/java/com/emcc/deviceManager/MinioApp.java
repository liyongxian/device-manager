package com.emcc.deviceManager;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioApp {
	@Value("${minio-server}")
    private String server;
	@Value("${minio-user}")
    private String user;
	@Value("${minio-password}")
    private String password;
	public MinioClient minioClient = null;
	@Bean
	public  MinioClient initMinio(){
		
		try {
			System.out.println("server: "+server+"  user:  "+user+"  password :  "+password);
			minioClient = new MinioClient(server, user, password);
			
		} catch (InvalidEndpointException e) {
			e.printStackTrace();
		} catch (InvalidPortException e) {
			e.printStackTrace();
		}
		
		return minioClient;
	}
	

}
