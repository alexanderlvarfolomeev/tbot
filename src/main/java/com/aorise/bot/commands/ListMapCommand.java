package com.aorise.bot.commands;

import com.aorise.BotLogger;
import com.aorise.bot.BotConst;
import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.nio.file.Files;

public class ListMapCommand extends FileBotCommand {
    public ListMapCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        try {
            String result = Files.readString(BotConst.ACC_MAPPING);
            sendMessage(bot, message, result, false);
        } catch (IOException e) {
            BotLogger.botExc(e.getMessage());
            e.printStackTrace();
        }
        return "Listed accepted tags";
    }
}
