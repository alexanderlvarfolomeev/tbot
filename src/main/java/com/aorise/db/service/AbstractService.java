package com.aorise.db.service;

import com.aorise.db.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractService<S extends AbstractEntity> {
    abstract JpaRepository<S, Long> getRepository();

    public List<S> loadByIds(final Iterable<Long> ids) {
        return getRepository().findAllById(ids);
    }

    public S loadOrNull(Long id) {
        return getRepository().findById(id).orElse(null);
    }

    public S save(final S entity) {
        return getRepository().save(entity);
    }

    public List<S> save(final Collection<S> list) {
        return getRepository().saveAll(list);
    }

    public List<S> loadAll() {
        return getRepository().findAll();
    }

    public void delete(S s) {
        getRepository().delete(s);
    }

    public void deleteById(Long id) {
        getRepository().deleteById(id);
    }

    public void deleteAll() {
        getRepository().deleteAll();
    }

    public void deleteAll(Iterable<S> s) {
        getRepository().deleteAll(s);
    }
}
