package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import com.aorise.bot.handlers.OshieteHandler;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class OshieteOverCommand extends FileBotCommand {
    private final OshieteHandler oshieteHandler;

    public OshieteOverCommand(String commandIdentifier, String description, OshieteHandler oshieteHandler) {
        super(commandIdentifier, description);
        this.oshieteHandler = oshieteHandler;
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        return oshieteHandler.stopGame(message);
    }
}
