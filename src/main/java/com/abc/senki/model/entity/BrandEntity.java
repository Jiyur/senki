package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "brand")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private int id;

    @Column(name = "\"name\"")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "brand")
    private List<ProductEntity> listProduct;


}
