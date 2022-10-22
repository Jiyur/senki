package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"vn_province\"")
@Entity
public class ProvinceEntity {
    @Id
    @Column(name = "\"id\"")
    private String id;
    @Column(name = "\"name\"")
    private String name;
    @Column(name = "\"type\"")
    private String type;
    @Column(name = "\"slug\"")
    private String slug;

    @JsonIgnore
    @OneToMany(mappedBy = "province",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<AddressEntity> addressEntities;
}
