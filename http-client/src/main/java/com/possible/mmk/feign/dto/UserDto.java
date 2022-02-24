package com.possible.mmk.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Abayomi
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String username;
    private String password;

    private String roles;

    private List<PhoneNumberDto> phoneNumbers = new ArrayList<>();
}
