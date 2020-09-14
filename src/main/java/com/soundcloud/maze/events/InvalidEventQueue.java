package com.soundcloud.maze.events;

import java.util.concurrent.PriorityBlockingQueue;

public class InvalidEventQueue {
    static PriorityBlockingQueue<IEvent> deadEventQueue = new PriorityBlockingQueue<>();

    public static void addEvent(IEvent event) {
        deadEventQueue.add(event);
    }
}
