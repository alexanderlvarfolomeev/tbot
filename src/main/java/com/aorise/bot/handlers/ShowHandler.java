package com.aorise.bot.handlers;

import com.aorise.BotLogger;
import com.aorise.bot.BotConst;
import com.aorise.bot.FileBot;
import com.aorise.bot.commands.FileBotCommand;
import com.aorise.db.entity.Rate;
import com.aorise.db.entity.Restriction;
import com.aorise.db.entity.Syntag;
import com.aorise.db.service.RateService;
import com.aorise.util.Utils;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
            FileBotCommand.sendMessage(bot, msg, BotConst.REPLY_NOT_IMPLEMENTED, false);
            return "Key \"" + initText + "\" is not found.";
        } else {
            String toReturn;
            try (Stream<Path> pathStream = Files.walk(bot.getDirectory().resolve(text))) {
                List<Path> paths = pathStream.filter(p -> !Files.isDirectory(p)).collect(Collectors.toList());
                Path path = Utils.getRandom(paths);
                if (path == null) {
                    BotLogger.botExc("\"" + text + "\" is empty directory.");
                    FileBotCommand.sendMessage(bot, msg, BotConst.REPLY_EMPTY_DIR, false);
                    toReturn = "Empty dir.";
                } else {
                    var mmw = new MimeHandler().wrap(String.valueOf(msg.getChatId()), path);

                    if (rated) {
                        mmw.setReplyMarkup(Utils.buildIKM(BotConst.RATE, List.of(BotConst.LIKE, BotConst.DISLIKE)));
                    }
                    mmw.execute(bot);
                    toReturn = "File \"" + path + "\" was sent.";
                }
            } catch (IOException | TelegramApiException e) {
                BotLogger.exc(e);
                e.printStackTrace();
                toReturn = e.getClass().getSimpleName();
            }
            return toReturn;
        }
    }

    public void rate(String data, CallbackQuery query) {
        boolean pos = data.equals(BotConst.LIKE);
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
        var replyMarkup = new EditMessageReplyMarkup(
                String.valueOf(chat),
                msg,
                query.getInlineMessageId(),
                Utils.buildIKM1(BotConst.RATE, List.of(join(BotConst.LIKE, likes), join(BotConst.DISLIKE, dislikes)))
        );
        try {
            bot.execute(replyMarkup);
        } catch (TelegramApiException e) {
            BotLogger.botExc(e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map.Entry<String, String> join(String name, int amount) {
        return Map.entry(name, amount == 0 ? name : name + "  " + amount);
    }
}
