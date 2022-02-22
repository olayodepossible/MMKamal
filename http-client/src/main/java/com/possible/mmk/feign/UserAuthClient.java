package com.possible.mmk.feign;

import com.possible.mmk.feign.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "auth-service", path = "api/v1/auth/users/")
public interface UserAuthClient {

    @GetMapping("{username}")
    UserDto getUser(@PathVariable("username") String username);
}
