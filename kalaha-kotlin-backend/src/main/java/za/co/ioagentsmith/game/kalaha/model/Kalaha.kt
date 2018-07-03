package za.co.ioagentsmith.game.kalaha.model

import za.co.ioagentsmith.game.kalaha.util.StringUtil

class Kalaha {

    var player1: Player? = null
    var player2: Player? = null
    var board: Board? = null
    var message: String? = null

    val activePlayer: Player?
        get() = if (player1!!.isActive) player1 else player2

    constructor() {
        this.board = Board()
        this.player1 = Player()
        this.player2 = Player()

        player1!!.playerName = "Player 1"
        player2!!.playerName = "Player 2"
        player1!!.playerNumber = 1
        player2!!.playerNumber = 2
        player1!!.isActive = true
        player2!!.isActive = false
    }

    constructor(players: PlayerNames) {
        this.board = Board()
        this.player1 = Player()
        this.player2 = Player()

        player1!!.playerName = if (StringUtil.isNullOrEmpty(players.player1Name)) "Player 1" else players.player1Name
        player2!!.playerName = if (StringUtil.isNullOrEmpty(players.player2Name)) "Player 2" else players.player2Name
        player1!!.playerNumber = 1
        player2!!.playerNumber = 2
        player1!!.isActive = true
        player2!!.isActive = false
    }
}
