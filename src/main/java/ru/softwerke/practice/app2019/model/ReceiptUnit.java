package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

public class ReceiptUnit {
    private static final String DEVICE_ID_FIELD = "deviceId";
    private static final String QUANTITY_FIELD = "quantity";
    private static final String PRICE_FIELD = "price";
    
    private final long deviceId;
    private final long quantity;
    private final BigDecimal price;
    
    @JsonCreator
    public ReceiptUnit(
            @NotNull @JsonProperty(value = DEVICE_ID_FIELD, required = true) long deviceId,
            @NotNull @JsonProperty(value = QUANTITY_FIELD, required = true) long quantity,
            @NotNull @JsonProperty(value = PRICE_FIELD, required = true) BigDecimal price) {
        this.deviceId = deviceId;
        this.quantity = quantity;
        this.price = price;
    }
    
    public long getDeviceId() {
        return deviceId;
    }
    
    public long getQuantity() {
        return quantity;
    }
    
    @JsonIgnore
    public BigDecimal getTotalPrice() {
        return price.multiply(new BigDecimal(quantity));
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptUnit that = (ReceiptUnit) o;
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
        return "ReceiptUnit{" +
                "deviceId=" + deviceId +
                ", quantity=" + quantity +
                ", price=" + price.toString() +
                '}';
    }
}
