package io.github.arkobat.softwarebot.utils;

import net.dv8tion.jda.core.entities.Message;

public class Utils {

    public static String[] getArgs(Message message) {
        return getArgs(message.getContentRaw());
    }

    public static String[] getArgs(String message) {
        while (message.contains("  ")) {
            message = message.replaceAll(" {2}", " ");
        }
        return message.split(" ");
    }
}
