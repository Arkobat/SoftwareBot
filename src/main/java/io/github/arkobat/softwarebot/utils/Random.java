package io.github.arkobat.softwarebot.utils;

public class Random {
    public Random() {
        random = new java.util.Random();
    }
    private static java.util.Random random;

    /**
     *  Returns a random value (int)
     *
     * @param max - Max value to be returned
     * @return - A random value between (and including) 0 and max
     */
    public static int getRandomNumber(int max) {
        return getRandomNumber(0, max);
    }

    public static int getRandomNumber(int min, int max) {
        int val = max - min;
        return val == 0 ? 0 : random.nextInt(val) + min;
    }
}
