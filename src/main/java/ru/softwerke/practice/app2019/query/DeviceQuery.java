package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.util.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DeviceQuery extends Query<Device> {
    public static final String MODEL = "model";
    public static final String TYPE = "type";
    public static final String COLOR = "color";
    public static final String DATE = "date";
    public static final String PRICE = "price";
    public static final String PRODUCER = "producer";
    public static final String PRICE_FROM = "priceFrom";
    public static final String PRICE_TO = "priceTo";
    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";
    private static final String ORDER_DATE_DEC = "dateDec";
    private static final String ORDER_PRICE_DEC = "priceDec";
    
    private static Map<String, Comparator<Device>> orderParamsMap = new HashMap<>();
    
    static {
        orderParamsMap.put(MODEL, Comparator.comparing(Device::getModel));
        orderParamsMap.put(TYPE, Comparator.comparing(Device::getType));
        orderParamsMap.put(PRODUCER, Comparator.comparing(Device::getProducer));
        orderParamsMap.put(DATE, Comparator.comparing(Device::getDate));
        orderParamsMap.put(ORDER_DATE_DEC, Comparator.comparing(Device::getDate).reversed());
        orderParamsMap.put(PRICE, Comparator.comparing(Device::getPrice));
        orderParamsMap.put(ORDER_PRICE_DEC, Comparator.comparing(Device::getPrice).reversed());
    }
    
    public static Map<String, Comparator<Device>> getOrderParamsMap() {
        return orderParamsMap;
    }
    
    private DeviceQuery() {
        super();
    }
    
    public static class Builder extends Query.Builder<Device> {
        private DeviceQuery deviceQueryToBuild;
        
        public Builder() {
            deviceQueryToBuild = new DeviceQuery();
        }
        
        public Builder setModel(StringParam model) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getModel, FilterOperator.EQ),
                    model.getValue()));
            return this;
        }
        
        public Builder setType(StringParam type) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getType, FilterOperator.EQ),
                    type.getValue()));
            return this;
        }
        
        public Builder setProducer(StringParam producer) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getProducer, FilterOperator.EQ),
                    producer.getValue()));
            return this;
        }
        
        public Builder setColor(ColorParam color) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getColorString, FilterOperator.EQ),
                    color.getColor().toString()));
            return this;
        }
        
        public Builder setDate(DateParam date) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getDate, FilterOperator.EQ),
                    date.getDate()));
            return this;
        }
        
        public Builder setDateFrom(DateParam dateFrom) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getDate, FilterOperator.GREATER_OR_EQ),
                    dateFrom.getDate()));
            return this;
        }
        
        public Builder setDateTo(DateParam dateTo) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getDate, FilterOperator.LESS_OR_EQ),
                    dateTo.getDate()));
            return this;
        }
        
        public Builder setPrice(PriceParam price) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getPrice, FilterOperator.EQ),
                    price.getPrice()));
            return this;
        }
        
        public Builder setPriceFrom(PriceParam priceFrom) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getPrice, FilterOperator.GREATER_OR_EQ),
                    priceFrom.getPrice()));
            return this;
        }
        
        public Builder setPriceTo(PriceParam priceTo) {
            this.deviceQueryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    new QueryProperty<>(Device::getPrice, FilterOperator.LESS_OR_EQ),
                    priceTo.getPrice()));
            return this;
        }
    
        public Query.Builder setDeviceComparator(Comparator<Device> comparator) {
            this.deviceQueryToBuild.holder.setQueryComparator(comparator);
            return this;
        }
        
        public DeviceQuery build() {
            DeviceQuery builtDeviceQuery = deviceQueryToBuild;
            deviceQueryToBuild = new DeviceQuery();
            
            return builtDeviceQuery;
        }
    }
}
