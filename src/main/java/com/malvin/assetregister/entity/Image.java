package com.malvin.assetregister.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String fileName;
    private String fileType;

    @Lob
    private Blob image;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

}