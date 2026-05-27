package net.mcreator.minigames.util;

public class ColorUtils {
    public static int parseHex(String value) {
        if (value == null || value.isEmpty()) return -1;
        String h = value.startsWith("#") ? value.substring(1) : value;
        if (!h.matches("^[0-9a-fA-F]{6}$")) return -1;
        try {
            return Integer.parseInt(h, 16);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}