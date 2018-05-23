package com.aethercoder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
//@ConfigurationProperties(prefix = "http.header")
public class CorsConfig{

    private String[] allowedOrigin = {"*"};
    private String[] allowedMethod = {"*"};
    private String[] allowedHeader = {"X-Requested-With","Content-Type","Authorization","X-T","X-AD","X-TS","X-S","X-NO-ENC-XXX","X-R","APP-Version","Device-Type","Source-Type"};

    public void setAllowedOrigin(String[] allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
    }

    public void setAllowedMethod(String[] allowedMethod) {
        this.allowedMethod = allowedMethod;
    }

    public void setAllowedHeader(String[] allowedHeader) {
        this.allowedHeader = allowedHeader;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders(allowedHeader)
                        .exposedHeaders(allowedHeader)
                        .allowedMethods(allowedMethod)
                        .allowedOrigins(allowedOrigin);
            }
        };
    }
}
