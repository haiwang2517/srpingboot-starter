package com.scudpower.starter.framework.autconfigure;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
@ConditionalOnClass(JavaTimeModule.class)
public class JsonConfig {
  @Bean
  @ConditionalOnMissingBean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    return jacksonObjectMapperBuilder ->
        jacksonObjectMapperBuilder
                // Long 转换为 String
            .serializerByType(Long.class, ToStringSerializer.instance)
            .serializerByType(Long.TYPE, ToStringSerializer.instance)
            .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter))
            .serializerByType(LocalDate.class, new LocalDateTimeSerializer(dateFormatter))
            .serializerByType(LocalTime.class, new LocalDateTimeSerializer(timeFormatter))
            .deserializerByType(
                LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter))
            .deserializerByType(LocalDate.class, new LocalDateTimeDeserializer(dateFormatter))
            .deserializerByType(LocalTime.class, new LocalDateTimeDeserializer(timeFormatter));
  }
}
