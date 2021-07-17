package com.aorise.db.service;

import com.aorise.db.entity.Restriction;
import com.aorise.db.entity.Tag;
import com.aorise.db.repo.RestrictionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestrictionService extends AbstractService<Restriction> {
    private final RestrictionRepository repository;

    public RestrictionService(RestrictionRepository repository) {
        this.repository = repository;
    }

    @Override
    public RestrictionRepository getRepository() {
        return repository;
    }

    public List<Restriction> findByTag(Tag tag) {
        return repository.findAllByTag(tag);
    }
}
