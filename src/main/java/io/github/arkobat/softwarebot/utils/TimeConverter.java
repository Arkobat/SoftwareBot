package io.github.arkobat.softwarebot.utils;

public final class TimeConverter {

    public static int getTimeInSeconds(String arg) {
        StringBuilder stringTime = new StringBuilder();
        StringBuilder factor = new StringBuilder();
        for (char c : arg.toCharArray()) {
            if (Character.isDigit(c)) {
                stringTime.append(c);
            } else if (Character.isAlphabetic(c)) {
                factor.append(c);
            }
        }
        int time;
        try {
            time = Integer.parseInt(stringTime.toString());
        } catch (NumberFormatException nfe) {
            return -1;
        }
        String fac = factor.toString().toLowerCase();
        switch (fac) {
            case "s":
            case "sec":
            case "second":
            case "seconds":
                return time;
            case "":
            case "m":
            case "min":
            case "minute":
            case "minutes":
                return time * 60;
            case "h":
            case "hour":
            case "hours":
                return time * 60 * 60;
            case "d":
            case "day":
            case "days":
                return time * 60 * 60 * 24;
            case "w":
            case "week":
            case "weeks":
                return time * 60 * 60 * 24 * 7;
            case "mo":
            case "month":
            case "months":
                return time * 60 * 60 * 24 * 30;
            case "y":
            case "year":
            case "years":
                return time * 60 * 60 * 24 * 365;
        }
        return -1;
    }

    public static String timeToString(int time) {
        String formattedTime = "";
        int timeRemoved = time / 86400;
        if (timeRemoved != 0) {
            time -= timeRemoved * 86400;
            formattedTime = timeRemoved == 1 ? formattedTime + timeRemoved + " dag " : formattedTime + timeRemoved + " dage ";
        }
        timeRemoved = time / 3600;
        if (timeRemoved != 0) {
            time -= timeRemoved * 3600;
            formattedTime = timeRemoved == 1 ? formattedTime + timeRemoved + " time " : formattedTime + timeRemoved + " timer ";
        }
        timeRemoved = time / 60;
        if (timeRemoved != 0) {
            time -= timeRemoved * 60;
            formattedTime = timeRemoved == 1 ? formattedTime + timeRemoved + " minut " : formattedTime + timeRemoved + " minutter ";
        }
        if (time != 0) {
            formattedTime = time == 1 ? formattedTime + time + " sekund " : formattedTime + time + " sekunder ";
        }
        formattedTime = formattedTime.equals("") ? "1 sekund" : formattedTime;
        return formattedTime;
    }
}