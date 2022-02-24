/*
 * Copyright (C) One Finance & Investments Ltd - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by OneFi Developers <developers@onefi.co>, 2019
 */
package com.possible.mmk.sms.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.KryoCodec;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *
 * @author Abayomi
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(value = {RedisProperties.class})
public class CacheConfiguration {

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        log.debug("Initializing Redis Manager");
        Config config = new Config();
        config.setCodec( new SerializationCodec())
                .useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setDatabase(redisProperties.getDatabase());


        return Redisson.create(config);
    }

}
