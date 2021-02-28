package com.aorise.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class QuineCommand extends FileBotCommand {
    public QuineCommand(String commandId, String description) {
        super(commandId, description);
    }

    @Override
    public String processMessageImpl(AbsSender absSender, Message message, String[] arguments) {
        String text = stripCommand(message);
        if (!text.isEmpty()) {
            sendMessage(absSender, message, text, false);
        }
        return "Quine was sent.";
    }
}
