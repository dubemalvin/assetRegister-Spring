package com.malvin.assetregister.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String liaison;
    private String liaisonEmail;
    private String description;

//    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL,orphanRemoval = true)
//    private List<User> user;

}
