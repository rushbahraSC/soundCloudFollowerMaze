package com.soundcloud.maze.events;

public class InvalidEvent implements IEvent {
    public final long seqNum;

    public InvalidEvent(long seqNum) {
        this.seqNum = seqNum;
    }
}
