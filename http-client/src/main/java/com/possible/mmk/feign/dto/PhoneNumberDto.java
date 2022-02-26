package com.possible.mmk.feign.dto;

import lombok.Data;
import java.io.Serializable;

/**
 *
 * @author Abayomi
 */

@Data
public class PhoneNumberDto implements Serializable {
    private static final long serialVersionUID = 2405172041950251807L;

    private Long id;
    private String number;

    private transient UserDto account;
}
