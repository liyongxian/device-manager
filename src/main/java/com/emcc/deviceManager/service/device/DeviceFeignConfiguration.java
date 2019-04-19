package com.emcc.deviceManager.service.device;

import com.emcc.deviceManager.service.fallback.DeviceFeignFallback;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class DeviceFeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverter;

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }

    @Bean
    public DeviceFeignFallback exportNodesFallback() {
        return new DeviceFeignFallback();
    }

    @Bean
    @Primary
    @Scope("prototype")
    public Encoder feignEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverter));
    }

    @Bean
    @Primary
    @Scope("prototype")
    public Decoder feignDecoder() {
        return  new SpringDecoder(messageConverter);
    }
}
