package com.aorise.bot.commands;

import com.aorise.BotLogger;
import com.aorise.bot.FileBot;
import com.aorise.util.MessageDescriber;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class FileBotAdminCommand extends FileBotCommand {
    public FileBotAdminCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        FileBot bot = (FileBot) absSender;
        if (bot.getId() != message.getFrom().getId()) {
            BotLogger.log(String.format("%n%s.%nResult: \"%s\" Message was not from Admin.", MessageDescriber.describe(message), this.getCommandIdentifier()));
            return;
        }
        super.processMessage(absSender, message, arguments);
    }
}
