package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import com.aorise.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

public class TestCommand extends FileBotAdminCommand {
    public TestCommand(String commandId, String description) {
        super(commandId, description);
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        String text = stripCommand(message);
        KeyboardRow row = new KeyboardRow();
        row.addAll(List.of("\uD83D\uDC4D", "\uD83D\uDC4E"));
        SendPhoto photo = SendPhoto
                .builder()
                .photo(new InputFile(new File("D:\\Github\\tbot\\data\\astolfo\\9K_fC0QhCeM.jpg")))
                .replyMarkup(Utils.buildInlineMarkup("rate", List.of("\uD83D\uDC4D", "\uD83D\uDC4E")))
                .chatId(String.valueOf(message.getChatId()))
                .build();
        try {
            bot.execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return "Test";
    }
}
