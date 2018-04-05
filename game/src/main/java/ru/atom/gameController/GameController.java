package ru.atom.gameController;

import java.util.concurrent.ThreadLocalRandom;

public class GameController {
    private static int MAX_GAMES = 1000;

    public static long create (int playerCount) {
        return ThreadLocalRandom.current().nextLong(1, MAX_GAMES);
    }
}
