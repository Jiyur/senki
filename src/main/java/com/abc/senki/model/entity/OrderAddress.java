package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderAddress {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "\"id\"")
    private String id;

    @Column(name = "\"full_name\"")
    private String fullName;

    @Basic
    @Column(name="\"company_name\"")
    private String companyName;

    @Column(name = "\"phone_number\"")
    private String phoneNumber;

    @ManyToOne
    @Nullable
    @JoinColumn(name="\"province\"")
    private ProvinceEntity province;

    @ManyToOne
    @Nullable
    @JoinColumn(name="\"district\"")
    private DistrictEntity district;

    @ManyToOne
    @Nullable
    @JoinColumn(name="\"commune\"")
    private CommuneEntity commune;

    @Column(name="\"address_detail\"")
    private String addressDetail;

    @JsonBackReference
    @OneToMany(mappedBy = "address",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderEntity> orders;

    public void setInfo(String fullName, String companyName, String phoneNumber, ProvinceEntity province, DistrictEntity district, CommuneEntity commune, String addressDetail) {
        this.fullName = fullName;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.province = province;
        this.district = district;
        this.commune = commune;
        this.addressDetail = addressDetail;
    }

}


