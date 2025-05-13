package com.fingerprint.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ip_addresses")
@Getter
@Setter
@NoArgsConstructor
public class IPAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ip;
    private String country;
    private String city;
    private LocalDateTime firstSeen;
    private LocalDateTime lastSeen;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
