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

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfSeeds() {
        return numberOfSeeds;
    }

    public void setNumberOfSeeds(int numberOfSeeds) {
        this.numberOfSeeds = numberOfSeeds;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isKalaha() {
        return isKalaha;
    }

    public void setKalaha(boolean isKalaha) {
        this.isKalaha = isKalaha;
    }

}
