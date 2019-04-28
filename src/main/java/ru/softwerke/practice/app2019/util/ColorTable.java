package ru.softwerke.practice.app2019.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ColorTable {
    private static final Map<String, Integer> colors = new HashMap<>();
    
    static {
        colors.put("black", 0);
        colors.put("gray", 8421504);
        colors.put("golden", 16766720);
        colors.put("dark-blue", 255);
        colors.put("silver", 12632256);
        colors.put("brown", 9849600);
        colors.put("orange", 16753920);
        colors.put("beige", 16119260);
        colors.put("yellow", 16776960);
        colors.put("green", 32768);
        colors.put("blue", 3975935);
        colors.put("purple", 9109759);
        colors.put("pink", 16519104);
    }
    
    static Map<String, Integer> getColors() {
        return colors;
    }
}
