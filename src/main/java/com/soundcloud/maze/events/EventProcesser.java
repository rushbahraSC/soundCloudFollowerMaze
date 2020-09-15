package com.soundcloud.maze.events;

import com.soundcloud.maze.util.Constants;
import com.soundcloud.maze.util.InvalidEventException;
import com.soundcloud.maze.util.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;

public class EventProcesser {

    private static long lastSeqNo = 0L;
    private static Map<Long, List<String>> seqNoToMessage = new HashMap<>();
    private static Map<Long, Set<Long>> followRegistry = new HashMap<>();

    public static void processEvents(String payload, Map<Long, Socket> clientPool) {
        Logger.info("EventProcesser", "Message received: " + payload);

        List<String> payloadParts = Arrays.asList(payload.split("\\|"));
        seqNoToMessage.put(Long.parseLong(payloadParts.get(0)), payloadParts);

        while (seqNoToMessage.containsKey(lastSeqNo + 1)) {
            List<String> nextMessage = seqNoToMessage.get(lastSeqNo + 1);
            String nextPayload = String.join("|", nextMessage);

            long seqNo = Long.parseLong(nextMessage.get(0));
            String kind = nextMessage.get(1);

            switch (kind) {
                case "F": {
                    long fromUserId = Long.parseLong(nextMessage.get(2));
                    long toUserId = Long.parseLong(nextMessage.get(3));

                    ValidEvent event = new FollowEvent(seqNo, fromUserId, toUserId);
                    /* Can use this queue to manage valid events.
                    ValidEventQueue.addEvent(event); */

                    Set<Long> followers = followRegistry.getOrDefault(toUserId, new HashSet<>());
                    followers.add(fromUserId);
                    followRegistry.put(toUserId, followers);

                    try {
                        Socket socket = clientPool.get(toUserId);
                        if (socket != null) {
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            writer.write(nextPayload + "\n");
                            writer.flush();
                        }
                    } catch (IOException e) {
                        InvalidEventQueue.addEvent(event);
                        Logger.error(Constants.EVENT_PROCESSER, "Event added to invalid queue.");
                        throw new InvalidEventException(e);
                    }
                }
                break;

                case "U": {
                    long fromUserId = Long.parseLong(nextMessage.get(2));
                    long toUserId = Long.parseLong(nextMessage.get(3));

                    Set<Long> followers = followRegistry.getOrDefault(toUserId, new HashSet<>());
                    followers.remove(fromUserId);
                    followRegistry.put(toUserId, followers);

                    ValidEvent event = new UnfollowEvent(seqNo, fromUserId, toUserId);
                    /* Can use this queue to manage valid events.
                    ValidEventQueue.addEvent(event); */
                }
                break;

                case "P": {
                    long toUserId = Long.parseLong(nextMessage.get(3));

                    ValidEvent event = new PrivateMessageEvent(seqNo, toUserId);
                    /* Can use this queue to manage valid events.
                    ValidEventQueue.addEvent(event); */

                    try {
                        Socket socket = clientPool.get(toUserId);
                        if (socket != null) {
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            writer.write(nextPayload + "\n");
                            writer.flush();
                        }
                    } catch (IOException e) {
                        InvalidEventQueue.addEvent(event);
                        Logger.error(Constants.EVENT_PROCESSER, "Event added to invalid queue.");
                        throw new InvalidEventException(e);
                    }
                }
                break;

                case "B": {
                    clientPool.values().forEach(socket -> {

                        ValidEvent event = new BroadcastEvent(seqNo);
                            /* Can use this queue to manage valid events.
                            ValidEventQueue.addEvent(event); */
                        try {
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            writer.write(nextPayload + "\n");
                            writer.flush();
                        } catch (IOException e) {
                            InvalidEventQueue.addEvent(event);
                            Logger.error(Constants.EVENT_PROCESSER, "Event added to invalid queue.");
                            throw new InvalidEventException(e);
                        }
                    });
                }
                break;

                case "S": {
                    long fromUserId = Long.parseLong(nextMessage.get(2));

                    ValidEvent event = new StatusUpdateEvent(seqNo, fromUserId);
                    /* Can use this queue to manage valid events.
                    ValidEventQueue.addEvent(event); */
                    Set<Long> followers = followRegistry.getOrDefault(fromUserId, emptySet());

                    followers.forEach(follower -> {
                        try {
                            Socket socket = clientPool.get(follower);
                            if (socket != null) {
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                writer.write(nextPayload + "\n");
                                writer.flush();
                            }
                        } catch (IOException e) {
                            InvalidEventQueue.addEvent(event);
                            Logger.error(Constants.EVENT_PROCESSER, "Event added to invalid queue.");
                            throw new InvalidEventException(e);
                        }
                    });
                }
                break;

                default:
                    // If the event doesn't match any of the cases then it is a Dead Letter Message and
                    // is being added to a different queue which can then be processed and looked at for analysis
                    InvalidEvent deadEvent = new InvalidEvent(seqNo);
                    InvalidEventQueue.addEvent(deadEvent);
                    throw new InvalidEventException();
            }

            lastSeqNo = seqNo;
        }
    }
}
