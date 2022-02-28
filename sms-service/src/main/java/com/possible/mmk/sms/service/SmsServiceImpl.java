package com.possible.mmk.sms.service;

import com.possible.mmk.feign.UserAuthClient;
import com.possible.mmk.feign.dto.PhoneNumberDto;
import com.possible.mmk.feign.dto.UserDto;
import com.possible.mmk.sms.dto.ResponseDto;
import com.possible.mmk.sms.dto.SmsDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Abayomi
 */

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    private static final String OBJ_KEY = "SMS";
    private static final long THRESHOLD_COUNT_TTL = 86400;
    private static final long STOP_TEXT_TTL = 14400;
    private static final int API_CALL_LIMIT = 50;
    private final UserAuthClient userAuthClient;
    private final RedissonClient redissonClient;

    public SmsServiceImpl(UserAuthClient userAuthClient, RedissonClient redissonClient) {
        this.userAuthClient = userAuthClient;
        this.redissonClient = redissonClient;
    }

    @Override
    public ResponseDto sendInboundSms(SmsDto smsDto, String username) {
        List<PhoneNumberDto> numberDtoList = fetchPhoneNumbers(username);

        boolean isEmpty = numberDtoList.stream()
                .filter(p -> p.getNumber().equals(smsDto.getTo())).findFirst().isEmpty();
        if (isEmpty) {
            return ResponseDto.builder()
                    .message("")
                    .error(String.format("'%s' - parameter not found", smsDto.getTo()))
                    .build();
        }

        if (smsDto.getText().trim().equalsIgnoreCase("STOP")) {
            RMapCache<String, SmsDto> map = redissonClient.getMapCache(OBJ_KEY);
            map.put(username, smsDto, STOP_TEXT_TTL, TimeUnit.SECONDS);
            return ResponseDto.builder().message("data cached successfully").error("").build();
        }

        return ResponseDto.builder().message("inbound sms ok").error("").build();
    }


    @Override
    public ResponseDto sendOutboundSms(SmsDto smsDto, String username) {
        Integer apiCalls = 0;
        String key = smsDto.getFrom();
        RMapCache<String, Integer> thresholdMap = redissonClient.getMapCache(key);
        boolean hasMap = thresholdMap.isEmpty();
        long ttl = thresholdMap.remainTimeToLive(key);
        if (!hasMap) {
            apiCalls = thresholdMap.getWithTTLOnly(key);
            if (ttl < 0 || apiCalls > API_CALL_LIMIT) {
                return ResponseDto.builder()
                        .message("")
                        .error(String.format("limit reached for from '%s'", smsDto.getFrom()))
                        .build();
            }
            ++apiCalls;
            thresholdMap.put(key, apiCalls);

        } else {
            ++apiCalls;
            thresholdMap.put(smsDto.getFrom(), apiCalls, THRESHOLD_COUNT_TTL, TimeUnit.SECONDS);
        }

        RMap<String, SmsDto> cachedSms = redissonClient.getMap(OBJ_KEY);

        boolean hasValue = cachedSms.entrySet().stream()
                .filter(e -> e.getValue().getTo().equals(smsDto.getTo()) || e.getValue().getFrom().equals(smsDto.getFrom()))
                .findFirst().isEmpty();
        if (!hasValue) {
            return ResponseDto.builder()
                    .message("")
                    .error(String.format("sms from '%s' to '%s' blocked by STOP request", smsDto.getFrom(), smsDto.getTo()))
                    .build();
        }

        List<PhoneNumberDto> numberDtoList = fetchPhoneNumbers(username);
        boolean isEmpty = numberDtoList.stream()
                .filter(p -> p.getNumber().equals(smsDto.getFrom())).findFirst().isEmpty();
        if (isEmpty) {
            return ResponseDto.builder()
                    .message("")
                    .error(String.format("'%s' - parameter not found", smsDto.getFrom()))
                    .build();
        }

        return ResponseDto.builder().message("outbound sms ok").error("").build();
    }

    private List<PhoneNumberDto> fetchPhoneNumbers(String username) {
        String objKey = username.toUpperCase();
        RMapCache<Object, List<PhoneNumberDto>> map = redissonClient.getMapCache("phoneData");
        List<PhoneNumberDto>  phoneNumberDtoList = map.getWithTTLOnly(objKey);
        if (phoneNumberDtoList.isEmpty()) {
            UserDto dto = userAuthClient.getUser(username);
            map.put(objKey, dto.getPhoneNumbers(), THRESHOLD_COUNT_TTL, TimeUnit.SECONDS);
            return dto.getPhoneNumbers();
        }
        return phoneNumberDtoList;
    }

}

