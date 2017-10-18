package com.game.kalaha.util;

public final class StringUtil {
    private StringUtil() {
    }

    public static boolean isNullOrEmpty(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }

        return false;
    }
}
