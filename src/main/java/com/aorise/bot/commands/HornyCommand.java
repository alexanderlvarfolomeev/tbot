package com.aorise.bot.commands;

import com.aorise.BotLogger;
import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HornyCommand extends FileBotCommand {
    private static final String fileId = "CAACAgIAAxkBAAMnYDwXoPShmBKf6IMbv0X7xwtdio8AAg8BAAKVwmUXsN0VEn0taJgeBA";

    public HornyCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        SendSticker sticker = new SendSticker();
        sticker.setChatId(message.getChatId().toString());
        sticker.setSticker(new InputFile(fileId));
        try {
            bot.execute(sticker);
        } catch (TelegramApiException e) {
            BotLogger.botExc(e.getMessage());
            e.printStackTrace();
            return "TelegramApiException.";
        }
        return "Horny sticker was sent.";
    }
}
