package ru.softwerke.practice.app2019.util;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class BigDecimalParam {
    private static final String FORMAT = "big decimal number format";
    private final BigDecimal bigDecimal;
    private final String typeQuery;
    
    public BigDecimalParam(String priceStr, String typeField, String typeQuery) throws WebApplicationException {
        QueryUtils.checkLengthOfHeaderFields(priceStr, typeField, typeQuery);
        if (StringUtils.isBlank(priceStr)) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.EMPTY_VALUE_TYPE_ERROR,
                            QueryUtils.getEmptyOrNullParamsMessage(typeField, typeQuery));
            throw new WebApplicationException(response);
        }
        try {
            BigDecimal temp = new BigDecimal(priceStr.replaceAll(",", ""));
            if (temp.compareTo(BigDecimal.ZERO) >= 0) {
                this.bigDecimal = temp;
                this.typeQuery = typeQuery;
            } else {
                Response response = QueryUtils.
                        getResponseWithMessage(Response.Status.BAD_REQUEST,
                                QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                                QueryUtils.getNegativeNumberErrorMessage(priceStr, typeField, typeQuery));
                throw new WebApplicationException(response);
            }
        } catch (NumberFormatException e) {
            Response response = QueryUtils.
                    getResponseWithMessage(Response.Status.BAD_REQUEST,
                            QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                            QueryUtils.getInvalidFormatMessage(priceStr, typeField, typeQuery, FORMAT));
            throw new WebApplicationException(response);
        }
    }
    
    public BigDecimal getBigDecimalValue() {
        return bigDecimal;
    }
    
    public String getTypeQuery() {
        return typeQuery;
    }
}
