package ru.atom.other;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Leaderboard {
    static private Map <String, Integer> stat = new ConcurrentHashMap<>();

    static public Map<String,Integer> getStat() {
        return stat;
    }
}
