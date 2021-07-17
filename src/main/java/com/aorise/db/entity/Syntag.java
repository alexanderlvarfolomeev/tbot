package com.aorise.db.entity;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(columnList = "name", unique = true),
        @Index(columnList = "tag_id")
})
public class Syntag extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Tag tag;

    public Syntag(String name, Tag tag) {
        this.name = name;
        this.tag = tag;
    }

    public Syntag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getTagFolder() {
        return tag.getFolder();
    }

    @Override
    public String toString() {
        return String.format("Syntag(%s : %s)", name, tag.getName());
    }
}
