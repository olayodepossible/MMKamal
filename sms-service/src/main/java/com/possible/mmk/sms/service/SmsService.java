package com.possible.mmk.sms.service;

import com.possible.mmk.sms.dto.ResponseDto;
import com.possible.mmk.sms.dto.SmsDto;

public interface SmsService {
    ResponseDto sendInboundSms(SmsDto smsDto, String username);

    ResponseDto sendOutboundSms(SmsDto smsDto);
}
