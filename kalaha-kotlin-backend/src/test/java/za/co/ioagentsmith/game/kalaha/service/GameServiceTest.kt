package za.co.ioagentsmith.game.kalaha.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.test.context.junit.jupiter.SpringExtension
import za.co.ioagentsmith.game.kalaha.model.Board
import za.co.ioagentsmith.game.kalaha.model.Kalaha
import za.co.ioagentsmith.game.kalaha.model.Pit
import za.co.ioagentsmith.game.kalaha.model.Player

@ExtendWith(SpringExtension::class)
open class GameServiceTest {

    var testedObject: GameService? = null
    var player1: Player? = null
    var player2: Player? = null
    var pit: Pit? = null
    var board: Board? = null
    var game: Kalaha? = null

    @BeforeEach
    fun testSetup() {
        testedObject = GameService()
        player1 = Mockito.mock(Player::class.java)
        player2 = Mockito.mock(Player::class.java)
        pit = Mockito.mock(Pit::class.java)
        board = Mockito.mock(Board::class.java)
        game = Mockito.mock(Kalaha::class.java)
    }

    @Test
    fun testIsLastSeedInOwnEmptyPit() {
        `when`(player1!!.isActive).thenReturn(true)
        `when`(player1!!.playerNumber).thenReturn(1)
        `when`(pit!!.id).thenReturn(0)

        assertEquals(testedObject!!.isLastSeedInOwnEmptyPit(player1!!, pit!!), true)
    }

    @Test
    fun testIsOwnPit() {
        `when`(player1!!.isActive).thenReturn(true)
        `when`(player1!!.playerNumber).thenReturn(1)
        `when`(pit!!.id).thenReturn(0)

        assertEquals(testedObject!!.isOwnPit(player1!!, pit!!.id), true)
    }

    @Test
    fun testIsPlayerKalahaTrue() {
        `when`(player1!!.isActive).thenReturn(true)
        `when`(player1!!.playerNumber).thenReturn(1)
        `when`(pit!!.id).thenReturn(6)

        assertEquals(testedObject!!.isPlayerKalaha(player1, pit!!.id), true)
    }

    @Test
    fun testIsPlayerKalahaFalse() {
        `when`(player1!!.isActive).thenReturn(true)
        `when`(player1!!.playerNumber).thenReturn(1)
        `when`(pit!!.id).thenReturn(5)

        assertEquals(testedObject!!.isPlayerKalaha(player1, pit!!.id), false)
    }

    @Test
    fun testGetOppositePit() {
        `when`(pit!!.id).thenReturn(6)
        `when`<Board>(game!!.board).thenReturn(board)
        `when`(board!!.getPitById(6)).thenReturn(pit)

        assertEquals(testedObject!!.getOppositePit(game!!, pit!!.id).id.toLong(), 6)
    }
}
