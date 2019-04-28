package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.softwerke.practice.app2019.util.IntegerParam;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class BillItem {
    private static final String DEVICE_ID_FIELD = "deviceId";
    private static final String QUANTITY_FIELD = "quantity";
    private static final String PRICE_FIELD = "price";
    private static final String ADDITIONAL_MESSAGE = "one of the elements of the post bill";
    
    private final int deviceId;
    private final int quantity;
    private final int price;
    
    @JsonCreator
    public BillItem(
            @NotNull @JsonProperty(value = DEVICE_ID_FIELD) String deviceId,
            @NotNull @JsonProperty(value = QUANTITY_FIELD) String quantity,
            @NotNull @JsonProperty(value = PRICE_FIELD) String price) {
        IntegerParam deviceIdParam = new IntegerParam(deviceId, DEVICE_ID_FIELD, ADDITIONAL_MESSAGE);
        IntegerParam quantityParam = new IntegerParam(quantity, QUANTITY_FIELD, ADDITIONAL_MESSAGE);
        IntegerParam bigDecimalParam = new IntegerParam(price, PRICE_FIELD, ADDITIONAL_MESSAGE);
        this.deviceId = deviceIdParam.getIntegerValue();
        this.quantity = quantityParam.getIntegerValue();
        this.price = bigDecimalParam.getIntegerValue();
    }
    
    private String getName() {
        return "a billItem";
    }
    
    public int getDeviceId() {
        return deviceId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    @JsonIgnore
    public int getTotalPrice() {
        return price * quantity;
    }
    
    public int getPrice() {
        return price;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItem that = (BillItem) o;
        return Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(price, that.price);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(deviceId, quantity, price);
    }
    
    @Override
    public String toString() {
        return "BillItem{" +
                "deviceId=" + deviceId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
