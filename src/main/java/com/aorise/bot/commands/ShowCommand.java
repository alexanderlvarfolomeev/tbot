package com.aorise.bot.commands;


import com.aorise.BotLogger;
import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShowCommand extends FileBotCommand {
    public ShowCommand(String commandId, String description) {
        super(commandId, description);
    }

    private String get(FileBot bot, String text, long chatId) {
        String got = bot.getMapper().get(text);
        if (got != null) {
            String restriction = bot.getExc_mapper().get(got);
            try {
                if (restriction != null && Long.parseLong(restriction) != chatId) {
                    got = null;
                }
            } catch (NumberFormatException e) {
                //nothing
            }
        }
        return got;
    }

    @Override
    public String processMessageImpl(AbsSender absSender, Message msg, String[] arguments) {
        FileBot bot = (FileBot) absSender;
        String initText = stripCommand(msg);

        String text = get(bot, initText.toLowerCase(), msg.getChatId());

        if (text == null) {
            sendMessage(absSender, msg, "Not implemented yet (or maybe never).", false);
            return "Key \"" + initText + "\" is not found.";
        } else {
            SendPhoto photo = new SendPhoto();
            photo.setChatId(String.valueOf(msg.getChatId()));
            String toReturn;
            try (Stream<Path> pathStream = Files.walk(bot.getDirectory().resolve(text))) {
                List<Path> paths = pathStream.filter(p -> !Files.isDirectory(p)).collect(Collectors.toList());
                Path path = FileBot.getRandom(paths);
                if (path == null) {
                    BotLogger.botExc("\"" + text + "\" is empty directory.");
                    sendMessage(absSender, msg, "Sorry, can't find any pics.", false);
                    toReturn = "Empty dir.";
                } else {
                    photo.setPhoto(new InputFile(path.toFile()));
                    bot.execute(photo);
                    toReturn = "File \"" + path.toFile().getCanonicalPath() + "\" was sent";
                }
            } catch (IOException e) {
                BotLogger.exc(e);
                sendMessage(absSender, msg, "Oh shit, I'm sorry. IOException.", false);
                toReturn = "IOException occurred.";
            } catch (TelegramApiException e) {
                BotLogger.botExc(e.getMessage());
                e.printStackTrace();
                toReturn = "TelegramApiException.";
            }
            return toReturn;
        }
    }
}
