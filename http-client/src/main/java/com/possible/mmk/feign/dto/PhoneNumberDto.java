package com.possible.mmk.feign.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class PhoneNumberDto {

    private Long id;
    private String number;

    private UserDto account;
}
