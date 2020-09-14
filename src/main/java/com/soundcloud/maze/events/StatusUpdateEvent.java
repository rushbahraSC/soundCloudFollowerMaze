package com.soundcloud.maze.events;

public class StatusUpdateEvent extends ValidEvent {
    public final long fromUserId;

    public StatusUpdateEvent(long seqNum, long fromUserId) {
        super(seqNum);
        this.fromUserId = fromUserId;
    }
    
}
