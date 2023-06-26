package za.co.ioagentsmith.game.kalaha.service

import za.co.ioagentsmith.game.kalaha.model.Board
import za.co.ioagentsmith.game.kalaha.model.Kalaha
import za.co.ioagentsmith.game.kalaha.model.Pit
import za.co.ioagentsmith.game.kalaha.model.Player
import za.co.ioagentsmith.game.kalaha.util.BoardEnum
import org.springframework.stereotype.Service

import java.util.ArrayList

@Service("gameService")
class GameService {

    internal var message = ""

    fun startTheGame(clickedPitId: Int, game: Kalaha): Kalaha {
        val newBoard = game.board
        val pitIndex = clickedPitId + 1
        val clickedPit = game.board!!.getPitById(clickedPitId)
        val seedsInClickedPit = clickedPit.numberOfSeeds
        val pits = game.board!!.pits
        pits!![clickedPitId].numberOfSeeds = 0
        val currentActivePlayer = game.activePlayer

        placeSeeds(game, pitIndex, seedsInClickedPit, pits, currentActivePlayer)
        updatePlayerConsecutiveTurns(currentActivePlayer!!)

        newBoard!!.pits = pits
        checkForWinningConditions(game, newBoard, pits)

        game.message = message
        game.board = newBoard

        return game
    }

    internal fun updatePlayerConsecutiveTurns(currentActivePlayer: Player) {
        if (currentActivePlayer.consecutiveTurns < BoardEnum.MAX_TURNS_PER_PLAYER_ROUND.value) {
            currentActivePlayer.consecutiveTurns = currentActivePlayer.consecutiveTurns + 1
        }
    }

    internal fun placeSeeds(game: Kalaha, pitIndex: Int, seedsInClickedPit: Int, pits: List<Pit>, currentActivePlayer: Player?) {
        var copiedPitIndex = pitIndex
        var copiedSeedsInClickedPit = seedsInClickedPit
        while (copiedPitIndex < BoardEnum.TOTAL_PITS.value && copiedSeedsInClickedPit > 0) {
            if (copiedSeedsInClickedPit == 1) {
                message = String.format(
                        "Last seed in %s's own Kalaha. %s, you are still the active player!",
                        game.activePlayer!!.playerName,
                        game.activePlayer!!.playerName)
                if (isLastSeedInOwnEmptyPit(currentActivePlayer!!, pits[copiedPitIndex])) {
                    val oppositePit = getOppositePit(game, pits[copiedPitIndex].id)
                    val numberOfSeedsToAdd = oppositePit.numberOfSeeds + 1
                    if (currentActivePlayer!!.playerNumber == 1) {
                        pits[BoardEnum.PITS_PER_PLAYER.value - 1].numberOfSeeds = pits[BoardEnum.PITS_PER_PLAYER.value - 1].numberOfSeeds + numberOfSeedsToAdd
                    } else {
                        pits[BoardEnum.TOTAL_PITS.value - 1].numberOfSeeds = pits[BoardEnum.TOTAL_PITS.value - 1].numberOfSeeds + numberOfSeedsToAdd
                    }
                    pits[oppositePit.id].numberOfSeeds = 0
                    pits[pits[copiedPitIndex].id].numberOfSeeds = 0
                    changeActivePlayer(game, currentActivePlayer)
                    break
                } else if (!isPlayerKalaha(currentActivePlayer, pits[copiedPitIndex].id)) {
                    //end turn if last placed seed not in active player's kahala
                    changeActivePlayer(game, currentActivePlayer!!)
                } else if (currentActivePlayer!!.consecutiveTurns >= BoardEnum.MAX_TURNS_PER_PLAYER_ROUND.value) {
                    //end player's turn if 7 consecutive moves have been made
                    changeActivePlayer(game, currentActivePlayer)
                }
            }
            copiedSeedsInClickedPit = updateRemainingSeedsToBePlaced(
                    pits[copiedPitIndex],
                    copiedSeedsInClickedPit,
                    !pits[copiedPitIndex].isKalaha || isPlayerKalaha(currentActivePlayer, pits[copiedPitIndex].id)
            )
            copiedPitIndex = incrementPitIdForSeedsToBePlaced(copiedPitIndex, copiedSeedsInClickedPit)
        }
    }

    internal fun incrementPitIdForSeedsToBePlaced(pitIndex: Int, seedsInClickedPit: Int): Int {
        //restart counter if end of board has been reached and players still needs to place seeds
        return if (pitIndex == BoardEnum.TOTAL_PITS.value - 1 && seedsInClickedPit > 0) {
            0
        } else {
            pitIndex + 1
        }
    }

    internal fun updateRemainingSeedsToBePlaced(pit: Pit, seedsInClickedPit: Int, isPlayerKalaha: Boolean): Int {
        //don't put seed in opponent's Kalaha
        if (isPlayerKalaha) {
            pit.numberOfSeeds = pit.numberOfSeeds + 1
            return seedsInClickedPit - 1
        }
        return seedsInClickedPit
    }

