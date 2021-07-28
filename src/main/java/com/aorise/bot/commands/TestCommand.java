package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import com.aorise.bot.handlers.MimeHandler;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TestCommand extends FileBotAdminCommand {
    public TestCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
        try (Stream<Path> pathStream = Files.walk(Path.of("data").resolve("test-samples"))) {
            MimeHandler mimeHandler = new MimeHandler();
            pathStream.filter(p -> !Files.isDirectory(p)).forEach(p -> {
                try {
                    mimeHandler.wrap(message.getChatId().toString(), p).execute(bot);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Test.";
    }
}
