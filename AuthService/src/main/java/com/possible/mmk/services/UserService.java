package com.possible.mmk.services;

import com.possible.mmk.model.AppUser;
import com.possible.mmk.model.request.RegistrationRequest;

public interface UserService {
    AppUser createNewUser(RegistrationRequest request);
}
