package com.aorise;

import com.aorise.bot.FileBot;

import java.io.IOException;
import java.util.logging.Logger;

public class BotLogger {
    private static final Logger logger = Logger.getLogger(FileBot.class.getName());

    public static void visitorExc(IOException exc) {
        botExc(exc.getMessage());
    }

    public static void botExc(String message) {
        logger.warning(message);
    }

    public static void log(String message) {
        logger.info(message);
    }
}
