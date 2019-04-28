package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.util.*;

import javax.annotation.Priority;
import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Device implements Entity {
    private static final String MODEL_FIELD = "modelName";
    private static final String TYPE_FIELD = "deviceType";
    private static final String COLOR_NAME_FIELD = "colorName";
    private static final String COLOR_RGB_FIELD = "colorRGB";
    private static final String MANUFACTURE_DATE_FIELD = "manufactureDate";
    private static final String PRICE_FIELD = "price";
    private static final String MANUFACTURER_FIELD = "manufacturer";
    
    private static AtomicInteger nextId = new AtomicInteger();
    
    private final String modelName;
    private final String deviceType;
    private final String colorName;
    private final int price;
    private final String manufacturer;
    private final LocalDate manufacturerDate;
    private final int colorRGB;
    private final int id;
    
    public static String getName() {
        return "a device";
    }
    
    @JsonCreator
    public Device(
            @NotNull @JsonProperty(value = MODEL_FIELD) String modelName,
            @NotNull @JsonProperty(value = TYPE_FIELD) String deviceType,
            @NotNull @JsonProperty(value = MANUFACTURER_FIELD) String manufacturer,
            @NotNull @JsonProperty(value = COLOR_NAME_FIELD) String colorName,
            @NotNull @JsonProperty(value = MANUFACTURE_DATE_FIELD) String manufacturerDate,
            @NotNull @JsonProperty(value = PRICE_FIELD) String price) throws WebApplicationException {
        StringParam modelParam = new StringParam(modelName, MODEL_FIELD, Query.POST_ENTITY + getName());
        StringParam typeParam = new StringParam(deviceType, TYPE_FIELD, Query.POST_ENTITY + getName());
        StringParam producerParam = new StringParam(manufacturer, MANUFACTURER_FIELD, Query.POST_ENTITY + getName());
        StringParam colorNameParam = new StringParam(colorName, COLOR_NAME_FIELD, Query.POST_ENTITY + getName());
        
        DateParam dateParam = new DateParam(manufacturerDate, MANUFACTURE_DATE_FIELD, Query.POST_ENTITY + getName());
        IntegerParam priceParam = new IntegerParam(price, PRICE_FIELD, Query.POST_ENTITY + getName());
        
        String colorNameTemp = colorNameParam.getValue();
        int colorRGBTemp = QueryUtils.checkDoesColorExist(colorNameTemp, Query.POST_ENTITY + getName());
        
        this.modelName = modelParam.getValue();
        this.deviceType = typeParam.getValue();
        this.manufacturer = producerParam.getValue();
        this.colorRGB = colorRGBTemp;
        this.colorName = colorNameTemp;
        this.manufacturerDate = dateParam.getDate();
        this.price = priceParam.getIntegerValue();
        this.id = nextId.getAndIncrement();
    }
    
    public String getModelName() {
        return modelName;
    }
    
    public String getDeviceType() {
        return deviceType;
    }
    
    @JsonProperty(value = COLOR_NAME_FIELD)
    public String getColorName() {
        return colorName;
    }
    
    public int getColorRGB() {
        return colorRGB;
    }
    
    @JsonIgnore
    public LocalDate getManufacturerDate() {
        return manufacturerDate;
    }
    
    @JsonProperty(value = MANUFACTURE_DATE_FIELD)
    public String getDateString() {
        return manufacturerDate.format(DateParam.formatter);
    }
    
    public int getPrice() {
        return price;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device that = (Device) o;
        return id == that.id &&
                Objects.equals(modelName, that.modelName) &&
                Objects.equals(deviceType, that.deviceType) &&
                Objects.equals(colorName, that.colorName) &&
                Objects.equals(manufacturerDate, that.manufacturerDate) &&
                Objects.equals(price, that.price) &&
                Objects.equals(manufacturer, that.manufacturer);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, modelName, deviceType, colorName, price, manufacturer);
    }
    
    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", modelName='" + modelName + '\'' +
                ", deviceType=" + deviceType +
                ", colorName=" + colorName +
                ", manufactureDate=" + manufacturerDate.toString() +
                ", price=" + price +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
