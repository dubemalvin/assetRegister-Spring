package com.malvin.assetregister.entity;

import com.malvin.assetregister.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId
    private String email;
    private String password;
//    @ManyToOne
//    @JoinColumn(name = "company_id", nullable = false)
//    private Company company;

    @Enumerated(EnumType.STRING)
    private Role role;
}
