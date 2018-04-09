package ru.atom.gamecontroller;

import java.util.Random;

public class GameController {
    private static int MAX_GAMES = 1000;

    public static long create(int playerCount) {
        Random random = new Random();
        return random.nextInt(MAX_GAMES);
    }
}
