package com.possible.mmk.controller;

import com.possible.mmk.model.request.RequestDto;
import com.possible.mmk.model.response.AppResponse;
import com.possible.mmk.model.response.AuthResponse;
import com.possible.mmk.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "api/v1/auth/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        @PostMapping(value = "/register")
        public ResponseEntity<AppResponse> createUser(@RequestBody RequestDto request){
             userService.createNewUser(request);
            return new ResponseEntity<>(AppResponse.builder().message("User Registration Successfully Completed").success(Boolean.TRUE).build(), HttpStatus.CREATED);
        }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody RequestDto request) throws Exception {
        AuthResponse response =  userService.createJwtToken(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
