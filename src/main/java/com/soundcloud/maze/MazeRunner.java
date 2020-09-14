package com.soundcloud.maze;

import com.soundcloud.maze.util.EventSource;
import com.soundcloud.maze.util.Logger;
import com.soundcloud.maze.util.SocketException;
import com.soundcloud.maze.util.UserClients;

public class MazeRunner {

    public static void main(String[] args) {

        Map<Long, Socket> clientPool = new ConcurrentHashMap<>();
        try {
            EventSource.EventThread(clientPool).start();
            UserClients.ClientThread(clientPool).start();
        } catch (SocketException e) {
            Logger.error("MazeRunner", "Unable to complete the request.");
        }
    }
}
