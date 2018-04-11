package ru.atom.other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class GameSession {
    private static final Logger log = LoggerFactory.getLogger(GameSession.class);

    private static AtomicLong idGenerator = new AtomicLong();
    private int playersInGame = 4;
    private ArrayList<Player> players = new ArrayList<>();
    private final long id = idGenerator.getAndIncrement();

    public GameSession(int playersInGame){
        this.playersInGame = playersInGame;
    }

    public GameSession(ArrayList<Player> players) {
        this.players = players;
    }

    public boolean addPlayer(Player player){
        if (players.size()+1 > playersInGame){
            return false;
        }
        players.add(player);
        return true;
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "connections=" + Arrays.toString(players.toArray()) +
                ", id=" + id +
                '}';
    }

    public float getRank(){
        int res = 0;
        for (Player i: players){
            res += i.getRank();
        }
        return res/players.size();
    }

    public int getPlayersInGame() {
        return playersInGame;
    }

    public long getId() {
        return id;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
