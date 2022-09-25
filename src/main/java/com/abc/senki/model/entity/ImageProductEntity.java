package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "\"list_img_products\"")
public class ImageProductEntity {
    @Id
    @Column(name = "\"img_id\"")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "\"product_id\"")
    private ProductEntity product;

    @Column(name = "url")
    private String url;

    public ImageProductEntity() {
    }


}
