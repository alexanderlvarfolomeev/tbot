package com.aorise.bot.handlers;

import com.aorise.BotLogger;
import com.aorise.bot.FileBot;
import com.aorise.bot.commands.FileBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;

public class OshieteHandler {
    private final FileBot bot;

    public OshieteHandler(FileBot bot) {
        this.bot = bot;
    }

    public void onGhoulMessage(FileBot.ChatContext context, Message message) {
        //TODO: polish
        if (message.hasText()) {
            try {
                int intText = Integer.parseInt(message.getText());
                if (context.getGhoulCounter() == intText) {
                    if (intText == 6) {
                        FileBotCommand.sendMessage(bot, message, "Чел доказал, что он реальный дед инсайд.", true);
                        context.setGhoulCounter(null);
                        context.setGhoulMessageId(null);
                        BotLogger.log("Ghoul game has been stopped in " + message.getChatId());
                    } else {
                        Message sent = FileBotCommand.sendMessage(bot, message, String.format("%d - 7?", context.getGhoulCounter()), false);
                        if (sent != null) {
                            context.setGhoulMessageId(sent.getMessageId());
                            context.setGhoulCounter(intText - 7);
                        }
                    }
                } else {
                    int x = FileBot.random(10);
                    if (x == 0) {
                        FileBotCommand.sendMessage(bot, message, "Чел ты...", true);
                    }
                    if (x == 1) {
                        FileBotCommand.sendMessage(bot, message, "Ты жалок.", true);
                    }
                    System.out.println(x);
                }
            } catch (NumberFormatException e) {
                //NAN
            }
        }
    }

    public String startGame(Message message) {
        FileBot.ChatContext context = bot.getContext(message.getChatId());
        if (context == null) {
            bot.onStart(message.getChatId());
            context = bot.getContext(message.getChatId());
        }
        if (context.getGhoulCounter() != null) {
            FileBotCommand.sendMessage(bot, message, "The fight to be the deadest inside is already in progress.", false);
            return "Double ghoul game start";
        }
        context.setGhoulCounter(993);
        Message sent = FileBotCommand.sendMessage(bot, message, "Okay... 1000 - 7?", false);
        if (sent != null) {
            context.setGhoulMessageId(sent.getMessageId());
            return "The ghoul game has been started.";
        } else {
            context.setGhoulCounter(null);
            return "Can't start the game";
        }
    }

    public String stopGame(Message message) {
        FileBot.ChatContext context = bot.getContext(message.getChatId());
        if (context == null) {
            return "There is no game";
        }
        context.setGhoulCounter(null);
        context.setGhoulMessageId(null);
        FileBotCommand.sendMessage(bot, message, "Okay there are no deadinsides in this chat.", false);
        return "Stopped";
    }
}
