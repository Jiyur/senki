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
@Table(name = "\"vn_commune\"")
@Entity
public class CommuneEntity {
    @Id
    @Column(name="\"id\"")
    private String id;
    @Column(name = "\"name\"")
    private String name;
    @Column(name = "\"type\"")
    private String type;
    @Column(name="\"dis_id\"")
    private String district;

    @JsonIgnore
    @OneToMany(mappedBy = "commune",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<AddressEntity> addressEntities;

}
