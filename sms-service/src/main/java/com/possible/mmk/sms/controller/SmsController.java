package com.possible.mmk.sms.controller;

import com.possible.mmk.sms.model.dto.SmsDto;
import com.possible.mmk.sms.service.SmsService;
import com.possible.mmk.sms.model.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("inbound/service")
    public ResponseEntity<ResponseDto> inboundSms(@RequestBody @Valid SmsDto smsDto){
        ResponseDto responseDto = smsService.sendInboundSms(smsDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("outbound/service")
    public ResponseEntity<ResponseDto> outboundSms(@RequestBody @Valid SmsDto smsDto){
        ResponseDto responseDto = smsService.sendOutboundSms(smsDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
