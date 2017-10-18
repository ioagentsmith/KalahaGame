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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("gameService")
@Transactional
public class GameService {

    String message = "";

    public Kalaha startTheGame(int clickedPitId, Kalaha game) {
        Board newBoard = game.getBoard();
        int pitIndex = clickedPitId + 1;
        Pit clickedPit = game.getBoard().getPitById(clickedPitId);
        int seedsInClickedPit = clickedPit.getNumberOfSeeds();
        Pit[] pits = game.getBoard().getPits();
        pits[clickedPitId].setNumberOfSeeds(0);
        Player currentActivePlayer = game.getActivePlayer();

        game = placeSeeds(game, pitIndex, seedsInClickedPit, pits, currentActivePlayer);

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

    private Kalaha placeSeeds(Kalaha game, int pitIndex, int seedsInClickedPit, final Pit[] pits, final Player currentActivePlayer) {
        while (pitIndex < BoardEnum.TOTAL_PITS.getValue() && seedsInClickedPit > 0) {
            if (seedsInClickedPit == 1) {
                message = String.format(
                        "Last seed in %s's own Kalaha. %s, you are still the active player!",
                        game.getActivePlayer().getPlayerName(),
                        game.getActivePlayer().getPlayerName()
                );
                if (isLastSeedInOwnEmptyPit(currentActivePlayer, pits[pitIndex])) {
                    Pit oppositePit = getOppositePit(game, pits[pitIndex].getId());
                    int numberOfSeedsToAdd = oppositePit.getNumberOfSeeds() + 1;
                    if (currentActivePlayer.getPlayerNumber() == 1) {
                        pits[BoardEnum.PITS_PER_PLAYER.getValue() - 1].setNumberOfSeeds(pits[BoardEnum.PITS_PER_PLAYER.getValue() - 1].getNumberOfSeeds() + numberOfSeedsToAdd);
                    } else {
                        pits[BoardEnum.TOTAL_PITS.getValue() - 1].setNumberOfSeeds(pits[BoardEnum.TOTAL_PITS.getValue() - 1].getNumberOfSeeds() + numberOfSeedsToAdd);
                    }
                    pits[oppositePit.getId()].setNumberOfSeeds(0);
                    pits[pits[pitIndex].getId()].setNumberOfSeeds(0);
                    game = changeActivePlayer(game, currentActivePlayer);
                    break;
                } else if (!isPlayerKalaha(currentActivePlayer, pits[pitIndex].getId())) {
                    //end turn if last placed seed not in active player's kahala
                    game = changeActivePlayer(game, currentActivePlayer);
                } else if (currentActivePlayer.getConsecutiveTurns() >= BoardEnum.MAX_TURNS_PER_PLAYER_ROUNDS.getValue()) {
                    //end player's turn if 7 consecutive moves have been made
                    game = changeActivePlayer(game, currentActivePlayer);
                }
            }

            seedsInClickedPit = updateRemainingSeedsToBePlaced(pits[pitIndex], seedsInClickedPit, !pits[pitIndex].isKalaha() || isPlayerKalaha(currentActivePlayer, pits[pitIndex].getId()));

            pitIndex = incrementPitIdForSeedsToBePlaced(pitIndex, seedsInClickedPit);
        }

        return game;
    }

    private int incrementPitIdForSeedsToBePlaced(int pitIndex, final int seedsInClickedPit) {
        //restart counter if end of board has been reached and players still needs to place seeds
        if (pitIndex == BoardEnum.TOTAL_PITS.getValue() - 1 && seedsInClickedPit > 0) {
            pitIndex = 0;
        } else {
            pitIndex++;
        }
        return pitIndex;
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
        String playerWinsText = "GAME FINISHED!!! PLAYER 1 HAS %s SEEDS AND PLAYER 2 HAS %s SEEDS. %s WINS!!!";
        if (player1TotalNoOfSeeds > player2TotalNoOfSeeds) {
            message = String.format(
                    playerWinsText,
                    player1TotalNoOfSeeds,
                    player2TotalNoOfSeeds,
                    game.getPlayer1().getPlayerName().toUpperCase()
            );
        } else if (player1TotalNoOfSeeds < player2TotalNoOfSeeds) {
            message = String.format(
                    playerWinsText,
                    player1TotalNoOfSeeds,
                    player2TotalNoOfSeeds,
                    game.getPlayer2().getPlayerName().toUpperCase()
            );
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

        message = String.format("%s, it is now your turn.", game.getActivePlayer().getPlayerName());
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
        /*List<Pit> pitsForPlayer = getPlayerPits(board, player);
        int numberOfSeedsForPlayer = 0;
        for (Pit pit : pitsForPlayer) {
            numberOfSeedsForPlayer += pit.getNumberOfSeeds();
        }
        return numberOfSeedsForPlayer;*/

        List<Pit> pitsForPlayer = getPlayerPits(board, player);
        int numberOfSeedsForPlayer = pitsForPlayer.stream().mapToInt(Pit::getNumberOfSeeds).sum();

        return numberOfSeedsForPlayer;
    }

    public List<Pit> getPlayerPits(Board board, Player player) {
        Pit[] allPits = board.getPits();
        List<Pit> pits = new ArrayList<>();

        if (player.getPlayerNumber() == 1) {
            for (int i = 0; i < BoardEnum.PITS_PER_PLAYER.getValue() - 2; i++) {
                pits.add(allPits[i]);
            }
            //pits = IntStream.range(0, BoardEnum.PITS_PER_PLAYER.getValue() - 2).mapToObj(i -> allPits[i]).collect(Collectors.toList());
        } else {
            for (int i = BoardEnum.PITS_PER_PLAYER.getValue(); i < BoardEnum.TOTAL_PITS.getValue() - 1; i++) {
                pits.add(allPits[i]);
            }
            //pits = IntStream.range(BoardEnum.PITS_PER_PLAYER.getValue(), BoardEnum.TOTAL_PITS.getValue() - 1).mapToObj(i -> allPits[i]).collect(Collectors.toList());
        }
        return pits;
    }

}