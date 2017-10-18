package com.game.kalaha.model;

import com.game.kalaha.util.BoardEnum;

public class Pit {

    private int id;
    private int numberOfSeeds;
    private boolean isEmpty;
    private boolean isKalaha;

    public Pit() {
        this.numberOfSeeds = BoardEnum.SEEDS_PER_PIT.getValue();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getNumberOfSeeds() {
        return numberOfSeeds;
    }

    public void setNumberOfSeeds(final int numberOfSeeds) {
        this.numberOfSeeds = numberOfSeeds;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(final boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isKalaha() {
        return isKalaha;
    }

    public void setKalaha(final boolean isKalaha) {
        this.isKalaha = isKalaha;
    }

}
