package com.possible.mmk.auth.controller;

import com.possible.mmk.auth.model.AppUser;
import com.possible.mmk.auth.model.request.RequestDto;
import com.possible.mmk.auth.model.response.AuthResponse;
import com.possible.mmk.auth.services.UserService;
import com.possible.mmk.auth.model.response.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author Abayomi
 */

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
    public ResponseEntity<AuthResponse> loginUser(@RequestBody RequestDto request) {
        AuthResponse response =  userService.userLogin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public  ResponseEntity<AppUser> getUser(@PathVariable("username") String username){
            return new ResponseEntity<>(userService.fetchUser(username), HttpStatus.OK);
    }


}
