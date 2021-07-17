package com.aorise.bot;

import com.aorise.BotLogger;
import com.aorise.bot.commands.*;
import com.aorise.bot.handlers.DBHandler;
import com.aorise.bot.handlers.OshieteHandler;
import com.aorise.bot.handlers.ShowHandler;
import com.aorise.db.entity.ChatContext;
import com.aorise.util.MessageDescriber;
import com.aorise.util.Utils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.file.Path;
import java.util.*;

public class FileBot extends TelegramLongPollingCommandBot {
    private final int id;
    private final String username;
    private final String token;
    private final Path directory;
    private final long time;
    private boolean ready;
    private final OshieteHandler oshieteHandler;
    private final ShowHandler showHandler;
    private final DBHandler dbHandler;

    public FileBot(String username, String token, String path, int id, DBHandler dbHandler, DefaultBotOptions options) {
        super(options);
        this.username = username;
        this.token = token;
        directory = Path.of(path);
        this.id = id;
        this.time = System.currentTimeMillis();
        this.ready = false;
        oshieteHandler = new OshieteHandler(this);
        showHandler = new ShowHandler(this);
        this.dbHandler = dbHandler;
        //TODO help, gachi
        register(new StartCommand("start", "Старт"));
        register(new AliveCommand("alive", "Проверка на проявление признаков жизнедеятельности"));
        register(new QuineCommand("repeat", "Продублировать сообщение"));
        register(new ShowCommand("show", "Показать пикчу", false, showHandler));
        register(new ShowCommand("showrt", "Показать оцениваемую пикчу", true, showHandler));
        register(new HornyCommand("horny", "В случае хорни."));
        register(new OshieteCommand("deadinside", "Узнать, кто в чате настоящий dead inside", oshieteHandler));
        register(new OshieteOverCommand("deadoutside", "Остановить игру", oshieteHandler));
        register(new RollCommand("roll", "Ролльнуть дайсы"));
        register(new ListMapCommand("showlist", "Показать одобренные партией теги"));
        // register(new HelpCommand("help","Помощь"));
        // register(new GachiCommand("gachi","Gachi"));
    }

    public DBHandler getDbHandler() {
        return dbHandler;
    }

    public String getBotUsername() {
        return username;
    }

    public String getBotToken() {
        return token;
    }

    public boolean isReady() {
        if (ready) {
            return true;
        } else {
            ready = System.currentTimeMillis() - time > 5_000L;
            return ready;
        }
    }

    public boolean onStart(long chatId) {
        com.aorise.db.entity.ChatContext ctx = dbHandler.getChatContextService().loadByChatIdOrNull(chatId);
        if (ctx == null) {
            dbHandler.getChatContextService().save(new com.aorise.db.entity.ChatContext(chatId));
            return true;
        } else {
            return false;
        }
    }

    public ChatContext getContext(long chatId) {
        return dbHandler.getChatContextService().loadByChatIdOrNull(chatId);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
//            ChatContext context = contextMap.get(update.getMessage().getChatId());
            com.aorise.db.entity.ChatContext context = dbHandler.getChatContextService().loadByChatIdOrNull(update.getMessage().getChatId());
            Message message = update.getMessage();
            Message replyToMessage = message.getReplyToMessage();
            if (context != null && replyToMessage != null) {
                if (Objects.equals(context.getGhoulMessageId(), replyToMessage.getMessageId())) {
                    BotLogger.log(String.format("Non command \"oshiete\" message. %s", MessageDescriber.describe(update.getMessage())));
                    oshieteHandler.onGhoulMessage(context, message);
                    dbHandler.getChatContextService().save(context);
                }
            }
        } else {
            if (update.hasCallbackQuery()) {
                CallbackQuery query = update.getCallbackQuery();
                Map.Entry<String, String> entry = Utils.splitBy(':', query.getData());
                if (entry.getKey().equals("rate")) {
                    showHandler.rate(entry.getValue(), query);
                    return;
                }
            }
            //TODO: for now, it's ok, but talking about the future...
            BotLogger.log(String.format("Update doesn't contain message: %s", update.toString()));
        }
    }

    @Override
    protected void processInvalidCommandUpdate(Update update) {
        processNonCommandUpdate(update);
    }

    //TODO make it one of CommandRegistry defaultConsumer part (if the command contains bot username)
//    private MessageWrapper unknown(Update update) {
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(update.getMessage().getChatId()));
//        message.setText("This command is unknown to me.");
//        message.setReplyToMessageId(update.getMessage().getMessageId());
//        return new MessageWrapper(message);
//    }

    public Path getDirectory() {
        return directory;
    }

    public int getId() {
        return id;
    }
}
