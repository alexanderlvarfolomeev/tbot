package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import com.aorise.bot.handlers.ShowHandler;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ShowCommand extends FileBotCommand {
    private final ShowHandler showHandler;
    private final boolean rated;

    public ShowCommand(String commandId, String description, boolean rated, ShowHandler showHandler) {
        super(commandId, description);
        this.showHandler = showHandler;
        this.rated = rated;
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        return showHandler.show(message, rated);
    }
}
