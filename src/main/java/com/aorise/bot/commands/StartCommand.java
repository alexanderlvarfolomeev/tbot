package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends FileBotCommand {
    public StartCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(AbsSender absSender, Message message, String[] arguments) {
        FileBot bot = (FileBot) absSender;
        bot.onStart(message.getChatId());
        if (bot.onStart(message.getChatId())) {
            sendMessage(absSender, message, "Hello there.", false);
        } else {
            sendMessage(absSender, message, "You do not need to repeat it.", false);
        }
        return String.format("Start in %d.", message.getChatId());
    }
}
