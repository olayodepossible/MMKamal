package com.possible.mmk.services;

import com.possible.mmk.model.AppUser;
import com.possible.mmk.model.request.RequestDto;
import com.possible.mmk.model.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    AppUser createNewUser(RequestDto request);

    AuthResponse createJwtToken(RequestDto request) throws Exception;
}
