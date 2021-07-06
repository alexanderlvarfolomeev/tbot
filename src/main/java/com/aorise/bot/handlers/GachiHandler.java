package com.aorise.bot.handlers;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.api.objects.User;

public class GachiHandler {
    private final FileBot bot;

    public GachiHandler(FileBot bot) {
        this.bot = bot;
    }

    public boolean register(User user, long chatId) {
        FileBot.ChatContext context = bot.getContext(chatId);
//        boolean presented = context
//                .getName2Id()
//                .entrySet()
//                .stream()
//                .filter(e -> e.getValue().equals(user.getId()))
//                .findFirst()
//                .map(e -> {
//                    context.getName2Id().remove(e.getKey());
//                    return true;
//                })
//                .orElse(false);
//        context.getName2Id().put(user.getUserName(), user.getId());
//        return presented;
        return false;
    }

    public String fight(User user, String mention, long chatId) {
        long sender = user.getId();
//        Long mentioned = bot.getContext(chatId).getName2Id().get(mention);
        Long mentioned = null;
        if (mentioned == null) {
            return null;
        } else {
            //TODO
            return FileBot.random(2) == 0 ? user.getUserName() : mention;
        }
    }
}
