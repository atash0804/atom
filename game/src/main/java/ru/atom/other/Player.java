package ru.atom.other;

public class Player {
    private String name;

    public Player (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player" + name;
    }
}
