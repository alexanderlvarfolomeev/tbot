package com.aorise.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends FileBotCommand {
    public StartCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(AbsSender absSender, Message message, String[] arguments) {
        sendMessage(absSender, message, "Hello there.", false);
        return "Start.";
    }
}
