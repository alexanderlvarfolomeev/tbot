package com.aorise;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.LogManager;

public class Runner {
    private enum ConsoleCommands {
        UPDATE, SAVE, STOP, SHOWMAP, GET, INVALID, SEND
    }

    private static ConsoleCommands getCommand(String cmd) {
        try {
            return ConsoleCommands.valueOf(cmd);
        } catch (IllegalArgumentException e) {
            return ConsoleCommands.INVALID;
        }
    }

    public static void main(String... args) {
        try (
                InputStream botStream = Files.newInputStream(RunnerConst.BOT_PROPERTIES);
                InputStream loggingStream = Files.newInputStream(RunnerConst.LOGGING_PROPERTIES);
                PrintStream printStream = new PrintStream(Files.newOutputStream(RunnerConst.ERROR_LOG), true)) {
            System.setErr(printStream);
            LogManager.getLogManager().readConfiguration(loggingStream);
            Properties properties = new Properties();
            properties.load(botStream);
            String username = properties.getProperty("username");
            String token = properties.getProperty("token");
            String path = properties.getProperty("directory");
            int id = Integer.parseInt(properties.getProperty("id"));
            Objects.requireNonNull(username);
            Objects.requireNonNull(token);
            Objects.requireNonNull(path);

            DefaultBotOptions options = new DefaultBotOptions();
            options.setMaxThreads(2);
            options.setAllowedUpdates(List.of("message")); //TODO: callback_query
            options.setGetUpdatesTimeout(10);

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            try (
                    FileBot bot = new FileBot(username, token, path, id, options).updateMapper().loadContexts();
                    Scanner scanner = new Scanner(System.in)) {
                BotSession session = botsApi.registerBot(bot);
                BotLogger.log("Bot started");
                System.out.println("=============>>");
                ConsoleCommands cont;

                while((cont = getCommand(scanner.nextLine().toUpperCase(Locale.ROOT))) != ConsoleCommands.STOP) {
                    switch (cont) {
                        case UPDATE -> {
                            bot.updateMapper();
                            System.out.println("Updated");
                        }
                        case SAVE -> {
                            bot.saveContexts();
                            System.out.println("Saved");
                        }
                        case SHOWMAP -> {
                            System.out.println("===========");
                            bot.getMapper().keySet().forEach(System.out::println);
                            System.out.println("===========");
                        }
                        case GET -> {
                            System.out.println("===========");
                            System.out.println(bot.getMapper().get(scanner.nextLine()));
                            System.out.println("===========");
                        }
                        case SEND -> {
                            try {
                                System.out.println("===========");
                                String text = scanner.nextLine();
                                System.out.println("1");
                                String chatId = scanner.nextLine();
                                System.out.println("2");
                                String replyTo = scanner.nextLine();
                                System.out.println("3");
                                String md = scanner.nextLine();
                                System.out.println("4");
                                SendMessage msg = new SendMessage(chatId, text);
                                msg.enableMarkdown(Boolean.getBoolean(md));
                                System.out.println("5");
                                if (!replyTo.isEmpty()) {
                                    msg.setReplyToMessageId(Integer.parseInt(replyTo));
                                }
                                System.out.println("6");
                                bot.execute(msg);
                                System.out.println("===========");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (session.isRunning()) {
                    session.stop();
                }
            } finally {
                BotLogger.log("Bot stopped");
            }

        } catch (IOException e) {
            System.err.println("Could not initialize bot: " + e.getMessage());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
