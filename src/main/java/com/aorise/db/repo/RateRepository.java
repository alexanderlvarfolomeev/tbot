package com.aorise.db.repo;

import com.aorise.db.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    Optional<Rate> findByUserAndChatAndMessage(long user, long chat, long message);
    int countByChatAndMessageAndPos(long chat, long message, boolean pos);
}
