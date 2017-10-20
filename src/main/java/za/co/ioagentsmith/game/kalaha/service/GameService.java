package za.co.ioagentsmith.game.kalaha.service;

import za.co.ioagentsmith.game.kalaha.model.Board;
import za.co.ioagentsmith.game.kalaha.model.Kalaha;
import za.co.ioagentsmith.game.kalaha.model.Pit;
import za.co.ioagentsmith.game.kalaha.model.Player;
import za.co.ioagentsmith.game.kalaha.util.BoardEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("gameService")
public class GameService {

    String message = "";

    public Kalaha startTheGame(final int clickedPitId, final Kalaha game) {
        Board newBoard = game.getBoard();
        int pitIndex = clickedPitId + 1;
        Pit clickedPit = game.getBoard().getPitById(clickedPitId);
        int seedsInClickedPit = clickedPit.getNumberOfSeeds();
        Pit[] pits = game.getBoard().getPits();
        pits[clickedPitId].setNumberOfSeeds(0);
        Player currentActivePlayer = game.getActivePlayer();

        placeSeeds(game, pitIndex, seedsInClickedPit, pits, currentActivePlayer);
        updatePlayerConsecutiveTurns(currentActivePlayer);

        newBoard.setPits(pits);
        checkForWinningConditions(game, newBoard, pits);

        game.setMessage(message);
        game.setBoard(newBoard);

        return game;
    }

    private void updatePlayerConsecutiveTurns(final Player currentActivePlayer) {
        if (currentActivePlayer.getConsecutiveTurns() < BoardEnum.MAX_TURNS_PER_PLAYER_ROUND.getValue()) {
            currentActivePlayer.setConsecutiveTurns(currentActivePlayer.getConsecutiveTurns() + 1);
        }
    }

    private void placeSeeds(final Kalaha game, final int pitIndex, final int seedsInClickedPit, final Pit[] pits, final Player currentActivePlayer) {
        int copiedPitIndex = pitIndex;
        int copiedSeedsInClickedPit = seedsInClickedPit;
        while (copiedPitIndex < BoardEnum.TOTAL_PITS.getValue() && copiedSeedsInClickedPit > 0) {
            if (copiedSeedsInClickedPit == 1) {
                message = String.format(
                        "Last seed in %s's own Kalaha. %s, you are still the active player!",
                        game.getActivePlayer().getPlayerName(),
                        game.getActivePlayer().getPlayerName());
                if (isLastSeedInOwnEmptyPit(currentActivePlayer, pits[copiedPitIndex])) {
                    Pit oppositePit = getOppositePit(game, pits[copiedPitIndex].getId());
                    int numberOfSeedsToAdd = oppositePit.getNumberOfSeeds() + 1;
                    if (currentActivePlayer.getPlayerNumber() == 1) {
                        pits[BoardEnum.PITS_PER_PLAYER.getValue() - 1].setNumberOfSeeds(pits[BoardEnum.PITS_PER_PLAYER.getValue() - 1].getNumberOfSeeds() + numberOfSeedsToAdd);
                    } else {
                        pits[BoardEnum.TOTAL_PITS.getValue() - 1].setNumberOfSeeds(pits[BoardEnum.TOTAL_PITS.getValue() - 1].getNumberOfSeeds() + numberOfSeedsToAdd);
                    }
                    pits[oppositePit.getId()].setNumberOfSeeds(0);
                    pits[pits[copiedPitIndex].getId()].setNumberOfSeeds(0);
                    changeActivePlayer(game, currentActivePlayer);
                    break;
                } else if (!isPlayerKalaha(currentActivePlayer, pits[copiedPitIndex].getId())) {
                    //end turn if last placed seed not in active player's kahala
                    changeActivePlayer(game, currentActivePlayer);
                } else if (currentActivePlayer.getConsecutiveTurns() >= BoardEnum.MAX_TURNS_PER_PLAYER_ROUND.getValue()) {
                    //end player's turn if 7 consecutive moves have been made
                    changeActivePlayer(game, currentActivePlayer);
                }
            }
            copiedSeedsInClickedPit = updateRemainingSeedsToBePlaced(pits[copiedPitIndex], copiedSeedsInClickedPit, !pits[copiedPitIndex].isKalaha() || isPlayerKalaha(currentActivePlayer, pits[copiedPitIndex].getId()));
            copiedPitIndex = incrementPitIdForSeedsToBePlaced(copiedPitIndex, copiedSeedsInClickedPit);
        }
    }

    private int incrementPitIdForSeedsToBePlaced(final int pitIndex, final int seedsInClickedPit) {
        //restart counter if end of board has been reached and players still needs to place seeds
        if (pitIndex == BoardEnum.TOTAL_PITS.getValue() - 1 && seedsInClickedPit > 0) {
            return 0;
        } else {
            return pitIndex + 1;
        }
    }

