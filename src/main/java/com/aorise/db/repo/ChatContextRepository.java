package com.aorise.db.repo;

import com.aorise.db.entity.ChatContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatContextRepository extends JpaRepository<ChatContext, Long> {
    ChatContext findByChatId(long chatId);
}
