package com.soundcloud.maze.events;

public class PrivateMessageEvent extends ValidEvent {
    public final long toUserId;

    public PrivateMessageEvent(long seqNum, long toUserId) {
        super(seqNum);
        this.toUserId = toUserId;
    }
    
}