    private int updateRemainingSeedsToBePlaced(final Pit pit, final int seedsInClickedPit, final boolean isPlayerKalaha) {
        //don't put seed in opponent's Kalaha
        if (isPlayerKalaha) {
            pit.setNumberOfSeeds(pit.getNumberOfSeeds() + 1);
            return seedsInClickedPit - 1;
        }
        return seedsInClickedPit;
    }

    private void checkForWinningConditions(final Kalaha game, final Board newBoard, final Pit[] pits) {
        int player1NoOfSeeds = getNumberOfSeedsForPlayer(newBoard, game.getPlayer1());
        int player2NoOfSeeds = getNumberOfSeedsForPlayer(newBoard, game.getPlayer2());
        if (player1NoOfSeeds == 0 || player2NoOfSeeds == 0) {
            gameOver(game, pits, player1NoOfSeeds, player2NoOfSeeds);
        }
    }

    private void gameOver(final Kalaha game, final Pit[] pits, final int player1NoOfSeeds, final int player2NoOfSeeds) {
        Pit player1Kalaha = pits[BoardEnum.PITS_PER_PLAYER.getValue() - 1];
        Pit player2Kalaha = pits[BoardEnum.TOTAL_PITS.getValue() - 1];
        int player1TotalNoOfSeeds = player1Kalaha.getNumberOfSeeds() + player1NoOfSeeds;
        int player2TotalNoOfSeeds = player2Kalaha.getNumberOfSeeds() + player2NoOfSeeds;
        String playerWinsText = "GAME FINISHED!!! %s HAS %s SEEDS AND %s HAS %s SEEDS. %s WINS!!!";
        if (player1TotalNoOfSeeds > player2TotalNoOfSeeds) {
            message = String.format(
                    playerWinsText,
                    game.getPlayer1().getPlayerName().toUpperCase(),
                    player1TotalNoOfSeeds,
                    game.getPlayer2().getPlayerName().toUpperCase(),
                    player2TotalNoOfSeeds,
                    game.getPlayer1().getPlayerName().toUpperCase()
            );
        } else if (player1TotalNoOfSeeds < player2TotalNoOfSeeds) {
            message = String.format(
                    playerWinsText,
                    game.getPlayer1().getPlayerName().toUpperCase(),
                    player1TotalNoOfSeeds,
                    game.getPlayer2().getPlayerName().toUpperCase(),
                    player2TotalNoOfSeeds,
                    game.getPlayer2().getPlayerName().toUpperCase()
            );
        } else {
            message = "GAME FINISHED!!! IT'S A DRAW!!!";
        }
        game.getPlayer1().setActive(false);
        game.getPlayer2().setActive(false);
    }

    boolean isLastSeedInOwnEmptyPit(Player currentActivePlayer, Pit pit) {
        return !isPlayerKalaha(currentActivePlayer, pit.getId()) && pit.getNumberOfSeeds() == 0 && isOwnPit(currentActivePlayer, pit.getId());
    }

    private void changeActivePlayer(final Kalaha game, final Player activePlayer) {
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
    }

    boolean isOwnPit(final Player player, final int pitId) {
        return ((player.getPlayerNumber() == 1 && pitId < BoardEnum.PITS_PER_PLAYER.getValue()) ||
                (player.getPlayerNumber() == 2 && pitId > BoardEnum.PITS_PER_PLAYER.getValue()));
    }

    Pit getOppositePit(final Kalaha game, final int pitId) {
        return game.getBoard().getPitById(BoardEnum.TOTAL_PITS.getValue() - 2 - pitId);
    }

    boolean isPlayerKalaha(final Player player, final int pitId) {
        return pitId == player.getPlayerNumber() * 7 - 1;
    }

    int getNumberOfSeedsForPlayer(final Board board, final Player player) {
        List<Pit> pitsForPlayer = getPlayerPits(board, player);
        return pitsForPlayer.stream().mapToInt(Pit::getNumberOfSeeds).sum();
    }

    private List<Pit> getPlayerPits(final Board board, final Player player) {
        Pit[] allPits = board.getPits();
        List<Pit> pits;

        if (player.getPlayerNumber() == 1) {
            pits = IntStream.range(0, BoardEnum.PITS_PER_PLAYER.getValue() - 2).mapToObj(i -> allPits[i]).collect(Collectors.toList());
        } else {
            pits = IntStream.range(BoardEnum.PITS_PER_PLAYER.getValue(), BoardEnum.TOTAL_PITS.getValue() - 1).mapToObj(i -> allPits[i]).collect(Collectors.toList());
        }
        return pits;
    }

}