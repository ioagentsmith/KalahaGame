package za.co.ioagentsmith.game.kalaha.service;

import za.co.ioagentsmith.game.kalaha.model.Board;
import za.co.ioagentsmith.game.kalaha.model.Kalaha;
import za.co.ioagentsmith.game.kalaha.model.Pit;
import za.co.ioagentsmith.game.kalaha.model.Player;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class KalahaServiceTest {

    private static GameService testedObject;
    private static Player player1;
    private static Player player2;
    private static Pit pit;
    private static Board board;
    private static Kalaha game;

    @BeforeClass
    public static void testSetup() {
        testedObject = new GameService();
        player1 = Mockito.mock(Player.class);
        player2 = Mockito.mock(Player.class);
        pit = Mockito.mock(Pit.class);
        board = Mockito.mock(Board.class);
        game = Mockito.mock(Kalaha.class);
    }

    @Test
    public void testIsLastSeedInOwnEmptyPit() {
        when(player1.isActive()).thenReturn(true);
        when(player1.getPlayerNumber()).thenReturn(1);
        when(pit.getId()).thenReturn(0);

        assertEquals(testedObject.isLastSeedInOwnEmptyPit(player1, pit), true);
    }

    @Test
    public void testIsOwnPit() {
        when(player1.isActive()).thenReturn(true);
        when(player1.getPlayerNumber()).thenReturn(1);
        when(pit.getId()).thenReturn(0);

        assertEquals(testedObject.isOwnPit(player1, pit.getId()), true);
    }

    @Test
    public void testIsPlayerKalahaTrue() {
        when(player1.isActive()).thenReturn(true);
        when(player1.getPlayerNumber()).thenReturn(1);
        when(pit.getId()).thenReturn(6);

        assertEquals(testedObject.isPlayerKalaha(player1, pit.getId()), true);
    }

    @Test
    public void testIsPlayerKalahaFalse() {
        when(player1.isActive()).thenReturn(true);
        when(player1.getPlayerNumber()).thenReturn(1);
        when(pit.getId()).thenReturn(5);

        assertEquals(testedObject.isPlayerKalaha(player1, pit.getId()), false);
    }

    @Test
    public void testGetOppositePit() {
        when(pit.getId()).thenReturn(6);
        when(game.getBoard()).thenReturn(board);
        when(board.getPitById(6)).thenReturn(pit);

        assertEquals(testedObject.getOppositePit(game, pit.getId()).getId(), 6);
    }

}
