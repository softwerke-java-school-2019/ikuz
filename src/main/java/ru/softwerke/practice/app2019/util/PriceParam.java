package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class PriceParam {
    private static final String PRICE = "price";
    private final BigDecimal price;
    private final String typeQuery;
    
    public PriceParam(String priceStr, String typeQuery) throws WebApplicationException {
        QueryUtils.checkLengthOfHeaderFields(priceStr, PRICE, typeQuery);
        if (StringUtils.isBlank(priceStr)) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "empty value of query parameters",
                            String.format("the value of %s parameter in %s query is null or empty",
                                    PRICE, typeQuery));
            throw new WebApplicationException(response);
        }
        try {
            BigDecimal temp = new BigDecimal(priceStr.replaceAll(",", ""));
            if (temp.compareTo(BigDecimal.ZERO) >= 0) {
                this.price = temp;
                this.typeQuery = typeQuery;
            } else {
                Response response = QueryUtils.
                        getResponseWithMessage(Response.Status.BAD_REQUEST,
                                "invalid query parameter format",
                                String.format("the value %s of %s parameter in %s query is negative",
                                        priceStr, PRICE, typeQuery));
                throw new WebApplicationException(response);
            }
        } catch (NumberFormatException e) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            "invalid query parameter format",
                            String.format("the value %s of %s parameter in %s query does not match big decimal number format",
                                    priceStr, PRICE, typeQuery));
            throw new WebApplicationException(response);
        }
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public String getTypeQuery() {
        return typeQuery;
    }
}
