package com.abc.senki.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "\"vouchers\"")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VoucherEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String description;
    private String code;
    private String type;
    private Double value;
    private LocalDateTime endDate;

    public VoucherEntity(String name,String description,String code,String type,Double value){
        this.name=name;
        this.description=description;
        this.code=code;
        this.type=type;
        this.value=value;
        this.endDate=LocalDateTime.now().plusDays(7);
    }

}
