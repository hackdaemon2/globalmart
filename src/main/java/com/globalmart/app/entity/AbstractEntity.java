package com.globalmart.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@Table(
        indexes = {
                @Index(name = "idx_guid", columnList = "guid")
        })
class AbstractEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("id")
    private String guid;

    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Column(name = "date_modified")
    private Timestamp dateModified;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        dateCreated = Timestamp.from(Instant.now());
        dateModified = dateCreated;
        guid = UUID.randomUUID().toString();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModified = Timestamp.from(Instant.now());
    }

}
