package com.possible.sms.model.dto;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_number_id_seq")
    @SequenceGenerator(name = "phone_number_id_seq", sequenceName = "phone_number_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "id", unique = true, nullable = false)
    private String number;
}
