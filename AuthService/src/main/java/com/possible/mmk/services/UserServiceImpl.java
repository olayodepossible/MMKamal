package com.possible.mmk.services;

import com.possible.mmk.model.AppUser;
import com.possible.mmk.model.request.RequestDto;
import com.possible.mmk.model.response.AuthResponse;
import com.possible.mmk.repository.AppUserRepository;
import com.possible.mmk.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService{

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtUtil;
    @Lazy
    private final AuthenticationManager authenticationManager;


    @Override
    public AppUser createNewUser(RequestDto request) {
        AppUser appUser = new AppUser();
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setUsername(request.getUserName());
        appUser.setRoles("ROLE_USER");
       return userRepository.save(appUser);
    }

    @Override
    public AuthResponse createJwtToken(RequestDto  jwtRequest) throws Exception{
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getPassword();
        final UserDetails userDetails = loadUserByUsername(userName);
        authenticate(userName, userPassword);
        String accessToken = jwtUtil.generate(userDetails, "ACCESS");
        String refreshToken = jwtUtil.generate(userDetails, "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUserByUsername = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("Username: " + username + " not found"));

        Collection<GrantedAuthority> grantedAuthorities = Stream.of(appUserByUsername.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(appUserByUsername.getUsername(), appUserByUsername.getPassword(), grantedAuthorities);

    }

    private void authenticate(String userName, String userPassword) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new DisabledException("User is disabled");
        } catch(BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials or you have not registered");
        }
    }

}
