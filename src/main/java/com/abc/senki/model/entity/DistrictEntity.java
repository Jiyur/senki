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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"vn_district\"")
public class DistrictEntity {
    @Id
    @Column(name="\"id\"")
    private String id;
    @Column(name = "\"name\"")
    private String name;
    @Column(name = "\"type\"")
    private String type;
    @Column(name = "\"pro_id\"")
    private String province;

    @OneToMany(mappedBy = "district",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AddressEntity> addressEntities;

}
