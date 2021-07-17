package com.aorise.db.service;

import com.aorise.db.entity.ChatContext;
import com.aorise.db.repo.ChatContextRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatContextService extends AbstractService<ChatContext> {
    private final ChatContextRepository repository;

    public ChatContextService(ChatContextRepository repository) {
        this.repository = repository;
    }

    public ChatContextRepository getRepository() {
        return repository;
    }

    public ChatContext loadByChatIdOrNull(long chatId) {
        return repository.findByChatId(chatId);
    }
}
