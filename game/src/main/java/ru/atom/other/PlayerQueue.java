package ru.atom.other;

import org.springframework.stereotype.Repository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Repository
public class PlayerQueue {
    static private BlockingQueue<Player> queue = new LinkedBlockingQueue<>();

    static public BlockingQueue<Player> getQueue() {
        return queue;
    }
}
