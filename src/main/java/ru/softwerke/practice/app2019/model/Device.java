package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.softwerke.practice.app2019.service.DeviceColorService;
import ru.softwerke.practice.app2019.service.DeviceTypeService;
import ru.softwerke.practice.app2019.util.*;

import javax.ws.rs.WebApplicationException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Device implements Entity {
    private static final String MODEL_FIELD = "modelName";
    private static final String TYPE_FIELD = "deviceType";
    private static final String COLOR_NAME_FIELD = "colorName";
    private static final String COLOR_RGB_FIELD = "colorRGB";
    private static final String MANUFACTURE_DATE_FIELD = "manufactureDate";
    private static final String PRICE_FIELD = "price";
    private static final String MANUFACTURER_FIELD = "manufacturer";
    
    private static AtomicLong nextId = new AtomicLong();
    
    private final String modelName;
    private final String deviceType;
    private final long price;
    private final String manufacturer;
    private final LocalDate manufacturerDate;
    private final Color color;
    private final long id;
    
    public static final String ENTITY_TYPE_NAME = "device";
    
    @JsonCreator
    public Device(
            @JsonProperty(value = MODEL_FIELD, required = true) String modelName,
            @JsonProperty(value = TYPE_FIELD, required = true) String deviceType,
            @JsonProperty(value = MANUFACTURER_FIELD, required = true) String manufacturer,
            @JsonProperty(value = COLOR_NAME_FIELD, required = true) String colorName,
            @JsonProperty(value = COLOR_RGB_FIELD) String colorRGB,
            @JsonProperty(value = MANUFACTURE_DATE_FIELD, required = true) String manufacturerDate,
            @JsonProperty(value = PRICE_FIELD, required = true) String price) throws WebApplicationException {
        StringParam modelParam = new StringParam(
                modelName,
                MODEL_FIELD
        );
        DeviceTypeService deviceTypeService = DeviceTypeService.getInstance();
        StringParam typeParam = new StringParam(
                deviceType,
                TYPE_FIELD
        );
        deviceTypeService.putDeviceType(typeParam.getValue());
        StringParam producerParam = new StringParam(
                manufacturer,
                MANUFACTURER_FIELD
        );
        StringParam colorNameParam = new StringParam(
                colorName,
                COLOR_NAME_FIELD
        );
        String colorNameParsed = colorNameParam.getValue();
        DeviceColorService deviceColorService = DeviceColorService.getInstance();
        if (colorRGB != null) {
            ParseFromStringParam<Integer> colorRGBParam = new ParseFromStringParam<>(
                    colorRGB,
                    COLOR_RGB_FIELD,
                    ParseFromStringParam.PARSE_INTEGER_FUN,
                    ParseFromStringParam.POSITIVE_NUMBER_FORMAT
            );
            deviceColorService.putColor(new Color(colorNameParsed, colorRGBParam.getParsedValue()));
        }
        ParseFromStringParam<LocalDate> dateParam = new ParseFromStringParam<>(
                manufacturerDate,
                MANUFACTURE_DATE_FIELD,
                ParseFromStringParam.PARSE_DATE_FUN,
                ParseFromStringParam.DATE_FORMAT
        );
        ParseFromStringParam<Long> priceParam = new ParseFromStringParam<>(
                price,
                PRICE_FIELD,
                ParseFromStringParam.PARSE_LONG_FUN,
                ParseFromStringParam.POSITIVE_NUMBER_FORMAT
        );
        
        this.modelName = modelParam.getValue();
        this.deviceType = typeParam.getValue();
        this.manufacturer = producerParam.getValue();
        this.color = deviceColorService.getColor(colorNameParsed);
        this.manufacturerDate = dateParam.getParsedValue();
        this.price = priceParam.getParsedValue();
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
        return color.getColorName();
    }
    
    public int getColorRGB() {
        return color.getColorRGB();
    }
    
    @JsonIgnore
    public LocalDate getManufacturerDate() {
        return manufacturerDate;
    }
    
    @JsonProperty(value = MANUFACTURE_DATE_FIELD)
    public String getDateString() {
        return manufacturerDate.format(ParseFromStringParam.dateFormatter);
    }
    
    public long getPrice() {
        return price;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return price == device.price &&
                id == device.id &&
                Objects.equals(modelName, device.modelName) &&
                Objects.equals(deviceType, device.deviceType) &&
                Objects.equals(color, device.color) &&
                Objects.equals(manufacturer, device.manufacturer) &&
                Objects.equals(manufacturerDate, device.manufacturerDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(modelName, deviceType, color, price, manufacturer, manufacturerDate, color, id);
    }
    
    @Override
    public String toString() {
        return "Device{" +
                "modelName='" + modelName + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", price=" + price +
                ", manufacturer='" + manufacturer + '\'' +
                ", manufacturerDate=" + manufacturerDate +
                ", color=" + color +
                ", id=" + id +
                '}';
    }
}
