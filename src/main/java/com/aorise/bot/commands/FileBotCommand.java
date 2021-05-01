package com.aorise.bot.commands;

import com.aorise.BotLogger;
import com.aorise.bot.FileBot;
import com.aorise.util.MessageDescriber;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class FileBotCommand extends BotCommand {
    public FileBotCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    static String stripCommand(Message message) {
        return message.getText().replaceFirst("\\S+", "").strip();
    }

    public static Message sendMessage(AbsSender sender, Message msg, String text, boolean isNeedToReply) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(msg.getChatId().toString());
        if (isNeedToReply) {
            message.setReplyToMessageId(msg.getMessageId());
        }
        try {
            return sender.execute(message);
        } catch (TelegramApiException e) {
            BotLogger.botExc(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        FileBot bot = (FileBot) absSender;
        if (bot.isReady()) {
            BotLogger.log(String.format("%n%s.%nResult: %s", MessageDescriber.describe(message), processMessageImpl(absSender, message, arguments)));
        }
    }

    protected abstract String processMessageImpl(AbsSender absSender, Message message, String[] arguments);

    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        // useless
    }
}
