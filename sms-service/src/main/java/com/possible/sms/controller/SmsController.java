package com.possible.sms.controller;

import com.possible.sms.model.dto.ResponseDto;
import com.possible.sms.model.dto.SmsDto;
import com.possible.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("inbound/sms")
    public ResponseEntity<ResponseDto> inboundSms(@RequestBody @Valid SmsDto smsDto){
        ResponseDto responseDto = smsService.sendInboundSms(smsDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("outbound/sms")
    public ResponseEntity<ResponseDto> outboundSms(@RequestBody @Valid SmsDto smsDto){
        ResponseDto responseDto = smsService.sendOutboundSms(smsDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
