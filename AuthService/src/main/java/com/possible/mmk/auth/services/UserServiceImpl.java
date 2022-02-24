package com.possible.mmk.auth.services;

import com.possible.mmk.auth.model.request.RequestDto;
import com.possible.mmk.auth.model.response.AuthResponse;
import com.possible.mmk.auth.model.AppUser;
import com.possible.mmk.auth.repository.AppUserRepository;
import com.possible.mmk.auth.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * @author Abayomi
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtUtil;
    private final UserDetailsService userDetailsService;
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
    public AuthResponse userLogin(RequestDto request) {
        AuthResponse response = null;
        try{
           response = createJwtToken(request);
        }
        catch (Exception e){
            response = new AuthResponse(Boolean.FALSE, e.getMessage(), null, null);
        }
        return response;
    }

    @Override
    public AppUser fetchUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    private AuthResponse createJwtToken(RequestDto  jwtRequest) {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getPassword();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        authenticate(userName, userPassword);
        String accessToken = jwtUtil.generate(userDetails, "ACCESS");
        String refreshToken = jwtUtil.generate(userDetails, "REFRESH");
        String message = userDetails.getUsername() + " login successfully";
        return new AuthResponse(Boolean.TRUE, message, accessToken, refreshToken);
    }

    private void authenticate(String userName, String userPassword) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new DisabledException("User is disabled");
        } catch(BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials or you have not registered");
        }
    }

}