    internal fun checkForWinningConditions(game: Kalaha, newBoard: Board, pits: List<Pit>) {
        val player1NoOfSeeds = getNumberOfSeedsForPlayer(newBoard, game.player1)
        val player2NoOfSeeds = getNumberOfSeedsForPlayer(newBoard, game.player2)
        if (player1NoOfSeeds == 0 || player2NoOfSeeds == 0) {
            gameOver(game, pits, player1NoOfSeeds, player2NoOfSeeds)
        }
    }

    internal fun gameOver(game: Kalaha, pits: List<Pit>, player1NoOfSeeds: Int, player2NoOfSeeds: Int) {
        val player1Kalaha = pits[BoardEnum.PITS_PER_PLAYER.value - 1]
        val player2Kalaha = pits[BoardEnum.TOTAL_PITS.value - 1]
        val player1TotalNoOfSeeds = player1Kalaha.numberOfSeeds + player1NoOfSeeds
        val player2TotalNoOfSeeds = player2Kalaha.numberOfSeeds + player2NoOfSeeds
        val playerWinsText = "GAME FINISHED!!! %s HAS %s SEEDS AND %s HAS %s SEEDS. %s WINS!!!"
        if (player1TotalNoOfSeeds > player2TotalNoOfSeeds) {
            message = String.format(
                    playerWinsText,
                    game.player1!!.playerName!!.uppercase(),
                    player1TotalNoOfSeeds,
                    game.player2!!.playerName!!.uppercase(),
                    player2TotalNoOfSeeds,
                    game.player1!!.playerName!!.uppercase()
            )
        } else if (player1TotalNoOfSeeds < player2TotalNoOfSeeds) {
            message = String.format(
                    playerWinsText,
                    game.player1!!.playerName!!.uppercase(),
                    player1TotalNoOfSeeds,
                    game.player2!!.playerName!!.uppercase(),
                    player2TotalNoOfSeeds,
                    game.player2!!.playerName!!.uppercase()
            )
        } else {
            message = "GAME FINISHED!!! IT'S A DRAW!!!"
        }
        game.player1!!.isActive = false
        game.player2!!.isActive = false
    }

    fun isLastSeedInOwnEmptyPit(currentActivePlayer: Player, pit: Pit): Boolean {
        return !isPlayerKalaha(currentActivePlayer, pit.id) && pit.numberOfSeeds == 0 && isOwnPit(currentActivePlayer, pit.id)
    }

    internal fun changeActivePlayer(game: Kalaha, activePlayer: Player) {
        if (activePlayer.playerNumber == 1) {
            game.player1!!.isActive = false
            game.player2!!.isActive = true
        } else {
            game.player1!!.isActive = true
            game.player2!!.isActive = false
        }

        game.player1!!.consecutiveTurns = 1
        game.player2!!.consecutiveTurns = 1

        message = String.format("%s, it is now your turn.", game.activePlayer!!.playerName)
        game.message = message
    }

    fun isOwnPit(player: Player, pitId: Int): Boolean {
        return player.playerNumber == 1 && pitId < BoardEnum.PITS_PER_PLAYER.value || player.playerNumber == 2 && pitId > BoardEnum.PITS_PER_PLAYER.value
    }

    fun getOppositePit(game: Kalaha, pitId: Int): Pit {
        return game.board!!.getPitById(BoardEnum.TOTAL_PITS.value - 2 - pitId)
    }

    fun isPlayerKalaha(player: Player?, pitId: Int): Boolean {
        return pitId == player!!.playerNumber * 7 - 1
    }

    fun getNumberOfSeedsForPlayer(board: Board, player: Player?): Int {
//      val pitsForPlayer = getPlayerPits(board, player)
//      var numberOfSeedsForPlayer = 0
//      for (pit in pitsForPlayer) {
//          numberOfSeedsForPlayer += pit.numberOfSeeds
//      }
//      return numberOfSeedsForPlayer

        val pitsForPlayer = getPlayerPits(board, player)
        return pitsForPlayer.sumOf { it.numberOfSeeds }
    }

    fun getPlayerPits(board: Board, player: Player?): List<Pit> {
        val allPits = board.pits
        val pits = ArrayList<Pit>()

        if (player!!.playerNumber == 1) {
//          for (i in 0 until BoardEnum.PITS_PER_PLAYER.value - 2) {
//              pits.add(allPits!![i])
//          }

            (0 until BoardEnum.PITS_PER_PLAYER.value - 2).mapTo(pits) { allPits!![it] }
        } else {
//          for (i in BoardEnum.PITS_PER_PLAYER.value until BoardEnum.TOTAL_PITS.value - 1) {
//              pits.add(allPits!![i])
//          }

            (BoardEnum.PITS_PER_PLAYER.value until BoardEnum.TOTAL_PITS.value - 1).mapTo(pits) { allPits!![it] }
        }
        return pits
    }

}