package ru.softwerke.practice.app2019.service;

import ru.softwerke.practice.app2019.model.Color;
import ru.softwerke.practice.app2019.storage.DeviceColorStorage;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Singleton color control service
 */
public class DeviceColorService {
    private static final DeviceColorService INSTANCE = new DeviceColorService(new DeviceColorStorage());
    
    private static final String NOT_FOUND_COLOR_PROP_MESSAGE = "not found in the table of available colors";
    
    private final DeviceColorStorage deviceColorStorage;
    
    private DeviceColorService(DeviceColorStorage deviceColorStorage) {
        this.deviceColorStorage = deviceColorStorage;
    }
    
    public static DeviceColorService getInstance() {
        return INSTANCE;
    }
    
    /**
     * The method of processing adding color to the storage.
     *
     * @param color add color
     * @return color that has been added to the storage
     * @throws WebApplicationException if the storage already has a color with the same name, but another colorRGB code
     */
    public Color putColor(Color color) throws WebApplicationException {
        String colorName = color.getColorName();
        Integer colorRGB = color.getColorRGB();
        if (deviceColorStorage.containsColorName(colorName)
                && !deviceColorStorage.getColor(colorName).getColorRGB().equals(colorRGB)) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.BAD_REQUEST,
                    QueryUtils.INVALID_FORMAT_TYPE_ERROR,
                    String.format(
                            "entered colorRGB code '%d' is different from the code '%d' in the database for colorName: '%s'",
                            colorRGB,
                            deviceColorStorage.getColor(colorName).getColorRGB(),
                            deviceColorStorage.getColor(colorName).getColorName()
                    )
            );
            throw new WebApplicationException(response);
        }
        return deviceColorStorage.addColorToStorage(color);
    }
    
    public List<Color> getColorList() {
        return deviceColorStorage.getColorListFromStorage();
    }
    
    public Color getColor(String colorName) {
        checkDoesColorNameExist(colorName);
        return deviceColorStorage.getColor(colorName);
    }
    
    /**
     * Assert that received value of color rgb code exist in the data base
     *
     * @param colorRGB client's requested color rgb code
     * @throws WebApplicationException with response's status 404 {@code Response.Status.NOT_FOUND}
     *         if given color rgb code does not exist in the data base
     */
    public void checkDoesColorRGBExist(Integer colorRGB) throws WebApplicationException {
        if (!deviceColorStorage.containsColorRGB(colorRGB)) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.NOT_FOUND,
                    QueryUtils.NOT_FOUND_VALUE_ERROR,
                    String.format("сolorRGB '%d' %s", colorRGB, NOT_FOUND_COLOR_PROP_MESSAGE)
            );
            throw new WebApplicationException(response);
        }
    }
    
    /**
     * Assert that received value of color name exist in the data base
     *
     * @param colorName client's requested color name
     * @throws WebApplicationException with response's status 404 {@code Response.Status.NOT_FOUND}
     *         if given color name does not exist in the data base
     */
    public void checkDoesColorNameExist(String colorName) throws WebApplicationException {
        if (!deviceColorStorage.containsColorName(colorName)) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.NOT_FOUND,
                    QueryUtils.NOT_FOUND_TYPE_ERROR,
                    String.format("colorName '%s' %s", colorName, NOT_FOUND_COLOR_PROP_MESSAGE)
            );
            throw new WebApplicationException(response);
        }
    }
}
