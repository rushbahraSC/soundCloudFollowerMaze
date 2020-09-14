package com.soundcloud.maze.events;

public class UnfollowEvent extends ValidEvent {
    public final long fromUserId;
    public final long toUserId;

    public UnfollowEvent(long seqNum, long fromUserId, long toUserId) {
        super(seqNum);
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

}
