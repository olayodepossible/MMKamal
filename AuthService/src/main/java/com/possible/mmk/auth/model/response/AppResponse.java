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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse {
    private Boolean success;
    private String message;
}
