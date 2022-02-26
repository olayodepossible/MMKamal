package com.possible.mmk.sms.service;

import com.possible.mmk.feign.UserAuthClient;
import com.possible.mmk.feign.dto.PhoneNumberDto;
import com.possible.mmk.feign.dto.UserDto;
import com.possible.mmk.sms.dto.ResponseDto;
import com.possible.mmk.sms.dto.SmsDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import redis.embedded.RedisServer;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@Slf4j
class SmsServiceImplTest {


    @Autowired
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();


    }

    @Test
    void testSmsRequestValidData() {

        SmsDto smsDto = new SmsDto();
        smsDto.setFrom("49241955090008");
        smsDto.setTo("4924195509196");
        smsDto.setText("hello");
        Set<ConstraintViolation<SmsDto>> violations = validator.validate(smsDto);
        assertNotEquals(0, smsDto.getText().length());
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSmsToRequestIsInValid() {

        SmsDto smsDto = new SmsDto();
        smsDto.setFrom("49241955090008");
        smsDto.setTo("492");
        smsDto.setText("hello");
        Set<ConstraintViolation<SmsDto>> violations = validator.validate(smsDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSmsFromRequestIsInValid() {

        SmsDto smsDto = new SmsDto();
        smsDto.setFrom("");
        smsDto.setTo("4924195509196");
        smsDto.setText("hello");
        Set<ConstraintViolation<SmsDto>> violations = validator.validate(smsDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSmsRequestIsInValid() {

        SmsDto smsDto = new SmsDto();
        smsDto.setFrom("492419550900086899989895975475");
        smsDto.setTo("4924195509196298453534093849333879059357");
        smsDto.setText("hello");
        Set<ConstraintViolation<SmsDto>> violations = validator.validate(smsDto);
        assertFalse(violations.isEmpty());
    }




}