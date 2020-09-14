package com.soundcloud.maze.util;

public class InvalidEventException extends RuntimeException {

    public InvalidEventException() {
        Logger.error("InvalidEventException", "Error occurred while processing event.");
    }

    public InvalidEventException(Throwable cause) {
        Logger.error("InvalidEventException", cause);
    }
}
