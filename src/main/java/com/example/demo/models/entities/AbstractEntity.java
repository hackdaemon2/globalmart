package com.example.demo.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "date_created")
    private Timestamp dateCreated = Timestamp.from(Instant.now());

    @Column(name = "date_modified")
    private Timestamp dateModified = Timestamp.from(Instant.now());

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
