package ru.atom.other;

import java.util.Date;

public class Player{
    private String name;
    private int rank;
    private Date connectTime;
    public long gameId;
    public Thread location;

    public Player(String name, int rank) {
        this.name = name;
        this.rank = rank;
        connectTime = new Date();
    }

    public String getNameCustom() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    public Date getConnectTime() {
        return connectTime;
    }
}
