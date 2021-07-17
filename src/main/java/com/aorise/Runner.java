package com.aorise;

import com.aorise.bot.FileBot;
import com.aorise.bot.handlers.DBHandler;
import com.aorise.util.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

@SpringBootApplication
public class Runner {
    private final DBHandler dbHandler; //TODO

    public Runner(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void run() {
        Properties properties = new Properties();

        try (
                PrintStream ignored1 = Utils.propagate(
                        new PrintStream(Files.newOutputStream(RunnerConst.ERROR_LOG), true),
                        System::setErr
                );
                InputStream ignored2 = Utils.propagate(
                        Files.newInputStream(RunnerConst.BOT_PROPERTIES),
                        properties::load
                );
                InputStream ignored3 = Utils.propagate(
                        Files.newInputStream(RunnerConst.LOGGING_PROPERTIES),
                        LogManager.getLogManager()::readConfiguration)
        ) {
//            System.setErr(printStream);
//            LogManager.getLogManager().readConfiguration(loggingStream);
//            properties.load(botStream);
            String username = properties.getProperty("username");
            String token = properties.getProperty("token");
            String path = properties.getProperty("directory");
            int id = Integer.parseInt(properties.getProperty("id"));
            Objects.requireNonNull(username);
            Objects.requireNonNull(token);
            Objects.requireNonNull(path);

            DefaultBotOptions options = new DefaultBotOptions();
            options.setMaxThreads(2);
            options.setAllowedUpdates(List.of("message", "callback_query"));
//            options.setGetUpdatesTimeout(10);

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            try (Scanner scanner = new Scanner(System.in)) {
                FileBot bot = new FileBot(username, token, path, id, dbHandler, options);
                BotSession session = botsApi.registerBot(bot);
                BotLogger.log("Bot started");
                System.out.println("=============>>");
                ConsoleCommands cont;

                while((cont = getCommand(scanner.nextLine().toUpperCase(Locale.ROOT))) != ConsoleCommands.STOP) {
                    switch (cont) {
                        case GET -> {
                            System.out.println("===========");
                            System.out.println(bot.getDbHandler().getSyntagService().loadByNameOrNull(scanner.nextLine()).getTag().getName());
                            System.out.println("===========");
                        }
                        case SEND -> {
                            try {
                                System.out.println("===========");
                                System.out.print("Text: ");
                                String text = scanner.nextLine();
                                System.out.print("ChatId: ");
                                String chatId = scanner.nextLine();
                                System.out.print("Markdown: ");
                                String md = scanner.nextLine();
                                SendMessage msg = new SendMessage(chatId, text);
                                msg.enableMarkdown(Boolean.getBoolean(md));
                                bot.execute(msg);
                                System.out.println("===========");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                System.out.println("=============<<");
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

    private enum ConsoleCommands {
        STOP, GET, INVALID, SEND
    }

    private static ConsoleCommands getCommand(String cmd) {
        try {
            return ConsoleCommands.valueOf(cmd);
        } catch (IllegalArgumentException e) {
            return ConsoleCommands.INVALID;
        }
    }

    public static void main(String... args) {
        final var run = SpringApplication.run (Runner.class, args);
        run.getBean(Runner.class).run();
        run.close();
    }
}
