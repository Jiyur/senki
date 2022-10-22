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
}
