package com.possible.mmk.gatewayserver.config;

import com.possible.mmk.gatewayserver.filters.AuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GateWayConfig {

    @Autowired
    AuthenticationFilter authFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("sms-service", r -> r.path("/api/v1/sms/**")
                        .filters(f -> f.filter(authFilter).removeRequestHeader("Cookie"))
                        .uri("lb://sms-service"))

                .route("auth-service", r -> r.path("/api/v1/auth/users/**")
                        .filters(f -> f.filter(authFilter).removeRequestHeader("Cookie"))
                        .uri("lb://auth-service"))
                .build();

    }
}
