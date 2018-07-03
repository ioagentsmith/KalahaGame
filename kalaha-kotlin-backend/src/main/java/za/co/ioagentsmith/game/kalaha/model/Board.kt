package za.co.ioagentsmith.game.kalaha.model

import za.co.ioagentsmith.game.kalaha.util.BoardEnum

import java.util.stream.IntStream

class Board {

    var pits: MutableList<Pit>? = null

    init {
        pits = mutableListOf()

        IntStream.range(0, BoardEnum.TOTAL_PITS.value).forEach { i ->
            val pit = Pit()
            pits?.add(pit)
            pits!![i].id = i
            if ((i + 1) % BoardEnum.PITS_PER_PLAYER.value == 0) {
                pits!![i].isKalaha = true
            } else {
                pits!![i].isKalaha = false
            }
            if (!pits!![i].isKalaha) {
                pits!![i].numberOfSeeds = BoardEnum.SEEDS_PER_PIT.value
            } else {
                pits!![i].numberOfSeeds = 0
            }
        }
    }

    fun getPitById(pitId: Int): Pit {
        return this.pits!![pitId]
    }
}
