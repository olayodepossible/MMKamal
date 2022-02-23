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
import java.util.stream.Collectors;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService{

    private final String OBJ_KEY = "SMS";
    private final int API_CALL_LIMIT = 50;
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
        List<PhoneNumberDto> numberDtoList = fetchPhoneNumbers(username);

        boolean isEmpty = numberDtoList.stream()
                        .filter(p -> p.getNumber().equals(smsDto.getTo())).findFirst().isEmpty();
        if (isEmpty){
            return ResponseDto.builder()
                    .message("")
                    .error(String.format("'%s' - parameter not found", smsDto.getTo()))
                    .build();
        }

        if(smsDto.getText().trim().equalsIgnoreCase("STOP")){
            hashOperations.put(OBJ_KEY, username, smsDto);
            return ResponseDto.builder().message("data cached successfully").error("").build();
        }

        return ResponseDto.builder().message("inbound sms ok").error("").build();
    }


    @Override
    public ResponseDto sendOutboundSms(SmsDto smsDto, String username) {
        String dataKey = username +smsDto.getFrom();
        Integer apiCalls = 0;
        boolean hasKey = hashOperations.hasKey(dataKey, smsDto.getFrom());

        if (hasKey ){
            Long ttl = redisTemplate.getExpire(dataKey, TimeUnit.SECONDS);
            if(ttl > 0 && apiCalls < API_CALL_LIMIT){
                ++apiCalls;
                hashOperations.put(dataKey, smsDto.getFrom(), apiCalls);
            }
            else {
                return ResponseDto.builder()
                        .message("")
                        .error(String.format("limit reached for from '%s'",smsDto.getFrom()))
                        .build();
            }

        }

        else {
            ++apiCalls;
            hashOperations.put(dataKey, smsDto.getFrom(), apiCalls);
            redisTemplate.expire(dataKey, 120, TimeUnit.SECONDS);
        }

        Map<String, SmsDto> mapDto = hashOperations.entries(OBJ_KEY);
        boolean result = mapDto.entrySet().stream()
                .filter(x -> smsDto.getTo().equals(x.getValue()) || smsDto.getFrom().equals(x.getValue()))
                .findFirst().isEmpty();
        if (!result){
            return ResponseDto.builder()
                    .message("")
                    .error(String.format("sms from '%s' to '%s' blocked by STOP request",smsDto.getFrom(), smsDto.getTo()))
                    .build();
        }

        List<PhoneNumberDto> numberDtoList = fetchPhoneNumbers(username);
        boolean isEmpty = numberDtoList.stream()
                .filter(p -> p.getNumber().equals(smsDto.getFrom())).findFirst().isEmpty();
        if (isEmpty){
            return ResponseDto.builder()
                    .message("")
                    .error(String.format("'%s' - parameter not found", smsDto.getFrom()))
                    .build();
        }





        //TODO

        /*




Output JSON response
If required parameter is missing:
{“message”: “”, “error”: “<parameter_name> is missing”}

If parameter is invalid:
{“message”: “”, “error”: “<parameter_name> is invalid”}


Any unexpected error:
{“message”: “”, “error”: “unknown failure”}

If all parameters are valid:
{“message”: “outbound sms ok”, “error”: “”}

         */
        return ResponseDto.builder().message("outbound sms ok").error("").build();
    }

    private List<PhoneNumberDto> fetchPhoneNumbers(String username){
        String objKey = username.toUpperCase();
        if (hashOperations.entries(objKey).isEmpty()){
            UserDto dto = userAuthClient.getUser(username);
            hashOperations.put(objKey, username, dto.getPhoneNumbers());
            redisTemplate.expire(objKey, 120, TimeUnit.SECONDS);
            return dto.getPhoneNumbers();
        }

        return hashOperations.values(objKey);
    }
}
