package ru.atom;

import org.junit.Assert;
import org.junit.Test;
import ru.atom.models.Player;
import ru.atom.models.Session;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GameTests {

    @Test
    public void testGameSessionAddPlayer()
    {
        String[] names = {"Kolya", "Sonya", "Masha", "Dima"};
        Session gameSession = new Session(1, 4);
        List<Player> playerList = new ArrayList<>();
        for (String name : names)
            playerList.add(new Player(name));
        gameSession.setPlayers(playerList);
        assertTrue(gameSession.isFull());
        assertEquals(4, gameSession.getPlayers().size());
    }
}
