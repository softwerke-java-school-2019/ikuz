package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Receipt implements Entity {
    private static final String CLIENT_ID_FIELD = "clientId";
    private static final String ITEMS_LIST_FIELD = "unitList";
    private static final String TIME_SALE_FIELD = "timeSale";
    
    private static AtomicLong nextId = new AtomicLong();
    
    private final long id;
    private final long clientId;
    private final List<ReceiptUnit> unitList;
    private final LocalDateTime timeSale;
    
    public static String getName() {
        return "device";
    }
    
    @JsonIgnore
    private BigDecimal totalPrice;
    
    public Receipt(@NotNull @JsonProperty(value = CLIENT_ID_FIELD, required = true) long clientId,
                   @NotNull @JsonProperty(value = ITEMS_LIST_FIELD, required = true) List<ReceiptUnit> unitList) {
        this.id = nextId.getAndIncrement();
        this.clientId = clientId;
        this.unitList = unitList;
        this.timeSale = LocalDateTime.now();
        this.totalPrice = this.unitList.stream().map(ReceiptUnit::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public boolean containsDevice(long id) {
        return unitList.stream().anyMatch(it -> it.getDeviceId() == id);
    }
    
    public long getClientId() {
        return clientId;
    }
    
    public List<ReceiptUnit> getUnitList() {
        return unitList;
    }
    
    public LocalDateTime getTimeSale() {
        return timeSale;
    }
    
    public BigDecimal getTotalPrice() {
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
        Receipt receipt = (Receipt) o;
        return id == receipt.id &&
                clientId == receipt.clientId &&
                Objects.equals(unitList, receipt.unitList) &&
                Objects.equals(timeSale, receipt.timeSale);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, unitList, timeSale);
    }
    
    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", unitList=" + unitList.toString() +
                ", timeSale=" + timeSale.toString() +
                '}';
    }
}
