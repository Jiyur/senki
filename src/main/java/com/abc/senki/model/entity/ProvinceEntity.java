package com.abc.senki.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
