package com.possible.mmk.sms.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class SmsDto implements Serializable {

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
