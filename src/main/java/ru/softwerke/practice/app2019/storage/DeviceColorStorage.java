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
        if (!this.containsColorName(color.getColorName())) {
            colorMap.put(color.getColorName(), color);
            colorRGBList.add(color.getColorRGB());
        }
        return color;
    }
    
    /**
     * Get list of colors in natural order of their names
     *
     * @return list if device colors
     */
    public List<Color> getColorListFromStorage() {
        return colorMap
                .values()
                .stream()
                .sorted(Comparator.comparing(Color::getColorName))
                .collect(Collectors.toList());
    }
    
    /**
     * Check that storage contains color with such name ignoring case
     *
     * @param colorName name to check
     * @return {@code true} if the color with such name is present in storage otherwise {@code false}
     */
    public boolean containsColorName(String colorName) {
        for (String color : colorMap.keySet()) {
            if (colorName.equalsIgnoreCase(color)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get color by colorName
     *
     * @param colorName name with which the specified color is to be associated
     * @return the color associated with the passed colorName if it present in storage otherwise {@code null}
     */
    public Color getColor(String colorName) {
        for (String color : colorMap.keySet()) {
            if (colorName.equalsIgnoreCase(color)) {
                return colorMap.get(color);
            }
        }
        return null;
    }
    
    /**
     * Check that storage contains color with such colorRGB code
     *
     * @param colorRGB code to check
     * @return {@code true} if the color with such colorRGB code is present in storage otherwise {@code false}
     */
    public boolean containsColorRGB(Integer colorRGB) {
        return colorRGBList.contains(colorRGB);
    }
}
