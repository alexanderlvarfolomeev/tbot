package com.aorise.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(columnList = "chatId", unique = true))
public class ChatContext extends AbstractEntity {
    @Column(nullable = false)
    private long chatId;

    private Integer ghoulCounter;

    private Integer ghoulMessageId;

    public ChatContext(long chatId) {
        this.chatId = chatId;
        ghoulCounter = null;
        ghoulMessageId = null;
    }

    public ChatContext(long chatId, Integer ghoulCounter, Integer ghoulMessageId) {
        this.chatId = chatId;
        this.ghoulCounter = ghoulCounter;
        this.ghoulMessageId = ghoulMessageId;
    }

    public ChatContext() {
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public Integer getGhoulCounter() {
        return ghoulCounter;
    }

    public void setGhoulCounter(Integer ghoulCounter) {
        this.ghoulCounter = ghoulCounter;
    }

    public Integer getGhoulMessageId() {
        return ghoulMessageId;
    }

    public void setGhoulMessageId(Integer ghoulMessageId) {
        this.ghoulMessageId = ghoulMessageId;
    }
}
