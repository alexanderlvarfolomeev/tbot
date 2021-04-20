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

    @Override
    public String processMessageImpl(AbsSender absSender, Message msg, String[] arguments) {
        FileBot bot = (FileBot) absSender;
        String text = stripCommand(msg);

        text = text.toLowerCase();
        if (!bot.getMapper().containsKey(text)) {
            sendMessage(absSender, msg, "Not implemented yet (or maybe never).", false);
            return "Key \"" + text + "\" is not found.";
        } else {
            text = bot.getMapper().get(text);
            SendPhoto photo = new SendPhoto();
            photo.setChatId(String.valueOf(msg.getChatId()));
            try (Stream<Path> pathStream = Files.walk(bot.getDirectory().resolve(text))) {
                List<Path> paths = pathStream.filter(p -> !Files.isDirectory(p)).collect(Collectors.toList());
                Path path = FileBot.getRandom(paths);
                if (path == null) {
                    BotLogger.botExc("\"" + text + "\" is empty directory.");
                    sendMessage(absSender, msg, "Sorry, can't find any pics.", false);
                    return "Empty dir.";
                } else {
                    photo.setPhoto(new InputFile(path.toFile()));
                    bot.execute(photo);
                    return "File \"" + path.toFile().getCanonicalPath() + "\" was sent";
                }
            } catch (IOException e) {
                BotLogger.visitorExc(e);
                sendMessage(absSender, msg, "Oh shit, I'm sorry. IOException.", false);
                return "IOException occurred.";
            } catch (TelegramApiException e) {
                BotLogger.botExc(e.getMessage());
                e.printStackTrace();
                return "TelegramApiException.";
            }
        }
    }
}
