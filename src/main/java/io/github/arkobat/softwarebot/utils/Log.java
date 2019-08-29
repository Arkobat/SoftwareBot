package io.github.arkobat.softwarebot.utils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;

public class Log {
    private static DateFormat df = new SimpleDateFormat("kk:mm:ss");

    private static String time() {
        return "[" + df.format(Date.from(OffsetDateTime.now().toInstant())) + "] - ";
    }

    public static void out(String string) {
        System.out.println(time() + string);
    }

    public static void debug(String string) {
        System.out.println(time() + "DEBUG: " + string);
    }
}
