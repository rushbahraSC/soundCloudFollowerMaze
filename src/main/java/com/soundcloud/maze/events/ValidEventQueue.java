package com.soundcloud.maze.events;

import java.util.concurrent.PriorityBlockingQueue;

/* Can be used for future to expand and consume events in a queue */
public class ValidEventQueue {
    static PriorityBlockingQueue<IEvent> validEventQueue = new PriorityBlockingQueue<>();

    public static void addEvent(IEvent event) {
        validEventQueue.add(event);
    }
}
