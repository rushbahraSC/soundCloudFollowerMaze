package com.soundcloud.maze.util;

public class Logger {
    public static void info(String who, Object message) {
       System.out.println("INFO: " + who + "() - " + message);
    }

    public static void error(String who, Object message) {
        System.out.println("ERROR: " + who + "() - " + message);
    }
}
