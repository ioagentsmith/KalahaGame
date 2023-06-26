package za.co.ioagentsmith.game.kalaha.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

class PlayerNames {

    @NotNull
    @Size(min = 2)
    var player1Name: String? = null
    @NotNull
    @Size(min = 2)
    var player2Name: String? = null

    constructor() {}

    constructor(player1Name: String, player2Name: String) {
        this.player1Name = player1Name
        this.player2Name = player2Name
    }
}
