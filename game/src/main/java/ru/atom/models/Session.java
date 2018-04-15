package ru.atom.models;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private int id;
    private int playerCount;
    private List<Player> players = new ArrayList<>();

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    private boolean isFinished = false;

    public int getPlayerCount() {
        return playerCount;
    }

    public Session(int id, int playerCount) {
        this.id = id;
        this.playerCount = playerCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFull() {
        return players.size() >= playerCount;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public double getAverageRating() {
        double rating = 0;
        for (Player player : players)
            rating += player.getRating();
        return rating / players.size();
    }
}
