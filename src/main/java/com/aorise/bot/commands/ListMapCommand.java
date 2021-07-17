package com.aorise.bot.commands;

import com.aorise.bot.FileBot;
import com.aorise.db.entity.Tag;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Collectors;

public class ListMapCommand extends FileBotCommand {
    public ListMapCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    protected String processMessageImpl(FileBot bot, Message message, String[] arguments) {
//        try {
//            String result = Files.readString(BotConst.ACC_MAPPING);
        String result = bot.getDbHandler().getTagService().findAllByCommonTrue()//TODO: check valid
                .stream()
                .map(Tag::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.joining("\r\n"));
        sendMessage(bot, message, result, false);
//        } catch (IOException e) {
//            BotLogger.botExc(e.getMessage());
//            e.printStackTrace();
//        }
        return "Listed accepted tags";
    }
}
