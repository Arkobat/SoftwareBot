package io.github.arkobat.softwarebot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    private static Map<String, String> config = new HashMap<>();

    public Config() {
        Properties loadProps = new Properties();
        try {
            loadProps.loadFromXML(new FileInputStream("config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.put(Path.DB_HOST.getValue(), loadProps.getProperty("DB_HOST"));
        config.put(Path.DB_PORT.getValue(), loadProps.getProperty("DB_PORT"));
        config.put(Path.DB_NAME.getValue(), loadProps.getProperty("DB_NAME"));
        config.put(Path.DB_USER.getValue(), loadProps.getProperty("DB_USER"));
        config.put(Path.DB_PASSWORD.getValue(), loadProps.getProperty("DB_PASSWORD"));
        config.put(Path.BOT_TOKEN.getValue(), loadProps.getProperty("BOT_TOKEN"));
    }

    public enum Path {
        DB_HOST("DB_HOST"),
        DB_PORT("DB_PORT"),
        DB_NAME("DB_NAME"),
        DB_USER("DB_USER"),
        DB_PASSWORD("DB_PASSWORD"),
        BOT_TOKEN("BOT_TOKEN");

        String path;

        Path(String s) {
            path = s;
        }

        public String getValue() {
            return path;
        }
    }

    public static String get(Path path) {
        return config.get(path.getValue());
    }

    public static int getInt(Path path) {
        return Integer.valueOf(config.get(path.getValue()));
    }

}