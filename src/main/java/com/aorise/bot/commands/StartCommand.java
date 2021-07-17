package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends FileBotCommand {
    public StartCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        if (bot.onStart(message.getChatId())) {
            sendMessage(bot, message, "Hello there.", false);
        } else {
            sendMessage(bot, message, "You do not need to repeat it.", false);
        }
        return String.format("Start in %d.", message.getChatId());
    }
}
