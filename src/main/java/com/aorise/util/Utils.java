package com.aorise.util;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static final Random RANDOM = new Random();

    public static String getUserName(User user) {
        return user.getUserName();
    }

    public static InlineKeyboardMarkup buildIKM(String prefix, List<String> buttonNames) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(
                        buttonNames
                                .stream()
                                .map(n -> InlineKeyboardButton
                                        .builder()
                                        .text(n)
                                        .callbackData(prefix + ":" + n)
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static InlineKeyboardMarkup buildIKM1(String prefix, List<Map.Entry<String, String>> buttonNames) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(
                        buttonNames
                                .stream()
                                .map(n -> InlineKeyboardButton
                                        .builder()
                                        .text(n.getValue())
                                        .callbackData(prefix + ":" + n.getKey())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    public static <T extends AutoCloseable, E extends Exception> T propagate(T entity, ThrowingConsumer<T, E> consumer) throws E {
        consumer.accept(entity);
        return entity;
    }

    public static int random(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static Map.Entry<String, String> splitBy(char delimiter, String text) {
        int pos = text.indexOf(delimiter);
        return Map.entry(text.substring(0, pos), text.substring(pos + 1));
    }

    public static <T> T getRandom(List<T> list) {
        return list.isEmpty() ? null : list.get(random(list.size()));
    }
}
