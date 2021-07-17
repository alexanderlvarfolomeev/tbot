package com.aorise.db.repo;

import com.aorise.db.entity.Syntag;
import com.aorise.db.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SyntagRepository extends JpaRepository<Syntag, Long> {
    Optional<Syntag> findByName(String name);
}
