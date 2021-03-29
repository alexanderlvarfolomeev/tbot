package com.aorise;

import com.aorise.bot.FileBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.LogManager;

public class Runner {
    public static void main(String... args) {
        try (
                InputStream botStream = Runner.class.getResourceAsStream("/bot.properties");
                InputStream loggingStream = Runner.class.getResourceAsStream("/logging.properties")) {
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

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new FileBot(username, token, path, id).updateMapper().loadContexts());
        } catch (IOException e) {
            System.err.println("Could not initialize bot: " + e.getMessage());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
