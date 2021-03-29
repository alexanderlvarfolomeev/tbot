package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class OshieteCommand extends FileBotCommand {
    public OshieteCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(AbsSender absSender, Message message, String[] arguments) {
        FileBot bot = (FileBot) absSender;
        FileBot.ChatContext context = bot.getContext(message.getChatId());
        if (context == null) {
            bot.onStart(message.getChatId());
            context = bot.getContext(message.getChatId());
        }
        if (context.getGhoulCounter() != null) {
            sendMessage(absSender, message, "The fight to be the deadest inside is already in progress.", false);
            return "Double ghoul game start";
        }
        context.setGhoulCounter(993);
        Message sent = sendMessage(absSender, message, "Okay... 1000 - 7?", false);
        if (sent != null) {
            context.setGhoulMessageId(sent.getMessageId());
            return "The ghoul game has been started.";
        } else {
            context.setGhoulCounter(null);
            return "Can't start the game";
        }
    }
}
