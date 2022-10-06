package com.abc.senki.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "\"attribute\"")
public class AttributeEntity {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "attributeEntity",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<AttributeValueEntity> attributeValueEntity;
}
