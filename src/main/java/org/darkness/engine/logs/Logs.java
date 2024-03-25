package org.darkness.engine.logs;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public abstract class Logs {
    private static final Logger LOGGER = Logger.getLogger(Logs.class.getName());

    public static void makeErrorLog(@NotNull Exception e){
        e.printStackTrace();

        LOGGER.severe(e.toString());
    }

    public static void makeInfoLog(String message){
        LOGGER.info(message);
    }


}
