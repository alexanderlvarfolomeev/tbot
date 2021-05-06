package com.aorise;

import com.aorise.bot.FileBot;

import java.util.logging.Logger;

public class BotLogger {
    private static final Logger logger = Logger.getLogger(FileBot.class.getName());

    public static void exc(Exception e) {
        botExc(e.getMessage());
    }

    public static void botExc(String message) {
        logger.warning(message);
    }

    public static void log(String message) {
        logger.info(message);
    }
}
