package com.possible.controller;

import com.possible.model.AppUser;
import com.possible.model.AppUserRole;
import com.possible.model.request.RegistrationRequest;
import com.possible.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( "api/v1/users")
public class UserController {


        @Autowired
        BCryptPasswordEncoder bCryptPasswordEncoder;

        @Autowired
        AppUserRepository appUserRepository;

        @PostMapping(value = "/create")
        public ResponseEntity createUser(@RequestBody RegistrationRequest userRegistrationRequest){
            AppUser appUser = new AppUser();
            appUser.setPassword(bCryptPasswordEncoder.encode(userRegistrationRequest.getPassword()));
            appUser.setUsername(userRegistrationRequest.getUserName());
            List<AppUserRole> appUserRoles = new ArrayList<>();
            appUserRoles.add(AppUserRole.builder().roleName("ROLE_ADMIN").appUser(appUser).build());
            appUserRoles.add(AppUserRole.builder().roleName("ROLE_NEWS").appUser(appUser).build());
            appUser.setAppUserRoleList(appUserRoles);
            appUserRepository.save(appUser);
            return ResponseEntity.ok("User Registration Successfully Completed");
        }


}
