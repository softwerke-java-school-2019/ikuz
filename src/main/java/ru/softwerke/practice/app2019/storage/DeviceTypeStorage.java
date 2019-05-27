package ru.softwerke.practice.app2019.storage;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Storage for DeviceType
 */
public class DeviceTypeStorage {
    private final Collection<String> deviceList = new ConcurrentLinkedQueue<>();
    
    public String addDeviceTypeToStorage(String deviceType) {
        if (!this.containsDeviceType(deviceType)) {
            deviceList.add(deviceType);
        }
        return deviceType;
    }
    
    /**
     * Get list of device's types in natural order
     *
     * @return list if device's types
     */
    public List<String> getDeviceTypeListFromStorage() {
        return deviceList
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }
    
    /**
     * Check that storage contains deviceType with such name
     *
     * @param deviceTypeName name to check
     * @return {@code true} if the color with such typeName is present in storage otherwise {@code false}
     */
    public boolean containsDeviceType(String deviceTypeName) {
        for (String type : deviceList) {
            if (deviceTypeName.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}
