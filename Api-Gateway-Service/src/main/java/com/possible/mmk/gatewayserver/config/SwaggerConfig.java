package com.possible.mmk.gatewayserver.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class SwaggerConfig  implements SwaggerResourcesProvider {

        @Override
        public List get() {
            List resources = new ArrayList();
            resources.add(swaggerResource("sms-service", "/api/v1/users", "1.0"));
            resources.add(swaggerResource("auth-service", "/api/v1/auth/users", "1.0"));
            return resources;
        }

        private SwaggerResource swaggerResource(String name, String location, String version) {
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setName(name);
            swaggerResource.setLocation(location);
            swaggerResource.setSwaggerVersion(version);
            return swaggerResource;
        }


}
