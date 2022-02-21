package com.possible.mmk.services;

import com.possible.mmk.model.AppUser;
import com.possible.mmk.model.request.RegistrationRequest;
import com.possible.mmk.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AppUser createNewUser(RegistrationRequest request) {
        AppUser appUser = new AppUser();
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setUsername(request.getUserName());
        appUser.setRoles("ROLE_USER");
       return userRepository.save(appUser);
    }
}
