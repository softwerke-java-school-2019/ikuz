package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Bill;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code BillQuery} class represents bill filter or order GET-query.
 */
public class BillQuery extends Query<Bill> {
    private static Map<String, Comparator<Bill>> orderParamsMap = new HashMap<>();
    
    private static final String CUSTOMER_ID = "customerId";
    private static final String PURCHASE_DATE_TIME = "purchaseDateTime";
    private static final String PURCHASE_DATE_TIME_FROM = "purchaseDateTimeFrom";
    private static final String PURCHASE_DATE_TIME_TO = "purchaseDateTimeTo";
    private static final String TOTAL_PRICE = "totalPrice";
    private static final String TOTAL_PRICE_FROM = "totalPriceFrom";
    private static final String TOTAL_PRICE_TO = "totalPriceTo";
    private static final String BILL_ITEM_DEVICE_ID = "deviceId";
    private static final String BILL_ITEM_QUANTITY = "quantity";
    private static final String BILL_ITEM_QUANTITY_FROM = "quantityFrom";
    private static final String BILL_ITEM_QUANTITY_TO = "quantityTo";
    private static final String BILL_ITEM_PRICE = "price";
    private static final String BILL_ITEM_PRICE_FROM = "priceFrom";
    private static final String BILL_ITEM_PRICE_TO = "priceTo";
    private static final String ORDER_PURCHASE_DATE_TIME_TO = "-purchaseDateTime";
    private static final String ORDER_TOTAL_PRICE_TO = "-totalPrice";
    
    static {
        orderParamsMap.put(CUSTOMER_ID, Comparator.comparing(Bill::getCustomerId));
        orderParamsMap.put(PURCHASE_DATE_TIME, Comparator.comparing(Bill::getPurchaseDateTime));
        orderParamsMap.put(ORDER_PURCHASE_DATE_TIME_TO, Comparator.comparing(Bill::getPurchaseDateTime).reversed());
        orderParamsMap.put(TOTAL_PRICE, Comparator.comparing(Bill::getTotalPrice));
        orderParamsMap.put(ORDER_TOTAL_PRICE_TO, Comparator.comparing(Bill::getTotalPrice).reversed());
    }
    
