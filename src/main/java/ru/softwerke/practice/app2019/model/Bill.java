package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.ws.rs.WebApplicationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Bill implements Entity {
    private static final String CUSTOMER_ID_FIELD = "customerId";
    private static final String ITEMS_LIST_FIELD = "items";
    private static final String PURCHASE_DATE_TIME = "purchaseDateTime";
    
    private static AtomicLong nextId = new AtomicLong();
    
    private final long customerId;
    private final List<BillItem> items;
    private final LocalDateTime purchaseDateTime;
    private final Long totalPrice;
    private final long id;
    
    public static final String ENTITY_TYPE_NAME = "bill";
    
    @JsonCreator
    public Bill(@JsonProperty(value = CUSTOMER_ID_FIELD, required = true) String customerId,
                @JsonProperty(value = ITEMS_LIST_FIELD, required = true) List<BillItem> items) throws WebApplicationException {
        ParseFromStringParam<Long> customerIdParam = new ParseFromStringParam<>(
                customerId,
                CUSTOMER_ID_FIELD,
                Long::parseLong,
                ParseFromStringParam.POSITIVE_NUMBER_FORMAT
        );
        QueryUtils.checkListForNulls(items);
        
        this.customerId = customerIdParam.getParsedValue();
        this.items = items;
        this.totalPrice = this.items.stream().map(BillItem::getTotalPrice).reduce(0L, Long::sum);
        this.id = nextId.getAndIncrement();
        this.purchaseDateTime = LocalDateTime.now();
    }
    
    public Boolean containsDevice(long id) {
        return items.stream().anyMatch(it -> it.getDeviceId() == id);
    }
    
    public Boolean containsQuantity(long quantity) {
        return items.stream().anyMatch(it -> it.getQuantity() == quantity);
    }
    
    public Boolean containsQuantityGreaterThan(long quantity) {
        return items.stream().anyMatch(it -> it.getQuantity() >= quantity);
    }
    
    public Boolean containsQuantityLessThan(long quantity) {
        return items.stream().anyMatch(it -> it.getQuantity() <= quantity);
    }
    
    public Boolean containsPrice(Long price) {
        return items.stream().anyMatch(it -> it.getPrice() == price);
    }
    
    public long getCustomerId() {
        return customerId;
    }
    
    @JsonProperty(value = ITEMS_LIST_FIELD)
    public List<BillItem> getItems() {
        return items;
    }
    
    @JsonIgnore
    public LocalDateTime getPurchaseDateTime() {
        return purchaseDateTime;
    }
    
    @JsonProperty(value = PURCHASE_DATE_TIME)
    public String getDateTimeString() {
        return purchaseDateTime.format(ParseFromStringParam.dateTimeFormatter);
    }
    
    public Long getTotalPrice() {
        return totalPrice;
    }
    
    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return customerId == bill.customerId &&
                id == bill.id &&
                Objects.equals(items, bill.items) &&
                Objects.equals(purchaseDateTime, bill.purchaseDateTime) &&
                Objects.equals(totalPrice, bill.totalPrice);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerId, items, purchaseDateTime, totalPrice, id);
    }
    
    @Override
    public String toString() {
        return "Bill{" +
                "customerId=" + customerId +
                ", items=" + items +
                ", purchaseDateTime=" + purchaseDateTime +
                ", totalPrice=" + totalPrice +
                ", id=" + id +
                '}';
    }
}
