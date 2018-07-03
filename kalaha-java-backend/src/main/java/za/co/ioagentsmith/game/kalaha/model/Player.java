package za.co.ioagentsmith.game.kalaha.model;

public class Player {

    private String playerName;
    private int playerNumber;
    private int consecutiveTurns = 1;
    private boolean isActive;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(final String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(final int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getConsecutiveTurns() {
        return consecutiveTurns;
    }

    public void setConsecutiveTurns(final int consecutiveTurns) {
        this.consecutiveTurns = consecutiveTurns;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

}
