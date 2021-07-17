package com.aorise.db.service;

import com.aorise.db.entity.Syntag;
import com.aorise.db.repo.SyntagRepository;
import org.springframework.stereotype.Service;

@Service
public class SyntagService extends AbstractService<Syntag> {
    private final SyntagRepository repository;

    public SyntagService(SyntagRepository tagRepository) {
        this.repository = tagRepository;
    }

    public Syntag loadByNameOrNull(String name) {
        return repository.findByName(name).orElse(null);
    }

    public SyntagRepository getRepository() {
        return repository;
    }
}
