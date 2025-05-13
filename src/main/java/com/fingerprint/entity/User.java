package com.fingerprint.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "fingerprint_id")
    private Fingerprint visitorId;


    @OneToMany(mappedBy = "user")
    private List<IPAddress> ipAddresses;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "last_known_ip")
    private String lastKnownIp;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
}
