package com.aorise.db.entity;

import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.*;
import java.util.function.Supplier;

@Entity
@Table(indexes = {
        @Index(columnList = "tag_id")
})
public class Restriction extends AbstractEntity {
    @OneToOne
    private Tag tag;

    @Column(nullable = false)
    private R rest;

    private String chat;

    public Restriction(Tag tag, R rest, String chat) {
        this.tag = tag;
        this.rest = rest;
        this.chat = chat;
    }

    public Restriction(Tag tag, R rest) {
        this.tag = tag;
        this.rest = rest;
    }

    public Restriction() {
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public R getRest() {
        return rest;
    }

    public void setRest(R rest) {
        this.rest = rest;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public boolean isPassing(Message msg, long adminId) {
        return switch (rest) {
            case ALL -> true;
            case ADMIN -> msg.getFrom().getId() == adminId;
            case CHAT -> msg.getChatId().toString().equals(chat);
            case NONE -> false;
        };
    }

    public enum R {
        ALL, ADMIN, CHAT, NONE
    }
}
