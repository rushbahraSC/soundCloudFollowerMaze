package com.soundcloud.maze.events;

public class FollowEvent extends ValidEvent {

    public final long fromUserId;
    public final long toUserId;

    public FollowEvent(long seqNum, long fromUserId, long toUserId) {
        super(seqNum);
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }
}
