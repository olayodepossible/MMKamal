package com.possible.mmk.sms.service;

import com.possible.mmk.feign.UserAuthClient;
import com.possible.mmk.feign.dto.PhoneNumberDto;
import com.possible.mmk.feign.dto.UserDto;
import com.possible.mmk.sms.dto.ResponseDto;
import com.possible.mmk.sms.dto.SmsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService{

    private final UserAuthClient userAuthClient;
    private final RedisTemplate<String, Map<String, Object>> redisTemplate;
    private HashOperations hashOperations;

    public SmsServiceImpl(UserAuthClient userAuthClient, RedisTemplate<String, Map<String, Object>> redisTemplateStandAlone) {
        this.userAuthClient = userAuthClient;
        this.redisTemplate = redisTemplateStandAlone;
        hashOperations = redisTemplate.opsForHash();
    }



    @Override
    public ResponseDto sendInboundSms(SmsDto smsDto, String username) {
        UserDto dto = userAuthClient.getUser(username);
        List<PhoneNumberDto> numberDtoList = dto.getPhoneNumbers();

        Optional<PhoneNumberDto> phoneNumberObject = numberDtoList.stream()
                        .filter(p -> p.getNumber().equals(smsDto.getTo())).findFirst();
        if (phoneNumberObject.isEmpty()){
            return ResponseDto.builder().message("").error(String.format("'%s' - parameter not found", smsDto.getTo())).build();
        }

        if(smsDto.getText().trim().equalsIgnoreCase("STOP")){
            hashOperations.put("SMS", dto.getId(), smsDto);
            return ResponseDto.builder().message("data cached successfully").error("").build();
        }

        return ResponseDto.builder().message("inbound sms ok").error("").build();
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
