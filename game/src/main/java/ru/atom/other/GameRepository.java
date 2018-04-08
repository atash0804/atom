package ru.atom.other;

import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameRepository {
    private ConcurrentHashMap<Long, GameSession> map = new ConcurrentHashMap<>();

    public void put(GameSession session) {
        map.put(session.getId(), session);
    }

    public Collection<GameSession> getAll() {
        return map.values();
    }
}
