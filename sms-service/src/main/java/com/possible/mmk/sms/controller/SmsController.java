package com.possible.mmk.sms.controller;

import com.possible.mmk.sms.dto.SmsDto;
import com.possible.mmk.sms.dto.ResponseDto;
import com.possible.mmk.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 * @author Abayomi
 */

@Slf4j
@RestController
@RequestMapping("api/v1/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("inbound/service")
    public ResponseEntity<ResponseDto> inboundSms(@RequestBody @Valid SmsDto smsDto,  @RequestHeader(value = "user_id") String username) {

        ResponseDto responseDto = smsService.sendInboundSms(smsDto, username);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("outbound/service")
    public ResponseEntity<ResponseDto> outboundSms(@RequestBody @Valid SmsDto smsDto, @RequestHeader(value = "user_id") String username){
        ResponseDto responseDto = smsService.sendOutboundSms(smsDto, username);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
