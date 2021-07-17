package com.aorise.db.service;

import com.aorise.db.entity.Tag;
import com.aorise.db.repo.TagRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class TagService extends AbstractService<Tag> {
    private final TagRepository repository;

    public TagService(TagRepository tagRepository) {
        this.repository = tagRepository;
    }

    public Tag loadByNameOrNull(String name) {
        return repository.findByName(name).orElse(null);
    }

    public TagRepository getRepository() {
        return repository;
    }

    public List<Tag> findAllByCommonTrue() {
        return repository.findAllByCommonTrue();
    }
}
