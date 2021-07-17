package com.aorise.db.service;

import com.aorise.db.entity.Rate;
import com.aorise.db.repo.RateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class RateService extends AbstractService<Rate> {
    private final RateRepository repository;

    public RateService(RateRepository repository) {
        this.repository = repository;
    }

    public JpaRepository<Rate, Long> getRepository() {
        return repository;
    }

    public Rate loadOrNull(long user, long chat, long message) {
        return repository.findByUserAndChatAndMessage(user, chat, message).orElse(null);
    }

    public int count(long chat, long message, boolean pos) {
        return repository.countByChatAndMessageAndPos(chat, message, pos);
    }
}
