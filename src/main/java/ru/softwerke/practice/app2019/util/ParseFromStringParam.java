package ru.softwerke.practice.app2019.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.function.Function;


/**
 * Validate value entered by user for a query key that requires a special format appropriate to the value type
 * @param <T> value's type
 */
public class ParseFromStringParam<T> extends StringParam {
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter
            .ofPattern(DATE_PATTERN)
            .withLocale(Locale.getDefault());
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern(DATE_TIME_PATTERN)
            .withLocale(Locale.getDefault());
    
    public static final String DATE_TIME_FORMAT = "the dateTime format: " + DATE_TIME_PATTERN;
    public static final String DATE_FORMAT = "the date format: " + DATE_PATTERN;
    public static final String POSITIVE_NUMBER_FORMAT = "the positive number format";
    
    public static final Function<String, Long> PARSE_LONG_FUN = Long::parseLong;
    public static final Function<String, Integer> PARSE_INTEGER_FUN = Integer::parseInt;
    public static final Function<String, LocalDate> PARSE_DATE_FUN =
            localDate -> LocalDate.parse(localDate, dateFormatter);
    public static final Function<String, LocalDateTime> PARSE_DATE_TIME_FUN =
            localDateTime -> LocalDateTime.parse(localDateTime, dateTimeFormatter);
    
    private final T parsedValue;
    
    /**
     * Validate value entered by user. Converts all of the characters in this {@code String} to lower case and
     * make string with any leading and trailing whitespace removed, parses a string into {@code T}
     * data type that corresponds the format passed by {@code format}.
     * Stores the created value with the appropriate type .
     *
     * @param valueByString value represented by string
     * @param fieldName auxiliary parameter needed to display the correct error message - the key of filter or order query
     * @param parseFunction string parsing function (from {@code String} to {@code T})
     * @param format format corresponding to {@code T} data type
     *
     * @throws WebApplicationException with response's status 400 {@code Response.Status.BAD_REQUEST}
     *         if the entered value is empty string on null or has too large length
     *         (if it thrown by {@link StringParam} superclass) if the value does not correspond the format
     *         if values of some types do not pass additional validation
     *         {@link QueryUtils#checkIsDateTimeBeforeNow(LocalDateTime, String)},
     *         {@link QueryUtils#checkIsDateBeforeNow(LocalDate, String)},
     *         {@link QueryUtils#checkIsPositiveNumber(String, String)}
     */
    public ParseFromStringParam(String valueByString, String fieldName, Function<String, T> parseFunction, String format)
            throws WebApplicationException {
        super(valueByString, fieldName);
        try {
            this.parsedValue = parseFunction.apply(this.getValue());
        } catch (NumberFormatException | DateTimeParseException e) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                    QueryUtils.getInvalidFormatMessage(valueByString, fieldName, format)
            );
            throw new WebApplicationException(response);
        }
        
        // Additional validation of some types
        if (parsedValue instanceof LocalDateTime) {
            QueryUtils.checkIsDateTimeBeforeNow((LocalDateTime) parsedValue, fieldName);
        } else if(parsedValue instanceof LocalDate) {
            QueryUtils.checkIsDateBeforeNow((LocalDate) parsedValue, fieldName);
        } else if (parsedValue instanceof Number){
            QueryUtils.checkIsPositiveNumber(this.getValue(), fieldName);
        }
    }
    
    public T getParsedValue() {
        return parsedValue;
    }
}
