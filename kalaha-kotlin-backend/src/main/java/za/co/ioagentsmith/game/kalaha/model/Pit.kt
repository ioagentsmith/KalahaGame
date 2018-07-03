package za.co.ioagentsmith.game.kalaha.model

import za.co.ioagentsmith.game.kalaha.util.BoardEnum

class Pit {

    var id: Int = 0
    var numberOfSeeds: Int = 0
    var isEmpty: Boolean = false
    var isKalaha: Boolean = false

    init {
        this.numberOfSeeds = BoardEnum.SEEDS_PER_PIT.value
    }

}
