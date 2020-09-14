package com.soundcloud.maze.util;

import com.soundcloud.maze.events.EventProcesser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class EventSource {

    public static Thread EventThread() throws SocketException {

        return new Thread(() -> {
            Logger.info(Constants.EVENT_SOURCE, "Listening for events on " + Constants.EVENT_PORT);
            try (Socket eventSocket = new ServerSocket(Constants.EVENT_PORT).accept()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(eventSocket.getInputStream()))) {
                    reader.lines().forEach(payload -> {
                        EventProcesser.processEvents(payload);
                    });
                } catch (InvalidEventException e) {
                    Logger.error(Constants.EVENT_SOURCE, "Unable to process events.");
                }
            } catch (IOException e) {
                throw new SocketException(e);
            }
        });
    }
}
