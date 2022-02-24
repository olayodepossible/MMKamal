package com.possible.mmk.auth.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Abayomi
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private Boolean isSuccess;
    private String message;
    private String accessToken;
    private String refreshToken;


}
