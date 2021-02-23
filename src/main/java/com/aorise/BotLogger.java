package com.aorise;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.logging.Logger;

public class BotLogger {
    private static final Logger logger = Logger.getLogger(FileBot.class.getName());

    public static void visitorExc(IOException  exc) {
        logger.warning(exc.getMessage());
    }

    public static void botExc(String message) {
        logger.warning(message);
    }

    public static void logUpdate(Update update) {
        logger.info(update.toString());
    }
}
