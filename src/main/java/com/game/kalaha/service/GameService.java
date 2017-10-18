package com.game.kalaha.service;

import com.game.kalaha.model.Board;
import com.game.kalaha.model.Kalaha;
import com.game.kalaha.model.Pit;
import com.game.kalaha.model.Player;
import com.game.kalaha.util.BoardEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("gameService")
@Transactional
public class GameService {

    String message = "";

    public Kalaha startTheGame(int clickedPitId, Kalaha game) {
        Board newBoard = game.getBoard();
        int i = clickedPitId + 1;
        Pit clickedPit = game.getBoard().getPitById(clickedPitId);
        int seedsInClickedPit = clickedPit.getNumberOfSeeds();
        Pit[] pits = game.getBoard().getPits();
        pits[clickedPitId].setNumberOfSeeds(0);
        Player currentActivePlayer = game.getActivePlayer();

        game = placeSeeds(game, i, seedsInClickedPit, pits, currentActivePlayer);

        updatePlayerTurns(currentActivePlayer);

        newBoard.setPits(pits);

        game = checkForWinningConditions(game, newBoard, pits);

        game.setMessage(message);
        game.setBoard(newBoard);

        return game;
    }

    private void updatePlayerTurns(final Player currentActivePlayer) {
        if (currentActivePlayer.getConsecutiveTurns() < BoardEnum.MAX_TURNS_PER_PLAYER_ROUNDS.getValue()) {
            currentActivePlayer.setConsecutiveTurns(currentActivePlayer.getConsecutiveTurns() + 1);
        }
    }

    private Kalaha placeSeeds(Kalaha game, int i, int seedsInClickedPit, final Pit[] pits, final Player currentActivePlayer) {
        while (i < BoardEnum.TOTAL_PITS.getValue() && seedsInClickedPit > 0) {
            if (seedsInClickedPit == 1) {
                message = "Last seed in " + game.getActivePlayer().getPlayerName() + "'s own Kalaha. " + game.getActivePlayer().getPlayerName() + ", you are still the active player!";
                if (isLastSeedInOwnEmptyPit(currentActivePlayer, pits[i])) {
                    Pit oppositePit = getOppositePit(game, pits[i].getId());
                    int numberOfSeedsToAdd = oppositePit.getNumberOfSeeds() + 1;
                    if (currentActivePlayer.getPlayerNumber() == 1) {
                        pits[BoardEnum.PITS_PER_PLAYER.getValue() - 1].setNumberOfSeeds(pits[BoardEnum.PITS_PER_PLAYER.getValue() - 1].getNumberOfSeeds() + numberOfSeedsToAdd);
                    } else {
                        pits[BoardEnum.TOTAL_PITS.getValue() - 1].setNumberOfSeeds(pits[BoardEnum.TOTAL_PITS.getValue() - 1].getNumberOfSeeds() + numberOfSeedsToAdd);
                    }
                    pits[oppositePit.getId()].setNumberOfSeeds(0);
                    pits[pits[i].getId()].setNumberOfSeeds(0);
                    game = changeActivePlayer(game, currentActivePlayer);
                    break;
                } else if (!isPlayerKalaha(currentActivePlayer, pits[i].getId())) {
                    //end turn if last placed seed not in active player's kahala
                    game = changeActivePlayer(game, currentActivePlayer);
                } else if (currentActivePlayer.getConsecutiveTurns() >= BoardEnum.MAX_TURNS_PER_PLAYER_ROUNDS.getValue()) {
                    //end player's turn if 7 consecutive moves have been made
                    game = changeActivePlayer(game, currentActivePlayer);
                }
            }

            seedsInClickedPit = updateRemainingSeedsToBePlaced(pits[i], seedsInClickedPit, !pits[i].isKalaha() || isPlayerKalaha(currentActivePlayer, pits[i].getId()));

            i = incrementPitIdForSeedsToBePlaced(i, seedsInClickedPit);
        }

        return game;
    }

    private int incrementPitIdForSeedsToBePlaced(int i, final int seedsInClickedPit) {
        //restart counter if end of board has been reached and players still needs to place seeds
        if (i == BoardEnum.TOTAL_PITS.getValue() - 1 && seedsInClickedPit > 0) {
            i = 0;
        } else {
            i++;
        }
        return i;
    }

    private int updateRemainingSeedsToBePlaced(final Pit pit, int seedsInClickedPit, final boolean isPlayerKalaha) {
        //don't put seed in opponent's Kalaha
        if (isPlayerKalaha) {
            pit.setNumberOfSeeds(pit.getNumberOfSeeds() + 1);
            seedsInClickedPit--;
        }
        return seedsInClickedPit;
    }

