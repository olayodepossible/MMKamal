package com.possible.mmk.auth.services;

import com.possible.mmk.auth.model.request.RequestDto;
import com.possible.mmk.auth.model.response.AuthResponse;
import com.possible.mmk.auth.model.AppUser;

import java.util.Optional;

/**
 *
 * @author Abayomi
 */
public interface UserService {
    AppUser createNewUser(RequestDto request);

    AuthResponse userLogin(RequestDto request);

    AppUser fetchUser(String username);
}
