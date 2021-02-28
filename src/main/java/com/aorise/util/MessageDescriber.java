package com.aorise.util;

import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.stream.Collectors;

public class MessageDescriber {
    public static String describe(Message message) {
        String userName = (message.getFrom().getUserName() != null) ? message.getFrom().getUserName() :
                String.format("%s %s", message.getFrom().getLastName(), message.getFrom().getFirstName());
        return String.format("Person: %s | Chat: %s (%d)%s | Photos: %s%s%s",
                userName,
                message.getChat().getUserName(),
                message.getChatId(),
                message.isCommand() ? " | Command" : "",
                message.getPhoto() != null ? message.getPhoto().stream().map(p -> p.getFilePath() + " (" + p.getFileId() + ")").collect(Collectors.joining(", ", "[", "]")) : "[]",
                message.hasAnimation() ? " | Animation: " + message.getAnimation().getFileId() : "",
                message.hasSticker() ? " | Sticker: " + message.getSticker().getFileId() : "");
    }
}
