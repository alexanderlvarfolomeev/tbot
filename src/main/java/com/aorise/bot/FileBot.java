package com.aorise.bot;

import com.aorise.BotLogger;
import com.aorise.bot.commands.*;
import com.aorise.util.MessageDescriber;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBot extends TelegramLongPollingCommandBot {
    private final int id;
    private final String username;
    private final String token;
    private final Path directory;
    private Map<String, String> mapper;

    private static final Random RANDOM = new Random();
    private static final Map<String, String> INITIAL_MAPPER = new HashMap<>();

    public FileBot(String username, String token, String path, int id) {
        this.username = username;
        this.token = token;
        directory = Path.of(path);
        this.mapper = INITIAL_MAPPER;
        this.id = id;
        //TODO help
        register(new StartCommand("start", "Старт"));
        register(new AliveCommand("alive", "Проверка на проявление признаков жизнедеятельности"));
        register(new QuineCommand("quine", "Продублировать сообщение"));
        register(new ShowCommand("show", "Показать пикчу"));
        register(new HornyCommand("horny", "В случае хорни."));
        register(new UpdateMappingCommand("update","Обновить отображение /show"));
        //register(new HelpCommand("help","Помощь"));
    }

    public String getBotUsername() {
        return username;
    }

    public String getBotToken() {
        return token;
    }

    public FileBot updateMapper() {
        Map<String, String> localMapper = new HashMap<>();
        try (BufferedReader mapperReader = Files.newBufferedReader(Path.of("src/main/resources/mapping.txt"), StandardCharsets.UTF_8)) {
            Properties mapperProperties = new Properties();
            mapperProperties.load(mapperReader);
            for (final String name : mapperProperties.stringPropertyNames()) {
                localMapper.put(name, mapperProperties.getProperty(name));
            }
            mapper = localMapper;
        } catch (IOException e) {
            BotLogger.botExc("Couldn't update mapper: " + e.getMessage());
        }
        //System.out.println(mapper);
        return this;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            BotLogger.log(String.format("Non command message. %s", MessageDescriber.describe(update.getMessage())));
        } else {
            BotLogger.log("Update doesn't contain message");
        }
    }

    @Override
    protected void processInvalidCommandUpdate(Update update) {
        super.processInvalidCommandUpdate(update);
        //TODO: ?
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

    public int getId() {
        return id;
    }

    public static <T> T getRandom(List<T> list) {
        return list.isEmpty() ? null : list.get(RANDOM.nextInt(list.size()));
    }
}
