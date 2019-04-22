package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.paint.Color;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.util.*;

import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Device implements Entity {
    private static final String MODEL_FIELD = "model";
    private static final String TYPE_FIELD = "type";
    private static final String COLOR_FIELD = "color";
    private static final String DATE_FIELD = "date";
    private static final String PRICE_FIELD = "price";
    private static final String PRODUCER_FIELD = "producer";
    
    private static AtomicLong nextId = new AtomicLong();
    
    private final long id;
    private final String model;
    private final String type;
    private final Color color;
    private final BigDecimal price;
    private final String producer;
    private final LocalDate date;
    
    public static String getName() {
        return "device";
    }
    
    @JsonCreator
    public Device(
            @NotNull @JsonProperty(value = MODEL_FIELD) String model,
            @NotNull @JsonProperty(value = TYPE_FIELD) String type,
            @NotNull @JsonProperty(value = PRODUCER_FIELD) String producer,
            @NotNull @JsonProperty(value = COLOR_FIELD) String color,
            @NotNull @JsonProperty(value = DATE_FIELD) String date,
            @NotNull @JsonProperty(value = PRICE_FIELD) String price) throws WebApplicationException {
        StringParam modelParam = new StringParam(model, MODEL_FIELD, Query.ADD_ENTITY + " " + getName());
        StringParam typeParam = new StringParam(type, TYPE_FIELD, Query.ADD_ENTITY + " " + getName());
        StringParam producerParam = new StringParam(producer, PRODUCER_FIELD, Query.ADD_ENTITY + " " + getName());
        ColorParam colorParam = new ColorParam(color, Query.ADD_ENTITY + " " + getName());
        DateParam dateParam = new DateParam(date, Query.ADD_ENTITY + " " + getName());
        PriceParam priceParam = new PriceParam(price, Query.ADD_ENTITY + " " + getName());
        this.model = modelParam.getValue();
        this.type = typeParam.getValue();
        this.producer = producerParam.getValue();
        this.color = colorParam.getColor();
        this.date = dateParam.getDate();
        this.price = priceParam.getPrice();
        this.id = nextId.getAndIncrement();
    }
    
    public String getModel() {
        return model;
    }
    
    public String getType() {
        return type;
    }
    
    @JsonIgnore
    public Color getColor() {
        return color;
    }
    
    @JsonProperty(value = COLOR_FIELD)
    public String getColorString() {
        return color.toString();
    }
    
    @JsonIgnore
    public LocalDate getDate() {
        return date;
    }
    
    @JsonProperty(value = DATE_FIELD)
    public String getDateString() {
        return date.format(DateParam.formatter);
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public String getProducer() {
        return producer;
    }
    
    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device that = (Device) o;
        return id == that.id &&
                Objects.equals(model, that.model) &&
                Objects.equals(type, that.type) &&
                Objects.equals(color, that.color) &&
                Objects.equals(date, that.date) &&
                Objects.equals(price, that.price) &&
                Objects.equals(producer, that.producer);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, model, type, color, price, producer);
    }
    
    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", type=" + type +
                ", color=" + color +
                ", date=" + date.toString() +
                ", price=" + price.toString() +
                ", producer='" + producer + '\'' +
                '}';
    }
}
