package com.possible.mmk.feign.dto;

import lombok.Data;
import java.io.Serializable;

/**
 *
 * @author Abayomi
 */

@Data
public class PhoneNumberDto implements Serializable {

    private Long id;
    private String number;

    private UserDto account;
}
