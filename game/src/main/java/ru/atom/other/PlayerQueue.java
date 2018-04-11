package ru.atom.other;

import org.springframework.stereotype.Repository;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Repository
public class PlayerQueue {
    private static BlockingQueue<Player> queue = new LinkedBlockingQueue<>();

    public static BlockingQueue<Player> getQueue() {
        return queue;
    }
}
