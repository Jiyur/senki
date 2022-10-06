package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "\"attribute_value\"")
public class AttributeValueEntity {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String value;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "\"attribute_id\"")
    private AttributeEntity attributeEntity;

    public void setInfo(AttributeEntity attributeEntity, String value) {
        this.attributeEntity = attributeEntity;
        this.value = value;
    }
}
