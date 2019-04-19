package com.emcc.deviceManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: renpeng
 * @email: renp90@qq.com
 * @createTime: 2018/5/21 11:51
 * @description:
 */
@Configuration
@ConditionalOnClass(value = {RestTemplate.class, HttpClient.class})
public class RestTemplateConfiguration {
    @Value("${remote.maxTotalConnect:0}")
    private int maxTotalConnect; //连接池的最大连接数默认为0
    @Value("${remote.maxConnectPerRoute:200}")
    private int maxConnectPerRoute; //单个主机的最大连接数
    @Value("${remote.connectTimeout:2000}")
    private int connectTimeout; //连接超时默认2s
    @Value("${remote.readTimeout:30000}")
    private int readTimeout; //读取超时默认30s
    @Value("${requestSentRetryEnabled:false}")
    private boolean requestSentRetryEnabled;//是否开启重试
    @Value("${retryCount:2}")
    private int retryCount; //重试次数


    private ClientHttpRequestFactory createHttpFactory() {
        if (this.maxTotalConnect <= 0) {
            SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
            simpleClientHttpRequestFactory.setConnectTimeout(this.connectTimeout);
            simpleClientHttpRequestFactory.setReadTimeout(this.readTimeout);
            return simpleClientHttpRequestFactory;
        }
        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(retryCount, requestSentRetryEnabled);
        CloseableHttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(this.maxTotalConnect)
                .setMaxConnPerRoute(this.maxConnectPerRoute).setRetryHandler(retryHandler).build();
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(this.connectTimeout);
        httpComponentsClientHttpRequestFactory.setReadTimeout(this.readTimeout);
        return httpComponentsClientHttpRequestFactory;

    }
    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(this.createHttpFactory());
        return restTemplate;
    }

}
