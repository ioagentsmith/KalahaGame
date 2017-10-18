package com.game.kalaha.model;

public class Player {

    private String playerName;
    private int playerNumber;
    private int consecutiveTurns = 1;
    private boolean isActive;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getConsecutiveTurns() {
        return consecutiveTurns;
    }

    public void setConsecutiveTurns(int consecutiveTurns) {
        this.consecutiveTurns = consecutiveTurns;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
