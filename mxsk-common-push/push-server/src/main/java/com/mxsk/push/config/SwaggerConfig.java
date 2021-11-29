package com.mxsk.push.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: zhengguican
 * create in 2021/5/26 17:48
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        Contact contact = new Contact("zhengguican", "http://mxsk", "ronaldocan@163.com");
        return new ApiInfoBuilder()
                .title("短信服务")
                .description("短信服务")
                .contact(contact)
                .version("1.0.0")
                .build();
    }
}