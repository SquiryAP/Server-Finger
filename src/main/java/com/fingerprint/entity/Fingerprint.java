package com.fingerprint.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fingerprints")
@Getter
@Setter
@NoArgsConstructor
public class Fingerprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String visitorId;

    @Column(name = "hash_components", columnDefinition = "MEDIUMTEXT")
    private String hashComponents; // полный JSON отпечатка
}
