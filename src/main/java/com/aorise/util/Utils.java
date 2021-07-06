package com.aorise.util;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static String getUserName(User user) {
        return user.getUserName();
    }

    public static ReplyKeyboard buildInlineMarkup(String prefix, List<String> buttonNames) {
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
}
