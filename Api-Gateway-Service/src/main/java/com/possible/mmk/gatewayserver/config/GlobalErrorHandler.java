package com.possible.mmk.gatewayserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 *
 * @author Abayomi
 */

@Component
@Slf4j
public class GlobalErrorHandler extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Map<String, Object> map = super.getErrorAttributes(request, options);
        map.put("status", HttpStatus.FORBIDDEN);
        map.put("error_code", HttpStatus.FORBIDDEN.value());
        map.put("message", "Authorization header is invalid");
        return map;
    }


}
