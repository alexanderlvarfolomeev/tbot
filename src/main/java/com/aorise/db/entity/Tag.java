package com.aorise.db.entity;

import javax.persistence.*;

@Entity
@Table(indexes = @Index(columnList = "name", unique = true))
public class Tag extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String folder;

    @Column(nullable = false)
    private boolean common;

    public Tag(String name, String folder) {
        this.name = name;
        this.folder = folder;
        this.common = true;
    }

    public Tag(String name, String folder, boolean common) {
        this.name = name;
        this.folder = folder;
        this.common = common;
    }

    public Tag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }
}
