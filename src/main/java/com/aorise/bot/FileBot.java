package com.aorise.bot;

import com.aorise.BotLogger;
import com.aorise.bot.commands.*;
import com.aorise.util.MessageDescriber;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
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
    private Map<Long, ChatContext> contextMap = new HashMap<>();

    private static final Random RANDOM = new Random();
    private static final Map<String, String> INITIAL_MAPPER = new HashMap<>();

    public FileBot(String username, String token, String path, int id) {
        this.username = username;
        this.token = token;
        directory = Path.of(path);
        this.mapper = INITIAL_MAPPER;
        this.id = id;
        //TODO help, gachi
        register(new StartCommand("start", "Старт"));
        register(new SaveCommand("saveme", "Save"));
        register(new AliveCommand("alive", "Проверка на проявление признаков жизнедеятельности"));
        register(new QuineCommand("quine", "Продублировать сообщение"));
        register(new ShowCommand("show", "Показать пикчу"));
        register(new HornyCommand("horny", "В случае хорни."));
        register(new UpdateMappingCommand("update","Обновить отображение /show"));
        register(new OshieteCommand("deadinside", "Узнать, кто в чате настоящий dead inside"));
        register(new OshieteOverCommand("deadoutside", "Остановить игру"));
        // register(new HelpCommand("help","Помощь"));
        // register(new GachiCommand("gachi",""));
    }

    public String getBotUsername() {
        return username;
    }

    public String getBotToken() {
        return token;
    }

    public boolean onStart(long idx) {
        return contextMap.putIfAbsent(idx, new ChatContext()) == null;
    }

    public ChatContext getContext(long idx) {
        return contextMap.get(idx);
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
        return this;
    }

    @SuppressWarnings("unchecked")
    public FileBot loadContexts() {
        System.out.println(Files.isRegularFile(Path.of("src/main/resources/contexts")));
        if (Files.isRegularFile(Path.of("src/main/resources/contexts"))) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("src/main/resources/contexts"))) {
                contextMap = (Map<Long, ChatContext>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    // TODO: implement Autocloseable
    public void saveContexts() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src/main/resources/contexts"))) {
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
                    //TODO: to separated method + polish
                    if (message.hasText()) {
                        try {
                            int intText = Integer.parseInt(message.getText());
                            if (context.getGhoulCounter() == intText) {
                                if (intText == 6) {
                                    FileBotCommand.sendMessage(this, message, "Чел доказал, что он реальный дед инсайд.", true);
                                    context.setGhoulCounter(null);
                                    context.setGhoulMessageId(null);
                                    BotLogger.log("Ghoul game has been stopped in " + update.getMessage().getChatId());
                                } else {
                                    Message sent = FileBotCommand.sendMessage(this, message, String.format("%d - 7?", context.getGhoulCounter()), false);
                                    if (sent != null) {
                                        context.setGhoulMessageId(sent.getMessageId());
                                        context.setGhoulCounter(intText - 7);
                                    }
                                }
                            } else {
                                int x = RANDOM.nextInt(10);
                                if (x == 0) {
                                    FileBotCommand.sendMessage(this, message, "Чел ты...", true);
                                }
                                if (x == 1) {
                                    FileBotCommand.sendMessage(this, message, "Ты жалок.", true);
                                }
                                System.out.println(x);
                            }
                        } catch (NumberFormatException e) {
                            //NAN
                        }
                    }
                }
            }
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
