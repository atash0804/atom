package ru.atom.repositories;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import ru.atom.models.Session;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Repository
public class SessionRepository {
    private BlockingQueue<Session> sessions = new LinkedBlockingQueue<>();

    public void add(Session session)
    {
        sessions.add(session);
    }

    public void remove(Session session)
    {
        sessions.remove(session);
    }

    public Session get(long id)
    {
        for (Session session : sessions)
            if (session.getId() == id)
                return session;
        return null;
    }

    public BlockingQueue<Session> getSessions() {
        return sessions;
    }
}
