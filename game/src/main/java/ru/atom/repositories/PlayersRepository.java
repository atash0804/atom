package ru.atom.repositories;

import org.springframework.stereotype.Repository;
import ru.atom.models.Player;

import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Repository
public class PlayersRepository {
    private static BlockingQueue<Player> players = new LinkedBlockingQueue<>();

    public void add(Player player) {
        players.add(player);
    }

    public Player get(String name)
    {
        for(Player player : players)
            if (player.getUsername().equals(name))
                return player;
        return null;
    }
}
