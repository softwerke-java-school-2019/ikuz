package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Customer;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;
import ru.softwerke.practice.app2019.util.QueryUtils;
import ru.softwerke.practice.app2019.util.StringParam;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code CustomerQuery} class represents customer filter or order GET-query.
 */
public class CustomerQuery extends Query<Customer> {
    private static Map<String, Comparator<Customer>> orderParamsMap = new HashMap<>();
    
    private static final String FIRST_NAME = "firstName";
    private static final String FIRST_NAME_PREFIX = "firstNamePrefix";
    private static final String LAST_NAME = "lastName";
    private static final String LAST_NAME_PREFIX = "lastNamePrefix";
    private static final String MIDDLE_NAME = "middleName";
    private static final String MIDDLE_NAME_PREFIX = "middleNamePrefix";
    private static final String BIRTH_DATE = "birthdate";
    private static final String BIRTH_DATE_FROM = "birthdateFrom";
    private static final String BIRTH_DATE_TO = "birthdateTo";
    private static final String ORDER_BIRTH_DATE_TO = "-birthdate";
    
    static {
        orderParamsMap.put(FIRST_NAME, Comparator.comparing(Customer::getFirstName));
        orderParamsMap.put(LAST_NAME, Comparator.comparing(Customer::getLastName));
        orderParamsMap.put(MIDDLE_NAME, Comparator.comparing(Customer::getMiddleName));
        orderParamsMap.put(BIRTH_DATE, Comparator.comparing(Customer::getBirthDate));
        orderParamsMap.put(ORDER_BIRTH_DATE_TO, Comparator.comparing(Customer::getBirthDate).reversed());
    }
    
    
    /**
     * Customer query constructor
     *
     * Constructs a customer query by parsing the {@code UriInfo}. After calling the {@link Query(UriInfo)} constructor
     * all remaining unprocessed key-value parameters of customer are contained in {@link Query#queryParams}.
     * Each key parameter is checked for compliance with the filtering option if the key does not match
     * any of the filtering options than the method {@link QueryUtils#getWrongParamsMessage(String)} is called.
     * {@link Query#queryParams} is {@code MultivaluedMap}, where each key is associated with a list of arguments, because each key
     * can be used several times, this API takes the value corresponding to the first appearance of the key in the query, for example,
     * in query: <blockquote>http://localhost:8080/api/customer?firstName=John&bithdate=22.12.1980&firstName=Jane</blockquote>
     * only the first value=John for the key=firstName will be taken
     *
     * @param uriInfo  URI information taken from GET-query.
     * @throws WebApplicationException
     *         if key of {@link Query#queryParams} is none of filtering customer option or
     *         if it was thrown by the following classes: {@link ParseFromStringParam}, {@link StringParam} or
     *         the following method {@link Query#addOrderType}}
     */
    public CustomerQuery(UriInfo uriInfo) throws WebApplicationException {
        super(uriInfo);
        for (String key : queryParams.keySet()) {
            switch (key) {
                case CustomerQuery.FIRST_NAME: {
                    StringParam firstName = new StringParam(
                            queryParams.getFirst(key),
                            CustomerQuery.FIRST_NAME
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Customer::getFirstName,
                                    FilterOperator.EQ,
                                    firstName.getValue()
                            )
                    );
                    break;
                }
                case CustomerQuery.MIDDLE_NAME: {
                    StringParam middleName = new StringParam(
                            queryParams.getFirst(key),
                            CustomerQuery.MIDDLE_NAME
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Customer::getMiddleName,
                                    FilterOperator.EQ,
                                    middleName.getValue()
                            )
                    );
                    break;
                }
                case CustomerQuery.LAST_NAME: {
                    StringParam lastName = new StringParam(
                            queryParams.getFirst(key),
                            CustomerQuery.LAST_NAME
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Customer::getLastName,
                                    FilterOperator.EQ,
                                    lastName.getValue()
                            )
                    );
                    break;
                }
                case CustomerQuery.FIRST_NAME_PREFIX: {
                    StringParam firstNamePrefix = new StringParam(
                            queryParams.getFirst(key),
                            CustomerQuery.FIRST_NAME_PREFIX
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Customer::getFirstName,
                                    firstNamePrefix.getValue()
                            )
                    );
                    break;
                }
                case CustomerQuery.MIDDLE_NAME_PREFIX: {
                    StringParam middleNamePrefix = new StringParam(
                            queryParams.getFirst(key),
                            CustomerQuery.MIDDLE_NAME_PREFIX
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Customer::getMiddleName,
                                    middleNamePrefix.getValue()
                            )
                    );
                    break;
                }
                case CustomerQuery.LAST_NAME_PREFIX: {
                    StringParam lastNamePrefix = new StringParam(
                            queryParams.getFirst(key),
                            CustomerQuery.LAST_NAME_PREFIX
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Customer::getLastName,
                                    lastNamePrefix.getValue()
                            )
                    );
                    break;
                }
                case CustomerQuery.BIRTH_DATE: {
                    ParseFromStringParam<LocalDate> birthDate = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            CustomerQuery.BIRTH_DATE,
                            ParseFromStringParam.PARSE_DATE_FUN,
                            ParseFromStringParam.DATE_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Customer::getBirthDate,
                                    FilterOperator.EQ,
                                    birthDate.getParsedValue()
                            )
                    );
                    break;
                }
                case CustomerQuery.BIRTH_DATE_FROM: {
                    ParseFromStringParam<LocalDate> birthDateFrom = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            CustomerQuery.BIRTH_DATE_FROM,
                            ParseFromStringParam.PARSE_DATE_FUN,
                            ParseFromStringParam.DATE_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Customer::getBirthDate,
                                    FilterOperator.GREATER_OR_EQ,
                                    birthDateFrom.getParsedValue()
                            )
                    );
                    break;
                }
                case CustomerQuery.BIRTH_DATE_TO: {
                    ParseFromStringParam<LocalDate> birthDateTo = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            CustomerQuery.BIRTH_DATE_TO,
                            ParseFromStringParam.PARSE_DATE_FUN,
                            ParseFromStringParam.DATE_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Customer::getBirthDate,
                                    FilterOperator.LESS_OR_EQ,
                                    birthDateTo.getParsedValue()
                            )
                    );
                    break;
                }
                case Query.ORDER_TYPE: {
                    addOrderType(queryParams.getFirst(key), CustomerQuery.orderParamsMap);
                    break;
                }
                default: {
                    QueryUtils.getWrongParamsMessage(key);
                }
            }
        }
    }
}
