package com.example.challange6.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "merchants")
public class Merchants {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "merchant_location")
    private String merchantLocation;

    @Column(name = "open")
    private Boolean open;

    @OneToMany(mappedBy = "merchant", fetch = FetchType.EAGER)
    private List<Products> products;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;


    public Merchants(String merchantName, String merchantLocation, Users users) {
        this(merchantName, merchantLocation, false, users);
    }

    public Merchants(String merchantName, String merchantLocation, boolean open, Users users) {
        this.merchantName = merchantName;
        this.merchantLocation = merchantLocation;
        this.open = open;
        this.user = users;
    }
}