    /**
     * Bill query constructor
     *
     * Constructs a bill query by parsing the {@code UriInfo}. After calling the {@link Query(UriInfo)} constructor
     * all remaining unprocessed key-value parameters of bill are contained in {@link Query#queryParams}.
     * Each key parameter is checked for compliance with the filtering option if the key does not match
     * any of the filtering options than the method {@link QueryUtils#getWrongParamsMessage(String)} is called.
     * {@link Query#queryParams} is {@code MultivaluedMap}, where each key is associated with a list of arguments,
     * because each key can be used several times, this API takes the value corresponding to the first appearance
     * of the key in the query, for example, in query:
     * <blockquote>http://localhost:8080/api/bill?totalPrice=1000&customerId=20&totalPrice=500</blockquote>
     * only the first value=1000 for the key=totalPrice will be taken
     *
     * @param uriInfo  URI information taken from GET-query.
     * @throws WebApplicationException
     *         if key of {@link Query#queryParams} is none of filtering bill option or
     *         if it was thrown by the following classes: {@link ParseFromStringParam} or
     *         the following method {@link Query#addOrderType}}
     */
    public BillQuery(UriInfo uriInfo) throws WebApplicationException {
        super(uriInfo);
        for (String key : queryParams.keySet()) {
            switch (key) {
                case BillQuery.CUSTOMER_ID: {
                    ParseFromStringParam<Long> customerId = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.CUSTOMER_ID,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Bill::getCustomerId,
                                    FilterOperator.EQ,
                                    customerId.getParsedValue()
                            )
                    );
                    break;
                }
                case BillQuery.PURCHASE_DATE_TIME: {
                    ParseFromStringParam<LocalDateTime> dateTime = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.PURCHASE_DATE_TIME,
                            ParseFromStringParam.PARSE_DATE_TIME_FUN,
                            ParseFromStringParam.DATE_TIME_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Bill::getPurchaseDateTime,
                                    FilterOperator.EQ,
                                    dateTime.getParsedValue()
                            )
                    );
                    break;
                }
                case BillQuery.PURCHASE_DATE_TIME_FROM: {
                    ParseFromStringParam<LocalDateTime> dateTimeFrom = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.PURCHASE_DATE_TIME_FROM,
                            ParseFromStringParam.PARSE_DATE_TIME_FUN,
                            ParseFromStringParam.DATE_TIME_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Bill::getPurchaseDateTime,
                                    FilterOperator.GREATER_OR_EQ,
                                    dateTimeFrom.getParsedValue()
                            )
                    );
                    break;
                }
                case BillQuery.PURCHASE_DATE_TIME_TO: {
                    ParseFromStringParam<LocalDateTime> dateTimeTo = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.PURCHASE_DATE_TIME_TO,
                            ParseFromStringParam.PARSE_DATE_TIME_FUN,
                            ParseFromStringParam.DATE_TIME_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Bill::getPurchaseDateTime,
                                    FilterOperator.LESS_OR_EQ,
                                    dateTimeTo.getParsedValue()
                            )
                    );
                    break;
                }
                case BillQuery.TOTAL_PRICE: {
                    ParseFromStringParam<Long> totalPrice = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.TOTAL_PRICE,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Bill::getTotalPrice,
                                    FilterOperator.EQ,
                                    totalPrice.getParsedValue()
                            )
                    );
                    break;
                }
                case BillQuery.TOTAL_PRICE_FROM: {
                    ParseFromStringParam<Long> totalPriceFrom = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.TOTAL_PRICE_FROM,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Bill::getTotalPrice,
                                    FilterOperator.GREATER_OR_EQ,
                                    totalPriceFrom.getParsedValue()
                            )
                    );
                    break;
                }
                case BillQuery.TOTAL_PRICE_TO: {
                    ParseFromStringParam<Long> totalPriceTo = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.TOTAL_PRICE_TO,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Bill::getTotalPrice,
                                    FilterOperator.LESS_OR_EQ,
                                    totalPriceTo.getParsedValue()
                            )
                    );
                    break;
                }
                case BillQuery.BILL_ITEM_DEVICE_ID: {
                    ParseFromStringParam<Long> billItemDeviceId = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.BILL_ITEM_DEVICE_ID,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    bill -> bill.containsDevice(billItemDeviceId.getParsedValue()),
                                    FilterOperator.CONTAINS
                            )
                    );
                    break;
                }
                case BillQuery.BILL_ITEM_PRICE: {
                    ParseFromStringParam<Long> billItemPrice = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.BILL_ITEM_PRICE,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    bill -> bill.containsPrice(billItemPrice.getParsedValue()),
                                    FilterOperator.CONTAINS
                            )
                    );
                    break;
                }
                case BillQuery.BILL_ITEM_PRICE_FROM: {
                    ParseFromStringParam<Long> billItemPriceFrom = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.BILL_ITEM_PRICE,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    bill -> bill.containsPriceGreaterThan(billItemPriceFrom.getParsedValue()),
                                    FilterOperator.CONTAINS
                            )
                    );
                    break;
                }
                case BillQuery.BILL_ITEM_PRICE_TO: {
                    ParseFromStringParam<Long> billItemPriceTo = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.BILL_ITEM_PRICE,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    bill -> bill.containsPriceLessThan(billItemPriceTo.getParsedValue()),
                                    FilterOperator.CONTAINS
                            )
                    );
                    break;
                }
                case BillQuery.BILL_ITEM_QUANTITY: {
                    ParseFromStringParam<Long> billItemQuantity = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.BILL_ITEM_QUANTITY,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    bill -> bill.containsQuantity(billItemQuantity.getParsedValue()),
                                    FilterOperator.CONTAINS
                            )
                    );
                    break;
                }
                case BillQuery.BILL_ITEM_QUANTITY_FROM: {
                    ParseFromStringParam<Long> billItemQuantityFrom = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.BILL_ITEM_QUANTITY_FROM,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    bill -> bill.containsQuantityGreaterThan(billItemQuantityFrom.getParsedValue()),
                                    FilterOperator.CONTAINS
                            )
                    );
                    break;
                }
                case BillQuery.BILL_ITEM_QUANTITY_TO: {
                    ParseFromStringParam<Long> billItemQuantityTo = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            BillQuery.BILL_ITEM_QUANTITY_TO,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    bill -> bill.containsQuantityLessThan(billItemQuantityTo.getParsedValue()),
                                    FilterOperator.CONTAINS
                            )
                    );
                    break;
                }
                case Query.ORDER_TYPE: {
                    addOrderType(queryParams.getFirst(key), BillQuery.orderParamsMap);
                    break;
                }
                default: {
                    QueryUtils.getWrongParamsMessage(key);
                }
            }
        }
    }
    
    /**
     * Constructor for more convenient testing bill queries
     *
     * @param holder stores filtering, sorting and output parameters for bill queries
     */
    public BillQuery(QueryConditionsHolder<Bill> holder) {
        super(holder);
    }
}
