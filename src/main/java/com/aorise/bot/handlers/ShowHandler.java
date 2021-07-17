package com.aorise.bot.handlers;

import com.aorise.BotLogger;
import com.aorise.bot.FileBot;
import com.aorise.bot.commands.FileBotCommand;
import com.aorise.db.entity.Rate;
import com.aorise.db.entity.Restriction;
import com.aorise.db.entity.Syntag;
import com.aorise.db.service.RateService;
import com.aorise.util.Utils;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShowHandler {
    private final FileBot bot;

    public ShowHandler(FileBot bot) {
        this.bot = bot;
    }

    private String get(FileBot bot, String text, Message msg) {
        Syntag got = bot.getDbHandler().getSyntagService().loadByNameOrNull(text);
        if (got != null) {
            List<Restriction> restrictions = bot.getDbHandler().getRestrictionService().findByTag(got.getTag());
            if (restrictions.stream().allMatch(r -> r.isPassing(msg, bot.getId()))) {
                return got.getTagFolder();
            }
        }
        return null;
    }

    //TODO: remove static methods from FileBotCommand
    public String show(Message msg, boolean rated) {
        String initText = FileBotCommand.stripCommand(msg);

        String text = get(bot, initText.toLowerCase(), msg);

        if (text == null) {
            FileBotCommand.sendMessage(bot, msg, "Not implemented yet (or maybe never).", false);
            return "Key \"" + initText + "\" is not found.";
        } else {
            String toReturn;
            try (Stream<Path> pathStream = Files.walk(bot.getDirectory().resolve(text))) {
                List<Path> paths = pathStream.filter(p -> !Files.isDirectory(p)).collect(Collectors.toList());
                Path path = Utils.getRandom(paths);
                if (path == null) {
                    BotLogger.botExc("\"" + text + "\" is empty directory.");
                    FileBotCommand.sendMessage(bot, msg, "Sorry, can't find any pics.", false);
                    toReturn = "Empty dir.";
                } else {
                    SendPhoto photo = new SendPhoto(String.valueOf(msg.getChatId()), new InputFile(path.toFile()));
                    if (rated) {
                        photo.setReplyMarkup(Utils.buildInlineMarkup("rate", List.of("\uD83D\uDC4D", "\uD83D\uDC4E")));
                    }
                    bot.execute(photo);
                    toReturn = "File \"" + path + "\" was sent.";
                }
            } catch (IOException e) {
                BotLogger.exc(e);
                FileBotCommand.sendMessage(bot, msg, "Oh shit, I'm sorry. IOException.", false);
                toReturn = "IOException occurred.";
            } catch (TelegramApiException e) {
                BotLogger.botExc(e.getMessage());
                e.printStackTrace();
                toReturn = "TelegramApiException.";
            }
            return toReturn;
        }
    }

    public void rate(String data, CallbackQuery query) {
        boolean pos = data.equals("\uD83D\uDC4D");
        long user = query.getFrom().getId();
        long chat = query.getMessage().getChatId();
        int msg = query.getMessage().getMessageId();
        RateService rateService = bot.getDbHandler().getRateService();
        Rate actual = rateService.loadOrNull(user, chat, msg);
        if (actual == null) {
            rateService.save(new Rate(user, chat, msg, pos));
        } else if (actual.isPos() == pos) {
            rateService.delete(actual);
        } else {
            actual.setPos(pos);
            rateService.save(actual);
        }
        int likes = rateService.count(chat, msg, true);
        int dislikes = rateService.count(chat, msg, false);
        EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup(String.valueOf(chat), msg, query.getInlineMessageId(), Utils.buildInlineMarkup1("rate", List.of(Map.entry("\uD83D\uDC4D", likes == 0 ? "\uD83D\uDC4D" : ("\uD83D\uDC4D  " + likes)), Map.entry("\uD83D\uDC4E", dislikes == 0 ? "\uD83D\uDC4E" : ("\uD83D\uDC4E  " + dislikes)))));
        try {
            bot.execute(replyMarkup);
        } catch (TelegramApiException e) {
            BotLogger.botExc(e.getMessage());
            e.printStackTrace();
        }
    }
}
