package com.aorise.bot.handlers;

import com.aorise.BotLogger;
import com.aorise.bot.FileBot;
import com.aorise.db.entity.ChatContext;
import com.aorise.bot.commands.FileBotCommand;
import com.aorise.util.Utils;
import org.telegram.telegrambots.meta.api.objects.Message;

public class OshieteHandler {
    private final FileBot bot;

    public OshieteHandler(FileBot bot) {
        this.bot = bot;
    }

    public void onGhoulMessage(ChatContext context, Message message) {
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
                    String answer = switch (Utils.random(15)) {
                        case 0 -> "Чел ты...";
                        case 1 -> "Ты жалок.";
                        case 2 -> "Чекай мать";
                        case 3 -> "В муте";
                        case 4 -> "Соболезную";
                        default -> null;
                    };
                    if (answer != null) {
                        FileBotCommand.sendMessage(bot, message, answer, true);
                    }
                }
            } catch (NumberFormatException e) {
                //NAN
            }
        }
    }

    public String startGame(Message message) {
        ChatContext context = bot.getContext(message.getChatId());
        if (context == null) {
            bot.onStart(message.getChatId());
            context = bot.getContext(message.getChatId());
        }
        if (context.getGhoulCounter() != null) {
            FileBotCommand.sendMessage(bot, message, "Битва за право называться дединсайдом уже в прогрессе.", false);
            return "Double ghoul game start";
        }
        context.setGhoulCounter(993);
        Message sent = FileBotCommand.sendMessage(bot, message, "Ок... 1000 - 7?", false);
        if (sent != null) {
            context.setGhoulMessageId(sent.getMessageId());
            bot.getDbHandler().getChatContextService().save(context);
            return "The ghoul game has been started.";
        } else {
            context.setGhoulCounter(null);
            return "Can't start the game";
        }
    }

    public String stopGame(Message message) {
        ChatContext context = bot.getContext(message.getChatId());
        if (context == null) {
            return "There is no game";
        }
        context.setGhoulCounter(null);
        context.setGhoulMessageId(null);
        bot.getDbHandler().getChatContextService().save(context);
        FileBotCommand.sendMessage(bot, message, "Окей, в этом чате нет дединсайдов.", false);
        return "Stopped";
    }
}
