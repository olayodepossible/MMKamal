package com.possible.mmk.auth.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "account")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String username;

    @Column(name = "auth_id")
    private String password;

    private String roles;

    @JsonManagedReference
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
}
