package ru.softwerke.practice.app2019.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import ru.softwerke.practice.app2019.query.Query;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiStringParamTest extends StringParamTest {
    
    @Test
    void correctStringTest() {
        MultiStringParam paramList = new MultiStringParam("  price,            colorName,   modelName       ", Query.ORDER_TYPE);
        List<String> params = paramList.getValueList();
        List<String> expected = new ArrayList<>();
        expected.add("price");
        expected.add("colorName");
        expected.add("modelName");
        assertEquals(params, expected, "lists are not equal");
    }
    
    @Test
    void stringWithEmptyParamsTest() {
        try {
            MultiStringParam paramList = new MultiStringParam("  ,  ,          colorName,   modelName       ", Query.ORDER_TYPE);
            List<String> params = paramList.getValueList();
            Assert.fail("Expected WebApplicationException did not occur");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
        }
    }
}