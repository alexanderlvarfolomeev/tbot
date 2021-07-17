package com.aorise.util;

import com.aorise.db.entity.Restriction;
import com.aorise.db.entity.Syntag;
import com.aorise.db.entity.Tag;
import com.aorise.db.service.RestrictionService;
import com.aorise.db.service.SyntagService;
import com.aorise.db.service.TagService;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {
    public static final Random RANDOM = new Random();

    public static String getUserName(User user) {
        return user.getUserName();
    }

    public static InlineKeyboardMarkup buildInlineMarkup(String prefix, List<String> buttonNames) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(
                        buttonNames
                                .stream()
                                .map(n -> InlineKeyboardButton
                                        .builder()
                                        .text(n)
                                        .callbackData(prefix + ":" + n)
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static InlineKeyboardMarkup buildInlineMarkup1(String prefix, List<Map.Entry<String, String>> buttonNames) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(
                        buttonNames
                                .stream()
                                .map(n -> InlineKeyboardButton
                                        .builder()
                                        .text(n.getValue())
                                        .callbackData(prefix + ":" + n.getKey())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }

    public static <T extends AutoCloseable, E extends Exception> T propagate(T entity, ThrowingConsumer<T, E> consumer) throws E {
        consumer.accept(entity);
        return entity;
    }

    public static int random(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static Map.Entry<String, String> splitBy(char delimiter, String text) {
        int pos = text.indexOf(delimiter);
        return Map.entry(text.substring(0, pos), text.substring(pos + 1));
    }

    public static <T> T getRandom(List<T> list) {
        return list.isEmpty() ? null : list.get(random(list.size()));
    }

//    public static void save(Map<String, String> mapper, Map<String, String> exc_mapper, TagService tagService, SyntagService syntagService, RestrictionService restrictionService) {
//        Set<String> actualTags = tagService
//                .loadAll()
//                .stream()
//                .map(Tag::getName)
//                .collect(Collectors.toSet());
//        List<Tag> tags = mapper
//                .values()
//                .stream()
//                .distinct()
//                .filter(t -> !actualTags.contains(t))
//                .map(s -> new Tag(s, s))
//                .collect(Collectors.toList());
//        tagService.save(tags);
//        tags = tagService.loadAll();
//        Set<String> actualSyntags = syntagService
//                .loadAll()
//                .stream()
//                .map(Syntag::getName)
//                .collect(Collectors.toSet());
//        Map<String, Tag> m = tags
//                .stream()
//                .collect(Collectors.toMap(Tag::getName, Function.identity()));
//        List<Syntag> syntags = mapper
//                .entrySet()
//                .stream()
//                .filter(e -> !actualSyntags.contains(e.getKey()))
//                .map(e -> new Syntag(e.getKey(), m.get(e.getValue())))
//                .collect(Collectors.toList());
//        syntagService.save(syntags);
//        List<Restriction> restrictions = exc_mapper
//                .entrySet()
//                .stream()
//                .map(e -> new Restriction(m.get(e.getKey()), Restriction.R.CHAT, e.getValue()))
//                .collect(Collectors.toList());
//        restrictionService.save(restrictions);
//    }
}
