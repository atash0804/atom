package ru.atom.other;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Leaderboard {
    private static Map<String, Integer> stat = new ConcurrentHashMap<>();

    public static Map<String,Integer> getStat() {
        return stat;
    }
}
