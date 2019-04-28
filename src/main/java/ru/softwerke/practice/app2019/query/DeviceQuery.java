package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.util.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DeviceQuery extends Query<Device> {
    private static Map<String, Comparator<Device>> orderParamsMap = new HashMap<>();
    
    private static final String ORDER_MANUFACTURE_DATE_TO = "-manufactureDate";
    private static final String ORDER_PRICE_TO = "-price";
    
    public static final String MODEL = "modelName";
    public static final String MODEL_PREFIX = "modelNamePrefix";
    public static final String TYPE = "deviceType";
    public static final String TYPE_PREFIX = "deviceTypePrefix";
    public static final String COLOR = "colorName";
    public static final String COLOR_RGB = "colorRGB";
    public static final String MANUFACTURE_DATE = "manufactureDate";
    public static final String PRICE = "price";
    public static final String MANUFACTURER = "manufacturer";
    public static final String MANUFACTURER_PREFIX = "manufacturerPrefix";
    public static final String PRICE_FROM = "priceFrom";
    public static final String PRICE_TO = "priceTo";
    public static final String MANUFACTURE_DATE_FROM = "manufactureDateFrom";
    public static final String MANUFACTURE_DATE_TO = "manufactureDateTo";
    
    static {
        orderParamsMap.put(MODEL, Comparator.comparing(Device::getModelName));
        orderParamsMap.put(TYPE, Comparator.comparing(Device::getDeviceType));
        orderParamsMap.put(MANUFACTURER, Comparator.comparing(Device::getManufacturer));
        orderParamsMap.put(MANUFACTURE_DATE, Comparator.comparing(Device::getManufacturerDate));
        orderParamsMap.put(ORDER_MANUFACTURE_DATE_TO, Comparator.comparing(Device::getManufacturerDate).reversed());
        orderParamsMap.put(PRICE, Comparator.comparing(Device::getPrice));
        orderParamsMap.put(ORDER_PRICE_TO, Comparator.comparing(Device::getPrice).reversed());
    }
    
    public static Map<String, Comparator<Device>> getOrderParamsMap() {
        return orderParamsMap;
    }
    
    private DeviceQuery() {
        super();
    }
    
    public static class Builder extends Query.Builder<Device> {
        
        public Builder() {
            queryToBuild = new DeviceQuery();
        }
        
        public Query<Device> build() {
            Query<Device> builtDeviceQuery = queryToBuild;
            queryToBuild = new DeviceQuery();
            
            return builtDeviceQuery;
        }
        
        public Builder setModel(StringParam model) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getModelName, FilterOperator.EQ,
                    model.getValue()));
            return this;
        }
        
        public Builder setType(StringParam type) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getDeviceType, FilterOperator.EQ,
                    type.getValue()));
            return this;
        }
        
        public Builder setProducer(StringParam producer) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getManufacturer, FilterOperator.EQ,
                    producer.getValue()));
            return this;
        }
        
        public Builder setColorName(StringParam colorName) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getColorName, FilterOperator.EQ,
                    colorName.getValue()));
            return this;
        }
        
        public Builder setColorRGB(IntegerParam colorRGB) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getColorRGB, FilterOperator.EQ,
                    colorRGB.getIntegerValue()));
            return this;
        }
        
        public Builder setDate(DateParam date) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getManufacturerDate, FilterOperator.EQ,
                    date.getDate()));
            return this;
        }
        
        public Builder setDateFrom(DateParam dateFrom) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getManufacturerDate, FilterOperator.GREATER_OR_EQ,
                    dateFrom.getDate()));
            return this;
        }
        
        public Builder setDateTo(DateParam dateTo) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getManufacturerDate, FilterOperator.LESS_OR_EQ,
                    dateTo.getDate()));
            return this;
        }
        
        public Builder setPrice(IntegerParam price) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getPrice, FilterOperator.EQ,
                    price.getIntegerValue()));
            return this;
        }
        
        public Builder setPriceFrom(IntegerParam priceFrom) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getPrice, FilterOperator.GREATER_OR_EQ,
                    priceFrom.getIntegerValue()));
            return this;
        }
        
        public Builder setPriceTo(IntegerParam priceTo) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getPrice, FilterOperator.LESS_OR_EQ,
                    priceTo.getIntegerValue()));
            return this;
        }
        
        public Builder setModelPrefix(StringParam modelPrefix) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getModelName, modelPrefix.getValue()
            ));
            return this;
        }
        
        public Builder setTypePrefix(StringParam typePrefix) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getDeviceType, typePrefix.getValue()
            ));
            return this;
        }
        
        public Builder setManufacturerPrefix(StringParam modelPrefix) {
            this.queryToBuild.holder.addFilterCondition(new QueryCondition<>(
                    Device::getManufacturer, modelPrefix.getValue()
            ));
            return this;
        }
    }
}
