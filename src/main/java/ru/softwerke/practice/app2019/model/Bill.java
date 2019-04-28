package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.util.DateTimeParam;
import ru.softwerke.practice.app2019.util.IntegerParam;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Bill implements Entity {
    private static final String CUSTOMER_ID_FIELD = "customerId";
    private static final String ITEMS_LIST_FIELD = "items";
    private static final String PURCHASE_DATE_TIME = "purchaseDateTime";
    
    private static AtomicInteger nextId = new AtomicInteger();
    
    private final int customerId;
    private final List<BillItem> items;
    private final LocalDateTime purchaseDateTime;
    private final Integer totalPrice;
    private final int id;
    
    public static String getName() {
        return "a bill";
    }
    
    @JsonCreator
    public Bill(@NotNull @JsonProperty(value = CUSTOMER_ID_FIELD) String customerId,
                @NotNull @JsonProperty(value = ITEMS_LIST_FIELD) List<BillItem> items) {
        IntegerParam customerIdParam = new IntegerParam(customerId, CUSTOMER_ID_FIELD, Query.POST_ENTITY + getName());
        this.customerId = customerIdParam.getIntegerValue();
        QueryUtils.checkListForNulls(items, Query.POST_ENTITY + getName());
        this.items = items;
        this.totalPrice = this.items.stream().map(BillItem::getTotalPrice).reduce(0, Integer::sum);
        this.id = nextId.getAndIncrement();
        this.purchaseDateTime = LocalDateTime.now();
    }
    
    public Boolean containsDevice(int id) {
        return items.stream().anyMatch(it -> it.getDeviceId() == id);
    }
    
    public Boolean containsQuantity(int quantity) {
        return items.stream().anyMatch(it -> it.getQuantity() == quantity);
    }
    
    public Boolean containsPrice(Integer price) {
        return items.stream().anyMatch(it -> it.getPrice() == price);
    }
    
    public int getCustomerId() {
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
        return purchaseDateTime.format(DateTimeParam.formatter);
    }
    
    public Integer getTotalPrice() {
        return totalPrice;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return id == bill.id &&
                customerId == bill.customerId &&
                Objects.equals(items, bill.items) &&
                Objects.equals(totalPrice, bill.totalPrice) &&
                Objects.equals(purchaseDateTime, bill.purchaseDateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, items, purchaseDateTime, totalPrice);
    }
    
    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", items=" + items.toString() +
                ", purchaseDateTime=" + purchaseDateTime.toString() +
                ", totalPrice=" + totalPrice.toString() +
                '}';
    }
}
