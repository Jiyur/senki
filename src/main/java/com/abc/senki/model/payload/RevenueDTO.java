package com.abc.senki.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.jcip.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "static_view")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueDTO implements Serializable {

    @Id
    @Column(name = "seller",insertable = false,updatable = false)
    private UUID seller;


    private Double revenue;

    @Column(name = "total_order",insertable = false,updatable = false)
    private Integer totalOrder;

    private String dtime;

}
