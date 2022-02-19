package com.possible.sms.service;

import com.possible.sms.model.dto.ResponseDto;
import com.possible.sms.model.dto.SmsDto;

public interface SmsService {
    ResponseDto sendInboundSms(SmsDto smsDto);

    ResponseDto sendOutboundSms(SmsDto smsDto);
}
