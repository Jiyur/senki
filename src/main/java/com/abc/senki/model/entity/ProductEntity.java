package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"products\"")
public class ProductEntity  {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private Double price;

    private String name;

    private String description;

    private int stock;

    private int status;

    private int sellAmount;

    @Column(name = "\"created_at\"")
    private LocalDateTime  createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ImageProductEntity> imageProductEntityList;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"cate_id\"")
    private CategoryEntity productCategory;

    public void setInfo(String name,String description,Double price,int stock){
        this.name=name;
        this.description=description;
        this.price=price;
        this.stock=stock;
    }


}
