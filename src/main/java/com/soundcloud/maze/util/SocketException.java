package com.soundcloud.maze.util;

public class SocketException extends RuntimeException {

    public SocketException(Throwable cause) {
        Logger.error("SocketException", cause);
    }
}
