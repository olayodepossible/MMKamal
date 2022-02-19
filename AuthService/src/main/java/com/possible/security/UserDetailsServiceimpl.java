package com.possible.security;

import com.possible.model.AppUser;
import com.possible.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private final BCryptPasswordEncoder encoder;


    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUserByUsername = appUserRepository.findAppUserByUsername(username);
        if(appUserByUsername != null){

            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            appUserByUsername.getAppUserRoleList().stream().forEach(appUserRole -> {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(appUserRole.getRoleName());
                grantedAuthorities.add(grantedAuthority);
            });

            return new User(appUserByUsername.getUsername(), appUserByUsername.getPassword(), grantedAuthorities);
        }
        // If user not found. Throw this exception.
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }


}

