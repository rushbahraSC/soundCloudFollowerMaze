package com.soundcloud.maze.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserClients {

    public static Thread ClientThread() throws SocketException {

        Map<Long, Socket> clientPool = new ConcurrentHashMap<>();

        return new Thread(() -> {
            Logger.info(Constants.USER_CLIENTS, "Listening for client requests on " + Constants.CLIENT_PORT);
            try {
                ServerSocket serverSocket = new ServerSocket(Constants.CLIENT_PORT);
                Socket clientSocket = serverSocket.accept();
                while (clientSocket != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String userId = reader.readLine();
                    if (userId != null) {
                        clientPool.put(Long.parseLong(userId), clientSocket);
                        Logger.info(Constants.USER_CLIENTS, "User connected: " + userId + " (" + clientPool.size() + " total)");
                    }
                    clientSocket = serverSocket.accept();
                }
            } catch (IOException e) {
                throw new SocketException(e);
            }
        });
    }
}
