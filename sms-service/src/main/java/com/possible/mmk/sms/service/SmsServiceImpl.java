package com.possible.mmk.sms.service;

import com.possible.mmk.feign.UserAuthClient;
import com.possible.mmk.feign.dto.PhoneNumberDto;
import com.possible.mmk.feign.dto.UserDto;
import com.possible.mmk.sms.config.CacheManagerConfig;
import com.possible.mmk.sms.dto.ResponseDto;
import com.possible.mmk.sms.dto.SmsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{

    private final String OBJ_KEY = "SMS";
    private final int API_CALL_LIMIT = 50;
    private final UserAuthClient userAuthClient;
    private final CacheManager cacheManager;



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
            cacheSmsData(smsDto, username);
            return ResponseDto.builder().message("data cached successfully").error("").build();
        }

        return ResponseDto.builder().message("inbound sms ok").error("").build();
    }


    @Override
    public ResponseDto sendOutboundSms(SmsDto smsDto, String username) {
        String dataKey = username +smsDto.getFrom();
        Integer apiCalls = 0;
        boolean hasKey = cacheManager.getCache(smsDto.getFrom()) != null;
        if (hasKey ){
            log.info("HASKEY ***** - {}", apiCalls);
            if(apiCalls < API_CALL_LIMIT){
                ++apiCalls;
                cacheManager.getCache(smsDto.getFrom()).put(smsDto.getFrom(), apiCalls);
                log.info("HASKEY-api ***** {}  ttl ****", apiCalls);
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
            cacheManager.getCache(smsDto.getFrom()).putIfAbsent(smsDto.getFrom(), apiCalls);
            log.info("NO-HASKEY **** {} *****", apiCalls);
        }

        Collection<String> mapDto = cacheManager.getCacheNames();
        Cache cacheDto = cacheManager.getCache(OBJ_KEY);
        mapDto.forEach(e -> log.info("{} - ***** {}", e, cacheDto.getName()));
        SmsDto cacheData = cacheSmsData(smsDto, username);
        log.info("********* DATA ****** {}", cacheData);
//        boolean result = mapDto.entrySet().stream()
//                .filter(x -> smsDto.getTo().equals(x.getValue()) || smsDto.getFrom().equals(x.getValue()))
//                .findFirst().isEmpty();
//        if (!result){
//            return ResponseDto.builder()
//                    .message("")
//                    .error(String.format("sms from '%s' to '%s' blocked by STOP request",smsDto.getFrom(), smsDto.getTo()))
//                    .build();
//        }

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

    @Cacheable(value = "usersCache", key = "#username")
    public List<PhoneNumberDto> fetchPhoneNumbers(String username){
        log.info("fetchPhoneNumbers **** {} ", username);
        UserDto dto = userAuthClient.getUser(username);
//        if (hashOperations.entries(objKey).isEmpty()){
//
//            hashOperations.put(objKey, username, dto.getPhoneNumbers());
//            redisTemplate.expire(objKey, 70, TimeUnit.SECONDS);
//            return dto.getPhoneNumbers();
//        }

        return dto.getPhoneNumbers();
    }

    @Cacheable(value = OBJ_KEY, key = "#username")
    public SmsDto cacheSmsData(SmsDto dto, String username){
        return dto;
    }


    @Cacheable(value = "api_calls", key = "#username", condition = "#smsDto.getFrom()")
    public SmsDto apiCallThreshold(SmsDto dto, String username){
        return dto;
    }


}
