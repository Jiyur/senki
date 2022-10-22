package com.abc.senki.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.annotation.Nullable;
import javax.persistence.*;

@Entity
@Table(name = "\"addresses\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "\"user\"")
    private UserEntity user;

    public void setInfo(String fullName,String companyName,String phoneNumber,String addressDetail,UserEntity user){
        this.fullName = fullName;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.addressDetail = addressDetail;
        this.user = user;
    }

}
