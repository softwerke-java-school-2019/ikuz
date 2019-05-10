package ru.softwerke.practice.app2019.service;

import ru.softwerke.practice.app2019.storage.DeviceTypeStorage;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Singleton deviceType control service
 */
public class DeviceTypeService {
    private static final DeviceTypeService INSTANCE = new DeviceTypeService(new DeviceTypeStorage());
    private static final String NOT_FOUND_DEVICE_TYPE_PROP_MESSAGE = "not found in the table of available device's types";
    
    private final DeviceTypeStorage deviceTypeStorage;
    
    private DeviceTypeService(DeviceTypeStorage deviceTypeStorage) {
        this.deviceTypeStorage = deviceTypeStorage;
    }
    
    public static DeviceTypeService getInstance() {
        return INSTANCE;
    }
    
    public String putDeviceType(String deviceType) {
        return deviceTypeStorage.addDeviceTypeToStorage(deviceType);
    }
    
    /**
     * Assert that received value of device type exist in the data base
     *
     * @param deviceType client's requested device type
     * @throws WebApplicationException with response's status 404 {@code Response.Status.NOT_FOUND}
     *         if given device type does not exist in the data base
     */
    public void checkDoesDeviceTypeExist(String deviceType) throws WebApplicationException {
        if (!deviceTypeStorage.containsDeviceType(deviceType)) {
            Response response = QueryUtils.getResponseWithMessage(
                    Response.Status.NOT_FOUND,
                    QueryUtils.NOT_FOUND_VALUE_ERROR,
                    String.format("deviceType '%s' %s", deviceType, NOT_FOUND_DEVICE_TYPE_PROP_MESSAGE)
            );
            throw new WebApplicationException(response);
        }
    }
    
    public List<String> getDeviceTypeList() {
        return deviceTypeStorage.getDeviceTypeListFromStorage();
    }
}
