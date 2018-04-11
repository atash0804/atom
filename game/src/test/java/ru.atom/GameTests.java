package ru.atom;

import org.junit.Assert;
import org.junit.Test;
import ru.atom.matchmaker.GameService;
import ru.atom.other.GameSession;
import ru.atom.other.Player;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GameTests {

    @Test
    public void testGameSessionAddPlayer()
    {
        GameSession gameSession = new GameSession(1);
        Player player = new Player("Kolya", 2);
        Player player2 = new Player("Sonya", 3);
        Assert.assertTrue(gameSession.addPlayer(player));
        assertEquals(1, gameSession.getPlayersInGame());
        assertEquals(1, gameSession.getPlayers().size());

        Assert.assertFalse(gameSession.addPlayer(player2));
        assertEquals(2, gameSession.getRank(), 0);
    }
}
