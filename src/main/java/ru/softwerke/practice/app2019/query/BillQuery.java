package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Bill;
import ru.softwerke.practice.app2019.util.IntegerParam;
import ru.softwerke.practice.app2019.util.DateTimeParam;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class BillQuery extends Query<Bill> {
    private static Map<String, Comparator<Bill>> orderParamsMap = new HashMap<>();
    
    private static final String ORDER_PURCHASE_DATE_TIME_TO = "-purchaseDateTime";
    private static final String ORDER_TOTAL_PRICE_TO = "-totalPrice";
    
    public static final String CUSTOMER_ID = "customerId";
    public static final String PURCHASE_DATE_TIME = "purchaseDateTime";
    public static final String PURCHASE_DATE_TIME_FROM = "purchaseDateTimeFrom";
    public static final String PURCHASE_DATE_TIME_TO = "purchaseDateTimeTo";
    public static final String TOTAL_PRICE = "totalPrice";
    public static final String TOTAL_PRICE_FROM = "totalPriceFrom";
    public static final String TOTAL_PRICE_TO = "totalPriceTo";
    public static final String BILL_ITEM_DEVICE_ID = "deviceId";
    public static final String BILL_ITEM_QUANTITY = "quantity";
    public static final String BILL_ITEM_PRICE = "price";
    
    static {
        orderParamsMap.put(CUSTOMER_ID, Comparator.comparing(Bill::getCustomerId));
        orderParamsMap.put(PURCHASE_DATE_TIME, Comparator.comparing(Bill::getPurchaseDateTime));
        orderParamsMap.put(ORDER_PURCHASE_DATE_TIME_TO, Comparator.comparing(Bill::getPurchaseDateTime).reversed());
        orderParamsMap.put(TOTAL_PRICE, Comparator.comparing(Bill::getTotalPrice));
        orderParamsMap.put(ORDER_TOTAL_PRICE_TO, Comparator.comparing(Bill::getTotalPrice).reversed());
    }
    
    public static Map<String, Comparator<Bill>> getOrderParamsMap() {
        return orderParamsMap;
    }
    
    private BillQuery() {
        super();
    }
    
    public static class Builder extends Query.Builder<Bill> {
        
        public Builder() {
            queryToBuild = new BillQuery();
        }
        
        public Query<Bill> build() {
            Query<Bill> builtBillQuery = queryToBuild;
            queryToBuild = new BillQuery();
            
            return builtBillQuery;
        }
        
        public Builder setPurchaseDateTime(DateTimeParam dateTime) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Bill::getDateTimeString, FilterOperator.EQ,
                    dateTime.getDateTime().toString()));
            return this;
        }
        
        public Builder setPurchaseDateTimeFrom(DateTimeParam dateTimeFrom) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Bill::getDateTimeString, FilterOperator.GREATER_OR_EQ,
                    dateTimeFrom.getDateTime().toString()));
            return this;
        }
        
        public Builder setPurchaseDateTimeTo(DateTimeParam dateTimeFrom) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Bill::getDateTimeString, FilterOperator.LESS_OR_EQ,
                    dateTimeFrom.getDateTime().toString()));
            return this;
        }
        
        public Builder setTotalPrice(IntegerParam totalPrice) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Bill::getTotalPrice, FilterOperator.EQ,
                    totalPrice.getIntegerValue()));
            return this;
        }
        
        public Builder setTotalPriceFrom(IntegerParam totalPriceFrom) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Bill::getTotalPrice, FilterOperator.GREATER_OR_EQ,
                    totalPriceFrom.getIntegerValue()));
            return this;
        }
        
        public Builder setTotalPriceTo(IntegerParam totalPriceTo) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Bill::getTotalPrice, FilterOperator.LESS_OR_EQ,
                    totalPriceTo.getIntegerValue()));
            return this;
        }
        
        public Builder setCustomerId(IntegerParam customerId) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Bill::getCustomerId, FilterOperator.EQ,
                    customerId.getIntegerValue()));
            return this;
        }
        
        public Builder setBillItemDeviceId(IntegerParam billItemDeviceId) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    bill -> bill.containsDevice(billItemDeviceId.getIntegerValue()), FilterOperator.CONTAINS
            ));
            return this;
        }
        
        public Builder setBillItemQuantity(IntegerParam billItemQuantity) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    bill -> bill.containsQuantity(billItemQuantity.getIntegerValue()), FilterOperator.CONTAINS
            ));
            return this;
        }
        
        public Builder setBillItemPrice(IntegerParam billItemPrice) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    bill -> bill.containsPrice(billItemPrice.getIntegerValue()), FilterOperator.CONTAINS
            ));
            return this;
        }
    }
}
