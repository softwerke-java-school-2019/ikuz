package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import ru.softwerke.practice.app2019.query.Query;

import javax.ws.rs.WebApplicationException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringParamTest {
    
    static Random random = new Random(2019);
    
    @Test
    void nullStringTest() {
        try {
            StringParam param = new StringParam(null, Query.ORDER_TYPE);
            String value = param.getValue();
            Assert.fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
    
    @Test
    void tooLargeLengthStringTest() {
        try {
            StringParam param = new StringParam(RandomStringUtils.random(100, true, true), Query.ORDER_TYPE);
            String value = param.getValue();
            Assert.fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(431, e.getResponse().getStatus());
        }
    }
}