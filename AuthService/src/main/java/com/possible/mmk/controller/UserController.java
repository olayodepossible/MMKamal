package com.possible.mmk.controller;

import com.possible.mmk.model.AppUser;
import com.possible.mmk.model.request.RegistrationRequest;
import com.possible.mmk.model.response.AppResponse;
import com.possible.mmk.repository.AppUserRepository;
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

        private final PasswordEncoder bCryptPasswordEncoder;

        private final AppUserRepository appUserRepository;

        @PostMapping(value = "/create")
        public ResponseEntity<AppResponse> createUser(@RequestBody RegistrationRequest userRegistrationRequest){
            AppUser appUser = new AppUser();
            appUser.setPassword(bCryptPasswordEncoder.encode(userRegistrationRequest.getPassword()));
            appUser.setUsername(userRegistrationRequest.getUserName());
            appUser.setRoles("ROLE_USER");
            appUserRepository.save(appUser);
            return new ResponseEntity(AppResponse.builder().message("User Registration Successfully Completed").data(appUser).build(), HttpStatus.CREATED);
        }


}
