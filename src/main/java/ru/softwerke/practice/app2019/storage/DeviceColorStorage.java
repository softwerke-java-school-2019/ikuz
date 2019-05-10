package ru.softwerke.practice.app2019.storage;

import ru.softwerke.practice.app2019.model.Color;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * Storage for Color
 * Additionally stores colorRGB list for fast retrieval
 */
public class DeviceColorStorage {
    private final Map<String, Color> colorMap = new ConcurrentHashMap<>();
    private final Collection<Integer> colorRGBList = new ConcurrentLinkedDeque<>();
    
    public Color addColorToStorage(Color color) {
        if (!colorMap.containsValue(color)) {
            colorMap.put(color.getColorName(), color);
            colorRGBList.add(color.getColorRGB());
        }
        return color;
    }
    
    /**
     * Get list of colors in natural order
     * @return list if device colors
     */
    public List<Color> getColorListFromStorage() {
        return colorMap
                .values()
                .stream()
                .sorted(Comparator.comparing(Color::getColorName))
                .collect(Collectors.toList());
    }
    
    public boolean containsColorName(String colorName) {
        return colorMap.containsKey(colorName);
    }
    
    public Color getColor(String colorName) {
        return colorMap.get(colorName);
    }
    
    public Integer getColorRGB(String colorName) {
        return colorMap.get(colorName).getColorRGB();
    }
    
    public boolean containsColorRGB(Integer colorRGB) {
        return colorRGBList.contains(colorRGB);
    }
}
