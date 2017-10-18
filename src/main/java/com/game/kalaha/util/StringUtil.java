package com.game.kalaha.util;

public final class StringUtil {
    private StringUtil() {
    }

    public static boolean isNullOrEmpty(final String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }

        return false;
    }
}
