package com.aorise.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class AliveCommand extends FileBotCommand {
    public AliveCommand(String commandId, String description) {
        super(commandId, description);
    }

    @Override
    public String processMessageImpl(AbsSender absSender, Message message, String[] arguments) {
        sendMessage(absSender, message, "I'm still alive.", true);
        return "Alive.";
    }
}
