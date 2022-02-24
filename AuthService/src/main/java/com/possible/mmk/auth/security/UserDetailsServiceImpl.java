package com.possible.mmk.auth.security;

import com.possible.mmk.auth.model.AppUser;
import com.possible.mmk.auth.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Abayomi
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final AppUserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUserByUsername = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("Username: " + username + " not found"));

        Collection<GrantedAuthority> grantedAuthorities = Stream.of(appUserByUsername.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(appUserByUsername.getUsername(), appUserByUsername.getPassword(), grantedAuthorities);

    }

}
