package com.game.kalaha.model;

import com.game.kalaha.util.StringUtil;

public class Kalaha {

    private Player player1;
    private Player player2;
    private Board board;
    private String message;

    public Kalaha() {
        this.board = new Board();
        this.player1 = new Player();
        this.player2 = new Player();

        player1.setPlayerName("Player 1");
        player2.setPlayerName("Player 2");
        player1.setPlayerNumber(1);
        player2.setPlayerNumber(2);
        player1.setActive(true);
        player2.setActive(false);
    }

    public Kalaha(PlayerNames players) {
        this.board = new Board();
        this.player1 = new Player();
        this.player2 = new Player();

        player1.setPlayerName(StringUtil.isNullOrEmpty(players.getPlayer1Name()) ? "Player 1" : players.getPlayer1Name());
        player2.setPlayerName(StringUtil.isNullOrEmpty(players.getPlayer2Name()) ? "Player 2" : players.getPlayer2Name());
        player1.setPlayerNumber(1);
        player2.setPlayerNumber(2);
        player1.setActive(true);
        player2.setActive(false);
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(final Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(final Player player2) {
        this.player2 = player2;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(final Board board) {
        this.board = board;
    }

    public Player getActivePlayer() {
        return player1.isActive() ? player1 : player2;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
