package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class SaveCommand extends FileBotAdminCommand {
    public SaveCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        bot.saveContexts();
        return "Saved";
    }
}
