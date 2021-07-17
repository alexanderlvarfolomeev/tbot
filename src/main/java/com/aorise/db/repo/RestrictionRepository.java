package com.aorise.db.repo;

import com.aorise.db.entity.Restriction;
import com.aorise.db.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestrictionRepository extends JpaRepository<Restriction, Long> {
    List<Restriction> findAllByTag(Tag tag);
}
