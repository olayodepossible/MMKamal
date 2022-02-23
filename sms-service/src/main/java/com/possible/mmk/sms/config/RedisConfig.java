package com.possible.mmk.sms.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisStandAloneConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("127.0.0.1", 6379));
    }



    @Bean
    public RedisTemplate<String, Map<String, Object>> redisTemplateStandAlone(@Qualifier("redisStandAloneConnectionFactory")LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Map<String, Object>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
