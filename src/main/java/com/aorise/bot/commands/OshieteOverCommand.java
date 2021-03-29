package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class OshieteOverCommand extends FileBotCommand {
    public OshieteOverCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(AbsSender absSender, Message message, String[] arguments) {
        FileBot bot = (FileBot) absSender;
        FileBot.ChatContext context = bot.getContext(message.getChatId());
        if (context == null) {
            return "There is no game";
        }
        context.setGhoulCounter(null);
        context.setGhoulMessageId(null);
        sendMessage(absSender, message, "Okay there are no deadinsides in this chat.", false);
        return "Stopped";
    }
}
