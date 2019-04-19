package com.emcc.deviceManager;

import static springfox.documentation.builders.PathSelectors.regex;


import io.swagger.annotations.Api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
public class Swagger2 {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.emcc.*"))
				.paths(PathSelectors.any()).build();
	}

	@SuppressWarnings("deprecation")
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("设备管理接口")
				.description("920项目接口")
				.termsOfServiceUrl("")
				.contact("").version("1.0.0-SNAPSHOT").build();
	}
	

	@Bean
	public Docket testDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(regex("/test/.*")).build();
		docket.apiInfo(apiInfo());
		docket.groupName("测试接口");
		return docket;
	}
	@Bean
	public Docket treeDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(regex("/common/.*")).build();
		docket.apiInfo(apiInfo());
		docket.groupName("通用接口");
		return docket;
	}

	@Bean
	public Docket simulatorDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(regex("/simulator/.*")).build();
		docket.apiInfo(apiInfo());
		docket.groupName("仿真数据接口");
		return docket;
	}
	@Bean
	public Docket proLineDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(regex("/proLine/.*")).build();
		docket.apiInfo(apiInfo());
		docket.groupName("产线接口");
		return docket;
	}
	@Bean
	public Docket deviceDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(regex("/device/.*")).build();
		docket.apiInfo(apiInfo());
		docket.groupName("设备接口");
		return docket;
	}
	@Bean
	public Docket imageDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(regex("/image/.*")).build();
		docket.apiInfo(apiInfo());
		docket.groupName("图片处理接口");
		return docket;
	}
	

	@Bean
	public Docket allDocket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.groupName("全部接口")
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/error.*")))
				.paths(Predicates.not(PathSelectors.regex("/auditevents")))
				.paths(Predicates.not(PathSelectors.regex("/auditevents.json")))
				.paths(Predicates.not(PathSelectors.regex("/archaius")))
				.paths(Predicates.not(PathSelectors.regex("/archaius.json")))
				.paths(Predicates.not(PathSelectors.regex("/autoconfig")))
				.paths(Predicates.not(PathSelectors.regex("/autoconfig.json")))
				.paths(Predicates.not(PathSelectors.regex("/beans")))
				.paths(Predicates.not(PathSelectors.regex("/beans.json")))
				.paths(Predicates.not(PathSelectors.regex("/configprops")))
				.paths(Predicates.not(PathSelectors.regex("/configprops.json")))
				.paths(Predicates.not(PathSelectors.regex("/dump")))
				.paths(Predicates.not(PathSelectors.regex("/dump.json")))
				.paths(Predicates.not(PathSelectors.regex("/features")))
				.paths(Predicates.not(PathSelectors.regex("/features.json")))
				.paths(Predicates.not(PathSelectors.regex("/info")))
				.paths(Predicates.not(PathSelectors.regex("/info.json")))
				.paths(Predicates.not(PathSelectors.regex("/mappings")))
				.paths(Predicates.not(PathSelectors.regex("/mappings.json")))
				.paths(Predicates.not(PathSelectors.regex("/channels")))
				.paths(Predicates.not(PathSelectors.regex("/channels.json")))
				.paths(Predicates.not(PathSelectors.regex("/trace")))
				.paths(Predicates.not(PathSelectors.regex("/trace.json")))
				.build();
		return docket;
	}
}
