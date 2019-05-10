package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;

import javax.ws.rs.WebApplicationException;
import java.util.Objects;

public class BillItem {
    private static final String DEVICE_ID_FIELD = "deviceId";
    private static final String QUANTITY_FIELD = "quantity";
    private static final String PRICE_FIELD = "price";
    
    private final long deviceId;
    private final int quantity;
    private final long price;
    
    @JsonCreator
    public BillItem(@JsonProperty(value = DEVICE_ID_FIELD, required = true) String deviceId,
                    @JsonProperty(value = QUANTITY_FIELD, required = true) String quantity,
                    @JsonProperty(value = PRICE_FIELD, required = true) String price) throws WebApplicationException {
        ParseFromStringParam<Long> deviceIdParam = new ParseFromStringParam<>(
                deviceId,
                DEVICE_ID_FIELD,
                ParseFromStringParam.PARSE_LONG_FUN,
                ParseFromStringParam.POSITIVE_NUMBER_FORMAT
        );
        ParseFromStringParam<Integer> quantityParam = new ParseFromStringParam<>(
                quantity,
                QUANTITY_FIELD,
                ParseFromStringParam.PARSE_INTEGER_FUN,
                ParseFromStringParam.POSITIVE_NUMBER_FORMAT
        );
        ParseFromStringParam<Long> priceParam = new ParseFromStringParam<>(
                price,
                PRICE_FIELD,
                ParseFromStringParam.PARSE_LONG_FUN,
                ParseFromStringParam.POSITIVE_NUMBER_FORMAT
        );
        
        this.deviceId = deviceIdParam.getParsedValue();
        this.quantity = quantityParam.getParsedValue();
        this.price = priceParam.getParsedValue();
    }
    
    public long getDeviceId() {
        return deviceId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    @JsonIgnore
    public long getTotalPrice() {
        return price * quantity;
    }
    
    public long getPrice() {
        return price;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItem billItem = (BillItem) o;
        return deviceId == billItem.deviceId &&
                quantity == billItem.quantity &&
                price == billItem.price;
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
