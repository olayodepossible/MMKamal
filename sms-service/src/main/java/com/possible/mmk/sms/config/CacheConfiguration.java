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
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Abayomi
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(value = {RedisProperties.class})
public class CacheConfiguration {

    @Bean
    @Autowired
    @ConditionalOnProperty(name = "cache-manager.type", havingValue = "redis")
    public CacheManager redisCacheManager(RedisProperties redisProperties, CacheManagerConfig cacheConfig) {
        log.debug("Initializing Redis Manager");
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();

        String schema = redisProperties.isSsl() ? "rediss://" : "redis://";
        singleServerConfig.setAddress(schema + redisProperties.getHost() + ":" + redisProperties.getPort());
        log.info("*******************{}", singleServerConfig.getAddress());
        singleServerConfig.setDatabase(redisProperties.getDatabase());
        if (redisProperties.getPassword() != null) {
            singleServerConfig.setPassword(redisProperties.getPassword());
        }

        RedissonClient redissonClient = Redisson.create(config);
        RedissonSpringCacheManager cacheManager = new RedissonSpringCacheManager(redissonClient);

        cacheManager.setAllowNullValues(false);
        if (cacheConfig.getCachesTtl() != null && !cacheConfig.getCachesTtl().isEmpty()) {
            Map<String, CacheConfig> cacheConfigs = new HashMap<>();
            cacheConfig.getCachesTtl().forEach((name, ttl) -> {
                log.debug("Configuring cache for {} -> TTL = {}", name, ttl);
                CacheConfig cConfig = new CacheConfig();
                cConfig.setTTL(ttl);
                cacheConfigs.put(name, cConfig);
                cacheManager.setConfig(cacheConfigs);
            });
        }
        return cacheManager;
    }
}
