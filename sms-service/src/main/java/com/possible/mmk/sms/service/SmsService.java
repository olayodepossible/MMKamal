package com.possible.mmk.sms.service;

import com.possible.mmk.sms.dto.ResponseDto;
import com.possible.mmk.sms.dto.SmsDto;

/**
 *
 * @author Abayomi
 */

public interface SmsService {
    ResponseDto sendInboundSms(SmsDto smsDto, String username);

    ResponseDto sendOutboundSms(SmsDto smsDto, String username);
}
