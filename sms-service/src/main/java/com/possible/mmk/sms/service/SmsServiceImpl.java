package com.possible.mmk.sms.service;

import com.possible.mmk.sms.dto.ResponseDto;
import com.possible.mmk.sms.dto.SmsDto;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService{
    @Override
    public ResponseDto sendInboundSms(SmsDto smsDto) {
        //TODO
        /*
        - If the ‘to’ parameter is not present in the phone_number table for this specific account
you used for the basic authentication, return an error (see Output JSON response below).
- When text is STOP or STOP\n or STOP\r or STOP\r\n
- The ‘from’ and ‘to’ pair must be stored in cache as a unique entry and should expire after 4 hours.
Output JSON response
If required parameter is missing:
{“message”: “”, “error”: “<parameter_name> is missing”}
If parameter is invalid:
{“message”: “”, “error”: “<parameter_name> is invalid”}

If ‘to’ is not found in the phone_number table for this account: {“message”: “”, “error”: “to parameter not found”}

Any unexpected error:
{“message”: “”, “error”: “unknown failure”}
If all parameters are valid:
{“message”: “inbound sms ok”, “error”: ””}
         */
        return ResponseDto.builder().message("Available soon").error("no error").build();
    }

    @Override
    public ResponseDto sendOutboundSms(SmsDto smsDto) {

        //TODO

        /*

        - If the pair ‘to’, ‘from’ matches any entry in cache (STOP), return an error (see Output JSON response below).
- Using cache, do not allow more than 50 API requests using the same ‘from’ number in
24 hours from the first use of the ‘from’ number and reset counter after 24 hours. Return
an error in case of limit reached (see Output JSON response below).
- If the ‘from’ parameter is not present in the phone_number table for this specific account
you used for the basic authentication, return an error (see Output JSON response below).

Output JSON response
If required parameter is missing:
{“message”: “”, “error”: “<parameter_name> is missing”}

If parameter is invalid:
{“message”: “”, “error”: “<parameter_name> is invalid”}

If the pair ‘to’, ‘from’ matches any entry in cache (STOP):
{“message”: “”, “error”: “sms from <from> to <to> blocked by STOP request”}

If ‘from’’ is not found in the phone_number table for this account: {“message”: “”, “error”: “from parameter not found”}

If 50 requests in last 24 hours with same ‘from’ parameter: {“message”: “”, “error”: “limit reached for from <from>”}

Any unexpected error:
{“message”: “”, “error”: “unknown failure”}

If all parameters are valid:
{“message”: “outbound sms ok”, “error”: “”}

         */
        return ResponseDto.builder().message("Available soon").error("no error").build();
    }
}
