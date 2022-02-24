package com.possible.mmk.sms.dto;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Abayomi
 */

@Data
@Builder
public class ResponseDto {
    private String message;
    private String error;
}
