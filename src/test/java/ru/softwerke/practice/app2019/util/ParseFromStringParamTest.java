package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.WebApplicationException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ParseFromStringParamTest extends StringParamTest {
    private static final int REPEAT_COUNT = 100;
    
    @Test
    void correctLongStringParamTest() {
        
        ParseFromStringParam<Long> param = new ParseFromStringParam<>(
                "   12312311435346   ",
                "totalPrice",
                ParseFromStringParam.PARSE_LONG_FUN,
                ParseFromStringParam.POSITIVE_NUMBER_FORMAT
        );
        Long value = param.getParsedValue();
        assertEquals(new Long(12312311435346L), value, "longs are not equal");
    }
    
    @RepeatedTest(value = REPEAT_COUNT, name = "adding {currentRepetition} / {totalRepetitions}")
    void correctRandomLongStringParamTest() {
        String randomLongByString = RandomStringUtils.random(10, false, true);
        ParseFromStringParam<Long> param = new ParseFromStringParam<>(
                randomLongByString,
                "totalPrice",
                ParseFromStringParam.PARSE_LONG_FUN,
                ParseFromStringParam.POSITIVE_NUMBER_FORMAT
        );
        Long value = param.getParsedValue();
        assertEquals(Long.valueOf(randomLongByString), value, "longs are not equal");
    }
    
    @Test
    void incorrectLongStringParamTest() {
        try {
            ParseFromStringParam<Long> param = new ParseFromStringParam<>(
                    "   123123114asd35346   ",
                    "totalPrice",
                    ParseFromStringParam.PARSE_LONG_FUN,
                    ParseFromStringParam.POSITIVE_NUMBER_FORMAT
            );
            Long value = param.getParsedValue();
            Assert.fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void incorrectNonPositiveLongStringParamTest() {
        try {
            ParseFromStringParam<Long> param = new ParseFromStringParam<>(
                    "   -12312311435346   ",
                    "totalPrice",
                    ParseFromStringParam.PARSE_LONG_FUN,
                    ParseFromStringParam.POSITIVE_NUMBER_FORMAT
            );
            Long value = param.getParsedValue();
            Assert.fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void correctDateStringParamTest() {
        
        ParseFromStringParam<LocalDate> param = new ParseFromStringParam<>(
                " 22.12.2010   ",
                "birthdate",
                ParseFromStringParam.PARSE_DATE_FUN,
                ParseFromStringParam.DATE_FORMAT
        );
        LocalDate value = param.getParsedValue();
        assertEquals(LocalDate.parse("22.12.2010", ParseFromStringParam.dateFormatter), value, "dates are not equal");
    }
    
    @Test
    void incorrectDateStringParamTest() {
        try {
            ParseFromStringParam<LocalDate> param = new ParseFromStringParam<>(
                    " 2as2.12.2010   ",
                    "birthdate",
                    ParseFromStringParam.PARSE_DATE_FUN,
                    ParseFromStringParam.DATE_FORMAT
            );
            LocalDate value = param.getParsedValue();
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void incorrectFormatDateStringParamTest() {
        try {
            ParseFromStringParam<LocalDate> param = new ParseFromStringParam<>(
                    " 22/12/2010   ",
                    "birthdate",
                    ParseFromStringParam.PARSE_DATE_FUN,
                    ParseFromStringParam.DATE_FORMAT
            );
            LocalDate value = param.getParsedValue();
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void incorrectFutureDateStringParamTest() {
        try {
            ParseFromStringParam<LocalDate> param = new ParseFromStringParam<>(
                    " 22.12.99999   ",
                    "birthdate",
                    ParseFromStringParam.PARSE_DATE_FUN,
                    ParseFromStringParam.DATE_FORMAT
            );
            LocalDate value = param.getParsedValue();
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void correctDateTimeStringParamTest() {
        
        ParseFromStringParam<LocalDateTime> param = new ParseFromStringParam<>(
                " 22.12.2010 22:21:21  ",
                "birthdate",
                ParseFromStringParam.PARSE_DATE_TIME_FUN,
                ParseFromStringParam.DATE_TIME_FORMAT
        );
        LocalDateTime value = param.getParsedValue();
        assertEquals(LocalDateTime.parse("22.12.2010 22:21:21", ParseFromStringParam.dateTimeFormatter),
                value, "times are not equal");
    }
    
    @Test
    void incorrectDateTimeStringParamTest() {
        try {
            ParseFromStringParam<LocalDateTime> param = new ParseFromStringParam<>(
                    " 22.12.2010 22:asd21:21  ",
                    "birthdate",
                    ParseFromStringParam.PARSE_DATE_TIME_FUN,
                    ParseFromStringParam.DATE_TIME_FORMAT
            );
            LocalDateTime value = param.getParsedValue();
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void incorrectFormatDateTimeStringParamTest() {
        try {
            ParseFromStringParam<LocalDateTime> param = new ParseFromStringParam<>(
                    " 22.12.2010 21:2:40  ",
                    "birthdate",
                    ParseFromStringParam.PARSE_DATE_TIME_FUN,
                    ParseFromStringParam.DATE_TIME_FORMAT
            );
            LocalDateTime value = param.getParsedValue();
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void incorrectFutureDateTimeStringParamTest() {
        try {
            ParseFromStringParam<LocalDateTime> param = new ParseFromStringParam<>(
                    " 22.12.99999 21:21:21   ",
                    "birthdate",
                    ParseFromStringParam.PARSE_DATE_TIME_FUN,
                    ParseFromStringParam.DATE_TIME_FORMAT
            );
            LocalDateTime value = param.getParsedValue();
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
}