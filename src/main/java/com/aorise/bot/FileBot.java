package com.aorise.bot;

import com.aorise.BotLogger;
import com.aorise.bot.commands.*;
import com.aorise.bot.handlers.OshieteHandler;
import com.aorise.util.Loader;
import com.aorise.util.MessageDescriber;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.aorise.bot.BotConst;

public class FileBot extends TelegramLongPollingCommandBot implements AutoCloseable {
    private final int id;
    private final String username;
    private final String token;
    private final Path directory;
    private final long time;
    private boolean ready;
    private Map<String, String> mapper;
    private Map<String, String> exc_mapper;
    private Map<Long, ChatContext> contextMap = new HashMap<>();
    private final OshieteHandler oshieteHandler;

    private static final Random RANDOM = new Random();
    private static final Map<String, String> INITIAL_MAPPER = new HashMap<>();

    public FileBot(String username, String token, String path, int id, DefaultBotOptions options) {
        super(options);
        this.username = username;
        this.token = token;
        directory = Path.of(path);
        this.mapper = INITIAL_MAPPER;
        this.exc_mapper = INITIAL_MAPPER;
        this.id = id;
        this.time = System.currentTimeMillis();
        this.ready = false;
        oshieteHandler = new OshieteHandler(this);
        //TODO help, gachi
        register(new StartCommand("start", "Старт"));
        register(new SaveCommand("saveme", "Save"));
        register(new AliveCommand("alive", "Проверка на проявление признаков жизнедеятельности"));
        register(new QuineCommand("quine", "Продублировать сообщение"));
        register(new ShowCommand("show", "Показать пикчу"));
        register(new HornyCommand("horny", "В случае хорни."));
        register(new UpdateMappingCommand("update","Обновить отображение /show"));
        register(new OshieteCommand("deadinside", "Узнать, кто в чате настоящий dead inside", oshieteHandler));
        register(new OshieteOverCommand("deadoutside", "Остановить игру", oshieteHandler));
        // register(new HelpCommand("help","Помощь"));
        // register(new GachiCommand("gachi",""));
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
            ready = System.currentTimeMillis() - time > 10_000L;
            return ready;
        }
    }

    public static int random(int bound) {
        return RANDOM.nextInt(bound);
    }

    public boolean onStart(long idx) {
        return contextMap.putIfAbsent(idx, new ChatContext()) == null;
    }

    public ChatContext getContext(long idx) {
        return contextMap.get(idx);
    }

    public FileBot updateMapper() {
        try {
            mapper = Loader.loadMapper(BotConst.MAPPING);
            exc_mapper = Loader.loadMapper(BotConst.EXC_MAPPING);
        } catch (IOException e) {
            BotLogger.botExc("Couldn't update mapper: " + e.getMessage());
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public FileBot loadContexts() {
        if (Files.isRegularFile(BotConst.CONTEXTS)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(BotConst.CONTEXTS))) {
                contextMap = (Map<Long, ChatContext>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public void saveContexts() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(BotConst.CONTEXTS))) {
            objectOutputStream.writeObject(contextMap);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            BotLogger.log(String.format("Non command message. %s", MessageDescriber.describe(update.getMessage())));
            ChatContext context = contextMap.get(update.getMessage().getChatId());
            Message message = update.getMessage();
            Message replyToMessage = message.getReplyToMessage();
            if (context != null && replyToMessage != null) {
                if (Objects.equals(context.getGhoulMessageId(), replyToMessage.getMessageId())) {
                    oshieteHandler.onGhoulMessage(context, message);
                }
            }
        } else {
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

    public Map<String, String> getMapper() {
        return mapper;
    }

    public Map<String, String> getExc_mapper() {
        return exc_mapper;
    }

    public int getId() {
        return id;
    }

    public static <T> T getRandom(List<T> list) {
        return list.isEmpty() ? null : list.get(random(list.size()));
    }

    @Override
    public void close() {
        saveContexts();
    }

    public static class ChatContext implements Serializable {
        private Integer ghoulCounter = null;
        private Integer ghoulMessageId = null;

        public Integer getGhoulCounter() {
            return ghoulCounter;
        }

        public void setGhoulCounter(Integer ghoulCounter) {
            this.ghoulCounter = ghoulCounter;
        }

        public Integer getGhoulMessageId() {
            return ghoulMessageId;
        }

        public void setGhoulMessageId(Integer ghoulMessageId) {
            this.ghoulMessageId = ghoulMessageId;
        }

        @Override
        public String toString() {
            return "Context(" + ghoulCounter + ")";
        }
    }
}
