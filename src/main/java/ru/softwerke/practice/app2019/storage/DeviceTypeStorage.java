package ru.softwerke.practice.app2019.storage;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Storage for DeviceType
 */
public class DeviceTypeStorage {
    private final Collection<String> colorList = new ConcurrentLinkedQueue<>();
    
    public String addDeviceTypeToStorage(String deviceType) {
        if (!colorList.contains(deviceType)) {
            colorList.add(deviceType);
        }
        return deviceType;
    }
    /**
     * Get list of device's types in natural order
     *
     * @return list if device's types
     */
    public List<String> getDeviceTypeListFromStorage() {
        return colorList
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public boolean containsDeviceType(String deviceType) {
        return colorList.contains(deviceType);
    }
}
