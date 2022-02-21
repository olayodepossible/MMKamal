package com.possible.mmk.sms.service;

import com.possible.mmk.sms.model.dto.SmsDto;
import com.possible.mmk.sms.model.dto.ResponseDto;

public interface SmsService {
    ResponseDto sendInboundSms(SmsDto smsDto);

    ResponseDto sendOutboundSms(SmsDto smsDto);
}
