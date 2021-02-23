package com.aorise.bot;

import com.aorise.BotLogger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBot extends TelegramLongPollingBot {
    private final int id;
    private final String username;
    private final String token;
    private final Path directory;
    private final Map<String, String> mapper;

    private static final Executable EMPTY = new Executable();
    private static final Random RANDOM = new Random();

    private final Command UNKNOWN = new Command(EnumCommand.UNKNOWN, null);
    private final Command INVALID = new Command(EnumCommand.INVALID, null);

    public FileBot(String username, String token, String path, int id, Map<String, String> mapper) {
        this.username = username;
        this.token = token;
        directory = Path.of(path);
        this.mapper = mapper;
        this.id = id;
    }

    public String getBotUsername() {
        return username;
    }

    public String getBotToken() {
        return token;
    }

    public void onUpdateReceived(Update update) {
        BotLogger.logUpdate(update);
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getFrom().getId() == id) {
            Command command = getCommand(update.getMessage());
            Executable executable = switch (command.getCommand()) {
                case IS_ALIVE -> alive(update);
                case SHOW_PIC -> showPic(update, command.getText());
                case QUINE -> print(update, command.getText());
                case UNKNOWN -> unknown(update);
                case INVALID -> new Executable();
            };

            try {
                executable.execute();
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private MessageWrapper unknown(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("This command is unknown to me.");
        message.setReplyToMessageId(update.getMessage().getMessageId());
        return new MessageWrapper(message);
    }

    private Executable print(Update update, String text) {
        if (text.isEmpty()) {
            return EMPTY;
        }
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText(text);
        return new MessageWrapper(message);
    }

    private MessageWrapper alive(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("I'm still alive.");
        message.setReplyToMessageId(update.getMessage().getMessageId());
        return new MessageWrapper(message);
    }

    private Executable showPic(Update update, String text) {
        text = text.toLowerCase();
        if (!mapper.containsKey(text)) {
            return print(update, "Not implemented yet (or maybe never).");
        } else {
            text = mapper.get(text);
            SendPhoto message = new SendPhoto();
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
            try (Stream<Path> pathStream = Files.walk(directory.resolve(text))) {
                List<Path> paths = pathStream.filter(p -> !Files.isDirectory(p)).collect(Collectors.toList());
                Path path = getRandom(paths);
                if (path == null) {
                    BotLogger.botExc("\"" + text + "\" is empty directory.");
                    return print(update, "Sorry, can't find any pics.");
                }
                message.setPhoto(new InputFile(path.toFile()));
                return new PhotoWrapper(message);
            } catch (IOException e) {
                BotLogger.visitorExc(e);
                return print(update, "Oh shit, I'm sorry. IOException.");
            }
        }
    }

    private static <T> T getRandom(List<T> list) {
        return list.isEmpty() ? null : list.get(RANDOM.nextInt(list.size()));
    }

    private Command getCommand(Message message) {
        List<MessageEntity> entities = message.getEntities();
        return entities
                .stream()
                .filter(e -> e.getType().equals("bot_command") && e.getOffset() == 0)
                .map(e -> UNKNOWN.wrapCommand(message.getText(), e))
                .findFirst()
                .orElse(UNKNOWN);
    }

    private class Command {
        private final EnumCommand command;
        private final String text;

        private Command(EnumCommand command, String text) {
            this.command = command;
            this.text = text;
        }

        private boolean isCommand(String command, String expected) {
            String lower = command.toLowerCase();
            String prefix = "/" + expected;
            return lower.equals(prefix) ||
                    (lower.startsWith(prefix) && command.substring(prefix.length()).equals("@" + FileBot.this.username));
        }

        Command wrapCommand(String text, MessageEntity entity) {
            String commandText = entity.getText();
            if (isCommand(commandText, "show")) {
                return new Command(EnumCommand.SHOW_PIC, text.substring(entity.getLength()).strip());
            }
            if (isCommand(commandText, "alive")) {
                return new Command(EnumCommand.IS_ALIVE, null);
            }
            if (isCommand(commandText, "quine")) {
                return new Command(EnumCommand.QUINE, text.substring(entity.getLength()).strip());
            }
            return  commandText.contains("@" + FileBot.this.username) ? UNKNOWN : INVALID;
        }

        public EnumCommand getCommand() {
            return command;
        }

        public String getText() {
            return text;
        }
    }

    private static class Executable {
        void execute() throws TelegramApiException {
        }
    }

    private class PhotoWrapper extends Executable {
        private final SendPhoto photo;

        public PhotoWrapper(SendPhoto photo) {
            this.photo = photo;
        }

        @Override
        public void execute() throws TelegramApiException {
            FileBot.this.execute(photo);
        }
    }

    private class MessageWrapper extends Executable {
        private final SendMessage message;

        MessageWrapper(SendMessage message) {
            this.message = message;
        }

        @Override
        public void execute() throws TelegramApiException {
            FileBot.this.execute(message);
        }
    }

    //TODO start, gachi
    private enum EnumCommand {
        SHOW_PIC, IS_ALIVE, QUINE, UNKNOWN, INVALID
    }
}
