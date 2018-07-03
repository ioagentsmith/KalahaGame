package za.co.ioagentsmith.game.kalaha.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PlayerNames {

    @NotNull
    @Size(min = 2)
    private String player1Name;
    @NotNull
    @Size(min = 2)
    private String player2Name;

    public PlayerNames() {}

    public PlayerNames(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(final String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(final String player2Name) {
        this.player2Name = player2Name;
    }
}
