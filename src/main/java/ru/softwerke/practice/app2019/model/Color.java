package ru.softwerke.practice.app2019.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;
import ru.softwerke.practice.app2019.util.StringParam;

import javax.ws.rs.WebApplicationException;
import java.util.Objects;

public class Color {
    private static final String COLOR_NAME_FIELD = "colorName";
    private static final String COLOR_RGB_FIELD = "colorRGB";
    
    private final String colorName;
    private final Integer colorRGB;
    
    @JsonCreator
    public Color(
            @JsonProperty(value = COLOR_NAME_FIELD, required = true) String colorName,
            @JsonProperty(value = COLOR_RGB_FIELD, required = true) String colorRGB) throws WebApplicationException {
        StringParam colorNameParam = new StringParam(
                colorName,
                COLOR_NAME_FIELD
        );
        ParseFromStringParam<Integer> colorRGBParam;
        colorRGBParam = new ParseFromStringParam<>(
                colorRGB,
                COLOR_RGB_FIELD,
                ParseFromStringParam.PARSE_INTEGER_FUN,
                ParseFromStringParam.POSITIVE_NUMBER_FORMAT
        );
        
        this.colorName = colorNameParam.getValue();
        this.colorRGB = colorRGBParam.getParsedValue();
    }
    
    public Color(String colorName, Integer colorRGB) {
        this.colorName = colorName;
        this.colorRGB = colorRGB;
    }
    
    public String getColorName() {
        return colorName;
    }
    
    public Integer getColorRGB() {
        return colorRGB;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return colorRGB.equals(color.colorRGB) &&
                Objects.equals(colorName, color.colorName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(colorName, colorRGB);
    }
    
    @Override
    public String toString() {
        return "Color{" +
                ", colorName='" + colorName + '\'' +
                ", colorRGB=" + colorRGB +
                '}';
    }
}