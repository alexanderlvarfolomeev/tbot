package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class AliveCommand extends FileBotCommand {
    public AliveCommand(String commandId, String description) {
        super(commandId, description);
    }

    @Override
    public String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        sendMessage(bot, message, "I'm still alive.", true);
        return "Alive.";
    }
}
