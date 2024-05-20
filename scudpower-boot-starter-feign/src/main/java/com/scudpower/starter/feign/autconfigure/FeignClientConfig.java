package com.scudpower.starter.feign.autconfigure;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

/**
 * @author D1 Hai YinLong
 * @date 2022/4/27 15:46
 */
@Configuration
public class FeignClientConfig implements RequestInterceptor {

  @Bean
  @ConditionalOnMissingBean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.HEADERS;
  }

  @Override
  @ConditionalOnMissingBean
  public void apply(RequestTemplate requestTemplate) {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    Enumeration<String> authorization = attributes.getRequest().getHeaders("Authorization");
    if (authorization.hasMoreElements()) {
      requestTemplate.header("Authorization", authorization.nextElement());
    }
  }
}
