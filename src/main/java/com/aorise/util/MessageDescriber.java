package com.aorise.util;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessageDescriber {
    private static final List<Function<Message, String>> SHORT_LIST = List.of(
            MessageDescriber::getPerson,
            MessageDescriber::getChat
    );
    private static final List<Function<Message, String>> LONG_LIST = List.of(
            MessageDescriber::getPerson,
            MessageDescriber::getChat,
            MessageDescriber::getPhotos,
            MessageDescriber::getAnimation,
            MessageDescriber::getSticker
    );

    public static String describe(Message message, boolean shorten) {
        return (shorten ? SHORT_LIST : LONG_LIST)
                .stream()
                .map(f -> f.apply(message))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" | "));
    }

    public static String describe(Message message) {
        return describe(message, false);
    }

    private static String getPerson(Message message) {
        return "Person: " + (message.getFrom().getUserName() != null ? message.getFrom().getUserName() : message.getFrom().getId().toString());
    }

    private static String getChat(Message message) {
        return message.getChat().getUserName() == null ? ("Chat: " + message.getChatId()) : String.format("Chat: %s(%d)", message.getChat().getUserName(), message.getChatId());
    }

    private static String getPhotos(Message message) {
        return message.hasPhoto() ? "Photos: " + message.getPhoto().stream().map(p -> p.getFilePath() + " (" + p.getFileId() + ")").collect(Collectors.joining(", ", "[", "]")) : "";
    }

    private static String getAnimation(Message message) {
        return message.hasAnimation() ? "Animation: " + message.getAnimation().getFileId() : "";
    }

    private static String getSticker(Message message) {
        return message.hasSticker() ? "Sticker: " + message.getSticker().getFileId() : "";
    }

}
