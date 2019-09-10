package io.github.arkobat.softwarebot.utils;

public class NumberUtils {
    public static Integer stringToInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
