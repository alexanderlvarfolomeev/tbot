package com.aorise.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = {
        @Index(columnList = "user, chat, message", unique = true),
        @Index(columnList = "chat, message, pos")
})
public class Rate extends AbstractEntity {
    @Column(nullable = false)
    private long user;

    @Column(nullable = false)
    private long chat;

    @Column(nullable = false)
    private long message;

    @Column(nullable = false)
    private boolean pos;

    public Rate(long user, long chat, long message, boolean pos) {
        this.user = user;
        this.chat = chat;
        this.message = message;
        this.pos = pos;
    }

    public Rate() {
    }

    public Long getUser() {
        return user;
    }

    public void setUser(long userId) {
        user = userId;
    }

    public Long getChat() {
        return chat;
    }

    public void setChat(long chatId) {
        this.chat = chatId;
    }

    public Long getMessage() {
        return message;
    }

    public void setMessage(long messageId) {
        message = messageId;
    }

    public boolean isPos() {
        return pos;
    }

    public void setPos(boolean pos) {
        this.pos = pos;
    }
}
