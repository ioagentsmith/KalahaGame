package com.game.kalaha.util;

public enum BoardEnum {

    PITS_PER_PLAYER(7),
    SEEDS_PER_PIT(6),
    TOTAL_PITS(14),
    MAX_TURNS_PER_PLAYER_ROUND(7);

    private final int value;

    BoardEnum(final int pits) {
        this.value = pits;
    }

    public int getValue() {
        return value;
    }

}
