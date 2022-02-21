package com.possible.mmk.controller;

import com.possible.mmk.model.AppUser;
import com.possible.mmk.model.request.RegistrationRequest;
import com.possible.mmk.model.response.AppResponse;
import com.possible.mmk.repository.AppUserRepository;
import com.possible.mmk.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "api/v1/auth/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        @PostMapping(value = "/create")
        public ResponseEntity<AppResponse> createUser(@RequestBody RegistrationRequest request){
             userService.createNewUser(request);
            return new ResponseEntity(AppResponse.builder().message("User Registration Successfully Completed").success(Boolean.TRUE).build(), HttpStatus.CREATED);
        }


}
