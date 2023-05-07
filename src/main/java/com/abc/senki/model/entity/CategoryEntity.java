package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "\"categories\"")
public class CategoryEntity  {
    @Id
    @Column(name = "cate_id")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "cate_name")
    private String name;

    @OneToMany(mappedBy = "productCategory",cascade = CascadeType.ALL)
    @JsonBackReference
    private List<ProductEntity> listProduct;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="\"parent_id\"",referencedColumnName="\"cate_id\"")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Collection<CategoryEntity> subCategories;


   
}