    private Kalaha checkForWinningConditions(Kalaha game, final Board newBoard, final Pit[] pits) {
        int player1NoOfSeeds = getNumberOfSeedsForPlayer(newBoard, game.getPlayer1());
        int player2NoOfSeeds = getNumberOfSeedsForPlayer(newBoard, game.getPlayer2());
        if (player1NoOfSeeds == 0 || player2NoOfSeeds == 0) {
            game = gameOver(game, pits, player1NoOfSeeds, player2NoOfSeeds);
        }
        return game;
    }

    private Kalaha gameOver(Kalaha game, Pit[] pits, int player1NoOfSeeds, int player2NoOfSeeds) {
        Pit player1Kalaha = pits[BoardEnum.PITS_PER_PLAYER.getValue() - 1];
        Pit player2Kalaha = pits[BoardEnum.TOTAL_PITS.getValue() - 1];
        int player1TotalNoOfSeeds = player1Kalaha.getNumberOfSeeds() + player1NoOfSeeds;
        int player2TotalNoOfSeeds = player2Kalaha.getNumberOfSeeds() + player2NoOfSeeds;
        if (player1TotalNoOfSeeds > player2TotalNoOfSeeds) {
            message = "GAME FINISHED!!! PLAYER 1 HAS "
                    + player1TotalNoOfSeeds
                    + " SEEDS AND PLAYER 2 HAS "
                    + player2TotalNoOfSeeds
                    + " SEEDS. "
                    + game.getPlayer1().getPlayerName().toUpperCase()
                    + "  WINS!!!";
        } else if (player1TotalNoOfSeeds < player2TotalNoOfSeeds) {
            message = "GAME FINISHED!!! PLAYER 1 HAS "
                    + player1TotalNoOfSeeds
                    + " SEEDS AND PLAYER 2 HAS "
                    + player2TotalNoOfSeeds
                    + " SEEDS. "
                    + game.getPlayer2().getPlayerName().toUpperCase()
                    + "  WINS!!!";
        } else {
            message = "GAME FINISHED!!! IT'S A DRAW!!!";
        }
        game.getPlayer1().setActive(false);
        game.getPlayer2().setActive(false);
        return game;
    }

    public boolean isLastSeedInOwnEmptyPit(Player currentActivePlayer, Pit pit) {
        return !isPlayerKalaha(currentActivePlayer, pit.getId()) && pit.getNumberOfSeeds() == 0 && isOwnPit(currentActivePlayer, pit.getId());
    }

    public Kalaha changeActivePlayer(Kalaha game, Player activePlayer) {
        if (activePlayer.getPlayerNumber() == 1) {
            game.getPlayer1().setActive(false);
            game.getPlayer2().setActive(true);
        } else {
            game.getPlayer1().setActive(true);
            game.getPlayer2().setActive(false);
        }

        game.getPlayer1().setConsecutiveTurns(1);
        game.getPlayer2().setConsecutiveTurns(1);

        message = game.getActivePlayer().getPlayerName() + ", it is now your turn.";
        game.setMessage(message);

        return game;
    }

    public boolean isOwnPit(Player player, int pitId) {
        return ((player.getPlayerNumber() == 1 && pitId < BoardEnum.PITS_PER_PLAYER.getValue()) ||
                (player.getPlayerNumber() == 2 && pitId > BoardEnum.PITS_PER_PLAYER.getValue()));
    }

    public Pit getOppositePit(Kalaha game, int pitId) {
        return game.getBoard().getPitById(BoardEnum.TOTAL_PITS.getValue() - 2 - pitId);
    }

    public boolean isPlayerKalaha(Player player, int pitId) {
        return pitId == player.getPlayerNumber() * 7 - 1;
    }

    public int getNumberOfSeedsForPlayer(Board board, Player player) {
        List<Pit> pitsForPlayer = getPlayerPits(board, player);
        int numberOfSeedsForPlayer = 0;

        for (Pit pit : pitsForPlayer) {
            numberOfSeedsForPlayer += pit.getNumberOfSeeds();
        }

        return numberOfSeedsForPlayer;
    }

    public List<Pit> getPlayerPits(Board board, Player player) {
        Pit[] allPits = board.getPits();
        List<Pit> pits = new ArrayList<>();

        if (player.getPlayerNumber() == 1) {
            for (int i = 0; i < BoardEnum.PITS_PER_PLAYER.getValue() - 2; i++) {
                pits.add(allPits[i]);
            }
        } else {
            for (int i = BoardEnum.PITS_PER_PLAYER.getValue(); i < BoardEnum.TOTAL_PITS.getValue() - 1; i++) {
                pits.add(allPits[i]);
            }
        }
        return pits;
    }

}