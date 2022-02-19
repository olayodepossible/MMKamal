package com.possible.sms.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SmsDto {

    @NotBlank(message = "Sender phone number may not be null")
    @Size(min = 6, max = 16)
    private String from;

    @NotBlank(message = "Receiver phone number may not be null")
    @Size(min = 6, max = 16)
    private String to;

    @NotBlank(message = "Text may not be null")
    @Size(min = 1, max = 120)
    private String text;
}
