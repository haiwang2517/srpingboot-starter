package com.scudpower.starter.redis.autconfigure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** redis 配置 */
@Configuration
public class RedisConfig {

  @Bean("redisTemplate")
  @ConditionalOnClass(RedisOperations.class)
  @ConditionalOnMissingBean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(
        LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
    javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
    javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
    javaTimeModule.addDeserializer(
        LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
    javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
    javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(javaTimeModule);
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.activateDefaultTyping(
        objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);

    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    redisTemplate.setKeySerializer(stringRedisSerializer);
    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}
