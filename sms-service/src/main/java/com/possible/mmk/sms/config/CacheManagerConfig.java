/*
 * Copyright (C) One Finance & Investments Ltd - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by OneFi Developers <developers@onefi.co>, 2019
 */
package com.possible.mmk.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 *
 * @author Abayomi
 */
@ConfigurationProperties("cache-manager")
@Data
@Validated
@Component
public class CacheManagerConfig {

    private String type = "redis";

    private Map<String, Long> cachesTtl;
}
