package com.aorise.bot;

public class BotConst {
    public static final String REPLY_NOT_IMPLEMENTED = "Not implemented yet (or maybe never).";
    public static final String REPLY_EMPTY_DIR = "Sorry, can't find any pics.";
    public static final String REPLY_CANT_PARSE = "Can't parse.";


    public static final String LIKE = "\uD83D\uDC4D";
    public static final String DISLIKE = "\uD83D\uDC4E";
    public static final String RATE = "rate";

    public enum Type {
        PHOTO, ANIMATION, VIDEO, UNKNOWN
    }
}
