package com.soundcloud.maze.events;

public abstract class ValidEvent implements IEvent {
    public final long seqNum;

    public ValidEvent(long seqNum) {
        this.seqNum = seqNum;
    }
}
