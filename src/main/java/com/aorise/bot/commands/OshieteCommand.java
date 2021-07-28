package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import com.aorise.bot.handlers.OshieteHandler;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class OshieteCommand extends FileBotAdminCommand {
    private final OshieteHandler oshieteHandler;

    public OshieteCommand(String commandIdentifier, String description, OshieteHandler oshieteHandler) {
        super(commandIdentifier, description);
        this.oshieteHandler = oshieteHandler;
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        return oshieteHandler.startGame(message);
    }

    @Override
    public void processNonAdminMessage(AbsSender absSender, Message message, String[] arguments) {
        super.processNonAdminMessage(absSender, message, arguments);
        //TODO: скинуть мем с канеки кеком
    }
}
