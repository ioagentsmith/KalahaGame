package com.game.kalaha.util;

public final class StringUtil {

    private StringUtil() {
    }

    public static boolean isNullOrEmpty(final String text) {
        return text == null || text.isEmpty();
    }
}
