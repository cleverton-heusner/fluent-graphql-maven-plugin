package org.example.utils;

public class Capitalizer {

    public static String capitalize(final String word) {
        if (word.isEmpty()) {
            return word;
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}